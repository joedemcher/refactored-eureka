package com.jpmc.theater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Theater {

  LocalDateProvider provider;
  private List<Showing> schedule;
  private List<Movie> movies;

  public Theater(LocalDateProvider provider) {
    this.provider = provider;

    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
    Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), 11, 0);
    Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), 9, 0);
    movies = List.of(spiderMan, turningRed, theBatMan);
    schedule =
        List.of(
            new Showing(
                turningRed, 1, LocalDateTime.of(provider.currentDate(), LocalTime.of(9, 0))),
            new Showing(
                spiderMan, 2, LocalDateTime.of(provider.currentDate(), LocalTime.of(11, 0))),
            new Showing(
                theBatMan, 3, LocalDateTime.of(provider.currentDate(), LocalTime.of(12, 50))),
            new Showing(
                turningRed, 4, LocalDateTime.of(provider.currentDate(), LocalTime.of(14, 30))),
            new Showing(
                spiderMan, 5, LocalDateTime.of(provider.currentDate(), LocalTime.of(16, 10))),
            new Showing(
                theBatMan, 6, LocalDateTime.of(provider.currentDate(), LocalTime.of(17, 50))),
            new Showing(
                turningRed, 7, LocalDateTime.of(provider.currentDate(), LocalTime.of(19, 30))),
            new Showing(
                spiderMan, 8, LocalDateTime.of(provider.currentDate(), LocalTime.of(21, 10))),
            new Showing(
                theBatMan, 9, LocalDateTime.of(provider.currentDate(), LocalTime.of(23, 0))));
  }

  public Reservation reserve(Customer customer, int sequence, int howManyTickets) {
    Showing showing;
    try {
      showing = schedule.get(sequence - 1);
    } catch (RuntimeException ex) {
      ex.printStackTrace();
      throw new IllegalStateException(
          "not able to find any showing for given sequence " + sequence);
    }
    return new Reservation(customer, showing, howManyTickets);
  }

  public void printSchedule() {
    int longestTitle = 0;
    for (Movie m : movies) {
      longestTitle =
          (m.getTitle().length() > longestTitle) ? m.getTitle().length() : longestTitle + 2;
    }
    final String titleHeaderLen = String.valueOf(longestTitle);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a E, MMM dd yyyy");
    System.out.println("===================================================");
    schedule.forEach(
        s ->
            System.out.format(
                "%2s%28s%" + titleHeaderLen + "s%22s%9s",
                s.getSequenceOfTheDay(),
                s.getStartTime().format(formatter),
                s.getMovie().getTitle(),
                humanReadableFormat(s.getMovie().getRunningTime()),
                "$" + s.getMovieFee() + "\n"));
    System.out.println("===================================================");
  }

  public void printScheduleJson() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(schedule);
    System.out.print(json);
  }

  public String humanReadableFormat(Duration duration) {
    long hour = duration.toHours();
    long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

    return String.format(
        "(%s hour%s %s minute%s)",
        hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
  }

  // (s) postfix should be added to handle plural correctly
  private String handlePlural(long value) {
    if (value == 1) {
      return "";
    } else {
      return "s";
    }
  }

  public static void main(String[] args) {
    Theater theater = new Theater(LocalDateProvider.singleton());
    theater.printSchedule();
  }
}

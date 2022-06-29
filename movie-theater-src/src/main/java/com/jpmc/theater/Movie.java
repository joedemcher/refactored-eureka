package com.jpmc.theater;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Movie {
  private static int MOVIE_CODE_SPECIAL = 1;

  private String title;
  private String description;
  private Duration runningTime;
  private double ticketPrice;
  private int specialCode;

  public Movie(String title, Duration runningTime, double ticketPrice, int specialCode) {
    this.title = title;
    this.runningTime = runningTime;
    this.ticketPrice = ticketPrice;
    this.specialCode = specialCode;
  }

  public String getTitle() {
    return title;
  }

  public Duration getRunningTime() {
    return runningTime;
  }

  public double getTicketPrice() {
    return ticketPrice;
  }

  public double calculateTicketPrice(Showing showing) {
    return ticketPrice - getDiscount(showing);
  }

  private double getDiscount(Showing showing) {
    int showSequence = showing.getSequenceOfTheDay();
    LocalDateTime startTime = showing.getStartTime();

    double discount = 0;
    if (MOVIE_CODE_SPECIAL == specialCode) {
      discount = ticketPrice * 0.2;
    }

    if (showSequence == 1) {
      discount = (discount > 3) ? discount : 3;
    } else if (showSequence == 2) {
      discount = (discount > 2) ? discount : 2;
    } else if (startTime.getHour() > 11 && startTime.getHour() < 16 || (startTime.getHour() == 16 && startTime.getMinute() == 0) || (startTime.getHour() == 11 && startTime.getMinute() == 0)) {
      discount = ((discount > (ticketPrice * 0.25)) ? discount : (ticketPrice * 0.25));
    } else if (startTime.getDayOfMonth() == 7) {
      discount = ((discount > 1) ? discount : 1);
    }

    return discount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Movie movie = (Movie) o;
    return Double.compare(movie.ticketPrice, ticketPrice) == 0
        && Objects.equals(title, movie.title)
        && Objects.equals(description, movie.description)
        && Objects.equals(runningTime, movie.runningTime)
        && Objects.equals(specialCode, movie.specialCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, runningTime, ticketPrice, specialCode);
  }
}

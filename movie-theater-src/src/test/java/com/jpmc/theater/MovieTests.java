package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieTests {
  @Test
  void specialMovieWith50PercentDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
    Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), LocalTime.now()));
    assertEquals(10, spiderMan.calculateTicketPrice(showing));
  }

  @Test
  void afternoonMovieDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 10, 1);
    LocalTime noon = LocalTime.of(12, 00, 0);
    Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(LocalDate.now(), noon));
    assertEquals(7.5, spiderMan.calculateTicketPrice(showing));
  }

  @Test
  void seventhDayOfMonthDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 10, 3);
    LocalDate seventh = LocalDate.of(2022, 01, 7);
    LocalTime fiveThirtyPm = LocalTime.of(17,30,00);
    Showing showing = new Showing(spiderMan, 5, LocalDateTime.of(seventh, fiveThirtyPm));
    assertEquals(9, spiderMan.calculateTicketPrice(showing));
  }
}

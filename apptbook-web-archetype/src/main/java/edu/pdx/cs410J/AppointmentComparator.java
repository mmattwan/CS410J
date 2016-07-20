//package edu.pdx.cs410J.markum2;
package edu.pdx.cs410J;

import java.util.Comparator;

/**
 * Class that defines appointment compares on chronological beginDateTime,
 * then chronological endDateTime, then description (lexographically).
 *
 * @author Markus Mattwandel
 * @version 2016.06.30
 **/
public class AppointmentComparator implements Comparator<Appointment> {

  /**
   * Method that defines appointment compares on chronological beginDateTime,
   * then chronological endDateTime, then description (lexographically).
   **/
  public int compare(Appointment a1, Appointment a2) {

    // beginDateTime ordering
    if (a1.getBeginDateTime().after(a2.getBeginDateTime()))
      return 1;
    else if (a1.getBeginDateTime().before(a2.getBeginDateTime()))
      return -1;
    else if (a1.getBeginDateTime().equals(a2.getBeginDateTime())) {

      // endDateTime ordering
      if (a1.getEndDateTime().after(a2.getEndDateTime()))
        return 1;
      else if (a1.getEndDateTime().before(a2.getEndDateTime()))
        return -1;
      else if (a1.getEndDateTime().equals(a2.getEndDateTime())) {

        // Description Ordering
        return a1.getDescription().compareTo(a2.getDescription());
      }
    }

  return 0; // catch the rest (syntactically required)
  }
}

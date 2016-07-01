package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointment;

/**
 * Class that defines an appointment.
 *
 * @author Markus Mattwandel
 * @version 2016.06.30
 **/
public class Appointment extends AbstractAppointment {

  private String owner;       // owner of the appointment
  private String description; // description of the appointment
  private String beginTime;   // begin time and date
  private String endTime;     // end time and date

  /**
   *  Constructs an appointment
   *
   *  @param  owner       owner of the appointment
   *  @param  description description of the appointment
   *  @param  beginTime   begin time and date
   *  @param  endTime     end time and date
   */
  public Appointment(String owner, String description, String beginTime, String endTime) {
    this.owner = owner;
    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  @Override
  public String getBeginTimeString() { // Ignore for Project 1
      throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getEndTimeString() {  // Ignore for Project 1
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override

  /**
   * Returns an appointment in a string
   *
   * @return s  string containing appointment information
   */
  public String getDescription() {

    String s = this.owner +" has this appointment:\n";
    s += "\t   Title: " + this.description + "\n";
    s += "\tStarting: " + this.beginTime + "\n";
    s += "\t  Ending: " + this.endTime + ".";
    return(s);
  }
}

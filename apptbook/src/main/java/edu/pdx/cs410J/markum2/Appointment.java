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


  /**
   * Returns an appointment in a string
   *
   * @return s  string containing appointment information
   */
  @Override
  public String getDescription() {
    return(this.owner+", "+this.description+", "+this.beginTime+", "+this.endTime);
  }
}

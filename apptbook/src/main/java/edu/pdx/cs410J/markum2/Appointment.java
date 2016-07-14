package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.DateFormat;
import java.util.Date;

/**
 * Class that defines an appointment.
 *
 * @author Markus Mattwandel
 * @version 2016.06.30
 **/
public class Appointment extends AbstractAppointment {

  private String owner;       // owner of the appointment
  private String description; // description of the appointment
  private Date beginDateTime; // begin time and date
  private Date endDateTime;   // end time and date

  /**
   *  Constructs an appointment
   *
   *  @param owner           : owner of the appointment
   *  @param description     : description of the appointment
   *  @param beginDateTime   : begin time and date
   *  @param endDateTime     : end time and date
   */
  public Appointment(String owner, String description, Date beginDateTime, Date endDateTime) {
    this.owner = owner;
    this.description = description;
    this.beginDateTime = beginDateTime;
    this.endDateTime = endDateTime;
  }

  /**
   * Returns appointment beginning Date and Time in SHORT Date format as a string.
   *
   * @return string : containing appointment's beginDateTime in SHORT format
   */
  public String getBeginTimeString() {

    int f = DateFormat.SHORT;
    DateFormat df = DateFormat.getDateTimeInstance(f,f);

    String s = df.format(this.beginDateTime);
    return(s);

  }
  /**
   * Returns appointment ending Date and Time in SHORT Date format as a string.
   *
   * @return string : containing appointment's endDateTime in SHORT format
   */
  public String getEndTimeString() {

    int f = DateFormat.SHORT;
    DateFormat df = DateFormat.getDateTimeInstance(f,f);

    String s = df.format(this.endDateTime);
    return(s);

  }

  /**
   * Returns appointment description.
   *
   * @return string : containing appointment's description
   */
  public String getDescription() {
    return(this.description);
  }

  /**
   * Returns appointment beginning Date and Time.
   *
   * @return string : containing appointment's beginDateTime
   */
  public Date getBeginDateTime() {
    return(this.beginDateTime);
  }

  /**
   * Returns appointment ending Date and Time.
   *
   * @return string : containing appointment's endDateTime
   */
  public Date getEndDateTime() {
    return(this.endDateTime);
  }

  /**
   * Returns the appointment as a string
   *
   * @return string : containing appointment information
   */
  public String toString() {
    return(this.owner+", "+this.description+", "+this.beginDateTime+", "+this.endDateTime);
  }
}

package edu.pdx.cs410J.markum2.client;

import edu.pdx.cs410J.AbstractAppointmentBook;
import java.io.Serializable;
import java.util.*;

/**
 * Class that defines an appointment book using TreeSet, which allows ordering at when adding.
 *
 * @author Markus Mattwandel
 * @version 2016.06.30
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

  private String owner; // owner of the appointment book

//  public TreeSet<Appointment> apptBook = new TreeSet<>(new AppointmentComparator());  // sorted list of appointments
  public TreeSet<Appointment> apptBook = new TreeSet<>();  // sorted list of appointments

  /**
   * Constructs an empty appointment book.
   */
  public AppointmentBook() {
  }

  /**
   * Method to return AppointmentBook owner.
   *
   * @return String : AppointmentBook owner.
   */
  public String getOwnerName() {
    return (this.owner);
  }

  public void setOwnerName(String owner) {
    this.owner = owner;
  }
  /**
   * Method to return appointmentBook
   *
   * @return apptBook : TreeSet of Appointments)
   */
  public TreeSet<Appointment>  getAppointments() {
    return apptBook;
  }

  /**
   * Method to add an appointment to the appointment book
   *
   * @param appt : appointment to add
   */
  public void addAppointment(Appointment appt) {
    apptBook.add(appt);
  }

  /**
   * Method to return owner and size of AppointmentBook
   *
   * @return String : owner and size of AppointmentBook
   */
  public String toString() {
    return this.getOwnerName() + "\'s appointment book with " + this.getAppointments().size() + " appointments";
  }
}

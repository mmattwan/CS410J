package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that defines an appointment book
 *
 * @author Markus Mattwandel
 * @version 2016.06.30
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

  private String owner; // owner of the appointment book
  public ArrayList<Appointment> apptBook = new ArrayList<>();  // list of appointments

  /**
   * Constructs an empty appointment book.
   *
   * @param  owner  owner of the appointment book
   */
  public AppointmentBook() {

  }

  @Override
  public String getOwnerName() {
    return (this.owner);
  }

  /**
   * Returns the appointmentBook (Arraylist[] for Appointments
   *
   * @return apptBook (Arraylist[] for Appointments
   */
  @Override
  public Collection<Appointment> getAppointments() {

    return apptBook;
  }

  /**
   * Adds an appointment to the appointment book
   *
   * @param  appt  appointment to add
   */
  @Override
  public void addAppointment(Appointment appt) {
    apptBook.add(appt);
//    System.out.println("Now there are "+apptBook.size()+" appointments");
  }

  @Override
  public String toString() {
    return this.getOwnerName() + "\'s appointment book with " + this.getAppointments().size() + " appointments";
  }
}

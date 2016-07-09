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
  private ArrayList<Appointment> apptBook = new ArrayList<>();  // list of appointments

  /**
   * Constructs an empty appointment book.
   *
   * @param  owner  owner of the appointment book
   */
  public AppointmentBook()   {
//    this.owner = owner;
  }

  @Override
  public String getOwnerName() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public Collection<Appointment> getAppointments() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  /**
   * Adds an appointment to the apppointment book
   *
   * @param  appt  appointment to add
   */
  @Override
  public void addAppointment(Appointment appt) {
    apptBook.add(appt);
  }

  @Override
  public String toString() {
    return this.getOwnerName() + "\'s appointment book with " + this.getAppointments().size() + " appointments";
  }
}

package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

  String owner;
//  Collection<Appointment> appointments;
  ArrayList<Appointment> apptBook = new ArrayList<>();

  public AppointmentBook(String owner)
  {
    System.out.println("Constructing appointmentBook for "+owner);
      this.owner = owner;
  }

  @Override
  public String getOwnerName()
  {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public Collection<Appointment> getAppointments()
  {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public void addAppointment(Appointment appt)
  {
    System.out.println("Adding appointment to appointment book");
    apptBook.add(appt);
  }

  @Override
  public String toString()
  {
    return this.getOwnerName() + "\'s appointment book with " + this.getAppointments().size() + " appointments";
  }




}

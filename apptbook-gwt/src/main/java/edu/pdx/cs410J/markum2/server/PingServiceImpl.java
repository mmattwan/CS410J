package edu.pdx.cs410J.markum2.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.markum2.client.Appointment;
import edu.pdx.cs410J.markum2.client.AppointmentBook;
import edu.pdx.cs410J.markum2.client.PingService;

/**
 * The server-side implementation of the division service
 */
public class PingServiceImpl extends RemoteServiceServlet implements PingService
{
  @Override
  public AppointmentBook  ping(int numberOfAppointments) {
    AppointmentBook book = new AppointmentBook();
    for (int i=0; i<numberOfAppointments; i++)
      book.addAppointment(new Appointment());
    return book;
  }

  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}

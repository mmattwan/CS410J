package edu.pdx.cs410J.markum2.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

/**
 * The client-side interface to the ping service
 */
public interface PingServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void createAppointmentBook(String owner, String desc, Date beginDateTime, Date endDateTime, AsyncCallback<AppointmentBook> async);
}

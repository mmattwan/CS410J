package edu.pdx.cs410J.markum2;

import com.google.common.annotations.VisibleForTesting;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * This servlet provides a REST API for working with an
 * <code>AppointmentBook</code>.  H
 */
public class AppointmentBookServlet extends HttpServlet
{
  // Server side appointmentBook
  private final AppointmentBook newAppointmentBook = new AppointmentBook();

  /**
   * Converts passed dateTime string to Date, writing to pw on error.
   * @param dateTimeStr : date/time string to convert from
   * @param pw          : printWriter to write error message to
   * @return Date or null if parse fails
   */
  protected Date stringToDate(String dateTimeStr, PrintWriter pw) {

    Date returnDate = null;
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    try {
      returnDate = df.parse(dateTimeStr);
    } catch (ParseException ex) {
      pw.println("Bad date/time: "+dateTimeStr);
      pw.flush();
      return null;
    }
    return returnDate;
  }

  /**
   * Handles an HTTP GET request from a client by writing the value of the key
   * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
   * parameter is not specified, all of the key/value pairs are written to the
   * HTTP response.
   */
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {

    PrintWriter pw = response.getWriter(); // URL to write to
    String option = null;                  // type of GET option

    // extract parameters
    String owner = getParameter( "owner", request );
    String beginDateTimeStr = getParameter( "beginTime", request );
    String endDateTimeStr = getParameter( "endTime", request );

    // evaluate queryString to determine operation or error
    if (request.getQueryString() == null) {
      pw.println("No URL parameter.");
      pw.flush();
      return;
    }
    else if (owner != null && beginDateTimeStr != null && endDateTimeStr != null)
      option = "searchRange";
    else if (owner != null)
      option = "queryByOwner";
    else {
      pw.println("Invalid URL parameter.");
      pw.flush();
      return;
    }

    // Extract owner from apptbook
    String apptBookOwner = newAppointmentBook.getOwnerName();

    // if empty appointmentBook, return
    if (apptBookOwner == null) return;

    // Make sure owner is the same as apptBook owner
    if (!apptBookOwner.equals(owner)) {
      pw.println("AppointmentBook owner "+apptBookOwner+" not the same as "+owner);
      pw.flush();
      return;
    }

    // if searching, convert begin and end dateTimes
    Date beginDateTime=null, endDateTime=null;
    if (option.equals("searchRange")) {

      // convert DateTime strings to Date
      beginDateTime=stringToDate(beginDateTimeStr,pw);
      if (beginDateTime == null) return;
      endDateTime=stringToDate(endDateTimeStr,pw);
      if (endDateTime == null) return;
    }

    // PrettyPrint to URL based on option
    try {
      TextDumper dumper = new TextDumper();
      if (option.equals("searchRange")) {
        dumper.prettyPrintRange(newAppointmentBook, pw, beginDateTime, endDateTime);
      }
      else
        dumper.prettyPrint(newAppointmentBook, pw);
    } catch (IOException ex) {
        pw.println("** " + ex.getMessage());
        pw.flush();
    }
  }

  /**
   * Handles an HTTP POST request by storing the key/value pair specified by the
   * "key" and "value" request parameters.  It writes the key/value pair to the
   * HTTP response.
   */
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    PrintWriter pw = response.getWriter(); // write to URL

    // HTTP response to capture
    response.setContentType( "text/plain" );

    // Appointment information passed via HTTP parameters
    String owner = getParameter( "owner", request );
    String description = getParameter( "description", request );
    String beginDateTimeStr = getParameter( "beginTime", request );
    String endDateTimeStr = getParameter( "endTime", request );

    // convert DateTime strings to Date
    Date beginDateTime=null;
    beginDateTime=stringToDate(beginDateTimeStr,pw);
    if (beginDateTime == null) return;

    Date endDateTime=null;
    endDateTime=stringToDate(endDateTimeStr,pw);
    if (endDateTime == null) return;

    // if first appointment, set owner for appointmentBook
    if (newAppointmentBook.getOwnerName() == null )
      newAppointmentBook.setOwnerName(owner);

    // Extract owner from apptbook
    String apptBookOwner = newAppointmentBook.getOwnerName();

    // Make sure owner is that same as apptBook owner
    if (!apptBookOwner.equals(owner)) {
      pw.println("AppointmentBook owner "+apptBookOwner+" not the same as "+owner);
      pw.flush();
      return;
    }

    // if not a search, add appt to apptBook
    if (description != null) {

      // Construct new Appointment from cmdLine args and add it to AppointmentBook
      Appointment newAppointment = new Appointment(owner, description, beginDateTime, endDateTime);

      // add new appointment to appointmentBook
      newAppointmentBook.addAppointment(newAppointment);
    }

    // if a search, prettyPrint to console
    else {
      TextDumper dumper = new TextDumper();
      PrintWriter cpw = new PrintWriter(System.out);
      dumper.prettyPrintRange(newAppointmentBook, cpw, beginDateTime, endDateTime);
    }

  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {

    String value = request.getParameter(name);

    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }
}

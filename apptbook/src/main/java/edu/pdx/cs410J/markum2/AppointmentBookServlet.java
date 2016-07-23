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
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet
{
  private final Map<String, String> data = new HashMap<>();

  // Server side appointmentBook
  private final AppointmentBook newAppointmentBook = new AppointmentBook();

  /**
   * Handles an HTTP GET request from a client by writing the value of the key
   * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
   * parameter is not specified, all of the key/value pairs are written to the
   * HTTP response.
   */

  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {

    PrintWriter pw = response.getWriter(); // write to URL
    String option = null;

    // evaluate queryString to determine operation or error
    if (request.getQueryString() == null) {
      pw.println("No URL parameter.");
      pw.flush();
      return;
    }
    else if (request.getQueryString().contains("owner=") &&
             request.getQueryString().contains("beginTime=") &&
             request.getQueryString().contains("endTime=") )
      option = "query";
    else if (request.getQueryString().contains("owner="))
      option = "owner";
    else {
      pw.println("Invalid URL parameter.");
      pw.flush();
      return;
    }

    System.out.println("In doGet: option = "+option);

    // Extract owner from URL
    response.setContentType( "text/plain" );
    String owner = getParameter( "owner", request );

    // Extract owner from apptbook
    String apptBookOwner = newAppointmentBook.getOwnerName();

/*
    System.out.println("owner is \t\t>>>"+owner+"<<<");
    System.out.println("apptBookOwner is \t>>>"+apptBookOwner+"<<<");
    System.out.println("apptBookOwner.compareTo(newAppointmentBook.getOwnerName()) = "+apptBookOwner.compareTo(newAppointmentBook.getOwnerName()));

    if (apptBookOwner.compareTo(newAppointmentBook.getOwnerName())!= 0) {
      pw.println("AppointmentBook owner "+apptBookOwner+" not the same as "+owner);
      pw.println("Different");
      System.out.println("Different");
      pw.flush();
      return;
    }
    pw.println("Same");
    System.out.println("Same");

    pw.println("Owner is "+owner);
    pw.flush();
*/

    // PrettyPrint to URL
    try {
      TextDumper dumper = new TextDumper();
      dumper.prettyPrint(newAppointmentBook, pw);
    } catch (IOException ex) {
      System.err.println("** " + ex.getMessage());
      System.exit(1);
    }

  }

  /**
   * Handles an HTTP POST request by storing the key/value pair specified by the
   * "key" and "value" request parameters.  It writes the key/value pair to the
   * HTTP response.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {

    // HTTP response to capture
    response.setContentType( "text/plain" );

    // Appointment information passed via HTTP parameters
    String owner = getParameter( "owner", request );
    String description = getParameter( "description", request );
    String beginDateTimeStr = getParameter( "beginDateTimeStr", request );
    String endDateTimeStr = getParameter( "endDateTimeStr", request );

    // convert DateTimeStr's to Date format
    Date beginDateTime = null, endDateTime = null ;
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
    try {
      beginDateTime = df.parse(beginDateTimeStr);
    } catch (ParseException ex) {
      System.err.println("Bad beginning Date and time: "+beginDateTimeStr);
      System.exit(1);
    }
    try {
      endDateTime = df.parse(endDateTimeStr);
    } catch (ParseException ex) {
      System.err.println("Bad beginning Date and time: "+endDateTimeStr);
      System.exit(1);
    }

    // if first appointment, set owner for appointmentBook
    if (newAppointmentBook.getOwnerName() == null )
      newAppointmentBook.setOwnerName(owner);

    // Construct new Appointment from cmdLine args and add it to AppointmentBook
    Appointment newAppointment = new Appointment(owner, description, beginDateTime, endDateTime);

    // check owner is the same, if not exit
/*    if (newAppointmentBook.getOwnerName() != owner) {
      System.err.println("AppointmentBook owner "+newAppointmentBook.getOwnerName()+" not "+owner);
      return;
    }
*/
    // add new appointment to appointmentBook
    newAppointmentBook.addAppointment(newAppointment);

  }

  /**
   * Handles an HTTP DELETE request by removing all key/value pairs.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/plain");

    this.data.clear();

    PrintWriter pw = response.getWriter();
    pw.println(Messages.allMappingsDeleted());
    pw.flush();

    response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
    throws IOException
  {
    String message = Messages.missingRequiredParameter(parameterName);
    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes the value of the given key to the HTTP response.
   *
   * The text of the message is formatted with {@link Messages#getMappingCount(int)}
   * and {@link Messages#formatKeyValuePair(String, String)}
   */
  private void writeValue( String key, HttpServletResponse response ) throws IOException
  {
    String value = this.data.get(key);

    System.out.println("Hello from writeValue");

    PrintWriter pw = response.getWriter();
    pw.println(Messages.getMappingCount( value != null ? 1 : 0 ));
    pw.println(Messages.formatKeyValuePair(key, value));

    pw.flush();

    response.setStatus( HttpServletResponse.SC_OK );
  }

  /**
   * Writes all of the key/value pairs to the HTTP response.
   *
   * The text of the message is formatted with
   * {@link Messages#formatKeyValuePair(String, String)}
   */
  private void writeAllMappings( HttpServletResponse response ) throws IOException
  {
    System.out.println("Hello from writeAllMappings");

    PrintWriter pw = response.getWriter();
    pw.println(Messages.getMappingCount(data.size()));

    for (Map.Entry<String, String> entry : this.data.entrySet()) {
       pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue()));
    }

    pw.flush();

    response.setStatus( HttpServletResponse.SC_OK );
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

  @VisibleForTesting
  void setValueForKey(String key, String value) {
        this.data.put(key, value);
    }

  @VisibleForTesting
  String getValueForKey(String key) {
        return this.data.get(key);
    }
}

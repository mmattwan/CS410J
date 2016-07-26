package edu.pdx.cs410J.markum2;

import com.google.common.annotations.VisibleForTesting;
import com.sun.org.apache.regexp.internal.RE;
import edu.pdx.cs410J.web.HttpRequestHelper;

import javax.xml.soap.SAAJResult;
import javax.xml.ws.Response;
import java.awt.*;
import java.io.IOException;
import java.util.Date;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper
{
  // App and Servlet
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";

  // The url to GET or POST to
  private final String url;

  /**
   * Creates a client to the appointment book REST service running on the given host and port
   * @param hostName The name of the host
   * @param port The port
   */
  public AppointmentBookRestClient( String hostName, int port, String owner)
  {
    this.url = String.format( "http://%s:%d/%s/%s?%s", hostName, port, WEB_APP, SERVLET, "owner="+owner);
  }

  public Response addApptKeyValuePair( String owner, String description, String beginDateTimeStr, String endDateTimeStr,AppointmentBook filteredAppointmentBook)
    throws IOException
  {
    return postToMyURL(filteredAppointmentBook, "key","owner","value",owner,"description",description,"beginTime",beginDateTimeStr,"endTime",endDateTimeStr);
  }

  public Response addApptSearchKeyValuePair( String owner, String beginDateTimeStr, String endDateTimeStr,AppointmentBook filteredAppointmentBook)
    throws IOException
  {
    return postToMyURL(filteredAppointmentBook, "key","owner","value",owner,"beginTime",beginDateTimeStr,"endTime",endDateTimeStr);
  }

  public Response postToMyURL(AppointmentBook filteredAppointmentBook, String... keysAndValues) throws IOException {
    return post(this.url, keysAndValues);
  }

}

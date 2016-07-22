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
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";

  private final String url;


  /**
   * Creates a client to the appointment book REST service running on the given host and port
   * @param hostName The name of the host
   * @param port The port
   */
  public AppointmentBookRestClient( String hostName, int port )
  {
    this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
  }

  public AppointmentBookRestClient( String hostName, int port, String args)
  {
    this.url = String.format( "http://%s:%d/%s/%s?%s", hostName, port, WEB_APP, SERVLET, args);
  }

      /**
   * Returns all keys and values from the server
   */
  public Response getAllKeysAndValues() throws IOException
  {
    return get(this.url );
  }

  /**
   * Returns all values for the given key
   */
  public Response getValues( String key ) throws IOException
  {
    return get(this.url, "key", key);
  }

  public Response addApptKeyValuePair( String owner, String description, String beginDateTimeStr, String endDateTimeStr) throws IOException
  {
    return postToMyURL("key","owner","value",owner,"description",description,"beginDateTimeStr",beginDateTimeStr,"endDateTimeStr",endDateTimeStr);
  }

  public Response addKeyValuePair( String key, String value ) throws IOException
  {
    return postToMyURL("key", key, "value", value, "description", "Lunch with Heather");
  }

  Response postToMyURL(String... keysAndValues) throws IOException {
    return post(this.url, keysAndValues);
  }

  public Response removeAllMappings() throws IOException {
    return delete(this.url);
  }
}

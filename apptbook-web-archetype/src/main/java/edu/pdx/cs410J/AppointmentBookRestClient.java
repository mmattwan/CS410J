package edu.pdx.cs410J;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import javax.xml.soap.SAAJResult;
import javax.xml.ws.Response;
import java.io.IOException;

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
        System.out.println("Hello from AppointmentBookRestClient");

        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );

        System.out.println(String.format( "Executed http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET ));
        System.out.println(this.url);
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

    public Response addKeyValuePair( String key, String value ) throws IOException
    {
        return postToMyURL("key", key, "value", value);
    }

    public Response addKeyValuePair(Appointment a) throws IOException
    {
//        String s = "(owner, "+a.owner+"), (description, "+a.description+"), (beginTime, "+a.beginDateTime+"), (endTime, "+a.endDateTime+")");
        String s = "(arg1, key1), (arg2, key2), (arg3, key3)";
        return postToMyURL(s);
    }

    @VisibleForTesting
    Response postToMyURL(String... keysAndValues) throws IOException {
        return post(this.url, keysAndValues);
    }

    public Response removeAllMappings() throws IOException {
        return delete(this.url);
    }
}

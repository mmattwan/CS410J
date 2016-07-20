package edu.pdx.cs410J;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.Date;

/**
 * The main class for the client side of CS410J appointment book Project 4.
 *
 * @author  Markus Mattwandel, adapted from Template by David Whitlock/
 * @version 2016.07.27
 *
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    /**
     * Prints README
     */
    private static void printReadme() {
        System.out.println("\n\nCS410J Project 4");
        System.out.println("Author: Markus Mattwandel\n");
        System.out.println("This program implements a REST client-server appointment book service.\n");
        System.out.println("The following options are supported:");
        System.out.println("  -host hostname\t name of host computer on which server is running");
        System.out.println("  -port port\t\t port on which the server is listening");
        System.out.println("  -search\t\t specifies that appointments should be searched for");
        System.out.println("  -print\t\t prints a description of the new appointment");
        System.out.println("  -README\t\t to print this message and exits");
        System.out.println("Options can be specified in any order but must precede appointment information.\n");
        System.out.println("Appointments must adhere to the following format in the order indicated:");
        System.out.println("  owner:\t\t the owner of the appointment book");
        System.out.println("  description:\t\t the description of the appointment");
        System.out.println("  beginDateTime:\t the beginning date and meridiem time (separated by a blank)");
        System.out.println("  beginTimeTime:\t the ending date and meridiem time (separated by a blank)");
        System.out.println("Time can have 1 or 2 digits for hour, but must have 2 digits for minutes.");
        System.out.println("Date can have 1 or 2 digits for month and day, but must have 4 digits for year.");
        System.out.println("owner and description can use double quotes to span multiple words.");
        System.out.println("If -search is specified, dscription is not required.\n");
    }

    /**
     * main for Project 4.  Parses command line, processes options, ensures a port and host are specified, and ebvokes
     * servlet to POST new appointments or to GET all or search criteria appointments.
     *
     * @param     args   command line arguments
     */
    public static void main(String... args) {

        String hostName = null;
        String portString = null;
        String key = null;
        String value = null;

        for (String arg : args) {
            if (hostName == null) {
                hostName = arg;

            } else if ( portString == null) {
                portString = arg;

            } else if (key == null) {
                key = arg;

            } else if (value == null) {
                value = arg;

            } else {
                usage("Extraneous command line argument: " + arg);
            }
        }

        if (hostName == null) {
            usage( MISSING_ARGS );

        } else if ( portString == null) {
            usage( "Missing port" );
        }

        int port;
        try {
            port = Integer.parseInt( portString );
            
        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        HttpRequestHelper.Response response;
        try {
            if (key == null) {
                // Print all key/value pairs
                response = client.getAllKeysAndValues();

            } else if (value == null) {
                // Print all values of key
                response = client.getValues(key);

            } else {
                // Post the key/value pair
                response = client.addKeyValuePair(key, value);
/*
                // Add Appointment

                // Construct new Appointment from cmdLine args and add it to AppointmentBook
                Date beginDateTime = new Date();
                Date endDateTime = new Date();

                Appointment newAppointment = new Appointment(
                        "Markus", "Lunch with Heather", beginDateTime, endDateTime);

                response = client.addKeyValuePair(newAppointment);
*/
            }

            checkResponseCode( HttpURLConnection.HTTP_OK, response);

        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        System.out.println(response.getContent());

        System.exit(0);
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
    }
}
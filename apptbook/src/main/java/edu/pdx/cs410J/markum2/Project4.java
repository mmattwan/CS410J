package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * The main class for the client side of CS410J appointment book Project 4.
 *
 * @author  Markus Mattwandel
 * @version 2016.07.27
 *
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {

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

    Boolean hostOption = Boolean.FALSE;   // set if -host specified in cmdLine
    String hostName = "";                 // name of host
    Boolean portOption = Boolean.FALSE;   // set if -port specified in cmdLine
    Integer portNumber=0;                 // port number
    Boolean searchOption = Boolean.FALSE; // set if -search specified in cmdLine
    Boolean printOption = Boolean.FALSE;  // set if -print specified in cmdLine
    Boolean readmeOption = Boolean.FALSE; // set if -README specified in cmdLine
    Integer optCnt = 0;                   // number of options found

    // no args is an error
    if (args.length==0) { System.err.println("Missing command line arguments"); System.exit(1); }

    // find all -options
    for (int i=0; i<args.length; i++) {

      // an arg preceded with a dash is an option
      if (args[i].substring(0,1).contains("-")) {

        // count #options found
        optCnt++;

        // check for valid options
        switch (args[i]) {

          case "-host":
            // verify that a hostName is specified after -host, exit if not
            if (i == args.length - 1) {
              System.err.println("No arguments after -host");
              System.exit(1);
            }
            // verify that no options follow directly after -host, exit if not
            if (args[i + 1].contains("-")) {
              System.err.println("No hostName specified after -host");
              System.exit(1);
            }
            // Set hostName option and save hostName name
            hostOption = Boolean.TRUE;
            optCnt++;  // hostName is also an option to count!
            hostName = args[i + 1];
            break;

          case "-port":
            // verify that something is specified after -port, exit if not
            if (i == args.length - 1) {
              System.err.println("No arguments after -port");
              System.exit(1);
            }
            // verify that no options follow directly after -port, exit if not
            if (args[i + 1].contains("-")) {
              System.err.println("No port number specified after -port");
              System.exit(1);
            }
            // verify that port is an integer
            try {
              portNumber = Integer.parseInt(args[i + 1]);
              } catch (NumberFormatException ex) {
              System.err.println("Port \"" + args[i + 1] + "\" must be an integer");
              System.exit(1);
            }
            // Set hostName option
            portOption = Boolean.TRUE;
            optCnt++;  // portNumber is also an option to count!
            break;

          case "-search":
            searchOption = Boolean.TRUE;
            break;

          case "-print":
            printOption = Boolean.TRUE;
            break;

          case "-README":
            readmeOption = Boolean.TRUE;
            break;

          // Any other options are invalid and signal and
          default:
            System.err.println("Invalid option found: " + args[i]);
            System.exit(1);
          }
        }
      }

    // print README and exit if -README found
    if (readmeOption) { printReadme(); System.exit(0); }

    // -port and -host must be specified
    if (!portOption || !hostOption) {
      System.err.println("-host hostName and -port portNumber must be specified");
      System.exit(1);
    }

    // it takes exactly 8 args after the options to specify an appointment
    // or 7 args to specify a -search.
    // Add that amount to optCnt to verify and exit if total amount is wrong.
    if ( (optCnt+8 != args.length && !searchOption) ||
         (optCnt+7 != args.length && searchOption ) ) {
      System.err.println("Invalid number of appointment arguments");
      System.exit(1);
    }

    // cmdLine good: start processing!

    // Extract new appointment details from args
    String beginDate = args[args.length-6], beginTime = args[args.length-5], beginTimeMeridiem = args[args.length-4];
    String beginDateTimeStr = beginDate+" "+beginTime+" "+beginTimeMeridiem;
    String endDate = args[args.length-3],   endTime = args[args.length-2],   endTimeMeridiem = args[args.length-1];
    String endDateTimeStr = endDate+" "+endTime+" "+endTimeMeridiem;
    String owner, description = "";
    if (searchOption)
      owner = args[args.length - 7];
    else {
      owner = args[args.length - 8];
      description = args[args.length - 7];
    }

    // convert command line string dates to Date class, flagging and exiting on bad dates
    Date beginDateTime = null, endDateTime = null ;
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
    try {
      beginDateTime = df.parse(beginDate+" "+beginTime+" "+beginTimeMeridiem);
    }
    catch (ParseException ex) {
      System.err.println("Bad beginning Date and time: "+beginDate+" "+beginTime+" "+beginTimeMeridiem);
      System.exit(1);
    }
    try {
      endDateTime = df.parse(endDate+" "+endTime+" "+endTimeMeridiem);
    }
    catch (ParseException ex) {
      System.err.println("Bad ending date and time: "+endDate+" "+endTime+" "+endTimeMeridiem);
      System.exit(1);
    }

    // Check whether beginDate < endDate, exit if not
    if (! beginDateTime.before(endDateTime)) {
      System.err.println("Appointment must begin before it ends.");
      System.exit(1);
    }

    // initialize HTTP response var
    HttpRequestHelper.Response response;

    // initialize RestClient
    AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, portNumber, owner);

    // POST to url
    try {
      if (searchOption) {

        response = client.addApptSearchKeyValuePair(owner,beginDateTimeStr,endDateTimeStr);
        // prettyPrint returned appointmentBook
      }
      else
        response = client.addApptKeyValuePair(owner,description,beginDateTimeStr,endDateTimeStr);
      checkResponseCode( HttpURLConnection.HTTP_OK, response);
    } catch ( IOException ex ) {
      error("While contacting server: " + ex);
      return;
    }

    // if -print specified, print new appt
    if (printOption && !searchOption)
      System.out.println("Added for "+owner+": \""+description+"\" "+beginDateTimeStr+" "+endDateTimeStr);

    // if you've made it this far, exit with success
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

}
package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.ParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * The main class for the client side of CS410J appointment book Project 4.
 *
 * @author  Markus Mattwandel
 * @version 2016.07.27
 */
public class Project4 {

  // The AppointmentBook the project will work on
  public static edu.pdx.cs410J.markum2.AppointmentBook newAppointmentBook = new edu.pdx.cs410J.markum2.AppointmentBook();

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
    System.out.println("An appointment must always be specified.\n");
  }

  /**
   * main for Project 4.  Parses command line, processes options,  ...
   *
   * @param     args   command line arguments
   */
  public static void main(String[] args) {

    Boolean hostOption = Boolean.FALSE;   // set if -host specified in cmdLine
    String hostName = "";                 // name of host
    Boolean portOption = Boolean.FALSE;   // set if -port specified in cmdLine
    Integer portNumber=0;                 // port number
    Boolean searchOption = Boolean.FALSE; // set if -search specified in cmdLine
    Date startSearchDate;                 // search start dateTime
    Date endSearchDate;                   // search end dateTime
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
/*
    System.out.println("hostOption is: "+hostOption);
    System.out.println("hostName is: "+hostName);
    System.out.println("portOption is: "+portOption);
    System.out.println("portNumber is: "+portNumber);
    System.out.println("searchOption is: "+searchOption);
    System.out.println("printOption is: "+printOption);
    System.out.println("readmeOption is: "+readmeOption);
    System.out.println("optCnt is: "+optCnt);
    System.out.println("totalArgsCnt is: "+args.length);
*/
    // print README and exit if -README found
    if (readmeOption) { printReadme(); System.exit(0); }

    // -port and -host must be specified
    if (!portOption || !hostOption) {
      System.err.println("-host hostName and -port portNumber must be specified");
      System.exit(1);
    }

    // it takes exactly 8 args after the options to specify an appointment:
    // or 7 args if -search is specified
    // add that to optCnt to verify and exit it wrong amount
    if ( (optCnt+8 != args.length && !searchOption) ||
         (optCnt+7 != args.length && searchOption ) ) {
      System.err.println("Invalid number of appointment arguments");
      System.exit(1);
    }

    // cmdLine good: start processing!

    // Extract new appointment details from args
    String beginDate = args[args.length-6], beginTime = args[args.length-5], beginTimeMeridiem = args[args.length-4];
    String endDate = args[args.length-3],   endTime = args[args.length-2],   endTimeMeridiem = args[args.length-1];
    String owner, description = "";
    if (searchOption)
      owner = args[args.length - 7];
    else {
      owner = args[args.length - 8];
      description = args[args.length - 7];
    }

    // convert command line dates to Date class, flagging and exiting on bad dates
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
/*
    System.out.println("owner = "+owner);
    System.out.println("description = "+description);
    System.out.println("beginDate = "+beginDate);
    System.out.println("beginTime = "+beginTime);
    System.out.println("beginTimeMeridiem = "+beginTimeMeridiem);
    System.out.println("beginDateTime = "+beginDateTime);
    System.out.println("endDate = "+endDate);
    System.out.println("endTime = "+endTime);
    System.out.println("endTimeMeridiem = "+endTimeMeridiem);
    System.out.println("endDateTime = "+endDateTime);
*/
/*
    // if -textFile specified, read appointments from file and add them to AppointmentBook
    if (textFileOption) {
      try {
        TextParser parser = new TextParser(textFileName,owner);
        edu.pdx.cs410J.markum2.AppointmentBook book = parser.parse();
      }
      catch (FileNotFoundException ex) {
      // catch exception, but do nothing which creates an empty file as specified
      }
      // catch parser exceptions and exit if true
      catch (ParserException ex) {
        System.err.println("** " + ex.getMessage());
        System.exit(1);
      }
    }

    // Construct new Appointment from cmdLine args and add it to AppointmentBook
    Appointment newAppointment = new Appointment(owner, description, beginDateTime, endDateTime);
    newAppointmentBook.addAppointment(newAppointment);

    // if -textFile specified, write all appointments back out
    if (textFileOption) {
      try {
        TextDumper dumper = new TextDumper(textFileName);
        dumper.dump(newAppointmentBook);
      } catch (IOException ex) {
        System.err.println("** " + ex.getMessage());
        System.exit(1);
      }
    }

    // if -prettyPrint specified, print out AppointmentBook in pretty format
    if (prettyFileOption) {
      try {
        TextDumper dumper = new TextDumper(prettyFileName);
        dumper.prettyPrint(newAppointmentBook, prettyFileName);
      } catch (IOException ex) {
        System.err.println("** " + ex.getMessage());
        System.exit(1);
      }
    }

    // if -print specified, print out cmdLine appointment added
    if (printOption)
      if (textFileOption) System.out.println("Added to "+textFileName+": "+newAppointment.toString());
      else System.out.println("Read: "+newAppointment.toString());
*/
    // if you've made it this far, exit with Success
    System.exit(0);
  }
}

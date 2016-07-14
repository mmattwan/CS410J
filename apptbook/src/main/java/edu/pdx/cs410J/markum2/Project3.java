package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.ParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * The main class for the CS410J appointment book Project 2.
 *
 * @author  Markus Mattwandel
 * @version 2016.07.20
 */
public class Project3 {

  // The AppointmentBook the project will work on
  public static AppointmentBook newAppointmentBook = new AppointmentBook();

  /**
   * Prints README
   */
  private static void printReadme() {
    System.out.println("\n\nCS410J Project 3");
    System.out.println("Author: Markus Mattwandel\n");
    System.out.println("This program creates an appointment book and adds a single appointment.  If");
    System.out.println("-textFile is used, it also reads from and adds the new appointments to the ");
    System.out.println("specified file,\n");
    System.out.println("The following 3 options are supported:");
    System.out.println("  -print\t\t to print the appointment (provided it can be created)");
    System.out.println("  -textFile file\t file name from which to read/write the appointment book");
    System.out.println("  -pretty file\t\t file to pretty print appointment book to (- for stdout)");
    System.out.println("  -README\t\t to print this message");
    System.out.println("Options can be specified in any order but must precede appointment information.\n");
    System.out.println("tileFile format is CSV, with every field separated by a comma.  For example:");
    System.out.println("  Markus, \"Lunch with Boss\", 7/13/2016 11:00 am, 7/13/2016 1:00 pm\n");
    System.out.println("Appointments must adhere to the following format in the order indicated:");
    System.out.println("  owner:\t\t the owner of the appointment book");
    System.out.println("  description:\t\t the description of the appointment");
    System.out.println("  beginDateTime:\t the beginning date and meridiem time (separated by a blank)");
    System.out.println("  beginTimeTime:\t the ending date and meridiem time (separated by a blank)");
    System.out.println("Time can have 1 or 2 digits for hour, but must have 2 digits for minutes.");
    System.out.println("Date can have 1 or 2 digits for month and day, but must have 4 digits for year.");
    System.out.println("owner and description can use double quotes to span multiple words.\n");
    System.out.println("An appointment must always be specified.\n");
  }

  /**
   * main for Project 2.  Parses command line, processes options, reads in and checks appointments from
   * textFile if specified, and if arguments are valid creates appointment, adds it to appointment book,
   * and prints the new appointment if specified.
   *
   * @param     args   command line arguments
   */
  public static void main(String[] args) {

    Boolean printOption = Boolean.FALSE;      // set if -print cmd line option found
    Boolean readmeOption = Boolean.FALSE;     // set if -README cmd line option found
    Boolean textFileOption = Boolean.FALSE;   // set if -textFile file cmd line option found
    Boolean prettyFileOption = Boolean.FALSE; // set if -textFile file cmd line option found
    String textFileName = "";                 // name of -textfile file
    String prettyFileName = "";               // name of -pretty print file
    Integer optCnt = 0;                       // number of options found

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
          case "-print": printOption = Boolean.TRUE; break;
          case "-README": readmeOption = Boolean.TRUE; break;
          case "-textFile":

            // verify that a fileName is specified after -textFile, exit if not
            if (i == args.length - 1) {
              System.err.println("No arguments after -textFile");
              System.exit(1);
            }

            // verify that no options follow directly after -textFile, exit if not
            if (args[i + 1].substring(0, 1).contains("-")) {
              System.err.println("No fileName specified after -textFile");
              System.exit(1);
            }

            // Set textFile option and save textFile name
            textFileOption = Boolean.TRUE;
            optCnt++;  // textFileName is also an option!
            textFileName = args[i + 1];
            break;

          case "-pretty":

            // verify that a fileName is specified after -textFile, exit if not
            if (i == args.length - 1) {
              System.err.println("No arguments after -textFile");
              System.exit(1);
            }

            // "-" as next argument signals pretty print to stdout
            if (args[i + 1].length() == 1) {
              prettyFileName = args[i + 1];
              i++;  // skip "-"
            }
            // "-anuthingElse" is an error
            else if (args[i + 1].substring(0, 2).contains("-")) {
              System.err.println("No fileName specified after -pretty");
              System.exit(1);
            }
            // Set prettyFile option and save textFile name
            else
              prettyFileName = args[i + 1];

            prettyFileOption = Boolean.TRUE;
            optCnt++;  // prettyFileName is also an option!
            break;

          // Exit if option is invalid
          default:
            System.err.println("Invalid option found: " + args[i]);
            System.exit(1);
        }
      }
    }

    // print README and exit if -README found
    if (readmeOption) { printReadme(); System.exit(0); }

    // it takes exactly 8 args to specify an appointment: add that to optCnt to verify and exit it wrong amount
    if (optCnt+8 != args.length) {
      System.err.println("Invalid #arguments to create appointment");
      System.exit(1);
    }

    // Extract new appointment details from args
    String owner =  args[args.length-8];
    String description = args[args.length-7];
    String beginDate = args[args.length-6];
    String beginTime = args[args.length-5];
    String beginTimeMeridiem = args[args.length-4];
    String endDate = args[args.length-3];
    String endTime = args[args.length-2];
    String endTimeMeridiem = args[args.length-1];

    // cmdLine good: start processing!

    // convert to Date class
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

    // if -textFile specified, read appointments from file and add them to AppointmentBook
    if (textFileOption) {
      try {
        TextParser parser = new TextParser(textFileName,owner);
        AppointmentBook book = parser.parse();
      }
      catch (FileNotFoundException ex) {
//      Catch exception, but do nothing which creates an empty file as specified
      }
      catch (ParserException ex) {
        System.err.println("** " + ex.getMessage());
        System.exit(1);
      }
    }

    // Construct new Appointment from cmdLine args
    Appointment newAppointment = new Appointment(owner, description, beginDateTime, endDateTime);

    // Add new Appointment to AppointmentBook
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

    // if -prettyPrint specified, print out new appointment
    if (prettyFileOption) {
      try {
        TextDumper dumper = new TextDumper(prettyFileName);
        dumper.prettyPrint(newAppointmentBook, prettyFileName);
      } catch (IOException ex) {
        System.err.println("** " + ex.getMessage());
        System.exit(1);
      }
    }

    // if -print specified, print out new appointment
    if (printOption)
      if (textFileOption) System.out.println("Added to "+textFileName+": "+newAppointment.toString());
      else System.out.println("Read: "+newAppointment.toString());

    // if you've made it this far, exit with Success
    System.exit(0);
  }
}

package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.ParserException;

import java.io.FileNotFoundException;

/**
 * The main class for the CS410J appointment book Project 2.
 *
 * @author Markus Mattwandel
 * @version 2016.07.13
 */
public class Project2 {

  // The AppointmentBook the project will work on
  public static AppointmentBook newAppointmentBook = new AppointmentBook();

  /**
   * Validates that date contains 1 or 2 digits for day and month, and 4 digits for year
   *
   * @param  dateString string to validate
   * @return boolean    string adheres to required format
   */
  private static boolean validDate(String dateString) {
    return (dateString.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})"));
  }

  /**
   * Validates that time contains 1 or 2 digits for hour and 2 digits for minutes
   *
   * @param  timeString string to validate
   * @return boolean    string adheres to required format
   */
  private static boolean validTime(String timeString) {
    return (timeString.matches("([0-9]{1,2}):([0-9]{2})"));
  }

  /**
   * Prints README
   */
  private static void printReadme() {
    System.out.println("\n\nCS410J Project 2");
    System.out.println("Author: Markus Mattwandel\n");
    System.out.println("This program creates an appointment book and adds a single appointment.  If");
    System.out.println("-textFile is used, it also reads from and adds the new appointments to the ");
    System.out.println("specified file,\n");
    System.out.println("The following 3 options are supported:");
    System.out.println("  -print\t\t to print the appointment (provided it can be created)");
    System.out.println("  -textFile file\t file name from which to read/write the appointment book");
    System.out.println("  -README\t\t to print this message");
    System.out.println("Options can be specified in any order but must precede appointment information.\n");
    System.out.println("Appointments must adhere to the following format in the order indicated:");
    System.out.println("  owner:\t\t the owner of the appointment book");
    System.out.println("  description:\t\t the description of the appointment");
    System.out.println("  beginDate:\t\t the appointment\'s beginning date");
    System.out.println("  beginTime:\t\t the appointment's beginning time");
    System.out.println("  endDate:\t\t the appointment's ending date");
    System.out.println("  endTime:\t\t the appointment's ending time\n");
    System.out.println("Time can have 1 or 2 digits for hour, but must have 2 digits for minutes.");
    System.out.println("Date can have 1 or 2 digits for month and day, but must have 4 digits for year.");
    System.out.println("owner and description can use double quotes to span multiple words.\n");
    System.out.println("An appointment must always be specified.\n");
  }

  /**
   * main for program.  Parses command line, processes options, and if arguments are valid,
   * creates appointment, adds it to appointment book, and prints the new appointmnet if
   * specified
   *
   * @param     args   command line arguments
   */
  public static void main(String[] args) {

    Boolean printOption = Boolean.FALSE;    // set if -print cmd line option found
    Boolean readmeOption = Boolean.FALSE;   // set if -README cmd line option found
    Boolean textFileOption = Boolean.FALSE; // set if -textFile file cmd line option found
    String textFileName = "";               // name of -textFile file
    Integer optCnt = 0;                     // number of options specified

    // no args is an error
    if (args.length==0) { System.err.println("Missing command line arguments"); System.exit(1); }

    // find all options
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

          // Exit if option is invalid
          default:
            System.err.println("Invalid option found: " + args[i]);
            System.exit(1);
        }
      }
    }

    // print README and exit if -README found
    if (readmeOption) { printReadme(); System.exit(0); }

    // it takes exactly 6 args to specify an appointment: add that to optCnt to verify and exit it wrong amount
    if (optCnt+6 != args.length) {
      System.err.println("Invalid #arguments to create appointment");
      System.exit(1);
    }

    // Extract new appointment details from args
    String owner =  args[args.length-6];
    String description = args[args.length-5];
    String beginDate = args[args.length-4];
    String beginTime = args[args.length-3];
    String endDate = args[args.length-2];
    String endTime = args[args.length-1];

    // Check beginning as well as end date and time formats and exit if any are not valid
    if (!validDate(beginDate)) { System.out.println("Bad beginDate format."); System.exit(1); }
    if (!validTime(beginTime)) { System.out.println("Bad beginTime format."); System.exit(1); }
    if (!validDate(endDate))   { System.out.println("Bad endDate format.");   System.exit(1); }
    if (!validTime(endTime))   { System.out.println("Bad endTime format.");   System.exit(1); }

    // cmdLine good: start processing!

    // if -textFile then read them from file and add to AppointmentBook
    if (textFileOption) {
      try {
        TextParser parser = new TextParser(textFileName,owner);
        AppointmentBook book = parser.parse();
      }
      catch (FileNotFoundException ex) {
        System.err.println("** Could not find file " + textFileName);
      }
      catch (ParserException ex) {
        System.err.println("** " + ex.getMessage());
      }
    }

    // Construct new Appointment from cmdLine
    Appointment newAppointment = new Appointment(owner, description, beginDate+" "+beginTime, endDate+" "+endTime);

    // Add new Appointment to AppointmentBook
    newAppointmentBook.addAppointment(newAppointment);

    // if -textFile, write all appointments back out
//    TODO: use TextDumper

    // if -print specified, print new appointment
    if (printOption) System.out.println(newAppointment.getDescription());

    // if you've made it this far, exit with Success
    System.exit(0);
  }
}

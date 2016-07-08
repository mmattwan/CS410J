package edu.pdx.cs410J.markum2;

import java.io.*;

/**
 * The main class for the CS410J appointment book Project 2.
 *
 * @author Markus Mattwandel
 * @version 2016.07.13
 */
public class Project2 {

  private static String owner;
  private static AppointmentBook newAppointmentBook = new AppointmentBook(owner);

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

// TODO: port to TextParser
  private static void FileReader(String fileName) {

    BufferedReader br = null;
    String currentLine;
    Integer lineNumber = 1;

    try {

      // if file does not exist, then create it
      File file = new File(fileName);
      if (!file.exists()) file.createNewFile();

      br = new BufferedReader(new FileReader(fileName));

      while ((currentLine = br.readLine()) != null) {

        // Desconstruct .CSV lines
        String[] parts = currentLine.split(",");

        // if first line, confirm owner is correct
        if (lineNumber==1 && !parts[0].contains(owner)) {
          System.out.println(owner+" does not own "+parts[0]);
          System.exit(1);
        }
        else if (parts.length != 1){ // need this to skip <newLine>s

          // make sure there are 5 parts
          if (parts.length !=5) {
            System.out.println("Malformed textFile line "+lineNumber+": "+currentLine);
            System.exit(1);
          }

          // Parse out fields
          String description = parts[0];
          String beginDate = parts[1];
          String beginTime = parts[2];
          String endDate = parts[3];
          String endTime = parts[4];

          // Construct new Appointment
          Appointment newAppointment = new Appointment(owner, description, beginDate + " " + beginTime, endDate + " " + endTime);

          // Add new Appointment to AppointmentBook
          newAppointmentBook.addAppointment(newAppointment);
        }
        lineNumber++;
      }
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        if (br != null) br.close();
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

// TODO: port to TextDumper
  private static void FileWriter(String fileName) {

    File file = new File(fileName);
    String description = "";
    String beginDate = "";
    String beginTime = "";
    String endDate = "";
    String endTime = "";

    try {

      // if file does not exist, then create it
      if (!file.exists()) file.createNewFile();

      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);

      // first line indicates the owner
      bw.write(owner + "\'s Appointment Book:\n");

      // loop through all Appointments in AppointmentBook
      // TODO: add loop through AddressBook
      bw.write(description + ", " + beginDate + ", " + beginTime + ", " + endDate + ", " + endTime + "\n");

      // close the file
      bw.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
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
        if (args[i].equals("-print")) printOption=Boolean.TRUE;
        else if (args[i].equals("-README")) readmeOption=Boolean.TRUE;
        else if (args[i].equals("-textFile")) {

          // verify that a fileName is specified after -textFile, exit if not
          if (i == args.length-1) {
            System.err.println("No arguments after -textFile");
            System.exit(1);
          }

          // verify that no options follow directly after -textFile, exit if not
          if (args[i+1].substring(0,1).contains("-")) {
            System.err.println("No fileName specified after -textFile");
            System.exit(1);
          }

          // Set textFile option and save textFile name
          textFileOption = Boolean.TRUE;
          optCnt++;  // textFileName is also an option!
          textFileName = args[i + 1];
        }

        // Exit if option is invalid
        else {
          System.err.println("Invalid option found: "+args[i]);
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
    owner = args[args.length-6];
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
// TODO: use TextParser instead
      FileReader(textFileName);
    }
    // Construct new Appointment
    Appointment newAppointment = new Appointment(owner, description, beginDate+" "+beginTime, endDate+" "+endTime);

    // Add new Appointment to AppointmentBook
    newAppointmentBook.addAppointment(newAppointment);

    // if -textFile, write all appointments back out
// TODO: use TextDumper

    // if -print specified, print new appointment
    if (printOption) System.out.println(newAppointment.getDescription());

    // if you've made it this far, exit with Success
    System.exit(0);
  }
}

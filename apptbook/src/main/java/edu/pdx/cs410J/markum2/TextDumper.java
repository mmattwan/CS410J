package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
import java.util.Date;
import java.util.Iterator;


/**
 * Class that dumps the contents of AppointmentBook to specified textfile.
 *
 * Some code adapted from David Whitlocks's family.TextDumper example.
 *
 * @author Markus Mattwandel
 * @version 2016.07.13
 */
class TextDumper implements AppointmentBookDumper {

  private PrintWriter pw;      // Dumping destination

  /**
   * Constructors to write AppointmentBook to textfile.
   *
   * @param fileName  : name of AppointmentBook file to write
   * @throws IOException
   */
  public TextDumper(String fileName) throws IOException {
    this(new File(fileName));
  }
  private TextDumper(File file) throws IOException {
    this(new PrintWriter(new FileWriter(file), true));
  }
  private TextDumper(PrintWriter pw) {
    this.pw = pw;
  }

  /**
   * Method that dumps the contents of AppointmentBook to specified textfile, passing any IOExecptions
   * back to calling routine
   *
   * @param AbstractAppointmentBook : the AppointmentBook to dump
   * @throws IOException            : errors associated with file writes
   */
  public void dump(AbstractAppointmentBook passedAbstractAppointmentBook) throws IOException {

    // Cast abstractAppointmentBook to concrete AppointmentBook in order to access elemente
    AppointmentBook nonAbstractAppointmentBook = (AppointmentBook) passedAbstractAppointmentBook;

    // Set up AppointmentBook iterator
    Iterator iterator = nonAbstractAppointmentBook.apptBook.iterator();

    try {

      // iterate through all appointments in the appointmentBook
      while (iterator.hasNext()) {

        Object a = iterator.next();       // next appointment extracted as Object
        Appointment ac = (Appointment) a;  // cast Object to Appointment

        // Split appointment by CSV
        String[] apptParts = a.toString().split(",");

        // Convert Dates to strings
        String beginDateTime = ac.getBeginTimeString();
        String endDateTime = ac.getEndTimeString();

        // Build string to write in order to optimize write performance
        String s = apptParts[0] + "," + apptParts[1] + ", " + beginDateTime + ", " + endDateTime + "\n";

        // Write and flush appointment information using CSV format to output file
        pw.append(s);
        pw.flush();
      }

      // Close the output file
      pw.close();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Method that nicely prints the AppointmentBook to a the specified textfile or to the screen if
   * "-" specified as file passing any IOExecption
   * back to calling routine
   *
   * @param AbstractAppointmentBook : the AppointmentBook to dump
   * @throws IOException            : errors associated with file writes
   */

  public void prettyPrint(AbstractAppointmentBook passedAbstractAppointmentBook) throws IOException {

    // Cast abstractAppointmentBook to concrete AppointmentBook in order to access elemente
    AppointmentBook nonAbstractAppointmentBook = (AppointmentBook)passedAbstractAppointmentBook;

    // Set up AppointmentBook iterator
    Iterator iterator = nonAbstractAppointmentBook.apptBook.iterator();

    boolean firstLine = true;     // signals first line
    String dateJustPrinted = "";  // contains last date printed
    String s;                     // string to print to file or screen

    try {

      // iterate through all appointments in the appointmentBook
      while (iterator.hasNext()) {

        Object a = iterator.next();       // next appointment extracted as Object
        Appointment ac = (Appointment)a;  // cast Object to Appointment

        // Split appointment by CSV
        String[] apptParts = a.toString().split(",");

        // Convert Dates to strings
        String ownerStr = apptParts[0];
        Date beginDateTimeDate = ac.getBeginDateTime();
        String beginDateTimeStr = ac.getBeginTimeString();
        Date endDateTimeDate = ac.getEndDateTime();
        String endDateTimeStr = ac.getEndTimeString();
        String dsscriptionStr = apptParts[1];
        long minutesSpan = (endDateTimeDate.getTime()-beginDateTimeDate.getTime()) / 1000 / 60;

        // if first line print header
        if (firstLine) {
          Date date = new Date(); // current date and time
          s = "\n\nAppointmentBook for "+ownerStr+ " as of "+date.toString()+":";
          System.out.println(s);
          // underline it
          String s2 = "";
          for (int i=2; i<s.length(); i++) s2 += "-";
          System.out.println(s2);
          firstLine = false;
        }

        // extract DateTime parts
        String[] beginDateTimeParts = beginDateTimeStr.split(" ");
        String beginDateStr = beginDateTimeParts[0];
        String beginTimeStr = beginDateTimeParts[1]+" "+beginDateTimeParts[2];
        String[] endDateTimeParts = endDateTimeStr.split(" ");
        String endDateStr = endDateTimeParts[0];
        String endTimeStr = endDateTimeParts[1]+" "+endDateTimeParts[2];

        // if new StartDate, print the date
        if (!dateJustPrinted.equals(beginDateStr)) {
          s = "\nOn "+beginDateStr+":";
          System.out.println(s);
        }
        dateJustPrinted = beginDateStr;

        // print appointment time and description information
        s = beginTimeStr+" to "+endTimeStr+" ("+minutesSpan+" minutes):\t"+dsscriptionStr;
        System.out.println(s);

        /*
        // Build string to write in order to optimize write performance
        String s = apptParts[0]+","+apptParts[1]+", "+beginDateTimeStr+", "+endDateTimeStr+"\n";

        // Write and flush appointment information using CSV format to output file
        pw.append(s);
        pw.flush();
*/
      }

      s="\n\n";
      System.out.println(s);

      // Close the output file
      pw.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}


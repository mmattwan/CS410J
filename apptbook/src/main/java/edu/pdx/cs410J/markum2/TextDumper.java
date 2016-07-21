package edu.pdx.cs410J.markum2;

import java.io.PrintWriter;

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
   * back to calling routine.
   *
   * @param passedAbstractAppointmentBook : the AppointmentBook to dump
   * @throws IOException            : errors associated with file writes
   */
  public void dump(AbstractAppointmentBook passedAbstractAppointmentBook) throws IOException {

    // Cast abstractAppointmentBook to concrete AppointmentBook in order to access elements
    AppointmentBook nonAbstractAppointmentBook = (AppointmentBook) passedAbstractAppointmentBook;

    // Set up AppointmentBook iterator
    Iterator iterator = nonAbstractAppointmentBook.apptBook.iterator();

    try {

      // iterate through all appointments in the appointmentBook
      while (iterator.hasNext()) {

        Object a = iterator.next();        // next appointment extracted as Object
        Appointment ac = (Appointment) a;  // cast Object to Appointment

        // Split appointment by CSV
        String[] apptParts = a.toString().split(",");

        // Convert Dates to strings
        String beginDateTime = ac.getBeginTimeString();
        String endDateTime = ac.getEndTimeString();

        // Build string to write
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
   * Method that nicely prints the AppointmentBook to specified textfile or to the screen if
   * "-" is specified as textfile, passing any IOExecption
   * back to calling routine
   *
   * @param  passedAbstractAppointmentBook : the AppointmentBook to dump
   * @throws IOException                   : errors associated with file writes
   */
  public void prettyPrint(AbstractAppointmentBook passedAbstractAppointmentBook,
                          String fileName) throws IOException {

    // Cast abstractAppointmentBook to concrete AppointmentBook in order to access elemente
    AppointmentBook nonAbstractAppointmentBook = (AppointmentBook)passedAbstractAppointmentBook;

    // Set up AppointmentBook iterator
    Iterator iterator = nonAbstractAppointmentBook.apptBook.iterator();

    boolean firstLine = true;     // signals first line, e.g. print header
    String dateJustPrinted = "";  // contains last date printed
    String s;                     // string to print to file or screen

    try {

      // iterate through all appointments in the appointmentBook
      while (iterator.hasNext()) {

        Object a = iterator.next();       // next appointment extracted as Object
        Appointment ac = (Appointment)a;  // cast Object to Appointment

        // Split appointment by CSV
        String[] apptParts = a.toString().split(",");

        // Grab appointment strings
        String ownerStr = apptParts[0];
        String beginDateTimeStr = ac.getBeginTimeString();
        String endDateTimeStr = ac.getEndTimeString();
        String descriptionStr = apptParts[1];

        // Grab appointment begin and end Dates to calculate length
        Date beginDateTimeDate = ac.getBeginDateTime();
        Date endDateTimeDate = ac.getEndDateTime();
        long minutesSpan = (endDateTimeDate.getTime()-beginDateTimeDate.getTime()) / 1000 / 60;

        // if first line print header
        if (firstLine) {
          Date date = new Date(); // current date and time

          s = "\n\nAppointmentBook for "+ownerStr+ " as of "+date.toString()+":\n";
          if (fileName.equals("-")) System.out.print(s);
          else pw.append(s);

          // underline header
          String s2 = "";
          for (int i=2; i<s.length(); i++) s2 += "-";
          s2 += "\n";
          if (fileName.equals("-")) System.out.print(s2);
          else pw.append(s2);

          // signal header printed
          firstLine = false;
        }

        // extract DateTime parts as strings
        String[] beginDateTimeParts = beginDateTimeStr.split(" ");
        String beginDateStr = beginDateTimeParts[0];
        String beginTimeStr = beginDateTimeParts[1]+" "+beginDateTimeParts[2];
        String[] endDateTimeParts = endDateTimeStr.split(" ");
        String endTimeStr = endDateTimeParts[1]+" "+endDateTimeParts[2];

        // if new StartDate, print the date and preceded by a blank line for visual grouping
        if (!dateJustPrinted.equals(beginDateStr)) {
          s = "\nOn "+beginDateStr+":\n";
          if (fileName.equals("-")) System.out.print(s);
          else pw.append(s);
        }
        dateJustPrinted = beginDateStr;

        // print appointment time and description information
        s = beginTimeStr+" to "+endTimeStr+" ("+minutesSpan+" minutes):\t"+descriptionStr+"\n";
        if (fileName.equals("-")) System.out.print(s);
        else pw.append(s);

      }

      // visually differentiate AppointmentBook from other output
      s="\n\n";
      if (fileName.equals("-")) System.out.print(s);
      else pw.append(s);

      // Flush and Close the output file
      if (!fileName.equals("-")) { pw.flush(); pw.close(); }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

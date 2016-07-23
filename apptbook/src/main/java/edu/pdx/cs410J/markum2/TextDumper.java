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
//class TextDumper implements AppointmentBookDumper {
class TextDumper {

  /**
   * Constructors to write AppointmentBook to textfile.
   *
   * @param fileName     : name of AppointmentBook file to write
   * @throws IOException :
   */
  public TextDumper() {
  }

  /**
   * Method that dumps the contents of AppointmentBook to specified textfile, passing any IOExecptions
   * back to calling routine.
   *
   * @param passedAbstractAppointmentBook : the AppointmentBook to dump
   * @throws IOException                  : errors associated with file writes
   */
  public void dump(AbstractAppointmentBook passedAbstractAppointmentBook, PrintWriter pw) throws IOException {

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
   * Method that nicely prints the AppointmentBook to the specified prtntWriter, passing any IOExecption
   * back to calling routine
   *
   * @param  passedAbstractAppointmentBook : the AppointmentBook to dump
   * @param  passedAbstractAppointmentBook : the primtWriter to print to
   * @throws IOException                   : errors associated with file writes
   */
  public void prettyPrint(AbstractAppointmentBook passedAbstractAppointmentBook, PrintWriter pw) throws IOException {

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
          pw.append(s);

          // underline header
          String s2 = "";
          for (int i=3; i<s.length(); i++) s2 += "-";
          s2 += "\n";
          pw.append(s2);

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
          pw.append(s);
        }
        dateJustPrinted = beginDateStr;

        // print appointment time and description information
        s = beginTimeStr+" to "+endTimeStr+" ("+minutesSpan+" minutes):\t"+descriptionStr+"\n";
        pw.append(s);

      }

      // visually differentiate AppointmentBook from other output
      s="\n\n";
      pw.append(s);

      // Flush and Close the output file
      pw.flush();
      pw.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Method that nicely prints the AppointmentBook to the specified prtntWriter, passing any IOExecption
   * back to calling routine
   *
   * @param  passedAbstractAppointmentBook : the AppointmentBook to dump
   * @param  passedAbstractAppointmentBook : the primtWriter to print to
   * @throws IOException                   : errors associated with file writes
   */
  public void prettyPrintRange(AbstractAppointmentBook passedAbstractAppointmentBook, PrintWriter pw,
                               Date searchBeginDateTime, Date searchEndDateTime) throws IOException {

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

        Object a = iterator.next();        // next appointment extracted as Object
        Appointment ac = (Appointment) a;  // cast Object to Appointment

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
        long minutesSpan = (endDateTimeDate.getTime() - beginDateTimeDate.getTime()) / 1000 / 60;

        // if first line print header
        if (firstLine) {
          Date date = new Date(); // current date and time

          pw.append("\n\nAppointmentBook for "+ownerStr+" as of "+date.toString()+":\n");
          s = "Searching for appointments overlapping between "+searchBeginDateTime+" and "+searchEndDateTime+"\n";
          pw.append(s);

          // underline header
          String s2 = "";
          for (int i = 1; i < s.length(); i++)
            s2 += "-";
          s2 += "\n";
          pw.append(s2);

          // signal header printed
          firstLine = false;
        }

        // check for and print overlapping appointments
        if (!(ac.getEndDateTime().before(searchBeginDateTime) || ac.getBeginDateTime().after(searchEndDateTime)) ) {

          // extract DateTime parts as strings
          String[] beginDateTimeParts = beginDateTimeStr.split(" ");
          String beginDateStr = beginDateTimeParts[0];
          String beginTimeStr = beginDateTimeParts[1] + " " + beginDateTimeParts[2];
          String[] endDateTimeParts = endDateTimeStr.split(" ");
          String endTimeStr = endDateTimeParts[1] + " " + endDateTimeParts[2];

          // if new StartDate, print the date and preceded by a blank line for visual grouping
          if (!dateJustPrinted.equals(beginDateStr))
            pw.append("\nOn " + beginDateStr + ":\n");
          dateJustPrinted = beginDateStr;

          // print appointment time and description information
          pw.append(beginTimeStr+" to "+endTimeStr+" ("+minutesSpan+" minutes):\t"+descriptionStr+"\n");
        }
      }

      // visually differentiate AppointmentBook from other output
      pw.append("\n\n");

      // Flush and Close the output file
      pw.flush();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }








}

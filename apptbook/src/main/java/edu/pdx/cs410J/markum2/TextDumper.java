package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
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
   * Class that dumps the contents of AppointmentBook to specified textfile, passing any IOExecptions
   * back to calling routine
   *
   * @param AbstractAppointmentBook : the AppointmentBook to dump
   * @throws IOException            : errors associated with file writes
   */
  public void dump(AbstractAppointmentBook passedAbstractAppointmentBook) throws IOException {

    // Cast abstractAppointmentBook to concrete AppointmentBook in order to access elemente
    AppointmentBook nonAbstractAppointmentBook = (AppointmentBook)passedAbstractAppointmentBook;

    // Set up AppointmentBook iterator
    Iterator iterator = nonAbstractAppointmentBook.apptBook.iterator();

    try {

      // iterate through all appointments in the appointmentBook
      while (iterator.hasNext()) {

        Object a = iterator.next();       // next appointment extracted as Object
        Appointment ac = (Appointment)a;  // cast Object to Appointment

        // Split appointment by CSV
        String[] apptParts = a.toString().split(",");

        // Convert Dates to strings
        String beginDateTime = ac.getBeginTimeString();
        String endDateTime = ac.getEndTimeString();

        // Build string to write in order to optimize write performance
        String s = apptParts[0]+","+apptParts[1]+", "+beginDateTime+", "+endDateTime+"\n";

        // Write and flush appointment information using CSV format to output file
        pw.append(s);
        pw.flush();
      }

      // Close the output file
      pw.close();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }
}


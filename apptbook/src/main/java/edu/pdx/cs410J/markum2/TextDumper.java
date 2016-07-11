package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;

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
  public void dump(AbstractAppointmentBook AbstractAppointmentBook) throws IOException {

    // Cast abtractAppointmentBook to concrete AppointmentBook in order to access elemente
    AppointmentBook nonAbstractAppointmentBook = (AppointmentBook)AbstractAppointmentBook;

    // Appointment to add to output file
    Appointment appt;

    try {

      // iterate through all appointments in the appointmentBook
      for (int i = 0; i <= nonAbstractAppointmentBook.apptBook.size() - 1; i++) {

        // extract next appointment from appointmentBoob
        appt = nonAbstractAppointmentBook.apptBook.get(i);

        // Deconstruct the last 2 fields
        String[] apptParts = appt.getDescription().split(",");

        // Deconstruct next to the last field into beginning date and time
        String beginDateTime = apptParts[2].trim();
        String[] beginDateTimeParts = beginDateTime.split(" ");
        String beginDate = beginDateTimeParts[0]; String beginTime = beginDateTimeParts[1];

        // Deconstruct last field into ending date and time
        String endDateTime = apptParts[3].trim();
        String[] endDateTimeParts = endDateTime.split(" ");
        String endDate = endDateTimeParts[0]; String endTime = endDateTimeParts[1];

        // Build string to write in order to optimize write performance
        String s = apptParts[0]+","+apptParts[1]+", "+beginDate+", "+beginTime+", "+endDate+", "+endTime+"\n";

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


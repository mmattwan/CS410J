package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;

/**
 * Class that reads the contents of a text file and from it creates an appointment book
 * with its associated appointments.
 *
 * @author Markus Mattwandel
 * @version 2016.07.13
 */

class TextDumper implements AppointmentBookDumper {

  private PrintWriter pw;      // Dumping destination

  public TextDumper(String fileName) throws IOException {
    this(new File(fileName));
  }
  private TextDumper(File file) throws IOException {
    this(new PrintWriter(new FileWriter(file), true));
  }
  private TextDumper(PrintWriter pw) {
    this.pw = pw;
  }


  public void dump(AbstractAppointmentBook AbstractAppointmentBook) throws IOException {

    AppointmentBook nonAbstractAppointmentBook = (AppointmentBook)AbstractAppointmentBook;
    Appointment appt;

//    try {
      for (int i = 0; i <= nonAbstractAppointmentBook.apptBook.size() - 1; i++) {
        appt = nonAbstractAppointmentBook.apptBook.get(i);

        String[] apptParts = appt.getDescription().split(",");
        String beginDateTime = apptParts[2].trim();
        String[] beginDateTimeParts = beginDateTime.split(" ");
        String beginDate = beginDateTimeParts[0]; String beginTime = beginDateTimeParts[1];
        String endDateTime = apptParts[3].trim();
        String[] endDateTimeParts = endDateTime.split(" ");
        String endDate = endDateTimeParts[0]; String endTime = endDateTimeParts[1];

        pw.append(apptParts[0]+","+apptParts[1]+", "+beginDate+", "+beginTime+", "+endDate+", "+endTime+"\n");
        pw.flush();
      }
      pw.close();
//    }
//    catch (IOException ex) {
//      ex.printStackTrace();
//    }
  }
}


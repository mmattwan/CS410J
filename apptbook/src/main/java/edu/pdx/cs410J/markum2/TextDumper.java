package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that reads the contents of a text file and from it creates an appointment book
 * with its associated appointments.
 *
 * @author Markus Mattwandel
 * @version 2016.07.13
 */

public class TextDumper implements AppointmentBookDumper {

  private PrintWriter pw;      // Dumping destination

  public TextDumper(String fileName) throws IOException {
//    this(new File(fileName));
  }

  public TextDumper(File file) throws IOException {
    this(new PrintWriter(new FileWriter(file), true));
  }

  public TextDumper(PrintWriter pw) {
    this.pw = pw;
  }

//  @Override
  public void dump(AbstractAppointmentBook aAB) throws IOException {

    AppointmentBook nonAbstractAppointmentBook = (AppointmentBook)aAB;

    System.out.println(nonAbstractAppointmentBook.apptBook.size());

    Appointment a;

    for (int i=0; i<nonAbstractAppointmentBook.apptBook.size()-1; i++) {
      a = nonAbstractAppointmentBook.apptBook.get(i);
      System.out.println("In TextDumper: "+a.getDescription());
    }

  }


}




/*
// TODO: port to TextDumper

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

      // loop through all Appointments in AppointmentBook
      // TODO: add loop through AddressBook
      bw.write(owner+", "+description + ", " + beginDate + ", " + beginTime + ", " + endDate + ", " + endTime + "\n");

      // close the file
      bw.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
*/

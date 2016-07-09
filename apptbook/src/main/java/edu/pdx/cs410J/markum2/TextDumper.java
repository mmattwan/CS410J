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

/*
public interface AppointmentBookDumper {
  void dump(AbstractAppointmentBook var1) throws IOException;
}
*/
class TextDumper implements AppointmentBookDumper {

  private PrintWriter pw;      // Dumping destination

  public TextDumper(String fileName) throws IOException {
    this(new File(fileName));
  }

  public TextDumper(File file) throws IOException {
    this(new PrintWriter(new FileWriter(file), true));
  }

  public TextDumper(PrintWriter pw) {
    this.pw = pw;
  }

  public void dump(AbstractAppointmentBook apptBook) throws IOException {

/*
    System.out.println("These are a total of "+apptBook.size()+" appointments as follows:");
    Appointment a;
    for (int i=0; i<apptBook.size()-1; i++) {
      a = apptBook.get(i);
      System.out.println(a.getDescription());
    }
*/
  }


}




/*
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
*/

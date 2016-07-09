package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.*;

import static edu.pdx.cs410J.markum2.Project2.newAppointmentBook;

/**
 * Class that reads the contents of a text file and from it creates an appointment book
 * with its associated appointments.
 *
 * @author Markus Mattwandel
 * @version 2016.07.13
 */

class TextParser implements AppointmentBookParser {

  private LineNumberReader in;     // Read input from here
  private String owner;            // The owner for the AppointmentBook

// CONSTRUCTORS
  public TextParser(String fileName, String thisOwner) throws FileNotFoundException {

    this(new File(fileName));
    owner = thisOwner;

  }

  public TextParser(File file) throws FileNotFoundException {
    this(new FileReader(file));
  }

  public TextParser(Reader reader) {
    this.in = new LineNumberReader(reader);
  }

// INSTANCE METHODS
  private void error(String message) throws ParserException {
    int lineNumber = this.in.getLineNumber();
    String m = "Error at line " + lineNumber + ": " + message;
    throw new ParserException(m);
  }

  @Override
  public AppointmentBook parse() throws ParserException {

    Integer lineNum = 1;

    try {
      while (this.in.ready()) {
        String line = this.in.readLine();
//        System.out.println("Read "+lineNum+": "+line);

        // Deconstruct .CSV lines
        String[] parts = line.split(",");

        // Make sure there are 6 elements
        if (parts.length != 6) error("Malformed input file.");

        // Parts out fields
        String readOwner = parts[0].trim();
        String description = parts[1].trim();
        String beginDate = parts[2].trim();
        String beginTime = parts[3].trim();
        String endDate = parts[4].trim();
        String endTime = parts[5].trim();

        // Make sure there are 6 elements
        if (!readOwner.equals(owner)) error("Wrong Owner.");

        // Construct new Appointment
        Appointment newAppointment = new Appointment(owner, description, beginDate + " " + beginTime, endDate + " " + endTime);

        // Add it to the AppointmentBook
        newAppointmentBook.addAppointment(newAppointment);

        // increment line number
        lineNum++;
      }
    }
    catch (IOException ex) {
      int lineNumber = this.in.getLineNumber();
      String m = "Parsing error at line " + lineNum;
      throw new ParserException(m);
    }
    return null;
  }
}

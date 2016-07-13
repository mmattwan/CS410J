package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static edu.pdx.cs410J.markum2.Project3.newAppointmentBook;

/**
 * Class that parses the contents of a text file and from it creates an AppointmentBook
 * with its associated appointments.  Checks for correct owner, correct line format, and
 * IOExceptions, communicating errors back to calling method if necessary.
 *
 * Some code adapted from David Whitlocks's family.TextParser example.
 *
 * @author Markus Mattwandel
 * @version 2016.07.13
 */
class TextParser implements AppointmentBookParser {

  private LineNumberReader in;     // Read input from here
  private String owner;            // The owner for the AppointmentBook

  /**
   * Constructors to read AppointmentBook textfile.
   *
   * @param fileName  : name of AppointmentBook file to parse
   * @param thisOwner : owner of new AppointmentBook
   * @throws FileNotFoundException : signals file not found to calling method
   */
  public TextParser(String fileName, String thisOwner) throws FileNotFoundException {
    this(new File(fileName));
    owner = thisOwner;
  }
  private TextParser(File file) throws FileNotFoundException {
    this(new FileReader(file));
  }
  private TextParser(Reader reader) {
    this.in = new LineNumberReader(reader);
  }

  /**
   * Helper method to refactor parse method.  Formats error message to throw back to
   * calling method.
   *
   * @param message          : message to pass back
   * @throws ParserException : exception associated with file writes
     */
  private void error(String message) throws ParserException {
    int lineNumber = this.in.getLineNumber();
    String m = "Error at line " + lineNumber + ": " + message;
    throw new ParserException(m);
  }

  /**
   * parser method that parses the contents of a text file and from it creates an AppointmentBook
   * with its associated appointments.  Checks for correct owner, correct line format, and
   * IOExceptions, communicating errors back to calling method if necessary.
   *
   * @throws ParserException : exception associated with file writes
   */
  public AppointmentBook parse() throws ParserException {

    // catch parsing exceptions to send back to calling method
    try {

      // iterate until last line is read
      while (this.in.ready()) {

        // read next line of Appointments
        String line = this.in.readLine();

        // Deconstruct .CSV lines
        String[] parts = line.split(",");

        // Make sure there are 4 elements
        if (parts.length != 4) error("Malformed input file.");

        // Parse out fields
        String readOwner = parts[0].trim();
        String description = parts[1].trim();
        String beginDateTime = parts[2].trim();
        String endDateTime = parts[3].trim();

        // convert to Date class
        Date beginDateTimeDate = null, endDateTimeDate = null;;
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        try {
          beginDateTimeDate = df.parse(beginDateTime);
        }
        catch (ParseException ex) {
          System.err.println("** Bad Date: "+beginDateTime);
          System.exit(1);
        }
        try {
          endDateTimeDate = df.parse(endDateTime);
        }
        catch (ParseException ex) {
          System.err.println("** Bad Date: "+endDateTime);
          System.exit(1);
        }

        // Make sure owner matches
        if (!readOwner.equals(owner)) error("Wrong Owner.");

        // Construct new Appointment
        Appointment newAppointment = new Appointment(owner, description, beginDateTimeDate, endDateTimeDate);

        // Add new Appointment to the AppointmentBook
        newAppointmentBook.addAppointment(newAppointment);

      }
    }
    // communicate IO exceptions to pass back to calling method
    catch (IOException ex) {
      int lineNumber = this.in.getLineNumber();
      String m = "Parsing error at line " + lineNumber;
      throw new ParserException(m);
    }

    return null;
  }
}

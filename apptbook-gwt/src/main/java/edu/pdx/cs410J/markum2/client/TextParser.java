package edu.pdx.cs410J.markum2.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.Date;


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

    AppointmentBook parsedAppointmentBook = new AppointmentBook();

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

        // convert to Date class for begin and end dateTimes
        Date beginDate = stringToDate(beginDateTime);
        Date endDate = stringToDate(endDateTime);

        // Make sure owner matches
        if (!readOwner.equals(owner)) error("Wrong Owner.");

        // Construct new Appointment
        Appointment newAppointment = new Appointment(owner, description, beginDate, endDate);

        // Add new Appointment to the AppointmentBook
        parsedAppointmentBook.addAppointment(newAppointment);

      }
    }
    // communicate IO exceptions to pass back to calling method
    catch (IOException ex) {
      int lineNumber = this.in.getLineNumber();
      String m = "Parsing error at line " + lineNumber;
      throw new ParserException(m);
    }

    return parsedAppointmentBook;
  }

  /**
   * Private method to convert passed Date to string
   * @param  dateString : string containing date of format dd/MM/yyyy hh:mm a
   * @return Date       : passed string converted to Date
   */
  private Date stringToDate(String dateString) {
    Date result = null;
    try {
      DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
      result = dateTimeFormat.parse(dateString);
    }
    catch (Exception ex) {
//      alert(ex);
    }
    return result;
  }

}

package edu.pdx.cs410J.markum2;

/**
 * The main class for the CS410J appointment book Project 1.
 *
 * @author Markus Mattwandel
 * @version 2016.06.30
 */
public class Project1 {

  /**
   * Validates that date contains 1 or 2 digits for day and month, and 4 digits for year
   *
   * @param  dateString string to validate
   * @return boolean    string adheres to required format
     */
  private static boolean validDate(String dateString) {
    return (dateString.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})"));
  }

  /**
   * Validates that time contains 1 or 2 digits for hour and 2 digits for minutes
   *
   * @param  timeString string to validate
   * @return boolean    string adheres to required format
   */
  private static boolean validTime(String timeString) {
    return (timeString.matches("([0-9]{1,2}):([0-9]{2})"));
  }

  /**
   * Prints README
   */
  private static void printReadme() {
    System.out.println("\n\nCS410J Project 1");
    System.out.println("Author: Markus Mattwandel\n");
    System.out.println("This program creates an appointment book and adds a single appointment.");
    System.out.println("Only 2 options are supported and must precede appointment information:");
    System.out.println("  -print\t to print the appointment (provided it can be created)");
    System.out.println("  -README\t to print this message\n");
    System.out.println("Appointments must adhere to the following format in the order indicated:");
    System.out.println("  owner:\t the owner of the appointment book");
    System.out.println("  description:\t the description of the appointment");
    System.out.println("  beginDate:\t the beginning date");
    System.out.println("  beginTime:\t the beginning time");
    System.out.println("  endDate:\t the ending date");
    System.out.println("  endTime:\t the ending time\n");
    System.out.println("Time can have 1 or 2 digits for hour, but must have 2 digits for minutes.");
    System.out.println("Date can have 1 or 2 digits for month and day, but must have 4 digits for year.");
    System.out.println("owner and description can use double quotes to span multiple words.\n");
    System.out.println("Limited error checking is enabled:");
    System.out.println("  Date and time formats are checked");
    System.out.println("  Correct number of appointment arguments are checked\n");
    System.out.println("The appointment is printed after being created if -print was specified.\n");
  }

  /**
   * main for program.  Parses command line, processes options, and if arguments are valid,
   * creates appointment, adds it to appointment book, and prints the new appointmnet if
   * specified
   *
   * @param     args   command line arguments
   */
  public static void main(String[] args) {

    Boolean printOption = Boolean.FALSE;  // set if -print cmd line option found
    Boolean readmeOption = Boolean.FALSE; // set if -README cmd line option found

    // 0 args is an error
    if (args.length==0) { System.err.println("Missing command line arguments"); System.exit(1); }

    // 1+ args could contain an option as the 1st arg
    if (args.length>0)
      if (args[0].contains("-print")) printOption=Boolean.TRUE;
      else if (args[0].contains("-README")) readmeOption=Boolean.TRUE;

    // 2+ args could contain an option as the 2nd arg
    if (args.length>1)
      if (args[1].contains("-print")) printOption=Boolean.TRUE;
      else if (args[1].contains("-README")) readmeOption=Boolean.TRUE;

    // print README and exit if -README found
    if (readmeOption) { printReadme(); System.exit(0); }

    // -print by itself is an error since no appointment information can be added
    if (args.length==1 && printOption) { System.err.println("No appointment details"); System.exit(1); }

    // Need 6 args to make appointment
    if (args.length<6 && !printOption) { System.err.println("Not enough details to make appointment."); System.exit(1); }

    // Need 7 args to make and print appointment
    if (args.length<7 && printOption)  { System.err.println("Not enough details to make and print appointment."); System.exit(1); }

    // Adjust appointment args offset based on whether -print is specified
    Integer offset = 0;
    if (printOption) offset = 1;

    // Extract appointment details from args
    String owner = args[offset];
    String description = args[1+offset];
    String beginDate = args[2+offset];
    String beginTime = args[3+offset];
    String endDate = args[4+offset];
    String endTime = args[5+offset];

    // Check beginning as well as end date and time formats and exit if any are not valid
    if (!validDate(beginDate)) { System.out.println("Bad beginDate format."); System.exit(1); }
    if (!validTime(beginTime)) { System.out.println("Bad beginTime format."); System.exit(1); }
    if (!validDate(endDate))   { System.out.println("Bad endDate format.");   System.exit(1); }
    if (!validTime(endTime))   { System.out.println("Bad endTime format.");   System.exit(1); }

    // Construct AppointmentBook
    AppointmentBook newAppointmentBook = new AppointmentBook(owner);

    // Construct Appointment
    Appointment newAppointment = new Appointment(owner, description, beginDate+" "+beginTime, endDate+" "+endTime);

    // Add new Appointment to AppointmentBook
    newAppointmentBook.addAppointment(newAppointment);

    // if -print specified, print appointment
    if (printOption) System.out.println(newAppointment.getDescription());

    System.exit(0);
  }

}
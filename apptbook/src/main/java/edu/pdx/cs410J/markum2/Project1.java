package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointmentBook;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project1 {

  private static boolean checkDate(String dateString) {
    return (dateString.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})"));
  }

  private static boolean checkTime(String timeString) {
    return (timeString.matches("([0-9]{1,2}):([0-9]{2})"));
  }

  public static void main(String[] args) {

  Boolean printOption = Boolean.FALSE;  // set if -print cmd line option found
  Boolean readmeOption = Boolean.FALSE; // set if -README cmd line option found

  // for (int i=0; i<args.length; i++ ) System.out.println(args[i]);

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

  // -print by itself is an error since no appt info entered
  if (args.length==1 && printOption) { System.err.println("No appt details"); System.exit(1); }

  // Need 6 args to make appt
  if (args.length<6 && !printOption) { System.err.println("Not enough details to make appt."); System.exit(1); }

  // Need 7 args to make and print appt
  if (args.length<7 && printOption)  { System.err.println("Not enough details to make and print appt."); System.exit(1); }

  // -README as either arg prints readme info and exits
  if (readmeOption) { System.out.println("-README entered"); System.exit(0); }

  // Adjust appt info offset based on whether -print is specified
  Integer offset = 0;
  if (printOption) offset = 1;

  // Extract appt details from args
  String owner = args[offset];
  String description = args[1+offset];
  String beginDate = args[2+offset];
  String beginTime = args[3+offset];
  String endDate = args[4+offset];
  String endTime = args[5+offset];

  // Check beginning and end date and time formats
  if (!checkDate(beginDate)) { System.out.println("Bad beginDate format."); System.exit(1); }
  if (!checkTime(beginTime)) { System.out.println("Bad beginTime format."); System.exit(1); }
  if (!checkDate(endDate))   { System.out.println("Bad endDate format.");   System.exit(1); }
  if (!checkTime(endTime))   { System.out.println("Bad endTime format.");   System.exit(1); }

  // If we've made it this far, we can finally create an appt

  // Class c = AbstractAppointmentBook.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath

  // Print appt if -print specified
  if (printOption) {
    System.out.println("\n"+owner + " has an appt entitled \"" + description + "\"");
    System.out.println("\tstarting on " + beginDate + " at " + beginTime);
    System.out.println("\tand ending on " + endDate + " at " + endTime + ".");
  }

  System.exit(0);
  }

}
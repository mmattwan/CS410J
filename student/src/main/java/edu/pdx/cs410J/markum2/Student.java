package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.lang.Human;

import java.util.ArrayList;
                                                                                    
/**                                                                                 
 * This class is represents a <code>Student</code>.                                 
 */                                                                                 
public class Student extends Human {

  private String sName;
  private ArrayList<String> sCourses;
  private Double sGpa;
  private String sGender;

  /**                                                                               
   * Creates a new <code>Student</code>                                             
   *                                                                                
   * @param name                                                                    
   *        The student's name                                                      
   * @param classes                                                                 
   *        The names of the classes the student is taking.  A student              
   *        may take zero or more classes.                                          
   * @param gpa                                                                     
   *        The student's grade point average                                       
   * @param gender                                                                  
   *        The student's gender ("male" or "female", case insensitive)             
   */                                                                               
  public Student(String name, ArrayList classes, double gpa, String gender) {

    super(name);

    this.sName = name;
    this.sCourses = classes;
    this.sGpa = gpa;
    this.sGender = gender;
  }

  /**                                                                               
   * All students say "This class is too much work"
   */
  @Override
  public String says() {
    return("This class is too much work");
  }
                                                                                    
  /**                                                                               
   * Returns a <code>String</code> that describes this                              
   * <code>Student</code>.                                                          
   */                                                                               
  public String toString() {
        return(sName+" has a GPA of "+sGpa+" and is taking "+sCourses.size()+" classes: "+sCourses+".  ");
  }

  /**
   * Main program that parses the command line, creates a
   * <code>Student</code>, and prints a description of the student to
   * standard out by invoking its <code>toString</code> method.
   */
  public static void main(String[] args) {

//  Courses are located at the end of the list.  Read them into an ArrayList
    ArrayList<String> s1Courses = new ArrayList<>();
    for (Integer i=3; i<args.length; i++)   s1Courses.add(args[i]);

//  Create new Student Object
    Student s1 = new Student(args[0],s1Courses, Double.parseDouble(args[2]), args[1]);

//  Print Student info
    System.out.println("\n"+s1.toString()+"He says \""+s1.says()+"\".");

    if (args.length == 0) {
      System.err.println("Missing command line arguments");
      System.exit(1);
    }
  }
}
package edu.pdx.cs410J.markum2;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {

  String owner;
  String description;
  String beginTime;
  String endTime;

  public Appointment(String owner, String description, String beginTime, String endTime)
  {
    System.out.println("Constructing appointment for "+owner);
    this.owner = owner;
    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  @Override
  public String getBeginTimeString() // Ignore for Project 1
  {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getEndTimeString()   // Ignore for Project 1
  {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }

  @Override
  public String getDescription()
  {
    return "This method is not implemented yet";
  }


}

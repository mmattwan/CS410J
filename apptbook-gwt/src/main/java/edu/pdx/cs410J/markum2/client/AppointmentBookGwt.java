package edu.pdx.cs410J.markum2.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Iterator;

/**
 * Client Class for CS410J Project 5
 */
public class AppointmentBookGwt implements EntryPoint {

  private final Alerter alerter;
  private AppointmentBook book;

  private TextBox ownerTextBox, startTextBox, endTextBox, descTextBox;
  private Button createButton;
  private Button exportButton;
  private Button importButton;
  private TextBox exportTextBox, importTextBox;
  private Button helpButton;
  private TextBox textBox;
  private TextArea appointmentBookTextArea;

  public AppointmentBookGwt() {

    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
      });
    }

  /**
   * Validates that time contains 1 or 2 digits for hour and 2 digits for minutes
   *
   * @param  timeString string to validate
   * @return boolean    string adheres to required format
   */
  private static boolean validDateTime(String dateString) {
    return (dateString.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4}) ([0-9]{1,2}):([0-9]{2}) (am|pm|AM|PM)"));
  }

  /**
   * Return README string
   */
  private String toStringReadme() {
    return ("CS410J Project 5\n"+
            "Author: Markus Mattwandel\n"+
            "\n"+
            "This webpage implements an AppointmentBook\n"+
            "\n"+
            "The Create/Search button creates the Appointment specified in the" +
            "Owner, Description, Start and End fields or searches and updates" +
            "display for all appointments that overlap Start and End.\n"+
            "\n"+
            "Start and End must be of the following format:\n"+
            "   1-2_digit_day/1-2_digit_month/4_digit_year follower by\n" +
            "   1-2_digit_hour:2_digit_minute AM_or_PM\n"+
            "\n"+
            "All active Appointments are displayed in the text field on the right,\n" +
            "unless a search occurred in which case only the Appointments\n" +
            "meeting the search criteria are displayed until another add or\n" +
            "search occurs.\n"+
            "\n"+
            "Appointments are only active as long as the webpage is loaded.");
  }

  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;
    addWidgets();
  }

  private void addWidgets() {

    this.ownerTextBox = new TextBox();
    this.descTextBox = new TextBox();
    this.startTextBox = new TextBox();
    this.endTextBox = new TextBox();
    this.exportTextBox = new TextBox();
    this.importTextBox = new TextBox();
    this.appointmentBookTextArea = new TextArea();
    exportButton = new Button("Write to file");
    importButton = new Button("Read from file");
    helpButton = new Button("Help");
    this.textBox = new TextBox();

    createButton = new Button("Create Appointment");
    createButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {

        // Make sure all textBoxes contain values
        if (ownerTextBox.getText() == "" || descTextBox.getText() == "" ||
            startTextBox.getText() == "" || endTextBox.getText() == "") {
          Window.alert("Owner, Description, Start, and End must be specified");
          return;
        }
        // If Owner not set, set it
        if (book.getOwnerName() == null) {
          Window.alert("Setting AppointmentBook owner to " + ownerTextBox.getText());
          book.setOwnerName(ownerTextBox.getText());
        }
        // Make sure Owner is the same
        else if (book.getOwnerName() != ownerTextBox.getText()) {
          Window.alert(ownerTextBox.getText()+" is not"+book.getOwnerName());
          return;
        }
        // Validate Start and End
        if (!validDateTime(startTextBox.getText())) {
          Window.alert("Bad Start time.  Example: 01/01/2016 10:00 AM");
          return;
        }
        if (!validDateTime(endTextBox.getText())) {
          Window.alert("Bad End time.  Example: 01/01/2016 10:00 AM");
          return;
        }

        String s = ownerTextBox.getText()+",\""+descTextBox.getText()+"\","+startTextBox.getText()+","+endTextBox.getText();
        appointmentBookTextArea.setText(s);

        createAppointments();
      }
    });

    helpButton = new Button("Help");
    helpButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        Window.alert(toStringReadme());
      }
    });

   }

  private void createAppointments() {
    PingServiceAsync async = GWT.create(PingService.class);
    int numberOfAppointments = getNumberOfAppointments();
    async.createAppointmentBook(numberOfAppointments, new AsyncCallback<AppointmentBook>() {

      @Override
      public void onSuccess(AppointmentBook book) {
        displayInAlertDialog(book);
      }

      @Override
      public void onFailure(Throwable ex) {
         alert(ex);
      }
    });
  }

  private int getNumberOfAppointments() {
    String number = this.textBox.getText();
    return Integer.parseInt(number);
  }

  private void displayInAlertDialog(AppointmentBook airline) {

    String s = "hello from displayInAlertDialog";

    // Set up AppointmentBook iterator
    Iterator iterator = airline.apptBook.iterator();

    while (iterator.hasNext()) {
      s += "iterating";
      Object a = iterator.next();       // next appointment extracted as Object
      Appointment ac = (Appointment)a;  // cast Object to Appointment

      s += ac.getDescription();
      s += ac.getBeginTimeString();
      s += ac.getEndTimeString();
      s += ac.getEndTimeString();
    }
    appointmentBookTextArea.setText(s);
//    alerter.alert(s);
  }

  private void alert(Throwable ex) {
    alerter.alert(ex.toString());
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }

  @Override
  public void onModuleLoad() {

    book = new AppointmentBook();

    RootPanel rootPanel = RootPanel.get();

    appointmentBookTextArea.setHeight("400");
    appointmentBookTextArea.setVisibleLines(40);
    appointmentBookTextArea.setCharacterWidth(100);
    appointmentBookTextArea.setReadOnly(true);

    ownerTextBox.setVisibleLength(19);
    startTextBox.setVisibleLength(19);
    endTextBox.setVisibleLength(19);
    descTextBox.setVisibleLength(30);
    exportTextBox.setVisibleLength(30);
    importTextBox.setVisibleLength(30);

    VerticalPanel vertPanel1 = new VerticalPanel(), vertPanel2 = new VerticalPanel(), vertPanel3 = new VerticalPanel();
    vertPanel1.setSpacing(18);                      vertPanel2.setSpacing(17);        vertPanel3.add(appointmentBookTextArea);
    vertPanel1.add(new Label("Owner"));             vertPanel2.add(ownerTextBox);
    vertPanel1.add(new Label("Description"));       vertPanel2.add(descTextBox);
    vertPanel1.add(new Label("Start"));             vertPanel2.add(startTextBox);
    vertPanel1.add(new Label("End"));               vertPanel2.add(endTextBox);
    vertPanel1.add(createButton);                   vertPanel2.add(new Label("."));
    vertPanel1.add(exportButton);                   vertPanel2.add(exportTextBox);
    vertPanel1.add(importButton);                   vertPanel2.add(importTextBox);
    vertPanel1.add(helpButton);

    HorizontalPanel horizontalPanel = new HorizontalPanel();
    horizontalPanel.add(vertPanel1);           horizontalPanel.add(vertPanel2);  horizontalPanel.add(vertPanel3);
    rootPanel.add(horizontalPanel);
    rootPanel.add(textBox);
  }
}
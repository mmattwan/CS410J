package edu.pdx.cs410J.markum2.client;

// gwt classes
import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

// Java classes
import java.util.Date;
import java.util.Iterator;

/**
 * Client Class for CS410J Project 5
 *
 * TODO: Fix owner
 * TODO: add Import/Export
 * TODO: add search
 */
public class AppointmentBookGwt implements EntryPoint {

  // for displaying Exceptions
  private final Alerter alerter;

  // The Appointment Book
  private AppointmentBook book;

  // The UI Widgets
  private TextBox ownerTextBox, startTextBox, endTextBox, descTextBox;
  private Button createButton;
  private Button exportButton, importButton;
  private TextBox exportTextBox, importTextBox;
  private Button helpButton;
  private TextArea bookTextArea;

  public AppointmentBookGwt() {

    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
      });
    }
    @VisibleForTesting
    interface Alerter {
      void alert(String message);
    }
    @VisibleForTesting
    private AppointmentBookGwt(Alerter alerter) {
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
      this.bookTextArea = new TextArea();

      // Import Button
      importButton = new Button("Read from file");
      importButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent clickEvent) {
          Window.alert("Import clicked");
        }
      });

      // Export Button
      exportButton = new Button("Write to file");
      exportButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent clickEvent) {
          Window.alert("Export clicked");
        }
      });

      // Help Button
      helpButton = new Button("Help");
      helpButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent clickEvent) {
          Window.alert(toStringReadme());
        }
      });

      // Create button
      this.createButton = new Button("Create/Search Appointment");
      createButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if (!validateFields())
          return;
        else
          createAppointments();
      }
    });
  }

  private void createAppointments() {

    Date startDateTime = stringToDate(startTextBox.getText());
    Date endDateTime = stringToDate(endTextBox.getText());

    PingServiceAsync async = GWT.create(PingService.class);
    async.createAppointmentBook(ownerTextBox.getText(), descTextBox.getText(),startDateTime, endDateTime, new AsyncCallback<AppointmentBook>() {

      @Override
      public void onSuccess(AppointmentBook book) {
        Date rangeStart = stringToDate(startTextBox.getText());
        Date rangeEnd = stringToDate(endTextBox.getText());
        if (descTextBox.getText().isEmpty())
          prettyPrintToTextArea(book, rangeStart, rangeEnd);
        else
          prettyPrintToTextArea(book, null, null);
      }
      @Override
      public void onFailure(Throwable ex) {
         alert(ex);
      }
    });
  }

  private void prettyPrintToTextArea(AppointmentBook book,Date rangeStart, Date rangeEnd) {

    String s="";
    boolean firstLine = true;     // signals first line, e.g. print header
    String dateJustPrinted = "";  // contains last date printed
    String prettyString = "";     // string to print to file or screen
    boolean printIt = false;

    bookTextArea.setText(prettyString);

    // Set up AppointmentBook iterator
    Iterator iterator = book.apptBook.iterator();

    // write Appointments to bookTextArea as CSV for now
    // TODO: replace with PrettyPrint
    while (iterator.hasNext()) {

      Object a = iterator.next();       // next appointment extracted as Object
      Appointment ac = (Appointment)a;  // cast Object to Appointment

      // Grab appointment strings
      String ownerStr = ac.getOwner();
      String descriptionStr = ac.getDescription();
      String beginDateTimeStr = ac.getBeginTimeString();
      String endDateTimeStr = ac.getEndTimeString();

      // Grab appointment begin and end Dates to calculate length
      long minutesSpan = (ac.getEndDateTime().getTime() - ac.getBeginDateTime().getTime()) / 1000 / 60;

      // if first line print header
      if (firstLine) {
        Date date = new Date(); // current date and time

        s += "\n AppointmentBook for "+ownerStr+ " as of "+date.toString()+":\n";
        prettyString += s;

        if (rangeStart != null && rangeEnd != null) {
          s = " Filtered on Appointments overlapping\n ";
          prettyString += s;
          s = rangeStart+" and "+rangeEnd+":\n";
          prettyString += s;
        }

        // underline header
        String s2 = " ";
        for (int i=1; i<s.length(); i++) s2 += "-";
        s2 += "\n";
        prettyString += s2;

        // signal header printed
        firstLine = false;

      }

      // check date args null before checking if in range
      if (rangeStart == null && rangeEnd == null) printIt = true;
      else if (ac.getEndDateTime().after(rangeStart) && ac.getBeginDateTime().before(rangeEnd) ) printIt = true;
      else printIt = false;

      if (printIt) {
        // extract DateTime parts as strings
        String[] beginDateTimeParts = beginDateTimeStr.split(" ");
        String beginDateStr = beginDateTimeParts[0];
        String beginTimeStr = beginDateTimeParts[1] + " " + beginDateTimeParts[2];
        String[] endDateTimeParts = endDateTimeStr.split(" ");
        String endTimeStr = endDateTimeParts[1] + " " + endDateTimeParts[2];

        // if new StartDate, print the date preceded by a blank line for visual grouping
        if (!dateJustPrinted.equals(beginDateStr)) {
          s = "\n On " + beginDateStr + ":\n";
          prettyString += s;
        }
        dateJustPrinted = beginDateStr;

        // print appointment time and description information
        s = " " + beginTimeStr + " to " + endTimeStr + " (" + minutesSpan + " minutes):\t" + descriptionStr + "\n";
        prettyString += s;
      }
    }
    bookTextArea.setText(prettyString);
  }

  private void alert(Throwable ex) {
    alerter.alert(ex.toString());
  }

  /**
   * Validates Appointment fields
   * @return true if valid Appointment fields
   */
  private boolean validateFields() {

    // Make sure all textBoxes contain values
    if (ownerTextBox.getText() == "" || startTextBox.getText() == "" || endTextBox.getText() =="") {
      Window.alert("Owner, Description, Start, and End must be specified to Add  -or-\n"+
                   "Owner, Start, and End must be specified to Search for Appointments.");
      return false;
    }
    // If Owner not set, set it
    if (book.getOwnerName() == null) {
      Window.alert("First Appointment!\nSetting AppointmentBook owner to " + ownerTextBox.getText());
      book.setOwnerName(ownerTextBox.getText());
    }
    // Make sure Owner is the same
    else if (!book.getOwnerName().equals(ownerTextBox.getText())) {
        Window.alert("Sorry, "+ownerTextBox.getText()+"!\n"+book.getOwnerName()+" owns this AppointmentBook.");
      return false;
    }
    // Validate Start and End Strings
    if (!validDateTime(startTextBox.getText())) {
      Window.alert("Bad Start time.  Example: 01/01/2016 10:00 AM");
      return false;
    }
    if (!validDateTime(endTextBox.getText())) {
      Window.alert("Bad End time.  Example: 01/01/2016 10:00 AM");
      return false;
    }
    return true;
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
      alert(ex);
    }
    return result;
  }

  /**
   * Returns string of passed Date.
   *
   * @return string : containing string version of date
   */
  private String dateToString(Date date) {
    String pattern = "M/dd/yyyy h:mm a";
    return DateTimeFormat.getFormat(pattern).format(date);
  }

  /**
   * Validates that time contains 1 or 2 digits for hour and 2 digits for minutes
   *
   * @param  dateString to validate
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
            "The Create/Search button creates the Appointment if Owner, Description\n"+
            "Start and End fields are specified.  If Description is not specified,\n"+
            "the display at the right will be updated with Appointments between Start.\n"+
            "and End.\n"+
            "\n"+
            "Start and End fields must be of the following format:\n"+
            "   1-2_digit_day/1-2_digit_month/4_digit_year follower by\n" +
            "   1-2_digit_hour:2_digit_minute AM_or_PM\n"+
            "\n"+
            "The Import button load all files specified in the corresponding\n"+
            "field into the appointment book.\n" +
            "\n"+
            "The Export button writes all Appointments in the Appointment book\n"+
            "to the file specified in the corresponding field\n"+
            "\n"+
            "AppointmentBook files must be of the following format:\n"+
            "   Owner, Description, startDateTime, endDataTime\n" +
            "Descriptions spanning spaces must be in double quotes and start and\n"+
            "end date/times must be of the smae format specified above.\n"+
            "\n"+
            "All active Appointments are displayed in the text field on the right,\n" +
            "unless a search occurred in which case only the Appointments\n" +
            "meeting the search criteria are displayed.\n" +
            "\n"+
            "Appointments are only active as long as the webpage is loaded.");
  }

  @Override
  public void onModuleLoad() {

    book = new AppointmentBook();

    RootPanel rootPanel = RootPanel.get();

    // Define input textbox sizes
    ownerTextBox.setVisibleLength(19);
    startTextBox.setVisibleLength(19);
    endTextBox.setVisibleLength(19);
    descTextBox.setVisibleLength(30);
    exportTextBox.setVisibleLength(30);
    importTextBox.setVisibleLength(30);

    // Auto-fill Start to current DateTime, and End to current DateTime + 1 hour
    Date curDate = new Date();
    Date curDatePlusOneHour = new Date(curDate.getTime() + (10000 * 60 * 6));

    startTextBox.setText(dateToString(curDate));
    endTextBox.setText(dateToString(curDatePlusOneHour  ));

    HorizontalPanel horizontalPanel = new HorizontalPanel();
    VerticalPanel vertPanel1 = new VerticalPanel(), vertPanel2 = new VerticalPanel(), vertPanel3 = new VerticalPanel();
    vertPanel1.setSpacing(18);                      vertPanel2.setSpacing(17);        bookTextArea.setHeight("400");
    vertPanel1.add(new Label("Owner"));             vertPanel2.add(ownerTextBox);     bookTextArea.setVisibleLines(40);
    vertPanel1.add(new Label("Description"));       vertPanel2.add(descTextBox);      bookTextArea.setCharacterWidth(80);
    vertPanel1.add(new Label("Start"));             vertPanel2.add(startTextBox);     bookTextArea.setReadOnly(true);
    vertPanel1.add(new Label("End"));               vertPanel2.add(endTextBox);       vertPanel3.add(bookTextArea);
    vertPanel1.add(createButton);                   vertPanel2.add(new Label("."));
    vertPanel1.add(importButton);                   vertPanel2.add(importTextBox);
    vertPanel1.add(exportButton);                   vertPanel2.add(exportTextBox);
    vertPanel1.add(helpButton);
    horizontalPanel.add(vertPanel1);                horizontalPanel.add(vertPanel2);  horizontalPanel.add(vertPanel3);
    rootPanel.add(horizontalPanel);
  }
}
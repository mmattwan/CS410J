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
import java.util.TreeSet;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {

  private final Alerter alerter;

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

        String s="";
        s += ownerTextBox.getText()+"\n";
        s += descTextBox.getText()+"\n";
        s += startTextBox.getText()+"\n";
        s += endTextBox.getText()+"\n";
        appointmentBookTextArea.setText(s);

        createAppointments();
      }
    });

    helpButton = new Button("Help");
    helpButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        Window.alert("help clicked");
      }
    });

   }

  private void createAppointments() {
    PingServiceAsync async = GWT.create(PingService.class);
    int numberOfAppointments = getNumberOfAppointments();
    async.createAppointmentBook(numberOfAppointments, new AsyncCallback<AppointmentBook>() {

      @Override
      public void onSuccess(AppointmentBook airline) {
        displayInAlertDialog(airline);
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
    RootPanel rootPanel = RootPanel.get();

    appointmentBookTextArea.setHeight("400");
    appointmentBookTextArea.setVisibleLines(40);
    appointmentBookTextArea.setCharacterWidth(100);
    appointmentBookTextArea.setReadOnly(true);

    ownerTextBox.setVisibleLength(30);
    startTextBox.setVisibleLength(19);
    endTextBox.setVisibleLength(19);
    descTextBox.setVisibleLength(30);
    exportTextBox.setVisibleLength(30);
    importTextBox.setVisibleLength(30);

    VerticalPanel vertPanel1 = new VerticalPanel(), vertPanel2 = new VerticalPanel(), vertPanel3 = new VerticalPanel();
    vertPanel1.setSpacing(20);                      vertPanel2.setSpacing(17);        vertPanel3.add(appointmentBookTextArea);
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

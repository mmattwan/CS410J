package edu.pdx.cs410J.markum2.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Collection;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
  private final Alerter alerter;

  @VisibleForTesting
  private Button button;
  private Button exportbutton;
  private Button importbutton;
  private Button showbutton;
  private Button helpbutton;
  private TextBox ownerTextBox, startTextBox, endTextBox, descTextBox;
  private TextBox exportTextBox, importTextBox;
  private TextBox textBox;

  public AppointmentBookGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

//  @VisibleForTesting
  AppointmentBookGwt(Alerter alerter) {
    this.alerter = alerter;
    addWidgets();
  }

  private void addWidgets() {
    button = new Button("Create Appointment");
    button.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        createAppointments();
      }
    });

    this.ownerTextBox = new TextBox();
    this.descTextBox = new TextBox();
    this.startTextBox = new TextBox();
    this.endTextBox = new TextBox();
    this.exportTextBox = new TextBox();
    this.importTextBox = new TextBox();

    exportbutton = new Button("Write to file");
    importbutton = new Button("Read from file");
    showbutton = new Button("Show Appointments");
    helpbutton = new Button("Help");

    this.textBox = new TextBox();
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
    StringBuilder sb = new StringBuilder(airline.toString());
    sb.append("\n");

    Collection<Appointment> flights = airline.getAppointments();
    for (Appointment flight : flights) {
      sb.append(flight);
      sb.append("\n");
    }
    alerter.alert(sb.toString());
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

    VerticalPanel verticalPanel1 = new VerticalPanel();  VerticalPanel verticalPanel2 = new VerticalPanel();
    verticalPanel1.setSpacing(20);                       verticalPanel2.setSpacing(17);
    verticalPanel1.add(new Label("Owner"));              verticalPanel2.add(ownerTextBox);
    verticalPanel1.add(new Label("Description"));        verticalPanel2.add(descTextBox);
    verticalPanel1.add(new Label("Start"));              verticalPanel2.add(startTextBox);
    verticalPanel1.add(new Label("End"));                verticalPanel2.add(endTextBox);
    verticalPanel1.add(button);                          verticalPanel2.add(new Label("."));
    verticalPanel1.add(exportbutton);                    verticalPanel2.add(exportTextBox);
    verticalPanel1.add(importbutton);                    verticalPanel2.add(importTextBox);
    verticalPanel1.add(showbutton);
    verticalPanel1.add(helpbutton);

    HorizontalPanel horizontalPanel = new HorizontalPanel();
    horizontalPanel.add(verticalPanel1);
    horizontalPanel.add(verticalPanel2);
    rootPanel.add(horizontalPanel);
    rootPanel.add(textBox);
  }
}

package edu.pdx.cs410J.markum2.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client-side interface to the ping service
 */
public interface HelpServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void showHelp();
}

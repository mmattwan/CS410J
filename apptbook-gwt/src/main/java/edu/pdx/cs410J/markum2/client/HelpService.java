package edu.pdx.cs410J.markum2.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A GWT remote service that returns a dummy appointment book
 */
@RemoteServiceRelativePath("ping")
public interface HelpService extends RemoteService {

  /**
   * Returns the current date and time on the server
   */
  public void showHelp ();

}



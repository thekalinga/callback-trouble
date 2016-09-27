package com.acme.broadcast;

public abstract class BroadcastReceiver {

  private boolean abortBroadcast;

  public void abortBroadcast() {
    abortBroadcast = true;
  }

  public boolean shouldAbort() {
    return abortBroadcast;
  }

  public abstract void handle(Intent intent);

}

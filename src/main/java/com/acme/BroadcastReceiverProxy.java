package com.acme;

public class BroadcastReceiverProxy {

  private BroadcastReceiver receiver;

  public BroadcastReceiverProxy(BroadcastReceiver receiver) {
    this.receiver = receiver;
  }

  public void abortBroadcast() {
    receiver.abortBroadcast();
  }

}

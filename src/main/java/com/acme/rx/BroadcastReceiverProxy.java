package com.acme.rx;

import com.acme.broadcast.BroadcastReceiver;

public class BroadcastReceiverProxy {

  private BroadcastReceiver receiver;

  public BroadcastReceiverProxy(BroadcastReceiver receiver) {
    this.receiver = receiver;
  }

  public void abortBroadcast() {
    receiver.abortBroadcast();
  }

}

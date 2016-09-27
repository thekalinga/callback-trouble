package com.acme.rx;

import com.acme.broadcast.Intent;

public class IntentAndReceiverProxy {
  public final Intent intent;
  public final BroadcastReceiverProxy proxy;

  public IntentAndReceiverProxy(Intent intent, BroadcastReceiverProxy proxy) {
    this.intent = intent;
    this.proxy = proxy;
  }
}

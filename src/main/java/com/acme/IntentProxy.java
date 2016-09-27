package com.acme;

public class IntentProxy {
  public final Intent intent;
  public final BroadcastReceiverProxy proxy;

  public IntentProxy(Intent intent, BroadcastReceiverProxy proxy) {
    this.intent = intent;
    this.proxy = proxy;
  }
}

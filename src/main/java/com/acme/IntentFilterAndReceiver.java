package com.acme;

public class IntentFilterAndReceiver {
  private BroadcastReceiver receiver;
  private IntentFilter filter;

  public IntentFilterAndReceiver(BroadcastReceiver receiver, IntentFilter filter) {
    this.receiver = receiver;
    this.filter = filter;
  }

  public BroadcastReceiver getReceiver() {
    return receiver;
  }

  public IntentFilter getFilter() {
    return filter;
  }
}

package com.acme.broadcast;

public class IntentFilter {
  private int priority;

  public IntentFilter(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return priority;
  }

}

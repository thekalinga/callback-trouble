package com.acme.platform;

import com.acme.BroadcastReceiver;
import com.acme.IntentFilter;
import com.acme.IntentFilterAndReceiver;

import java.util.Set;
import java.util.TreeSet;

public class Context {

  private Set<IntentFilterAndReceiver> highestToLowestPriorityReceivers =
      new TreeSet<>((o1, o2) -> o2.getFilter().getPriority() - o1.getFilter().getPriority());

  public void subscribe(BroadcastReceiver receiver, IntentFilter filter) {
    IntentFilterAndReceiver intentFilterAndReceiver = new IntentFilterAndReceiver(receiver, filter);
    highestToLowestPriorityReceivers.add(intentFilterAndReceiver);
  }

  public void reset() {
    highestToLowestPriorityReceivers.clear();
  }

  public Set<IntentFilterAndReceiver> getHighestToLowestPriorityReceivers() {
    return highestToLowestPriorityReceivers;
  }
}

package com.acme.broadcast.executor;

import com.acme.broadcast.Intent;
import com.acme.platform.IntentFilterAndReceiver;
import com.acme.platform.Context;

import java.util.Set;

public class VanillaOrderedBroadcastExecutor implements OrderedBroadcastExecutor {

  private final Context context;

  public VanillaOrderedBroadcastExecutor(Context context) {
    this.context = context;
  }

  @Override
  public void execute(Intent intent) {
    Set<IntentFilterAndReceiver> highestToLowestPriorityReceivers = context.getHighestToLowestPriorityReceivers();
    boolean shouldAbort = false;
    for (IntentFilterAndReceiver receiver : highestToLowestPriorityReceivers) {
      if (shouldAbort) {
        break;
      }
      try {
        receiver.getReceiver().handle(intent);
        shouldAbort = receiver.getReceiver().shouldAbort();
      } catch (Throwable e) {
        // ignore and move on
      }
    }
  }

}

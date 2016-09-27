package com.acme.broadcast.executor;

import com.acme.broadcast.Intent;
import com.acme.platform.IntentFilterAndReceiver;
import com.acme.platform.Context;

import java.util.Set;

public class ExceptionToleratingOrderedBroadcastExecutor implements OrderedBroadcastExecutor {

  private final Context context;

  public ExceptionToleratingOrderedBroadcastExecutor(Context context) {
    this.context = context;
  }

  @Override
  public void execute(Intent intent) {
    Set<IntentFilterAndReceiver> highestToLowestPriorityReceivers =
        context.getHighestToLowestPriorityReceivers();
    boolean shouldAbort = false;
    for (IntentFilterAndReceiver filterAndReceiver : highestToLowestPriorityReceivers) {
      if (shouldAbort) {
        break;
      }
      try {
        filterAndReceiver.getReceiver().handle(intent);
      } catch (Throwable e) {
        // ignore
        shouldAbort = filterAndReceiver.getReceiver().shouldAbort();
      }
    }
  }

}

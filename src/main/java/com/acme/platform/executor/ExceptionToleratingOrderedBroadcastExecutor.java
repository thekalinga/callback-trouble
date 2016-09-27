package com.acme.platform.executor;

import com.acme.Intent;
import com.acme.IntentFilterAndReceiver;
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
    for (IntentFilterAndReceiver receiver : highestToLowestPriorityReceivers) {
      if (shouldAbort) {
        break;
      }
      try {
        receiver.getReceiver().handle(intent);
      } catch (Throwable e) {
        // ignore
        shouldAbort = receiver.getReceiver().shouldAbort();
      }
    }
  }

}

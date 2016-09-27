package com.acme.broadcast.executor;

import com.acme.broadcast.Intent;

public interface OrderedBroadcastExecutor {
  void execute(Intent intent);
}

package com.acme.platform.executor;

import com.acme.broadcast.Intent;

public interface OrderedBroadcastExecutor {
  void execute(Intent intent);
}

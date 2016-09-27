package com.acme.platform.executor;

import com.acme.Intent;

public interface OrderedBroadcastExecutor {
  void execute(Intent intent);
}

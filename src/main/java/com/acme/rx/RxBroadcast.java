package com.acme.rx;

import com.acme.broadcast.BroadcastReceiver;
import com.acme.broadcast.Intent;
import com.acme.broadcast.IntentFilter;
import com.acme.platform.Context;
import rx.Observable;

import java.util.function.Consumer;

public class RxBroadcast {

  private RxBroadcast() {
    throw new AssertionError("Dont do it");
  }

  public static Observable<Intent> fromBroadcast(Context context, IntentFilter filter, Consumer<BroadcastReceiverProxy> proxyConsumer) {
    return Observable.create(subscriber -> {
      BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void handle(Intent intent) {
          subscriber.onNext(new Intent());
          proxyConsumer.accept(new BroadcastReceiverProxy(this));
          throw new RuntimeException("Intentional exception");
        }
      };
      context.subscribe(receiver, filter);
    });
  }

  public static Observable<IntentAndReceiverProxy> fromBroadcastWithIntentAndReceiverProxy(Context context, IntentFilter filter) {
    return Observable.create(subscriber -> {
      BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void handle(Intent intent) {
          subscriber.onNext(new IntentAndReceiverProxy(intent, new BroadcastReceiverProxy(this)));
        }
      };
      context.subscribe(receiver, filter);
    });
  }

}

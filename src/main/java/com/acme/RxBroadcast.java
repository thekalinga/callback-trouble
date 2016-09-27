package com.acme;

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
        }
      };
      context.subscribe(receiver, filter);
    });
  }

  public static Observable<IntentProxy> fromBroadcastWithIntentProxy(Context context, IntentFilter filter) {
    return Observable.create(subscriber -> {
      BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void handle(Intent intent) {
          subscriber.onNext(new IntentProxy(intent, new BroadcastReceiverProxy(this)));
        }
      };
      context.subscribe(receiver, filter);
    });
  }

}

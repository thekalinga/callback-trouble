package com.acme;

import com.acme.broadcast.BroadcastReceiver;
import com.acme.broadcast.Intent;
import com.acme.broadcast.IntentFilter;
import com.acme.platform.Context;
import com.acme.broadcast.executor.ExceptionToleratingOrderedBroadcastExecutor;
import com.acme.broadcast.executor.OrderedBroadcastExecutor;
import com.acme.broadcast.executor.VanillaOrderedBroadcastExecutor;

import static com.acme.rx.RxBroadcast.fromBroadcast;
import static com.acme.rx.RxBroadcast.fromBroadcastWithIntentAndReceiverProxy;
import static rx.Observable.empty;

public class Launcher {

  private static BroadcastReceiver highPriorityNativeReceiver = new BroadcastReceiver() {
    @Override
    public void handle(Intent intent) {
      System.out.println("Received by highest priority receiver");
      abortBroadcast();
      throw new RuntimeException("Intentional exception");
    }
  };
  private static BroadcastReceiver lowPriorityNativeReceiver = new BroadcastReceiver() {
    @Override
    public void handle(Intent intent) {
      System.out.println("Cascaded till lowest priority receiver");
    }
  };
  private static IntentFilter highPriorityReceiverFilter = new IntentFilter(1);
  private static IntentFilter lowPriorityReceiverFilter = new IntentFilter(0);

  public static void main(String[] args) {
    Context context = new Context();
    OrderedBroadcastExecutor vanillaExecutor = new VanillaOrderedBroadcastExecutor(context);
    OrderedBroadcastExecutor toleratingExecutor = new ExceptionToleratingOrderedBroadcastExecutor(context);

    System.out.println("\n");
    System.out.println("No rx approach; Vanilla executor");
    System.out.println("--------");
    noRx(context, vanillaExecutor);
    System.out.println("\n");

    System.out.println("Rx approach; Vanilla executor");
    System.out.println("--------");
    rxBroadcast(context, vanillaExecutor);
    System.out.println("\n");

    System.out.println("Rx approach (with IntentAndReceiverProxy); Vanilla executor");
    System.out.println("--------");
    rxBroadcastWithIntentAndReceiverProxy(context, vanillaExecutor);
    System.out.println("\n");

    System.out.println("No rx approach; Exception tolerating executor");
    System.out.println("--------");
    noRx(context, toleratingExecutor);
    System.out.println("\n");

    System.out.println("Rx approach; Exception tolerating executor");
    System.out.println("--------");
    rxBroadcast(context, toleratingExecutor);
    System.out.println("\n");

    System.out.println("Rx approach (with IntentAndReceiverProxy); Exception tolerating executor");
    System.out.println("--------");
    rxBroadcastWithIntentAndReceiverProxy(context, toleratingExecutor);
    System.out.println("\n");

  }

  private static void noRx(Context context, OrderedBroadcastExecutor executor) {
    context.reset();
    context.subscribe(highPriorityNativeReceiver, highPriorityReceiverFilter);
    context.subscribe(lowPriorityNativeReceiver, lowPriorityReceiverFilter);

    empty().doOnCompleted(() -> executor.execute(new Intent())).toBlocking().subscribe();
  }

  private static void rxBroadcast(Context context, OrderedBroadcastExecutor executor) {
    context.reset();
    fromBroadcast(context, highPriorityReceiverFilter, v -> v.abortBroadcast())
        .doOnNext(v -> {
          System.out.println("Received by highest priority receiver");
          throw new RuntimeException("Intentional exception");
        }).subscribe();
    fromBroadcast(context, lowPriorityReceiverFilter, v -> {})
        .doOnNext(v -> System.out.println("Cascaded till lowest priority receiver")).subscribe();

    empty().doOnCompleted(() -> executor.execute(new Intent())).toBlocking().subscribe();
  }

  private static void rxBroadcastWithIntentAndReceiverProxy(Context context,
      OrderedBroadcastExecutor executor) {
    context.reset();
    fromBroadcastWithIntentAndReceiverProxy(context, highPriorityReceiverFilter)
        .doOnNext(v -> {
          System.out.println("Received by highest priority receiver");
          v.proxy.abortBroadcast();
          throw new RuntimeException("Intentional exception");
        }).subscribe();
    fromBroadcastWithIntentAndReceiverProxy(context, lowPriorityReceiverFilter)
        .doOnNext(v -> System.out.println("Cascaded till lowest priority receiver")).subscribe();

    empty().doOnCompleted(() -> executor.execute(new Intent())).toBlocking().subscribe();
  }

}

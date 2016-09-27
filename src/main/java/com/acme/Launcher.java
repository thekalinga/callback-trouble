package com.acme;

import com.acme.platform.Context;
import com.acme.platform.executor.ExceptionToleratingOrderedBroadcastExecutor;
import com.acme.platform.executor.OrderedBroadcastExecutor;
import com.acme.platform.executor.VanillaOrderedBroadcastExecutor;

import static com.acme.RxBroadcast.fromBroadcast;
import static com.acme.RxBroadcast.fromBroadcastWithIntentProxy;
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

    System.out.println("Rx approach (with IntentProxy); Vanilla executor");
    System.out.println("--------");
    rxBroadcastWithIntentProxy(context, vanillaExecutor);
    System.out.println("\n");

    System.out.println("No rx approach; Exception tolerating executor");
    System.out.println("--------");
    noRx(context, toleratingExecutor);
    System.out.println("\n");

    System.out.println("Rx approach; Exception tolerating executor");
    System.out.println("--------");
    rxBroadcast(context, toleratingExecutor);
    System.out.println("\n");

    System.out.println("Rx approach (with IntentProxy); Exception tolerating executor");
    System.out.println("--------");
    rxBroadcastWithIntentProxy(context, toleratingExecutor);
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

  private static void rxBroadcastWithIntentProxy(Context context,
      OrderedBroadcastExecutor executor) {
    context.reset();
    fromBroadcastWithIntentProxy(context, highPriorityReceiverFilter)
        .doOnNext(v -> {
          System.out.println("Received by highest priority receiver");
          v.proxy.abortBroadcast();
          throw new RuntimeException("Intentional exception");
        }).subscribe();
    fromBroadcastWithIntentProxy(context, lowPriorityReceiverFilter)
        .doOnNext(v -> System.out.println("Cascaded till lowest priority receiver")).subscribe();

    empty().doOnCompleted(() -> executor.execute(new Intent())).toBlocking().subscribe();
  }

}

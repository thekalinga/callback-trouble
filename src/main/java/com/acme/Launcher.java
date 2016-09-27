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
    System.out.println("\n");

    System.out.println("No rx approach; Vanilla executor");
    System.out.println("--------");
    context.subscribe(highPriorityNativeReceiver, highPriorityReceiverFilter);
    context.subscribe(lowPriorityNativeReceiver, lowPriorityReceiverFilter);

    OrderedBroadcastExecutor vanillaExecutor = new VanillaOrderedBroadcastExecutor(context);
    empty().doOnCompleted(() -> vanillaExecutor.execute(new Intent())).toBlocking().subscribe();
    System.out.println("\n");

    context.reset();

    System.out.println("Rx approach; Vanilla executor");
    System.out.println("--------");
    fromBroadcast(context, highPriorityReceiverFilter, v -> v.abortBroadcast())
        .doOnNext(v -> {
          System.out.println("Received by highest priority receiver");
          throw new RuntimeException("Intentional exception");
        }).subscribe();
    fromBroadcast(context, lowPriorityReceiverFilter, v -> {})
        .doOnNext(v -> System.out.println("Cascaded till lowest priority receiver")).subscribe();

    OrderedBroadcastExecutor vanillaRxExecutor = new VanillaOrderedBroadcastExecutor(context);
    empty().doOnCompleted(() -> vanillaRxExecutor.execute(new Intent())).toBlocking().subscribe();
    System.out.println("\n");

    context.reset();

    System.out.println("Rx approach (with IntentProxy); Vanilla executor");
    System.out.println("--------");
    fromBroadcastWithIntentProxy(context, highPriorityReceiverFilter)
        .doOnNext(v -> {
          System.out.println("Received by highest priority receiver");
          v.proxy.abortBroadcast();
          throw new RuntimeException("Intentional exception");
        }).subscribe();
    fromBroadcastWithIntentProxy(context, lowPriorityReceiverFilter)
        .doOnNext(v -> System.out.println("Cascaded till lowest priority receiver")).subscribe();

    OrderedBroadcastExecutor vanillaRxWithProxyExecutor = new VanillaOrderedBroadcastExecutor(context);
    empty().doOnCompleted(() -> vanillaRxWithProxyExecutor.execute(new Intent())).toBlocking().subscribe();
    System.out.println("\n");

    context.reset();

    System.out.println("No rx approach; Exception tolerating executor");
    System.out.println("--------");
    context.subscribe(highPriorityNativeReceiver, highPriorityReceiverFilter);
    context.subscribe(lowPriorityNativeReceiver, lowPriorityReceiverFilter);

    OrderedBroadcastExecutor toleratingExecutor = new ExceptionToleratingOrderedBroadcastExecutor(context);
    empty().doOnCompleted(() -> toleratingExecutor.execute(new Intent())).toBlocking().subscribe();
    System.out.println("\n");

    context.reset();

    System.out.println("Rx approach; Exception tolerating executor");
    System.out.println("--------");
    fromBroadcast(context, highPriorityReceiverFilter, v -> v.abortBroadcast())
        .doOnNext(v -> {
          System.out.println("Received by highest priority receiver");
          throw new RuntimeException("Intentional exception");
        }).subscribe();
    fromBroadcast(context, lowPriorityReceiverFilter, v -> {})
        .doOnNext(v -> System.out.println("Cascaded till lowest priority receiver")).subscribe();

    OrderedBroadcastExecutor toleratingRxExecutor = new ExceptionToleratingOrderedBroadcastExecutor(context);
    empty().doOnCompleted(() -> toleratingRxExecutor.execute(new Intent())).toBlocking().subscribe();
    System.out.println("\n");

    context.reset();

    System.out.println("Rx approach (with IntentProxy); Exception tolerating executor");
    System.out.println("--------");
    fromBroadcastWithIntentProxy(context, highPriorityReceiverFilter)
        .doOnNext(v -> {
          System.out.println("Received by highest priority receiver");
          v.proxy.abortBroadcast();
          throw new RuntimeException("Intentional exception");
        }).subscribe();
    fromBroadcastWithIntentProxy(context, lowPriorityReceiverFilter)
        .doOnNext(v -> System.out.println("Cascaded till lowest priority receiver")).subscribe();

    OrderedBroadcastExecutor toleratingRxWithProxyExecutor = new ExceptionToleratingOrderedBroadcastExecutor(context);
    empty().doOnCompleted(() -> toleratingRxWithProxyExecutor.execute(new Intent())).toBlocking().subscribe();
    System.out.println("\n");
  }
  
}

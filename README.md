If you look at the output of the launcher, you will see that the outputs of `No rx approach` & `Rx approach (with IntentProxy)` matches irrespective of whether the underlying extor used is `Vanilla executor` or `Exception tolerating executor`

Output of Launcher
--
```
No rx approach; Vanilla executor
--------
Received by highest priority receiver
Cascaded till lowest priority receiver


Rx approach; Vanilla executor
--------
Received by highest priority receiver
Cascaded till lowest priority receiver


Rx approach (with IntentProxy); Vanilla executor
--------
Received by highest priority receiver
Cascaded till lowest priority receiver


No rx approach; Exception tolerating executor
--------
Received by highest priority receiver


Rx approach; Exception tolerating executor
--------
Received by highest priority receiver
Cascaded till lowest priority receiver


Rx approach (with IntentProxy); Exception tolerating executor
--------
Disconnected from the target VM, address: '127.0.0.1:45103', transport: 'socket'
Received by highest priority receiver
```

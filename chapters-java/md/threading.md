# Threading

<details>
<summary>What is a `thread`?</summary>

A thread is the smallest unit of execution within a program. Every Java program has at least one thread - the main thread - and can create aditional threads to perform tasks concurrently. Threads within the same program share the same memory space (heap) but each has its own stack for local variables and method calls.

```java
// The main thread runs this automatically
public static void main(String[] args) {
    System.out.println("Running on: " + Thread.currentThread().getName()); // main

    // Creating a new thread
    Thread t = new Thread(() -> {
        System.out.println("Running on: " + Thread.currentThread().getName()); // Thread-0
    });
    t.start(); // starts the new thread concurrently
}
```

</details>

<details>
<summary>What is a `program`?</summary>

A program is a set of instructions written in a programming language, stored on disk as an executable file. On its own it is static and inactive - it only comes to life when executed, at which point the OS loads it into memory and creates a process to run it.

</details>

<details>
<summary>What is a `process`?</summary>

Is a running instance of a program with its own alocated memory and processing resources. Each process is isolated from other processes - they don't share memory. A process can contain one or more threads that share its memory space and execute concurrently within it.

</details>

<details>
<summary>What is the difference between a thread and a process?</summary>

A thread is the smallest processing unit within a process, and a process is a running instance of a program with its own isolated memory. Threads within the same process share the heap but each has its own stack - making them lightweight and cheap to create. Processes are heavier, have completely isolated memory, and a crash in one process doesn't affect others - whereas an unhandled exception in one thread can bring down the entire process.

```java
// If Thread-2 throws an unhandled exception
// it can bring down the entire process including Thread-1
Thread t1 = new Thread(() -> { /* running fine */ });
Thread t2 = new Thread(() -> { throw new RuntimeException("crash"); });

// But if Process-2 crashes
// Process-1 is completely unaffected — isolated memory spaces
```

| Feature | Thread | Process |
| :--- | :--- | :--- |
| **Memory** | Shares heap with other threads | Own isolated memory space |
| **Stack** | Each has its own stack | Each process has its own stack |
| **Cost** | Lightweight — cheap to create | Heavyweight — expensive to create |
| **Communication** | Easy — shared heap | Complex — inter-process communication |
| **Failure impact** | One thread can crash the whole process | One process doesn't affect others |

</details>

<details>
<summary>What is the difference between a program and a process?</summary>

A program is just the code with the instructions to do something, but on its own is static and inactive - it only comes to life when executed and creates a process to run it. A process then is a running instance of a program. 

</details>

<details>
<summary>Thread, program and process:</summary>

A program is like a recipe — it's just instructions on paper. A process is like actually cooking that recipe — it's the active execution with ingredients (memory) and tools (CPU). A thread is like a chef's hand — a process can have multiple hands working simultaneously.

| Concept | What it is |
| :--- | :--- |
| **Program** | Static instructions stored on disk |
| **Process** | A running instance of a program loaded into memory |
| **Thread** | A unit of execution within a process |

</details>

<details>
<summary>Explain `context switching` of thread?</summary>

Context switching is the process where the CPU saves the current thread's state - registers, program counter and stack pointer - and loads the state of another thread to execute it. On a single core CPU this is rapid switching creates the ilusion of concurrency since only one thread executes at a time. On multi-core CPUs true parallelism is possible - multiple threads execute simultaneously on different cores. Context switching has a small performance cost since saving and restoring thread state takes time.

Small nuance worth adding:

* Concurrency — multiple threads making progress by rapidly switching on a  single core (what you described)
* Parallelism — multiple threads literally executing simultaneously on multiple cores

</details>

<details>
<summary>What is `parallel processing`?</summary>

Also called parallelism, pararell processing is when multiple threads execute simultaneously on multiple CPU cores - unlike concurrency where threads take turns on a single core. This enables programs to complete CPU intensive tasks faster by dividing the work across multiple cores.

```java
// Example — splitting a large task across multiple threads
// instead of processing 1 million records sequentially on one core
// parallel processing splits it across available cores simultaneously
list.parallelStream()
    .map(record -> process(record))
    .collect(Collectors.toList());
```

| Feature | Concurrency | Parallelism |
| :--- | :--- | :--- |
| **Cores needed** | Single core | Multiple cores |
| **Execution** | Taking turns — context switching | Truly simultaneous |
| **Goal** | Responsive program | Faster execution |

</details>

<details>
<summary>What is multi-threading</summary>

Multi-threading is when a process uses multiple threads simultaneously instead of just one to execute the programs tasks. The main benefits are improved performance - CPU intensive tasks can be divided across threads and executed in parallel - and better responsiveness - a program can keep the UI thread running smoothly while other threads handle heavy background work like network calls or file processing.

```java
// Without multi-threading — UI freezes while downloading
downloadLargeFile(); // blocks everything until done
updateUI();          // user sees frozen screen

// With multi-threading — UI stays responsive
Thread downloadThread = new Thread(() -> downloadLargeFile()); 
downloadThread.start(); // runs in background
updateUI();             // main thread keeps UI responsive
```

</details>

<details>
<summary>How `parallel processing` and `multi-threading` related?</summary>

Multi-threading is the technique of using multiple threads within a process to execute tasks concurrently. Paralallel processing is what you achieve when those threads run simultaneosly on multiple CPU cores - making multi-threading the mechanism that enables parallel processing. On a single core, multi-threading still provides concurrency through context switching but not true parallelism. On multiple cores, multi-threading achieves true parallel processing - dividing heavy tasks across cores fro better perfomance.

The relationship in one line:

Multi-threading is the how - parallel processing is the result when hardware allows it.

</details>

<details>
<summary>What is `deadlock`?</summary>

Deadlock is when two or more threads are permanently blocked waiting for each other to release a lock they need - creating a circular dependency where no thread can ever proceed.

```java
Object lockA = new Object();
Object lockB = new Object();

Thread t1 = new Thread(() -> {
    synchronized (lockA) {          // t1 acquires lockA
        synchronized (lockB) {      // t1 waits for lockB — held by t2
        }
    }
});

Thread t2 = new Thread(() -> {
    synchronized (lockB) {          // t2 acquires lockB
        synchronized (lockA) {      // t2 waits for lockA — held by t1
        }
    }
});

// Both threads wait forever — deadlock
```

| Condition | Meaning |
| :--- | :--- |
| **Mutual exclusion** | Only one thread can hold a lock at a time |
| **Hold and wait** | Thread holds a lock while waiting for another |
| **No preemption** | Locks can't be forcibly taken away |
| **Circular wait** | Thread 1 waits for Thread 2, Thread 2 waits for Thread 1 |

An example of how to prevent it:

```java
// ✅ both threads acquire locks in same order — no circular dependency
Thread t1 = new Thread(() -> {
    synchronized (lockA) {
        synchronized (lockB) { }
    }
});

Thread t2 = new Thread(() -> {
    synchronized (lockA) { // same order as t1
        synchronized (lockB) { }
    }
});
```

</details>

<details>
<summary>What is a `user thread`?</summary>

In Java there are two types of threads - user threads and daemon threads. User threads are the default type - they are created to perform the actual work of the application. The JVM keeps running as long at least one user thread is still alive, regardless of whether daemon threads are running or not.

```java
// The main thread is a user thread
public static void main(String[] args) {
    Thread t = new Thread(() -> {
        System.out.println("I am a: " + Thread.currentThread().isDaemon()); // false — user thread
    });
    t.start();
}
```

| Feature | User Thread | Daemon Thread |
| :--- | :--- | :--- |
| **Default type?** | ✅ Yes | ❌ No — must be set explicitly |
| **JVM waits for it?** | ✅ Yes | ❌ No |
| **Purpose** | Application work | Background support tasks |
| **Example** | Main thread, business logic | Garbage collector, logging |

</details>

<details>
<summary>What is `thread local storage`? What are the things would you store with thread local storage? -</summary>

Threads share the heap - so any object stored there is accesible by all threads, which can cause race conditions, thread local storage is a mechanism that lets you store data on the heap but make it only accessible to the thread that create it.

```java
// Without thread local — shared, unsafe
static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // shared by all threads ❌

// With thread local — each thread gets its own copy, safe
static ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(
    () -> new SimpleDateFormat("yyyy-MM-dd") // each thread gets its own instance ✅
);

// Each thread accesses its own copy
String date = formatter.get().format(new Date());
```

What you'd typically store in thread local storage:

* User session data - in a web server each request runs on a thread, store the current user without passing it through every method
* Database connections - give each thread its own connection
* Non thread safe objects - like SimpleDateFormat which is not thread safe

</details>

<details>
<summary>Do threads share stack memory?</summary>

No, each thread has its own isolated stack memory - because each thread has its own execution flow, method calls and local variables that are independent from other threads. What threads share is the heap, where objects live. This is why local variables are thread safe by default but shared objects on the heap are not.

```java
void method() {
    int localVar = 42; // ✅ on the stack — thread safe, each thread has its own copy
    sharedObject.value = 42; // ⚠️ on the heap — not thread safe, all threads share this
}
```

</details>

<details>
<summary>Describe different stages of thread lifecycle?</summary>

NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → RUNNABLE → TERMINATED

* NEW - thread created but `start()` not called yet
* RUNNABLE - thread is running or ready to run, waiting for CPU
* BLOCKED - thread waiting to acquire a lock held by another thread (`syncronized` block held by another thread)
* WAITING - thread waiting indefinitely for another thread to notify it (`wait()`, `join()`)
* TIMED_WAITING - thread waiting for a specific amount of time
* TERMINATED -thread finished execution

</details>

<details>
<summary>What is difference between `blocked` state and `waiting` state?</summary>

BLOCKED state signals that the thread is waiting for a lock that is currently held by another thread. WAITING signals the thread is waiting indefinitely for another thread to notify it

</details>

<details>
<summary>How can you find thread’s state?</summary>

Call thread.getState() — it returns a Thread.State enum with one of six values: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, or TERMINATED.

</details>

<details>
<summary>How thread `sleep()` method is different from thread `wait()` method?</summary>

`sleep()` pauses the thread for specific amount of time and then automatically resumes - it belongs to the `Thread` class and crucially does not release any locks the thread is holding. `wait()` pauses the thread indefinitely until another thread calls `notify()` or `notifyAll()` on the same object - it belongs to the `Object` class and releases the lock it is holding while waiting.

```java
// sleep() — holds the lock while sleeping
synchronized (lockA) {
    Thread.sleep(1000); // still holding lockA — other threads can't acquire it
}

// wait() — releases the lock while waiting
synchronized (lockA) {
    lockA.wait(); // releases lockA — other threads can acquire it
                  // resumes when another thread calls lockA.notify()
}
```

| Feature | sleep() | wait() |
| :--- | :--- | :--- |
| **Belongs to** | Thread class | Object class |
| **Duration** | Fixed time | Until notified |
| **Releases lock?** | ❌ No | ✅ Yes |
| **Used for** | Pausing execution | Thread communication |
| **Must be in synchronized block?** | ❌ No | ✅ Yes |

</details>

<details>
<summary>Define a good strategy to terminate a thread?</summary>

The recommend strategy to terminate a thread is using a volatile boolean flag that the threads checks periodically - when you want to stop the thread you set the flag to `false` from outside, and the thread stops itself cleanly when it sees the flag change, rather than being forcibly killed which could leave resources in an inconsistent state.

```java
class MyTask implements Runnable {
    private volatile boolean running = true; // volatile — visible to all threads

    public void stop() {
        running = false; // called from outside to signal stop
    }

    @Override
    public void run() {
        while (running) {        // thread checks flag on every iteration
            doWork();            // do the actual work
        }
        // thread exits cleanly when running becomes false
    }
}

MyTask task = new MyTask();
Thread t = new Thread(task);
t.start();

// later — signal the thread to stop cleanly
task.stop();
```

Without `volatile` the flag change might not be visible to the other thread due to CPU caching — `volatile` guarantees that any write to the flag is immediately visible to all threads.

</details>

<details>
<summary>Why you shouldn’t call thread’s `stop` and `suspend` methods?</summary>

`stop()` kills the thread immediately at whatever line it's currently executing - it doesn't give the thread a chance to clean up. This can leave data in an inconsistent state.

```java
// Thread is in the middle of a bank transfer
synchronized (account) {
    account.debit(100);   // ✅ money deducted
    // stop() called here — thread killed immediately
    account.credit(100);  // ❌ never executed — money vanished
}
// account is now in inconsistent state — money was deducted but never credited
```

`suspend()` pauses the thread but keeps all its locks - unlike `wait()` which releases them. If another thread needs those locks it will wait forever, causing a deadlock.

```java
Thread t1 = new Thread(() -> {
    synchronized (lockA) {
        t1.suspend(); // paused but still holding lockA ❌
    }
});

Thread t2 = new Thread(() -> {
    synchronized (lockA) { // waiting for lockA — held by suspended t1
        // t2 waits forever — deadlock
    }
});
```

</details>

<details>
<summary>What is thread interrupt? How thread’s interrupt method is different from thread’s stop method?</summary>

`interrupt()` is a cooperative signal that asks the thread to stop when it's ready - the thread checks the floag and stops cleanly on its own terms. `stop()` is a forceful kill that doesn't give the thread a chance to clean up, risking data inconsistency.

`interrupt()` sets a flag on the thread called the interrupt flag — it's just a signal saying "please stop when you get a chance". Unlike `stop()` which kills the thread immediately, `interrupt()`lets the thread decide when and how to stop cleanly.

Scenarios:

```java
// 1. If the thread is running normally - just sets the flag:

// Thread checks the flag itself and decides when to stop
public void run() {
    while (!Thread.currentThread().isInterrupted()) { // checks the flag
        doWork(); // finishes current work before stopping
    }
    // cleanup here — thread stops cleanly
}

// From outside — sends the signal
t.interrupt(); // sets the flag — thread will stop when it checks

// 2. If the thread is sleeping or waiting - throws InterruptedException:

public void run() {
    try {
        Thread.sleep(5000); // sleeping
    } catch (InterruptedException e) {
        // sleep was interrupted — handle cleanup and stop
        return; // exit cleanly
    }
}

t.interrupt(); // wakes the sleeping thread by throwing InterruptedException
```

</details>

<details>
<summary>What happens if a thread is sleeping and you call interrupt on the thread?</summary>

The thread is "woken up" by throwing an `InterruptedException` - which you should catch and handle cleanly by stopping the thread's work and performing any necessary cleanup, rather than swallowing it silently.

```java
public void run() {
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        // ✅ handle cleanly — don't swallow it
        System.out.println("Thread interrupted — cleaning up");
        return; // exit the thread cleanly
    }
}
```

</details>

<details>
<summary>Does calling interrupt stops the thread immediately?</summary>

No, calling `interrupt()` only sets a flag on the thread. It is up to the thread to check that flag and decide when it is safe or when it is ready to be stopped, allowing to finish the current work and clean up before teminating.

</details>

<details>
<summary>Once a thread is stopped can be restarted?</summary>

A terminated thread cannot be restarted - it's gone forever. If you need to run the same task again create a new thread or usea thread pool which handles thread reuse automatically.

```java

// 1. Create a new thread:

// ✅ create a fresh thread with the same task
Runnable task = () -> System.out.println("running");
Thread t1 = new Thread(task);
t1.start();

// later — run again
Thread t2 = new Thread(task); // new thread, same task
t2.start();

// 2. Usea thread pool - the recommended modern approach:

// ✅ thread pool reuses threads instead of creating new ones
ExecutorService pool = Executors.newFixedThreadPool(4);
pool.submit(task); // run
pool.submit(task); // run again — pool reuses an existing thread
```

</details>

<details>
<summary>What are the different ways to create a thread?</summary>

There are three ways to create a thread: extending the `Thread` class and overriding `run()`; implementing the Runnable interface and passing it to a Thread object; or implementing the `Callable` which additionally allows returning a result and throwing checked exceptions. Thread pools don't create threads differently - they use these same mechanisms internally but manage and reuse threads automatically.

```java
// 1. Extending Thread class:

class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Running");
    }
}

MyThread t = new MyThread();
t.start();

// 2. Implementing `Runnable` interface: 

class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Running");
    }
}

Thread t = new Thread(new MyTask());
t.start();

// 3. Implementing Callable interface: 

class MyTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "result";
    }
}

FutureTask<String> future = new FutureTask<>(new MyTask());
Thread t = new Thread(future);
t.start();
String result = future.get(); // retrieve result
```

Thread pools do create threads — but only once upfront when the pool is initialized. After that they reuse those same threads for multiple tasks instead of creating a new thread for every task.

```java
// Pool creates 4 threads upfront
ExecutorService pool = Executors.newFixedThreadPool(4);

// These tasks reuse the same 4 threads — no new threads created
pool.submit(task1); // runs on thread 1
pool.submit(task2); // runs on thread 2
pool.submit(task3); // runs on thread 3
pool.submit(task4); // runs on thread 4
pool.submit(task5); // waits until one of the 4 threads is free
```

</details>

<details>
<summary>How `implementing Runnable` interface is different from `extending Thread` class?</summary>

Implementing `Runnable` defines only the task - keeping it independent from the thread mechanism. This has two advantages over extending `Thread`; it frees your class to extend another class since Java doesn't support multiple inheritance; and it separates the task from how it runs, allowing the same `Runnable` to be passed to a plain thread, multiple threads or a thread pool without changing the task itself.

```java
// Extending Thread — task and thread are coupled
class MyThread extends Thread {
    @Override
    public void run() { System.out.println("task"); }
}
MyThread t = new MyThread();
t.start();

// Implementing Runnable — task is independent
class MyTask implements Runnable {
    @Override
    public void run() { System.out.println("task"); }
}

// same task, different execution mechanisms
new Thread(new MyTask()).start();                    // plain thread
executorPool.submit(new MyTask());                   // thread pool
new Thread(new MyTask()).start();                    // another thread
```

</details>

<details>
<summary>When should you extend Thread class?</summary>

Extend `Thread` when you need to run a simple task in a class that isn't already extending another class, and when you don't need to reuse the task across different execution mechanisms. However in modern Java this scenario is rare - `Runnable` with a thread pool is almost always preferred over extending `Thread` directly.

The only legitimate case where extending `Thread` makes sense:
When you need to customize the thread's behavior itself - not just the task it runs:

```java
class LoggingThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread starting: " + getName());
        super.run();
        System.out.println("Thread finished: " + getName());
    }
}
```

</details>

<details>
<summary>When should you implement Runnable Interface?</summary>

Implementing `Runnable` interface is the modern default choice when you need to execute a task in one or multiple threads, because it offers flexibility separating the task from the execution mechanism making it able to be executed through different mechanisms, like one or multiple threads, or using a thread pool. If you need a return value or to throw checked exceptions, use `Callable` instead.

```java
// Runnable — no return value, no checked exceptions
Runnable task = () -> processData();

// Callable — when you need a result or checked exception
Callable<String> task = () -> fetchDataFromApi(); // returns String, can throw IOException
```

</details>

<details>
<summary>What is the difference between `Callable` and `Runnable` interface?</summary>

They both define a task to be executed by a thread, but `Callable` allows you to return a value while `Runnable` return void, and throw checked exceptions, and also needs to be wrapped in `FutureTask` to be passed to a `Thread` since `Thread` only accepts `Runnable`. They also have different method names - `Runnable` defines `run()` while Callable defines `call()`.

```java
// Runnable — no return, no checked exception
class MyTask implements Runnable {
    public void run() { processData(); }
}

// Callable — returns value, can throw checked exception
class MyTask implements Callable<String> {
    public String call() throws Exception { return fetchData(); }
}
```

| Feature | Runnable | Callable |
| :--- | :--- | :--- |
| **Method** | `run()` | `call()` |
| **Return value** | `void` | Any type `T` |
| **Checked exceptions** | ✗ No | ✅ Yes |
| **Needs FutureTask?** | ✗ No | ✅ Yes |
| **Since** | Java 1.0 | Java 5 |

</details>

<details>
<summary>What is the benefit of using Runnable over Callable?</summary>

The benefit of `Runnable` over `Callable` is simplicity - when your task doesn't need to return a value or throw checked exceptions, `Runnable`requires less setup with no `FutureTask` wrapper or result management needed. Use `Callable` only when you actually need those extra capabilities.

```java
// Runnable — simple and clean when you don't need a result
Runnable task = () -> processData();
new Thread(task).start(); // done

// Callable — more setup needed even when you don't use the result
Callable<Void> task = () -> { processData(); return null; }; // forced to return something
FutureTask<Void> future = new FutureTask<>(task);
new Thread(future).start();
future.get(); // extra step
```

Don't pay for complexity you don't need — if Runnable covers your use case, prefer it over Callable.

</details>

<details>
<summary>Can you throw checked exception from Runnable interface?</summary>

No - Runnable's `run()` method signature does not declare `throws Exception`, so the compiler won't allow checked exceptions to propagate out of it. If you need to throw checked exceptions you must use Callable whose `call()` method declares throws Exception.

```java
// Runnable — can't throw checked exceptions
class MyTask implements Runnable {
    public void run() {
        throw new IOException("error"); // ❌ compile error — must handle internally
    }
}

// Workaround — wrap in unchecked exception
class MyTask implements Runnable {
    public void run() {
        try {
            riskyOperation();
        } catch (IOException e) {
            throw new RuntimeException(e); // ✅ wrap in unchecked
        }
    }
}

// Callable — can throw checked exceptions freely
class MyTask implements Callable<String> {
    public String call() throws Exception {
        throw new IOException("error"); // ✅ allowed
    }
}
```

</details>

<details>
<summary>Why Runnable and Callable interfaces are called Functional Interface?</summary>

Knowing that a functional interface is an interface that has exactly one abstract method. That's the only rule - one abstract method, no more.

They're called functional interfaces because they represent a single function or behavior which means they can be expressed as a lambda expression instead of a full class implementation.

```java
// Without lambda — verbose
Runnable task = new Runnable() {
    @Override
    public void run() {
        System.out.println("running");
    }
};

// With lambda — clean, because Runnable is a functional interface
Runnable task = () -> System.out.println("running");
```

That's why `Runnable` and `Callable` are considered functional interfaces, because each one of them only declare an abstract method: `run()` for `Runnable` and `call()` for `Callable`.

Java has a built in annotation to mark functional interfaces:

```java
@FunctionalInterface // compiler enforces exactly one abstract method
public interface Runnable {
    void run();
}

@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

The @`FunctionalInterface` annotation is optional but recommended - if you accidentaly add a second abstract method the compiler immediately throws an error, protecting the contract.

</details>

<details>
<summary>Which of the Interface returns result: Runnable or Callable?</summary>

`Callable` is the interface that allows you to return a value through its `call()`. Since `Thread` only accepts `Runnable`, `Callable` needs to be wrapped in `FutureTask` which stores the result once the thread finishes. The result is then retrieved by calling `future.get()` which blocks the current thread until the result is available.

```java
Callable<String> task = () -> "hello from thread";

FutureTask<String> future = new FutureTask<>(task);
new Thread(future).start();

String result = future.get(); // blocks until result is ready
System.out.println(result);   // "hello from thread"
```

The thread calling `future.get()` will wait indefinitely until the result is ready, which can cause performance issues if not handled carefully:

```java
// ✅ safer — wait with a timeout
String result = future.get(5, TimeUnit.SECONDS); // throws TimeoutException if too slow
```

</details>

<details>
<summary>What is `Race condition`?</summary>

A Race condition accurs when multiple threads try to read and modify the same shared data: object or value simultaneously, producting unpredictable or inconsistent resultss because the outcome depends on the order in which threads execute - which the JVM doesn't guarantee.

Example:

```java
class Counter {
    int count = 0;

    void increment() {
        count++; // looks atomic but it's actually 3 steps:
                 // 1. read count
                 // 2. add 1
                 // 3. write back
    }
}

Counter counter = new Counter();

// Two threads incrementing simultaneously
Thread t1 = new Thread(() -> counter.increment());
Thread t2 = new Thread(() -> counter.increment());

t1.start();
t2.start();

// Expected: count = 2
// Possible result: count = 1 ❌ — both threads read 0, both write 1
```

Why `count++` is dangerous:

It looks like one operation but is actually three steps - read, modify and write. If two threads interleave between those steps they both read the same value and one update gets lost.

```java
synchronized void increment() {
    count++; // ✅ only one thread can execute this at a time
}
```

</details>

<details>
<summary>What `synchronized` is for?</summary>

`synchronized` is Java's built in mechanism for preventing race conditions by ensuring only one thread at a time can execute a block of code or method.

It works by using a lock (also called monitor) - when a thread enters a synchronized block it acquires the lock, and any other thread that tries to enter the same block has to wait until the lock is released:

```java
class Counter {
    int count = 0;

    // ✅ synchronized method — only one thread executes this at a time
    synchronized void increment() {
        count++; // now truly atomic — no interleaving possible
    }

    // equivalent — synchronized block
    void increment() {
        synchronized (this) { // this = the lock object
            count++;
        }
    }
}
```

| Feature | Synchronized method | Synchronized block |
| :--- | :--- | :--- |
| **Lock scope** | Entire method | Just the block |
| **Lock object** | `this` implicitly | You choose explicitly |
| **Granularity** | Coarser | Finer — better performance |

`synchronized` solves race conditions but introduces a performance cost - threads has to wait for the lock, reducing the concurrency benefit of multithreading. This is why you synchronize the minimum amount of code necessary, not the entire method when possible.

`synchronized` can only be applied to methods or blocks - not fields. When used on a block you pass an object as the lock that controls which thread can enter, not the object beign protected.

</details>

<details>
<summary>What is `Immutable Object`?</summary>

An immutable object is one whose internal state cannot be changed after its creation. In Java you achieve immutability by: declaring the class as `final` so it can't be subclassed: declaring all fields as `private final`: only setting fields through the constructor: providing no setters and returning defensive copies of mutable fields.

```java
// ✅ immutable class
public final class Person {          // final — can't be subclassed
    private final String name;       // final — can't be reassigned
    private final List<String> hobbies;

    public Person(String name, List<String> hobbies) {
        this.name = name;
        this.hobbies = new ArrayList<>(hobbies); // defensive copy
    }

    public String getName() { return name; }
    
    public List<String> getHobbies() {
        return Collections.unmodifiableList(hobbies); // defensive copy on return
    }
    // no setters
}
```

</details>

<details>
<summary>How immutable objects help preventing race condition?</summary>

Because immutable objects cannot be modified after creation, multiple threads can only read them - never write. Since race conditions only occur when threads try to modify shared state simultaneously, immutable objects eliminate the problem entirely. No synchronization needed because there is no mutable state to protect.

```java
// Mutable — race condition possible
class Counter {
    int count = 0;        // shared mutable state — dangerous
    void increment() { count++; } // ⚠️ race condition
}

// Immutable — race condition impossible
final class Value {
    private final int count; // can't be changed after creation
    Value(int count) { this.count = count; }
    int getCount() { return count; }
    Value increment() { return new Value(count + 1); } // returns NEW object
}

// Multiple threads can read safely — no synchronization needed
Value v = new Value(0);
t1 reads v.getCount(); // ✅ safe
t2 reads v.getCount(); // ✅ safe — no modification possible
```

</details>

<details>
<summary>Why race condition may produce unpredictable results?</summary>

Because when multiple threads interleave reading and modifying shared data they might read the same original state but the final result depends on the order the threads executed and that's something that you cannot predict or control

</details>

<details>
<summary>Why should you declare immutable class as final?</summary>

Immutable classes should be declared `final` to prevent subclassing. Without `final`, a subclass could add mutable fields and setters, breaking immutability. Worse, a mutable subclass could be assigned to an immutable type reference - deceiving any code that assumes it's working with a truly immutable object, potentially causing race conditions in a multithreaded enviroment.

```java
// Without final — immutability can be broken through subclassing
class ImmutablePerson {
    private final String name;
    ImmutablePerson(String name) { this.name = name; }
    String getName() { return name; }
}

// ❌ subclass breaks immutability
class MutablePerson extends ImmutablePerson {
    private String nickname; // mutable field added
    MutablePerson(String name) { super(name); }
    void setNickname(String nickname) { this.nickname = nickname; } // setter added
}

// deceiving — looks immutable but isn't
ImmutablePerson p = new MutablePerson("Carlos"); // ⚠️ mutable subclass
p.setNickname("Carl"); // ❌ wait... immutable objects can't be modified!

// ✅ final prevents this entirely
public final class ImmutablePerson { ... } // can't be subclassed
```

In summary:

Declaring an immutable class `final` prevents subclasses from adding mutable state and breaking the immutability contract - without it a mutable subclass could be passed off as immutable, silently introducing race conditions.

</details>

<details>
<summary>Why constructor should be the only way to create immutable object? What happens if setters are provided?</summary>

To ensure that once the object is created its state cannot be changed, if setters are provided the immutability contract is broken - any code could modify the object's internal state after creation, reintroducing the risk of race coditions in multithreaded enviroments and making the object unreliable as a constant value.

```java
// ❌ setters break immutability
public final class Person {
    private final String name;
    
    Person(String name) { this.name = name; }
    
    // providing this breaks immutability entirely
    public void setName(String name) { 
        this.name = name; // ❌ compiler error — final field
    }
}

// ✅ constructor only — state set once, never changed
public final class Person {
    private final String name;
    
    Person(String name) { this.name = name; } // only way to set state
    
    public String getName() { return name; } // read only
    // no setters
}
```

The compiler enforces immutability when fields are `final`, making setters not just bad practice but actually impossible to implement correctly

</details>

<details>
<summary>What is `thread pool`?</summary>

A thread pool is a tool in Java for creating, managing and reusing a fixed set of threads - reducing the overhead of creating and destroting a new thread every time a task needs to be executed. Tasks are submitted to the pool and queued until a thread becomes available to execute them. In Java thread pools are managed to `ExecutorService` interface and create via `Executors` factory class. 

```java
// Creating a thread pool
ExecutorService pool = Executors.newFixedThreadPool(4); // 4 reusable threads

// Submitting tasks — pool manages thread assignment automatically
pool.submit(() -> processOrder());
pool.submit(() -> sendEmail());
pool.submit(() -> generateReport());

// Shutdown when done
pool.shutdown();
```

</details>

<details>
<summary>How a thread pool reduces the overhead of thread creation?</summary>

A thread pool reduces overhead by creating threads once upfront and reusing them for multiple tasks. Without a pool, every task would require creating a new thread - which is expensive because it envolves allocating memory for the thread's stack, registering it with the OS, and destroying it when done. With a pool, threads are kept alive and idle between tasks, ready to pick up the next task from the queue without any creation or destruction cost.

```java
// Without thread pool — expensive thread creation for every task
for (Task task : tasks) {
    Thread t = new Thread(task); // ❌ creates new thread every time
    t.start();                   // OS overhead
}                                // thread destroyed after — wasted work

// With thread pool — threads created once, reused for all tasks
ExecutorService pool = Executors.newFixedThreadPool(4); // 4 threads created once
for (Task task : tasks) {
    pool.submit(task); // ✅ reuses existing threads — no creation overhead
}
pool.shutdown();
```

In summary:

Thread pools eliminate the repeated cost of creating and destroying threads by keeping alive a fixed set of threads and reusing them - thread creation happens once, not once per task.

</details>

<details>
<summary>How a thread pool helps to prevent application from hanging or crashing?</summary>

A thread pool prevents hanging or crashing by limiting the number of threads that can exist simultaneously. Without a pool, an application under heavy load could create thousands of threads - exhausting memory and CPU resources, causing the application to crash. With a thread pool, excess tasks don't create new threads - they wait in a queue until a thread becomes available, keeping resource usage predictable and stable.

```java
// Without thread pool — unbounded thread creation, dangerous
for (Request request : thousands_of_requests) {
    new Thread(() -> handle(request)).start(); // ❌ thousands of threads
                                               // OutOfMemoryError likely
}

// With thread pool — bounded, controlled
ExecutorService pool = Executors.newFixedThreadPool(10); // max 10 threads
for (Request request : thousands_of_requests) {
    pool.submit(() -> handle(request)); // ✅ extras wait in queue
}                                       // never more than 10 threads alive
```

| Problem | How thread pool helps |
| :--- | :--- |
| **Crashing** | Limits max threads — no `OutOfMemoryError` |
| **Hanging** | Queue manages excess tasks — nothing gets lost |

</details>

<details>
<summary>How does thread pool enables loosely coupled design?</summary>

A thread pool enables loosely couples design by separating the task (what needs to be done) from the execution mechanism (how and when it runs). Your code only needs to define the task and submit it - it doesn't need to know how many threads exist, when they run, or how they're managed. This means you can change the pool size or swap the pool type without touching the task logic at all.

```java
// Task is completely independent of execution mechanism
Runnable task = () -> processOrder(); // just defines WHAT to do

// Execution mechanism is completely independent of task
ExecutorService pool = Executors.newFixedThreadPool(4);
pool.submit(task); // pool decides HOW and WHEN to run it

// Swap pool type without changing task at all
ExecutorService pool = Executors.newCachedThreadPool(); // ✅ task unchanged
ExecutorService pool = Executors.newSingleThreadExecutor(); // ✅ task unchanged
```

```java
// processOrder() is just a regular method somewhere in your code
void processOrder() {
    // any business logic here
    validateOrder();
    chargePayment();
    updateInventory();
    sendConfirmationEmail();
}

// Then you wrap it in a Runnable to run it in a thread pool
Runnable task = () -> processOrder();
pool.submit(task);
```

</details>

<details>
<summary>What ExecutorService is:</summary>

The interface that represents a thread pool — it manages thread lifecycle and task submission for you.

```java
// The most common pool types:

// Fixed pool — fixed number of threads, extras wait in queue
ExecutorService fixed = Executors.newFixedThreadPool(4);

// Single thread — one thread, tasks execute sequentially
ExecutorService single = Executors.newSingleThreadExecutor();

// Cached pool — creates threads as needed, reuses idle ones
ExecutorService cached = Executors.newCachedThreadPool();

// How to submit tasks and shutdown:

ExecutorService pool = Executors.newFixedThreadPool(4);

// submit Runnable — no return value
pool.submit(() -> processTask());

// submit Callable — with return value
Future<String> result = pool.submit(() -> fetchData());
String value = result.get(); // blocks until ready

// always shutdown when done
pool.shutdown(); // waits for running tasks to finish
pool.shutdownNow(); // attempts to stop immediately
```

</details>

<details>
<summary>What Executors is:</summary>

A factory class that provides static methods to create different types of thread pools.

</details>

<details>
<summary>What is the difference between submit() and execute()?</summary>

`submit()` is for passing into the queue Tasks that implement `Runnable` and `Callable` - returns a `Future`, allowing you to retrieve the result or check for exceptions later. `execute()` only accepts `Runnable`, returns nothing, and silently swallows exceptions - making `submit()` the preferred choice in most cases.

```java
ExecutorService pool = Executors.newFixedThreadPool(4);

// execute() — Runnable only, no return, exceptions silently lost
pool.execute(() -> processTask()); // ❌ if exception occurs — you'll never know

// submit() — Runnable or Callable, returns Future
Future<?> f1 = pool.submit(() -> processTask());      // Runnable
Future<String> f2 = pool.submit(() -> fetchData());   // Callable

// exceptions surface when you call get()
try {
    f1.get(); // ✅ exception thrown here if task failed
} catch (ExecutionException e) {
    System.out.println("Task failed: " + e.getCause());
}
```

| Feature | execute() | submit() |
| :--- | :--- | :--- |
| **Accepts** | `Runnable` only | `Runnable` and `Callable` |
| **Returns** | `void` | `Future<?>` |
| **Exceptions** | Silently lost | Surface via `Future.get()` |
| **Preferred?** | ✗ No | ✅ Yes |

</details>

<details>
<summary>What is the difference between fixed and cached thread pool?</summary>

The fixed thread pool creates a set number of threads upfront and keeps them alive permanently - if all threads are busy, excess tasks wait in a queue until a thread becomes available. A cached thread pool create threads on demand as tasks arrive reuses idle ones - if all threads are busy it creates a new one instead of queuing, but idle threads are terminated after 60 seconds if unused.

```java
// Fixed — predictable, bounded, good for CPU intensive tasks
ExecutorService fixed = Executors.newFixedThreadPool(4);
// always 4 threads — extras wait in queue

// Cached — flexible, unbounded, good for short lived tasks
ExecutorService cached = Executors.newCachedThreadPool();
// creates new thread if none available — can grow indefinitely
```

| Feature | FixedThreadPool | CachedThreadPool |
| :--- | :--- | :--- |
| **Thread count** | Bounded — set upfront | Unbounded — grows as needed |
| **When busy** | Queues excess tasks | Creates new thread |
| **Idle threads** | Kept alive permanently | Terminated after 60 seconds |
| **Best for** | CPU intensive, long tasks | Short lived, many small tasks |
| **Risk** | Tasks may wait in queue | Too many threads under heavy load |

</details>

<details>
<summary>What is a `Future`?</summary>

A `Future` represents the result of an asynchronous task that hasn't necessarily completed yet - it's a promise of a value that will be available at some point. It allows the main thread to continue doing other work while the task runs in the background, and retrieve the result later when needed via `get()`.

```java
ExecutorService pool = Executors.newFixedThreadPool(4);

// task submitted — starts running in background
Future<String> future = pool.submit(() -> fetchDataFromApi()); // returns immediately

// main thread can do other work while task runs
doOtherWork();

// retrieve result when needed — blocks if not ready yet
String result = future.get(); // waits until task completes
```

`Future` also provides useful methods worth knowing:

```java
future.get();                        // blocks until result ready
future.get(5, TimeUnit.SECONDS);     // blocks with timeout
future.isDone();                     // checks if task completed
future.isCancelled();                // checks if task was cancelled
future.cancel(true);                 // attempts to cancel the task
```

`CompletableFuture` is the modern replacement — it allows non-blocking, composable async pipelines instead of blocking on get()

</details>
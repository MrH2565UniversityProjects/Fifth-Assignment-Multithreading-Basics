### Q1a: What is the output of the program, and why?

The output appears in this order:

```
Calling run()
Running in: main
Calling start()
Running in: Thread-2
```

This happens because `run()` is called directly at first, so it runs within the **main thread**. Later, when `start()` is called, it triggers the creation of a **new thread**, which then executes its own `run()` method independently.

---

### Q1b: How does calling `start()` differ from calling `run()`?

- `run()` is a **regular method**—it executes in the **current thread**, like any other method.
- `start()` tells the JVM to **spawn a new thread** and have it execute the `run()` method, allowing for **parallel execution**.

---

### Q2a: What output does this program produce, and why?

The output **always** includes:

```Main thread ends.```

You may also see several lines like:

```Daemon thread running...```


However, once the **main thread finishes**, the JVM stops all **daemon threads** automatically, so those lines might not appear every time, or may appear only a few times depending on the scheduler.

---

### Q2b: What if we remove `thread.setDaemon(true)`?

Then the thread becomes a **normal (non-daemon)** thread.  
The JVM will now **wait** for it to complete its work before exiting. As a result, you'll see the message:


```Daemon thread running...```


printed **20 times**, as the loop inside `run()` is allowed to complete.

---

### Q2c: What are some real-world use cases for daemon threads?

- JVM **garbage collection** and **finalization** threads
- Background processes like **log flushing** or **metric collection**
- Lightweight services such as **timers** or **heartbeat monitors** that don’t need to block shutdown

---

### Q3a: What is the program’s output?

Just this single line:

```Thread is running using a ...!```


This is printed by the new thread defined using a lambda expression.

---

### Q3b: What is the `() -> { ... }` syntax called?

That’s a **lambda expression**, introduced in **Java 8**. It provides a concise way to represent functional interfaces.

---

### Q3c: How is this approach different from extending `Thread` or implementing `Runnable`?

- **Lambda expression**: Quick and minimal—perfect for short, one-time tasks; no need for extra classes.
- **Implementing `Runnable`**: Keeps your class inheritance open; better for reusable or stateful task logic.
- **Extending `Thread`**: Binds the logic directly to the thread instance; less flexible due to Java’s single inheritance model.



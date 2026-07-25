# Desing patterns

## Creational

### Singleton

<details>
<summary>Concept:</summary>

Problem it solves: you need exactly one instance of a class across the whole application, with a single global access point to it.

Core idea:
1. Make the constructor private (so no body outside can do `new Thing()`)
2. Hold the single instance as a static field inside the class itself
3. Expose a static method that returns that instance (creating it if it doesn't exist yet)

</details>

<details>
<summary>Eager vs lazy initialization</summary>

**Eager initialization:** The instance is create immediately, when the class is loaded - you don't wait for anyone to ask for it.

```java
public class ConfigManager {
    private static final ConfigManager instance = new ConfigManager();

    private ConfigManager() { }

    public static ConfigManager getInstance() {
        return instance;
    }
}
```
* Simple, and thread-safe for free (the JVM guarantees class loading is thread-safe).

* Downside: the object gets built even if the program never actually uses it. Wasteful if construction is expensive (e.g., opens a file, connects to a DB) and that path might not always run.

**Lazy initialization:** You dealy creating the instance until the first time someone actually calls `getInstance()`

```java
public class ConfigManager {
    private static ConfigManager instance;

    private ConfigManager() { }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
}
```

* Only pays the construction cost if/when needed.

* But this version is broken in a multithreaded context. Imagine two threads both call getInstance() at nearly the same time, both see instance == null, and both create a new object. Now you have two instances — the whole point of Singleton is violated.

</details>

<details>
<summary>Implementations:</summary>

Fix 1:

```java
public static synchronized ConfigManager getInstance() {
    if (instance == null) {
        instance = new ConfigManager();
    }
    return instance;
}
```

This works — only one thread can be inside getInstance() at a time. But it has a cost: every single call acquires a lock, even long after the instance already exists and there's no real race condition left to protect against. If this method gets called a lot, that's unnecessary overhead forever.

Fix 2:

```java
public class ConfigManager {
    private static volatile ConfigManager instance;

    private ConfigManager() { }

    public static ConfigManager getInstance() {
        if (instance == null) {                    // 1st check, no lock
            synchronized (ConfigManager.class) {
                if (instance == null) {             // 2nd check, inside lock
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
}
```

Two things worth understanding, not just memorizing:

* Why check twice? The first check (no lock) is a fast path — once the instance exists, threads skip the lock entirely. The second check (inside the lock) protects against two threads both passing the first check before either creates the instance.

* Why volatile? Without it, instance = new ConfigManager() isn't guaranteed to be seen as "fully constructed" by other threads — due to how the JVM can reorder instructions, another thread could see a non-null reference to an object that isn't finished initializing yet. volatile prevents that reordering.

Fix 3: the actually recommended

```java
public class ConfigManager {
    private ConfigManager() { }

    private static class Holder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        return Holder.INSTANCE;
    }
}
```

This is lazy (the Holder class only loads when getInstance() is first called) and thread-safe (JVM class-loading guarantees), with no synchronization overhead at all after that. It's considered the cleanest classic solution.

</details>

### Factory

<details>
<summary>Concept:</summary>

Problem it solves: you have code that needs to create objects, but you want that code hard-wired toa specific concrete class. You wnat to say "give me a `Shape`" without the code knowing (or caring) wether it gets a `Circle`, `Square`, or `Triangle`.

**Why not just use `new` directly?**: If your code is full of `new Circle()`, `new Square()` scattered around, then every time you add a new shape type, you have to hunt down and modify all that scattered code. It also means yout high-level logic is tightly coupled to concrete implementations - bad for testing, bad for extensibility.

**Core idea:**
1. Define a common interface or abstract class (`Shape`)
2. Each concrete type implements it (`Circle`, `Square`)
3. Instead of calling `new` directly, you call a factory method - amethod whose whole job is "decide which concrete class to instantiate, and return it as the common type".

**Flavor A - Simple Factory (not officially a GoF pattern, but very common in practice)**
One class with a method that takes some input (like a string or enum) and returns the right object via a `switch`/`if`.

```java
public class ShapeFactory {
    public static Shape createShape(String type) {
        switch (type) {
            case "circle": return new Circle();
            case "square": return new Square();
            default: throw new IllegalArgumentException("Unknown shape: " + type);
        }
    }
}
```

**Flavor B - True Factory Method (the actual GoF pattern)**
Instead of one factory with a switch statement, you make creation polymorphic. You define an abstract creator class with an abstract `createShape()` method, and each subclass decides what to instantiate.

```java
public abstract class ShapeCreator {
    public abstract Shape createShape();
}

public class CircleCreator extends ShapeCreator {
    @Override
    public Shape createShape(){
        return new Circle();
    }
}

public class SquareCreator extends ShapeCreator {
    @Override
    public Shape createShape(){
        return new Square();
    }
}
```

</details>

### Builder

<details>
<summary>Concept:</summary>

Problem it solves: you have an object with many fields, especially optional ones, and constructing it view contructor gets ugly fast. Think of a User class with `name`, `email`, `age`, `address`, `phone`, `isVerified`... Some required, some optional.

This is unreadable at the call site too — `new User("Alice", "a@x.com", 30, null, "555-1234")` — what's `null` here? What's `"555-1234"`? You have to go check the constructor signature to know.

**Core idea:** separate the construction of a complex object from its representation. You build the object step by step, usign chained method calls that read almost like a sentence, and only get the final object at the end via a `build()` call.

```java
public class User {
    private final String name;    // required
    private final String email;   // required
    private final int age;        // optional
    private final String address; // optional 
    private final String phone;   // optional

    private User(Builder builder){
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
        this.address = builder.adress;
        this.phone = builder.phone;
    }

    public static class Builder {
        private final String name;
        private final String email;
        private int age;
        private String address;
        private String phone;

        public Builder(String name, String email){
            this.name = name;
            this.email = email;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
```

</details>

## Behavioral

### Strategy

<details>
<summary>Concept:</summary>

Problem it solves: you have a behavior (an algorithm, a calculation, a way of doing something) that can vary, and you don't want to hard-code it with `if/else` or `switch` chains scattered through your code. You want to be able to swap the behavior at runtime, and add new behaviors without touching the existing code.

**Core idea:**
1. Define an interface representing "the behavior" (the strategy), with one method.
2. Each concrete way of doing that behavior implement the interface as its own class.
3. The class that needs the behavior (the context) doesn't implement the logic itself - it just holds a reference to a strategy object and delegates to it.
4. You can swap which strategy the context uses at runtime, just by giving it a different object.

```java
public interface DiscountStrategy {
    double applyDiscount(double price);
}

public class NoDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price){
        return price;
    }
}

public class PercentageDiscount implements DiscountStrategy {

    private float percentage;

    public PercentageDiscount(float percentage){
        this.percentage = percentage;
    }

    @Override
    publid double applyDiscount(double price){
        return price * (1 - percentage/100f);
    }
}

public class ShoppingCart {
    private DiscountStrategy discountStrategy;

    public ShoppingCart(DiscountStrategy discountStrategy){
        this.discountStrategy = discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy){
        this.discountStrategy = discountStrategy;
    }

    public void checkout(double price){
        System.out.println(discountStrategy.applyDiscount(price));
    }
}

```

</details>

### Observer

<details>
<summary>Concept:</summary>

Problem it solves: yhou have one object (the subject/publisher) whose state changes, and multiple other objects (observvers/subscribers) that neeed to know about it and react - without the subject needing to know any specifics about who's listening or what they'll do with the information.

**Core idea:**
1. Define an `Observer` interface with an `update(...)` method.
2. The subject keeps a list of observers (not concrete types - just the interface)
3. Subject exposes `subscribe()`/`unsubscribe()` methods to manage that list.
4. When the subject's state changes, it loops through the list and calls `update()` on each one - it has no idea what each observer actually does with that update.

```java
public interface Investor{

    void update(String stockSymbol, double price);
}

public class EmailInvestor implements Investor{

    private String name;

    public EmailInvestor (String name){
        this.name = name;
    }

    @Override
    public void update(String stockSymbol, double price){
        System.out.println(name + " notified: " + stockSymbol + " is now " + price);
    }
}

public class Stock{

    private List<Investor> investors = new ArrayList<>();

    public void setPrice(String symbol, double price){
        for(Investor investor : investors){
            investor.update(symbol, price);
        }
    }

    public void subscribe(Investor investor){
        investors.add(investor);
    }

    public void unsubscribe(Investor investor){
        investors.remove(investor);
    }
}
```

</details>

## Structural

### Decorator

<details>
<summary>Concept:</summary>

Problem it solves: you want to add extra behavior/responsabilities to an object dynamically, without modifying its class and without creating a huge explosion of subclasses for every possible combination of features.

**Core idea:**
1. Define a common interface/abstract class both the base object and the decorators implement (`Coffee`, with method `cost()` and `description()`).
2. The base concrete class implements the "plain" version (`SimpleCoffee`)
3. Each decorator also implements the same interface, but wraps another object of that same interface (composition, not inheritance) and adds its own behavior on top before/after delegating to the wrapped object.
4. You stack decorators around the base object like layers, at runtime, in whatever combination you want.

```java
public interface Pizza{

    double cost();
    String description();
}

public class PlainPizza implements Pizza{
    @Override
    public double cost(){
        return 5.0;
    }
    @Override
    public String description(){
        return "Pizza";
    }
}

public abstract class PizzaDecorator implements Pizza{

    protected Pizza decoratedPizza;

    public PizzaDecorator(Pizza pizza){
        this.decoratedPizza = pizza;
    }
}

public class CheeseTopping extends PizzaDecorator{
    public CheeseTopping(Pizza pizza){
        super(pizza);
    }

    @Override
    public double cost(){
        return decoratedPizza.cost() + 1.5;
    }

    @Override
    public String description(){
        return decoratedPizza.description() + " ,Cheese";
    }
}

Pizza base = new PlainPizza();
Pizza cheese1 = new CheeseTopping(base);
Pizza cheese2 = new CheeseTopping(cheese1);

System.out.println("Cost: " + cheese2.cost() + " Description: " + cheese2.description());
```

</details>

### Adapter

<details>
<summary>Concept:</summary>

Problem it solves: you have two incompatible interfaces that need to work together - typically, some existing class (oftern from a library you don't control, or legacy code) whose interface doesn't match what your code expects - and you can't (or shouldn't) modify that existing class.

**Core idea:**
1. You have a target interface your client code already expects (`MediaPlayer`).
2. You have an incompatible class you want to reuse (`VlcPlayer`) - the adaptee.
3. You create an Adapter class that implements the target interface, but internally holds an instance of the adaptee and translates calls from the target interface into whatever the adaptee actually needs.

```java
public interface Printer{
    public void print(String text);
}

public class OldPrinter{
    public void printOld(String text){
        System.out.println(text);
    }
}

public class PrinterAdapter implements Printer{
    private OldPrinter oldPrinter;

    public PrinterAdapter(OldPrinter oldPrinter){
        this.oldPrinter = oldPrinter;
    }

    @Override
    public void print(String text){
        oldPrinter.printOld(text);
    }
}
```

</details>
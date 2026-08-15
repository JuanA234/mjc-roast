# Functional programming, Stream API

<details>
<summary>What is `Function Interface`?</summary>

A functional interface is the one that declares only one abstract method. Because it represents a single behavior, its implementation can be expressed as a lambda expression instead ofa full anonymous class. Java provides the `@FunctionalInterface` annotation to enforce this contract - the compiler throws an error if you accidentally add a second abstract method.

```java
@FunctionalInterface
interface Greeting {
    String greet(String name); // only one abstract method
}

// Without lambda — verbose
Greeting g = new Greeting() {
    @Override
    public String greet(String name) {
        return "Hello " + name;
    }
};

// With lambda — clean
Greeting g = name -> "Hello " + name;

System.out.println(g.greet("Carlos")); // Hello Carlos
```

Java provides ready-made functional interfaces so you don't have to create your own for common use cases - `Predicate`, `Function`, `Supplier`, `Consumer`.

Functional interfaces can extend other interfaces - as long as the total number of abstract methods across the entire hierarchy remains exactly one. Default and static methods don't count towards that limit.

</details>

<details>
<summary>What is an anonymous class?</summary>

An anonymous class is a nameless class declared and instanciated in one expression - useful for one-time implementations but verbose. Lambdas replaced them for functional interfaces by reducing the boilerplate to a single expression.

```java
// Step 1 — named class (reusable but verbose)
class MyGreeting implements Greeting { ... }
Greeting g = new MyGreeting();

// Step 2 — anonymous class (one-time, still verbose)
Greeting g = new Greeting() {
    public String greet(String name) { return "Hello " + name; }
};

// Step 3 — lambda (one-time, clean and concise)
Greeting g = name -> "Hello " + name;
```

| Feature | Anonymous class | Lambda |
| :--- | :--- | :--- |
| **Extend a class** | ✅ Yes | ✗ No |
| **Implement one interface** | ✅ Yes | ✅ Yes (only functional) |
| **Implement multiple interfaces** | ✗ No | ✗ No |
| **Can have state (fields)** | ✅ Yes | ✗ No |
| **Concise syntax** | ✗ No | ✅ Yes |

An anonymous class can either:
* Extend one class, or
* Implement one interface

Never both, never multiple interfaces - that's one of its key limitations.

</details>

<details>
<summary>What are the benefits of using Functional Interface?</summary>

Functinoal interfaces provide several benefits: they enable concise lambda expressions replacing verbose anonymous classes; they allow passing behavior as parameters making code flexible and reusable without creating new classes for every variation; they are the foundation of the Stream API enabling a clean functional programming style; and they promote separation of concerns - defining what needs to be done independently of how it's executed.

1. Conciseness:

```java
// ❌ verbose anonymous class
Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});

// ✅ functional interface + lambda
Collections.sort(names, (a, b) -> a.compareTo(b));
```

2. Passing behavior as a parameter:

```java
// Method accepts behavior as parameter
void process(List<String> names, Predicate<String> filter) {
    names.stream().filter(filter).forEach(System.out::println);
}

// Pass different behaviors without changing the method
process(names, name -> name.startsWith("C")); // filter by C
process(names, name -> name.length() > 5);    // filter by length
```

3. Enables Stream API and functional programming style:

```java
// Stream API is built entirely on functional interfaces
names.stream()
     .filter(name -> name.startsWith("C"))  // Predicate
     .map(String::toUpperCase)              // Function
     .forEach(System.out::println);         // Consumer
```

</details>

<details>
<summary>What is the difference between normal and functional interfaces</summary>

The main difference is that functional interfaces declare exactly one abstract method, while normal interfaces can declare as many as needed. This single method constraint is what allows functional interfaces to be implemented as lambda expressions- normal interfaces cannot. Functional interfaces can also be annoted with `@FunctionalInterface` which enforces the contract at compile time. Additionally functional interfaces can only extend another interface if the total number of abstract methods across the hierarchy remains exactly one.

```java
// Normal interface — multiple abstract methods, no lambda possible
interface Animal {
    void speak();
    void move();
    void eat();
}

// Functional interface — one abstract method, lambda possible
@FunctionalInterface
interface Greeting {
    String greet(String name); // only one — lambda allowed

    default void helper() { } // default methods don't count
}

// Lambda — only possible because Greeting is functional
Greeting g = name -> "Hello " + name;
```

</details>

<details>
<summary>What is `Predicate` interface?</summary>

`Predicate<T>` is a functional interface that represents a condition that takes an argument of type `T` and returns a boolean. It is commonly used for filtering collections in Stream API or anywhere a boolean condition needs to be passed as a parameter.

```java
// Predicate definition
Predicate<String> startsWithC = name -> name.startsWith("C");

// Using it directly
System.out.println(startsWithC.test("Carlos")); // true
System.out.println(startsWithC.test("Maria"));  // false

// Most common use — filtering in streams
List<String> names = List.of("Carlos", "Maria", "Carmen");
names.stream()
     .filter(startsWithC) // Predicate passed as parameter
     .forEach(System.out::println); // Carlos, Carmen
```

Predicates can be combined:

```java
Predicate<String> startsWithC = name -> name.startsWith("C");
Predicate<String> longerThan5  = name -> name.length() > 5;

// and() — both conditions must be true
names.stream().filter(startsWithC.and(longerThan5))  // "Carlos"
// or() — either condition must be true
names.stream().filter(startsWithC.or(longerThan5))
// negate() — condition must be false
names.stream().filter(startsWithC.negate())          // "Maria"
```

A `Predicate` is essentially a boolean condition promoted to a first class object - it wraps the condition so it can be stored in a variable, passed as a parameter, returned from a method, and combined with other predicates. Regular boolean expressions can't do any of that.

</details>

<details>
<summary>First class object:</summary>

A first class object is something that can be:

* Stored in a variable
* Passed as a parameter
* Returned  from a method
* Created at runtime 

```java
// String is a first class object — can do all of these
String name = "Carlos";                    // stored in variable
void print(String name) { }               // passed as parameter
String getName() { return "Carlos"; }     // returned from method
String dynamic = new String("Carlos");    // created at runtime

// int, boolean, double etc. can't be passed as Object
// that's why wrapper classes exist — Integer, Boolean, Double
int x = 5;        // not a first class object
Integer x = 5;    // ✅ first class object — autoboxed
```

First class objects = data treated as objects.

</details>

<details>
<summary>First class function</summary>

In functional programming treating behavior (like conditions or functions) as objects that can be passed around is called first class functions. `Predicate`, `Function`, `Consumer`, `Supplier` are all Java's way of achieving this.

First class function = behavior treated as objects.

</details>

<details>
<summary>What is `Supplier` interface?</summary>

`Supplier<T>` is a functional interface that represents or wraps certain work or operation and returns a value, it doesn't take any arguments. The key benefit is deferred execution, `Supplier` shines when you want to delay the creation or computation until it's actually needed.

Scenario 1 - expensive object creation:

```java
// ❌ without Supplier — always creates the object, even if never used
User user = database.findUser(id); // expensive DB call happens immediately
logger.debug("User: " + user);    // but maybe debug logging is disabled!

// ✅ with Supplier — only creates if actually needed
logger.debug(() -> "User: " + database.findUser(id)); // DB call only if debug enabled
```

Scenario 2 - passing creation logic as a parameter:

```java
// ✅ pass HOW to create something, not the thing itself
void initializeIfEmpty(List<String> list, Supplier<List<String>> defaultSupplier) {
    if (list == null) {
        list = defaultSupplier.get(); // only created if list is null
    }
}

initializeIfEmpty(null, () -> new ArrayList<>()); // creation logic passed as parameter
```

Scenario 3 - `Optional.orElseGet()` - you've actually seen this already:

```java
// ❌ orElse — always evaluates, even if Optional has a value
Optional<String> name = Optional.of("Carlos");
name.orElse(expensiveComputation()); // expensiveComputation always runs ❌

// ✅ orElseGet — Supplier only called if Optional is empty
name.orElseGet(() -> expensiveComputation()); // only runs if name is empty ✅
```

`Supplier` is useful not just for what it returns but when - it wraps creation or computation logic so it can be passed around and executed only when actually needed, avoiding unnecessary work.

</details>

<details>
<summary>What is `Consumer` interface?</summary>

`Consumer<T>` is a functional interface that takes an argument of type `T`, performs some operation on it, and returns nothing. It represents a side-effect operation - like printing, saving or sending data - where the result is an action rather than a value.

```java
// Consumer definition
Consumer<String> printer = name -> System.out.println("Hello " + name);

// Using it directly
printer.accept("Carlos"); // Hello Carlos

// Most common use — forEach in streams
List<String> names = List.of("Carlos", "Maria", "Juan");
names.forEach(printer); // Consumer passed as parameter
// or inline
names.forEach(name -> System.out.println(name));
```

Consumers can be chained:

```java
Consumer<String> print  = name -> System.out.println(name);
Consumer<String> save   = name -> database.save(name);

// andThen() — execute both consumers sequentially
Consumer<String> printAndSave = print.andThen(save);
names.forEach(printAndSave); // prints and saves each name
```

</details>

<details>
<summary>What is `Function` interface?</summary>

`Function<T, R>` is a functional interface that takes an argument of type `T` and after performing some operation with it, returns a result of type `R`. It represents a transformation - converting or mapping one value into another.

```java
// Function definition
Function<String, Integer> length = name -> name.length();

// Using it directly
System.out.println(length.apply("Carlos")); // 6

// Most common use — map() in streams
List<String> names = List.of("Carlos", "Maria", "Juan");
names.stream()
     .map(length) // Function passed as parameter
     .forEach(System.out::println); // 6, 5, 4
```

Functions can be chained:

```java
Function<String, String> trim      = name -> name.trim();
Function<String, String> uppercase = name -> name.toUpperCase();

// andThen() — chain functions sequentially
Function<String, String> trimAndUpper = trim.andThen(uppercase);
System.out.println(trimAndUpper.apply("  carlos  ")); // "CARLOS"
```

| Interface | Takes | Returns | Use for |
| :--- | :--- | :--- | :--- |
| **Predicate\<T>** | `T` | `boolean` | Filtering |
| **Supplier\<T>** | nothing | `T` | Creating/providing |
| **Consumer\<T>** | `T` | nothing | Side effects |
| **Function\<T,R>** | `T` | `R` | Transforming |

</details>

<details>
<summary>`Predicate`, `Supplier`, `Consumer`, `Function`</summary>

Predicate<T>     → takes T        → returns boolean
Supplier<T>      → takes nothing  → returns T
Consumer<T>      → takes T        → returns nothing
Function<T, R>   → takes T        → returns R

</details>

<details>
<summary>What is Lambda Expression?</summary>

A lambda expression is an anonymous funtion that can be stored in a variable and passed as a parameter and executed when needed.

```java
// Lambda syntax — parameters -> body
() -> "Hello"                    // no params, returns value
name -> name.toUpperCase()       // one param, returns value
(a, b) -> a + b                  // two params, returns value
name -> System.out.println(name) // one param, no return
```

Are all functional interfaces lambdas?

Not exactly - here's the precise relationship:

* A lambda is the syntax - the `() ->` expression
* A functional interface is the type that holds the lambda
* They need each other - a lambda must be assigned to a functional interface

```java
// Lambda needs a functional interface type to exist
Predicate<String> p  = name -> name.startsWith("C"); // lambda stored in Predicate
Supplier<String>  s  = () -> "Hello";                // lambda stored in Supplier
Consumer<String>  c  = name -> System.out.println(name); // lambda stored in Consumer
Function<String, Integer> f = name -> name.length(); // lambda stored in Function
```

So, lambdas are not functional interfaces themselves - they are the implementation of a functional interface, expressed concisely.
* Functional interface = the contract (what shape the function should have)
* Lambda = the implementation (the actual behavior)

</details>

<details>
<summary>How is Lambda Expression and Anonymous class related?</summary>

They share the characteristic of being anonymous, they dont have a name, but can be stored in a variable to use when needed. A lambda is concise replacement for an anonymous class when implementing a functional interface - same behavior, far less boilerplate. Both contribute to functional programming by allowing behavior to be passed as a parameter.

```java
// Anonymous class — verbose
Predicate<String> p = new Predicate<String>() {
    @Override
    public boolean test(String name) {
        return name.startsWith("C");
    }
};

// Lambda — same result, concise
Predicate<String> p = name -> name.startsWith("C");
```

| Feature | Anonymous class | Lambda |
| :--- | :--- | :--- |
| **Can have state (fields)** | ✅ Yes | ✗ No |
| **Multiple methods** | ✅ Yes | ✗ No (only one) |
| **Conciseness** | ✗ verbose | ✅ concise |
| **`this` refers to** | anonymous class instance | enclosing class |

</details>

<details>
<summary>Can you pass Lambda Expression as method parameter?</summary>

Yes, it is possible to pass a lambda as a method parameter, that's one of its main features, to allow passing behavior as a parameter, but it needs to be wrapped in a functional interface like `Predicate`, `Supplier`, `Consumer`, `Function`, that's the only way Java knows what type the lambda is:

```java
// The method parameter must be a functional interface type
void process(Predicate<String> condition) {
    // use the lambda
}

// Passing lambda as parameter
process(name -> name.startsWith("C")); // ✅ lambda matches Predicate<String> shape
```

</details>

<details>
<summary>What is the meaning of deferred execution of functionality, using a Lambda Expression?</summary>

Defered execution means that the behavior wrapped in a lambda is only executed when is actually needed - not when defined or passed. This avoids unnecessary computation, saving resources when the result may never be needed.

```java
// ❌ eager execution — computation always happens
String result = expensiveComputation(); // runs immediately
logger.debug("Result: " + result);      // even if debug logging is disabled

// ✅ deferred execution — computation only happens if debug is enabled
logger.debug(() -> "Result: " + expensiveComputation()); // lambda only called if needed

// orElse — eager, always evaluates
optional.orElse(expensiveComputation());        // always runs ❌

// orElseGet — deferred, only evaluates if needed
optional.orElseGet(() -> expensiveComputation()); // only runs if empty ✅
```

</details>

<details>
<summary>How’s Lambda Expression and Functional Interface related?</summary>

Functional interfaces annd lambdas are two sides of the same coin - a fucntional interface defines de contratc (the shape: what parameters and return type the function should have), and the lambda provides the implementation (the actual behavior). Java uses functional interface type to infer what a lambda means - the same lambda body can mean different things depending on wich functional interface it's assigned to.

```java
// Same lambda body — different meaning based on functional interface type
Predicate<String>        p = s -> s.length() > 3; // returns boolean
Function<String, Boolean> f = s -> s.length() > 3; // returns Boolean object

// Functional interface = contract (shape)
// Lambda = implementation (behavior)
Predicate<String> condition = name -> name.startsWith("C");
//     ↑ contract                    ↑ behavior
```

Lambdas don't exist independently - they always need a functional interface type to give them meaning. The functional interface is the type, the lambda is the value.

Even inline lambdas always need a functional interface type - when used directly in methods like `filter()` or map, Java infers the type automatically from the method signature. You can never pass a lambda that doesn't match the expected functional interface shape.

</details>

<details>
<summary>What is `method reference`?</summary>

A method reference is shorthand syntax for a lambda that does nothing but call an existing method. Instead of writing `name -> System.out.println(name)` you write `System.out::println` - the `::` operator tells Java to use that method directly as the lambda implementation.

Four types of method references:

1. Static method:

```java
// lambda
Function<String, Integer> f = s -> Integer.parseInt(s);
// method reference
Function<String, Integer> f = Integer::parseInt;
```

2. Instance method on a specific object:

```java
// lambda
Consumer<String> c = s -> System.out.println(s);
// method reference
Consumer<String> c = System.out::println;
```

3. Instance method on an arbitrary object of a type:

```java
// lambda
Function<String, String> f = s -> s.toUpperCase();
// method reference
Function<String, String> f = String::toUpperCase;
```

4. Constructor:

```java
// lambda
Supplier<List<String>> s = () -> new ArrayList<>();
// method reference
Supplier<List<String>> s = ArrayList::new;
```

Quick memory trick:

Static → class does the work
Instance on type → the element does the work on itself
Instance on object → a specific object does the work
Constructor → creating a new object

</details>

<details>
<summary>What is a `Pure Function`?</summary>

A pure function, is a function that no matter how many times you call it the result for the same input is the same output, also doesn't modify any state outside of itself - no modifying global variables, no writing to database, no printing, no modifying the input arguments:

1. Same input always produces same output
2. No side effects

```java
// ✅ pure function — same input always same output, no side effects
int add(int a, int b) {
    return a + b; // depends only on inputs, modifies nothing outside
}

// ❌ not pure — depends on external state
int addWithTax(int price) {
    return price + taxRate; // taxRate could change — different result same input
}

// ❌ not pure — has side effect
int addAndLog(int a, int b) {
    logger.log("Adding"); // side effect — modifies external state, it writes to a log file
    return a + b;
}

// ❌ not pure — modifies input argument
void addToList(List<Integer> list, int value) {
    list.add(value); // modifies the input — side effect
}
```

A pure function should be completely replaceable by its return value with no observable difference to the rest of the program.

</details>

<details>
<summary>What is the use of `Pure Function` in Functional Programming?</summary>

Pure functions are the foundation of functional programming because their predictability unlocks three key benefits: they are easy to test - no external dependencies to mock; safe to parallelize - no shared state means no race conditions; and cacheable - since same input always produces same output, results can be stored and reused safely.

1. Easy to test:

```java
// Pure function — just call it with inputs and check output
int result = add(2, 3);
assert result == 5; // ✅ always true, no database, no mocks needed

// Impure function — need to mock database, logger, external state
int result = addAndSave(2, 3); // need to mock database ❌
```

2. Safe to parallelize:

```java
// Pure functions can run in parallel safely
list.parallelStream()
    .map(n -> n * 2) // pure — no shared state, safe in parallel ✅
    .collect(Collectors.toList());
```

3. Cacheable:

```java
// Result can be cached safely — will never change for same input
Map<Integer, Integer> cache = new HashMap<>();
int result = cache.computeIfAbsent(5, n -> expensiveComputation(n));
// second call with 5 returns cached result — no recomputation needed ✅
```

</details>

<details>
<summary>What is `Stream API`?</summary>

Stream API is a tool introduced in Java 8 for processing sequences of elements in a functional style. It allows you to perform operations like filtering, mapping, sorting and reducing on collections without modifying the original data source. Streams are lazy - operations are only executed when a terminal operation is called - and can be parallelized easily for better performance.

```java
List<String> names = List.of("Carlos", "Maria", "Juan", "Carmen");

// Without Stream API — verbose, imperative
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.startsWith("C")) {
        result.add(name.toUpperCase());
    }
}

// With Stream API — clean, functional
List<String> result = names.stream()
    .filter(name -> name.startsWith("C"))  // filtering
    .map(String::toUpperCase)              // transforming
    .collect(Collectors.toList());         // collecting result
// [CARLOS, CARMEN]
```

</details>

<details>
<summary> What is the difference between `Collection` and `Stream`?</summary>

`Collection` is an interface that is the parent of all data structures provided by the Java Collections API for storing bundles of objects, `Stream` meanwhile is a sequence of elements that allows to perform operations like filtering, mapping, sorting and reducing in a functional style.

A `Collention` is a data structure that store elements in memory - you can add, remove and access elements at any time. A `Stream` is a sequence of elements for processing - it doesn't store data, it takes data from a source like a collection and processes it.

```java
// Collection — stores data, can be modified
List<String> names = new ArrayList<>();
names.add("Carlos");    // ✅ can add
names.remove("Carlos"); // ✅ can remove
names.get(0);           // ✅ can access anytime

// Stream — processes data, can't be modified or reused
Stream<String> stream = names.stream();
stream.filter(...).map(...).collect(...); // ✅ process once
stream.filter(...); // ❌ stream already consumed — can't reuse
```

| Feature | Collection | Stream |
| :--- | :--- | :--- |
| **Purpose** | Storing data | Processing data |
| **Modifiable** | ✅ Yes | ✗ No |
| **Reusable** | ✅ Yes | ✗ Once consumed |
| **Lazy** | ✗ No | ✅ Yes |
| **Parallel** | ⚠️ Complex | ✅ Easy with `parallelStream()` |
| **Stores data** | ✅ Yes | ✗ No — reads from source |

</details>

<details>
<summary>What is the parallel Stream?</summary>

A parallel stream divides the data source into multiple chunks and processes them simultaneouly across multiple CPU cores, using the Fork Join pool under the hood. It's created simply by calling `parallelStream()` instead of `stream()` - the API is identical, Java handles the parallelization automatically.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);

// Sequential stream — one element at a time
numbers.stream()
       .map(n -> n * 2)
       .collect(Collectors.toList());

// Parallel stream — chunks processed simultaneously across cores
numbers.parallelStream()
       .map(n -> n * 2)
       .collect(Collectors.toList());
```

When to use parallel streams:

```java
// ✅ good for — large datasets, CPU intensive operations
largeList.parallelStream()
         .map(item -> heavyComputation(item))
         .collect(Collectors.toList());

// ❌ bad for — small datasets, order dependent operations, I/O bound tasks
smallList.parallelStream() // overhead of splitting > benefit of parallelism
         .forEach(System.out::println); // order not guaranteed
```

| Feature | Sequential Stream | Parallel Stream |
| :--- | :--- | :--- |
| **Performance on large data** | Slower | ✅ Faster |
| **Performance on small data** | ✅ Faster | ✗ Slower — splitting overhead |
| **Order guaranteed** | ✅ Yes | ✗ No |
| **Thread safe required** | ✗ No | ✅ Yes |

</details>

<details>
<summary>What are intermediate operations?</summary>

Intermediate operations are all those that are between the moment the stream is created and a terminal operations. Intermediate operations don't execute immediately - they just build up the pipeline. They only execute when a terminal operation is called.

```java
names.stream()
     .filter(name -> name.startsWith("C"))  // intermediate — lazy
     .map(String::toUpperCase)              // intermediate — lazy
     .sorted()                             // intermediate — lazy
     .collect(Collectors.toList());         // terminal — triggers everything
```

Common intermediate operations:

| Operation | What it does |
| :--- | :--- |
| **`filter(Predicate)`** | Keeps elements matching condition |
| **`map(Function)`** | Transforms each element |
| **`flatMap(Function)`** | Transforms and flattens nested streams |
| **`sorted()`** | Sorts elements |
| **`distinct()`** | Removes duplicates |
| **`limit(n)`** | Keeps first n elements |
| **`skip(n)`** | Skips first n elements |
| **`peek(Consumer)`** | Performs action without consuming stream |

```java
names.stream()
     .filter(name -> name.startsWith("C"))  // keep names starting with C
     .map(String::toUpperCase)              // transform to uppercase
     .sorted()                             // sort alphabetically
     .distinct()                           // remove duplicates
     .limit(5)                             // keep first 5
     .collect(Collectors.toList());         // terminal — triggers everything
```

</details>

<details>
<summary>What are terminal operations?</summary>

Terminal operations are the final operation in a stream pipeline, that trigger the execution of all intermediate operations and produce a result or side effect. Once a terminal operation is called the stream is consumed and cannot be reused.

```java
names.stream()
     .filter(name -> name.startsWith("C"))  // intermediate — not executed yet
     .map(String::toUpperCase)              // intermediate — not executed yet
     .collect(Collectors.toList());         // terminal — triggers everything
                                            // stream consumed — can't reuse
```

| Operation | What it does |
| :--- | :--- |
| **`collect(Collector)`** | Collects elements into a collection |
| **`forEach(Consumer)`** | Performs action on each element |
| **`count()`** | Returns number of elements |
| **`findFirst()`** | Returns first element |
| **`findAny()`** | Returns any element |
| **`anyMatch(Predicate)`** | Returns true if any element matches |
| **`allMatch(Predicate)`** | Returns true if all elements match |
| **`noneMatch(Predicate)`** | Returns true if no elements match |
| **`min(Comparator)`** | Returns minimum element |
| **`max(Comparator)`** | Returns maximum element |
| **`reduce()`** | Reduces elements to a single value |
| **`toArray()`** | Collects elements into an array |

</details>

<details>
<summary>What is the difference between intermediate and terminal operations on Stream?</summary>

The main difference is that intermediate operations don't consume the stream and don't produce a result or any side effect. Terminal operations are the final operation of the pipeline and the ones that triggers intermediate operations for execution, consume the stream and produce the result or side effect. Once a terminal operation is called the stream is consumed and cannot be reused.

```java
names.stream()
     .filter(name -> name.startsWith("C"))  // intermediate — lazy, not executed yet
     .map(String::toUpperCase)              // intermediate — lazy, not executed yet
     .collect(Collectors.toList());         // terminal — triggers everything
                                            // produces result, stream consumed
```

| Feature | Intermediate | Terminal |
| :--- | :--- | :--- |
| **Executes immediately?** | ✗ Lazy | ✅ Yes |
| **Returns** | New stream | Result or void |
| **Consumes stream?** | ✗ No | ✅ Yes |
| **Can reuse stream after?** | ✅ Yes | ✗ No |
| **Examples** | `filter`, `map`, `sorted` | `collect`, `forEach`, `count` |

</details>

<details>
<summary>What do we say that Stream is lazy (lazy evaluation)?</summary>

This is because a stream pipeline of operations is not executed until a final operation is called - the pipeline is just built up and optimized first. This is beneficial because it avoids unnecessary processing - if a terminal operation only needs a few elements, the stream doesn't process the entire source.

```java
// Without laziness — processes ALL elements at every step
List<String> names = List.of("Carlos", "Maria", "Carmen", "Juan", "Pedro");

names.stream()
     .filter(name -> {
         System.out.println("filtering: " + name);
         return name.startsWith("C");
     })
     .map(name -> {
         System.out.println("mapping: " + name);
         return name.toUpperCase();
     })
     .findFirst(); // only needs ONE element

// Output — laziness stops processing after first match:
// filtering: Carlos  ← first element passes filter
// mapping: Carlos    ← mapped immediately
// done — findFirst() has its result, rest never processed ✅
```

Streams are lazy because intermediate operations only execute when a terminal operation demands results — and only process as many elements as needed, avoiding unnecessary work on the rest of the source.

</details>

<details>
<summary>What is the difference between `findFirst()` and `findAny()` methods?</summary>

As the name says, `findFirst()` returns the first element of the stream in encounter order, while `findAny()` returns any element - in a sequential stream it typically returns the first element too, but in parallel stream it return whichever element is found first across the parallel threads, making it faster since it doesn't need to coordinate order. Both are terminal opertions that return an `Optional` and consume the stream.

```java
List<String> names = List.of("Carlos", "Maria", "Carmen");

// Sequential — both behave similarly
names.stream().findFirst(); // Optional["Carlos"] — always first
names.stream().findAny();   // Optional["Carlos"] — usually first

// Parallel — findAny() shines
names.parallelStream().findFirst(); // Optional["Carlos"] — must maintain order ❌ slower
names.parallelStream().findAny();   // Optional[any] — no order needed ✅ faster
```

Use `findFirst()` when order matters - use `findAny()` in parallel streams when you just need any match and want better performance.

</details>

<details>
<summary>What does `peek()` method do? When to use it?</summary>

The method `peek()` performs a `Consumer` action on each element without modifying them and passes the same elements downstream. It returns the same stream it received - elements are unchanged. Its main use case is debugging - inspecting elements as they flow through the pipeline without affecting the result.

```java
List<String> names = List.of("Carlos", "Maria", "Carmen");

List<String> result = names.stream()
     .filter(name -> name.startsWith("C"))
     .peek(name -> System.out.println("after filter: " + name)) // debug
     .map(String::toUpperCase)
     .peek(name -> System.out.println("after map: " + name))    // debug
     .collect(Collectors.toList());

// Output:
// after filter: Carlos
// after map: CARLOS
// after filter: Carmen
// after map: CARMEN
```

What not to use `peek()` for:

```java
// ❌ don't use peek() for side effects in production code
stream.peek(item -> database.save(item)) // bad practice — use forEach instead
      .collect(toList());

// ✅ peek() is for debugging only
// ✅ use forEach() for intentional side effects
```

</details>

<details>
<summary>What is the difference between `flatMap()` and `map()`?</summary>

`map()` is for realizing transformations, turning one value into another through some operation, and `flatMap()` is for flattening nested streams into just one.

```java
List<List<String>> nested = List.of(
    List.of("Carlos", "Maria"),
    List.of("Juan", "Pedro")
);

// map() — stays nested
nested.stream()
      .map(list -> list.stream())
      .collect(toList()); // Stream<Stream<String>> — still nested ❌

// flatMap() — flattens into single stream
nested.stream()
      .flatMap(list -> list.stream())
      .collect(toList()); // [Carlos, Maria, Juan, Pedro] ✅
```

The pattern to remember: whenever your lambda inside map() would itself return a Stream (or something you'd turn into a stream, like an array or a List), that's your signal to use flatMap() instead — otherwise you end up with that stream-of-streams problem we just talked about.

</details>

<details>
<summary>What is collector?</summary>

A `Collector` is a terminal operation tool that accumulates stream elemensts into a result container - like a collection, string or map. Collectors are passed to the `collect()` method and Java provides ready-made ones through the `Collectors` utility class.

```java
List<String> names = List.of("Carlos", "Maria", "Carmen", "Juan");

// Collecting into different structures
List<String> list    = names.stream().collect(Collectors.toList());
Set<String>  set     = names.stream().collect(Collectors.toSet());
String       joined  = names.stream().collect(Collectors.joining(", "));
// "Carlos, Maria, Carmen, Juan"

// Grouping by first letter
Map<Character, List<String>> grouped = names.stream()
    .collect(Collectors.groupingBy(name -> name.charAt(0)));
// {C=[Carlos, Carmen], M=[Maria], J=[Juan]}

// Counting
long count = names.stream().collect(Collectors.counting());
```

| Method | Returns |
| :--- | :--- |
| **`toList()`** | `List` |
| **`toSet()`** | `Set` |
| **`toMap()`** | `Map` |
| **`joining()`** | `String` |
| **`groupingBy()`** | `Map<K, List<T>>` |
| **`counting()`** | `Long` |

</details>

<details>
<summary>What is `java.util.stream.Collectors`?</summary>

`java.util.stream.Collectors` is a utility class that provides ready-made implementations of the `Collector` interface - passed as arguments to the `collect()` terminal operation to accumulate stream elements into different result containers like lists, sets, maps or strings.

```java
// Collectors provides the implementations
names.stream().collect(Collectors.toList());      // → List
names.stream().collect(Collectors.toSet());       // → Set
names.stream().collect(Collectors.joining(", ")); // → String
names.stream().collect(Collectors.groupingBy(name -> name.charAt(0))); // → Map
```

The relationship between the three:
* Stream.collect() — the terminal operation that accepts a Collector
* Collector — the interface defining how to accumulate elements
* Collectors — the utility class providing ready-made Collector implementations

</details>

<details>
<summary>What is inside `java.util.function` package?</summary>

`java.util.function` contains Java's built-in functional interfaces covering every common function shape - single and double parameter variants for conditions, side effects, transformations and provisions - so you rarel need to create your own.

```java
// UnaryOperator — special Function where input and output are same type
UnaryOperator<String> upper = s -> s.toUpperCase();
// same as Function<String, String> but more expressive

// BinaryOperator — special BiFunction where all types are the same
BinaryOperator<Integer> sum = (a, b) -> a + b;
// same as BiFunction<Integer, Integer, Integer> but more expressive
```

| Interface | Takes | Returns | Use for |
| :--- | :--- | :--- | :--- |
| **`Predicate<T>`** | `T` | `boolean` | Filtering, conditions |
| **`Supplier<T>`** | nothing | `T` | Creating, providing |
| **`Consumer<T>`** | `T` | nothing | Side effects |
| **`Function<T,R>`** | `T` | `R` | Transforming |
| **`BiPredicate<T,U>`** | `T`, `U` | `boolean` | Two param condition |
| **`BiConsumer<T,U>`** | `T`, `U` | nothing | Two param side effect |
| **`BiFunction<T,U,R>`** | `T`, `U` | `R` | Two param transform |
| **`UnaryOperator<T>`** | `T` | `T` | Transform same type |
| **`BinaryOperator<T>`** | `T`, `T` | `T` | Combine two same type |

</details>

<details>
<summary>What are techniques to create the stream</summary>

* empty stream: `Stream.empty()`
* from collection: `collection.stream()`
* parallel stream from collection: `collection.parallelStream()`
* from values: `Stream.of(value1,...valueN)`
* from array: `Arrays.stream(array)`
* from file: `Files.lines(file_path)`
* from string: "some_string".chars()`
* Stream.builder: `Stream.builder().add(...)....build()`
* endless stream via `Stream.iterate`: `Stream.iterate(initial_condition, generate_expressions)`
* endless stream via `Stream.generate`: `Stream.generate(expression)`
* stream of primitives: `IntStream.range(from, to)`, `LongStream.rangeCosed(from, to`)

</details>

<details>
<summary>Difference between Optional and null:</summary>

`Optional` is a container that explicitly represents the possible absence of a value. Before `Optional`, methods returned `null` to signal no result, but the compiler couldn't enforce null checks - leading to `NullPointerExceptions` at runtime, `Optional` makes absence explicit in the method signature, forcing callers to consciously handle the empty case using methods like `orElse()`, `orElseThrow()` or `ifPresent()`.

</details>

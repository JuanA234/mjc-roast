# Errors and Exceptions

<details>
<summary>What is an `Exception`?</summary>

An exception is an unexpected condition or event that occurs at runtime and disrupts the normal flow of the program. If left unhandled it causes the program to stop, but it can be caught with a `try-catch` block allowing the program to recover and continue. Exceptions are represented as objects in Java - every exception is an instance of a class that extends `Throwable`.

</details>

<details>
<summary>What are some advantages of exceptions</summary>

* They give you control over unexpected runtime conditions by allowing you to handle or recover from them gracefully.

* Custom exceptions let you know specifically why your application is failing

* They separate error handling logic from normal program flow keeping the code cleaner and more readable.

* They propagate automatically up the call stack, meaning that you handle the error at the level where it actually makes sense, not necessarily where it occured.

```java
void readConfig() {
    // deep in your code — file not found
    throw new FileNotFoundException("config.txt not found");
}

void loadApplication() {
    readConfig(); // doesn't handle it — passes it up automatically
}

void main() {
    try {
        loadApplication(); // exception bubbles up all the way here
    } catch (FileNotFoundException e) {
        System.out.println("Could not start — config file missing");
    }
}

// Without propagation — error handling at every step, very noisy
void readConfig() {
    return "error: file not found"; // manual error passing
}

void loadApplication() {
    String result = readConfig();
    if (result.contains("error")) {
        return result; // manually passing it up again
    }
}
```

* They allow grouping and differentiating error types so different problems can be handled differently.

</details>

<details>
<summary>What Is the Purpose of the `Throw` and `Throws` Keywords?</summary>

`throw` is used inside a method body to actually trigger an exception at a specific point. `throws` is used in the method signature to declare that the method might throw an exception, warning the caller that they must handle or propragate it.

```java
// throws — declares the method might throw this exception
void readFile(String path) throws FileNotFoundException {
    if (path == null) {
        throw new FileNotFoundException("Path cannot be null"); // throw — actually triggers it
    }
}

// caller must handle it or declare throws themselves
void loadConfig() throws FileNotFoundException {
    readFile(null); // propagates up
}

// or handle it
void loadConfig() {
    try {
        readFile(null);
    } catch (FileNotFoundException e) {
        System.out.println("File not found");
    }
}
```

</details>

<details>
<summary>Explain root level exception super classes in Java?</summary>

Throwable is the root of everything. It has two direct children - `Error` for serious JVM level problems you dont handle, and `Exception` for recoverable conditions you should handle.

`Exception` splits further into checked exceptions that the compiler enforces you two handle, and unchecked exceptions that extend `RuntimeException` where handling is optional.

Throwable
    ├── Error
    │     ├── OutOfMemoryError
    │     ├── StackOverflowError
    │     └── ...
    └── Exception
            ├── RuntimeException (unchecked)
            │     ├── NullPointerException
            │     ├── ArrayIndexOutOfBoundsException
            │     └── ...
            └── IOException (checked)
            └── SQLException (checked)
            └── ...

</details>

<details>
<summary>Why would you want to subclass an exception?</summary>

Creating a custom exception subclass lets you give a meaningful name to a specific failure condition in your domain, making it immediately clear what went wrong and where. It also allows callers to handle different failure conditions differently and precisely, and lets you add custom fields with extra context about the failure.

```java
// ❌ generic — tells you nothing about the business problem
throw new RuntimeException("error processing payment");

// ✅ custom subclass — immediately communicates what failed
throw new PaymentDeclinedException("Insufficient funds");
throw new InvalidCardException("Card expired");
```

In the example a `PaymentDeclinedException` is far more meaningful than a generic `RuntimeException`.

</details>

<details>
<summary>Which super class should you sub class to create checked exception?</summary>

For creating a checked exception you have to extend `Exception` - the compiler will enforce that callers handle or declare it.

```java
// Checked — caller must handle or declare throws
class InsufficientFundsException extends Exception {
    InsufficientFundsException(String message) { super(message); }
}
```

</details>

<details>
<summary>Which super class should you sub class to create unchecked exception?</summary>

To create an unchecked exception extend `RuntimeException` - handling these are optional.

```java
// Unchecked — handling is optional
class InvalidCardException extends RuntimeException {
    InvalidCardException(String message) { super(message); }
}
```

</details>

<details>
<summary>What are `checked exceptions`?</summary>

Are those that the compiler enforces you to handle by using a `try-catch` block, or propagate by declaring them in the method signature of the method that might produce it. They extend from `Exception`.

They represent External failures (IO, DB). Examples: `IOException`, `SQLException`. External conditions outside your control

</details>

<details>
<summary>What are `unchecked exceptions`?</summary>

Are those that handling them is optional, you could handle them with a `try-catch` block. They extend from `RuntimeException`.

They represent Programming mistakes. Examples: `NullPointerException`, `IllegalArgumentException`, `ArrayIndexOutOfBoundsException`. Conditions that shouldn't occur if the code is written correctly.

</details>

<details>
<summary>What is the difference between a checked and an unchecked exception?</summary>

The main difference is what they represent, checked exceptions represent external conditions that is already known might occur - like a file not beign found or a database beign unavailable - so the compiler enforces you to handle or propagate them. Unchecked are conditions that shouldn't occur if the code is written correctly, like `NullPointerException` or `ArrayIndexOutOfBoundsException` - and since they indicate programming mistakes rather than external conditions, the compiler doesn't enforce you to handle them.

```java
// Checked — external condition outside your control
// file might not exist regardless of how well you wrote the code
void readFile() throws IOException { ... }

// Unchecked — programming mistake
// this only happens if you wrote the code incorrectly
String name = null;
name.toUpperCase(); // ❌ NullPointerException — your fault, not an external condition
```

</details>

<details>
<summary>What is an `Error`?</summary>

An error is an unexpected condition that you should not try to handle or recover from.

</details>

<details>
<summary>What is the difference between an exception and error?</summary>

Both exceptions and errors are runtime conditions that disrupt normal program flow, but they differ in what causes them and whether you should handle them.

`Exceptions` represent conditions that can be anticipated and recovered from - either external conditions like a missing file (checked), or programming mistakes (unchecked). `Errors` represent severe JVM level failures like running out of memory (`OutOfMemoryError`) or infinite recursion exhausting the stack (`StackOverFlowError`) - conditions so serious that recovery is not possible or meaningful and you should not try to handle them.

```java
// Exception — recoverable, handle it
try {
    readFile("config.txt");
} catch (FileNotFoundException e) {
    useDefaultConfig(); // ✅ meaningful recovery
}

// Error — don't try to handle it
try {
    recursiveMethod();
} catch (StackOverflowError e) {
    // ❌ pointless — JVM is already in a broken state
}
```

</details>

<details>
<summary>What `types of exceptions` does the Error class in Java defines?</summary>

The `Error` class defines exceptions that represent JVM level failures that are unrecoverable. The most common ones are:

| Error | When it occurs |
| :--- | :--- |
| **OutOfMemoryError** | JVM runs out of heap memory |
| **StackOverflowError** | Infinite recursion exhausts the call stack |
| **VirtualMachineError** | JVM is broken or has run out of resources |
| **AssertionError** | An assert statement fails |
| **LinkageError** | Class dependency issues at loading time |

```java
// StackOverflowError — classic infinite recursion
void infinite() {
    infinite(); // ❌ calls itself forever → StackOverflowError
}

// OutOfMemoryError — allocating more than heap can hold
int[] huge = new int[Integer.MAX_VALUE]; // ❌ OutOfMemoryError
```

</details>

<details>
<summary>How can you handle an exception?</summary>

You can handle directly within a `try-catch` block or propagate it using `throws` in the method signature that might produce the exception.

```java
// Option 1 — handle directly with try-catch
void readFile() {
    try {
        // risky operation
    } catch (FileNotFoundException e) {
        useDefaultConfig(); // handle it here
    }
}

// Option 2 — propagate with throws, let the caller handle it
void readFile() throws FileNotFoundException {
    // risky operation — caller is responsible for handling
}

void loadConfig() {
    try {
        readFile(); // caller handles it here
    } catch (FileNotFoundException e) {
        useDefaultConfig();
    }
}
```

</details>

<details>
<summary>How can you catch multiple exceptions?</summary>

You can catch multiple exceptions in a single `catch` block by separating them with a pipe "`|`" operator - introduced in Java 7. Alternatively you can use multiple `catch` blocks when you need to handle each exception diferently. 

```java
// Multiple exceptions — same handling
try {
    riskyOperation();
} catch (FileNotFoundException | SQLException e) {
    System.out.println("Handled both the same way");
}

// Multiple catch blocks — different handling per exception
try {
    riskyOperation();
} catch (FileNotFoundException e) {
    useDefaultConfig();
} catch (SQLException e) {
    reconnectDatabase();
}
```

</details>

<details>
<summary>How can you handle checked exceptions?</summary>

Checked exceptions must be handled in one of two ways - the compiler enforces it. You can handle them directly with a `try-catch` block, or propagate them up the call the stack, by declaring them in the method signature with throws, delegating the responsability to the caller.

```java
// Option 1 — handle directly
void readFile() {
    try {
        new FileReader("config.txt"); // FileNotFoundException is checked
    } catch (FileNotFoundException e) {
        useDefaultConfig(); // ✅ handled here
    }
}

// Option 2 — propagate to caller
void readFile() throws FileNotFoundException { // ✅ caller must handle it
    new FileReader("config.txt");
}
```

</details>

<details>
<summary>What happens if an exception is un-handled?</summary>

The application stops its workflow.

If an exception is unhandled it propagates up the call stack through every method until it reaches the JVM. The JVM then terminates the program and prints the stack trace to the console - showing the exception type, message an the exact chain of method calls that led to the failure.

```java
void methodC() { throw new RuntimeException("something went wrong"); }
void methodB() { methodC(); } // unhandled — propagates up
void methodA() { methodB(); } // unhandled — propagates up
void main()    { methodA(); } // unhandled — reaches JVM

// JVM prints stack trace and terminates:
// Exception in thread "main" java.lang.RuntimeException: something went wrong
//     at methodC(Main.java:2)
//     at methodB(Main.java:5)
//     at methodA(Main.java:8)
//     at main(Main.java:11)
```

</details>

<details>
<summary>Call stack:</summary>

Think of the call stack as a stack of plates - each time a method is called a new plate is added on top, and when the method returns the plate is removed:

main() calls methodA()
methodA() calls methodB()
methodB() calls methodC()
methodC() throws exception

Call stack at the moment of the exception:

┌─────────────┐  ← top (where exception originated)
│  methodC()  │  throws RuntimeException
├─────────────┤
│  methodB()  │  called methodC()
├─────────────┤
│  methodA()  │  called methodB()
├─────────────┤
│   main()    │  called methodA()
└─────────────┘  ← bottom

**What happens when the exception is thrown:**

The exception starts at the top and travels downward through the stack looking for a `try-catch` block at each level:

```java
void methodC() { 
    throw new RuntimeException("error"); // 1. exception born here
}

void methodB() { 
    methodC(); // 2. no catch here — passes down
}

void methodA() { 
    try {
        methodB(); // 3. caught here — stack unwinds to this point
    } catch (RuntimeException e) {
        System.out.println("Caught at methodA"); // ✅ handled
    }
}
```

**If nobody catches it - reaches the JVM:**

methodC()  → no catch → passes down
methodB()  → no catch → passes down
methodA()  → no catch → passes down
main()     → no catch → passes down
JVM        → prints stack trace → terminates program

The stack trace you see in the console is literraly a printout of the stack - read from top to bottom it shows you exactly where the exception was thrown and every method it passed through on the way down:

Exception in thread "main" java.lang.RuntimeException: error
    at methodC(Main.java:2)   ← where it was thrown
    at methodB(Main.java:5)   ← passed through here
    at methodA(Main.java:8)   ← passed through here
    at main(Main.java:11)     ← reached here unhandled

</details>

<details>
<summary>Which exception classes can you use in the catch block to handle both checked and unchecked exceptions?</summary>

`Exception` can be used to handle both checked and unchecked exceptions since `RuntimeException` extends `Exception` - catching `Exception` covers the entire hierarchy below it. `Throwable` goes even broder and catches everything including `Error`, but that's generally considered a bad practice since you'd be catching JVM level failures you shouldn't handle.

Throwable        → catches everything
    Exception    → catches checked + unchecked
        RuntimeException → catches unchecked only

</details>

<details>
<summary>How do you make choice between checked and unchecked exceptions?</summary>

Use **checked exceptions** for external conditions outside your control that are know to potentially fail - like reading a file, connecting to a database, or making a network call. The compiler enforcing handling makes sense here because these failures can happen regardless of how well the code is written.

Use **unchecked exceptions** for programming mistakes and invalid states that indicate a bug in the code - like passing a null argument, going out of bounds, or violating the contract. Forcing the caller to handle these everywhere would be noisy and pointless since they shouldn't occur in correctly written code.

```java
// ✅ checked — external condition, caller should handle it
void connectToDatabase() throws SQLException {
    // network might be down, DB might be unavailable
}

// ✅ unchecked — programming mistake, indicates a bug
void processPayment(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive"); // bug in calling code
    }
}
```
Thumb rule:

If the caller can reasonably be expected to recover from it → checked.
If it indicates a bug that should be fixed instead of handled → unchecked.

</details>

<details>
<summary>Reasoning behind using unchecked exceptions in Spring Boot is:</summary>

In web applications the caller - the framework itself - is responsible for handling exceptions through a global handler. Forcing checked exceptions through every layer of your application just to reach that handler adds noise without adding safety. Unchecked exceptions let the exception propagate naturally up to the global handler without polluting every method signature.

```java
// ❌ checked — forces throws declarations everywhere, very noisy
class BookService {
    Book findBook(Long id) throws BookNotFoundException { ... }
}

class BookController {
    Book getBook(Long id) throws BookNotFoundException { // forced to declare it
        return bookService.findBook(id);
    }
}

// ✅ unchecked — clean, global handler takes care of it
class BookService {
    Book findBook(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new BookNotFoundException(id)); // clean
    }
}

// Controller advice:

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    ResponseEntity<String> handleNotFound(BookNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage()); // → HTTP 404
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage()); // → HTTP 400
    }
}
```

</details>

<details>
<summary>Can you recover from unchecked exception?</summary>

Yes, unchecked exceptions can be caught and recovered from just like checked exceptions - the only difference is the compiler doesn't force you to. The ones you truly cannot recover from are `Error` subclasses like `OutOfMemoryError` or `StackOverFlowError`.

```java
// ✅ recovering from an unchecked exception
try {
    int result = Integer.parseInt("not a number"); // NumberFormatException — unchecked
} catch (NumberFormatException e) {
    int result = 0; // ✅ recovered with a default value
}

// ✅ your own Spring Boot example
try {
    Book book = bookService.findBook(id); // throws unchecked BookNotFoundException
} catch (BookNotFoundException e) {
    return ResponseEntity.status(404).body("Book not found"); // ✅ recovered gracefully
}
```

</details>

<details>
<summary>How can you avoid unchecked exception?</summary>

Unchecked exceptions represent programming mistakes, so the best way to avoid them is through defensive programming - validating inputs and checking for null before operating on them.

```java
// NullPointerException — check before using
if (name != null) {
    name.toUpperCase(); // ✅ safe
}

// Or use Optional (modern Java)
Optional.ofNullable(name)
        .map(String::toUpperCase)
        .orElse("default"); // ✅ no NullPointerException possible

// ArrayIndexOutOfBoundsException — check bounds before accessing
if (index >= 0 && index < array.length) {
    array[index]; // ✅ safe
}

// IllegalArgumentException — validate inputs at method entry
void processPayment(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
    }
    // proceed safely
}

// NumberFormatException — validate before parsing
if (input.matches("\\d+")) {
    int number = Integer.parseInt(input); // ✅ safe
}
```

</details>

<details>
<summary>Can you throw checked exceptions from static block? Why?</summary>

Checked exceptions can't be thrown from static blocks because there is no caller to declare `throws` to and no method signature to attach it to - they must be handled internally with `try-catch`. Unchecked exceptions can be thrown but wrap into `ExceptionInitializerError` at runtime.

</details>

<details>
<summary>What should you do to handle an Error?</summary>

In general you should not try to handle `Error` - they represent JVM level failures so severe that the application is already in an unrecoverable state. Catching them is pointless at best and dangerous at worst, since the JVM may not be able to execute any recovery logic reliably anyway.

```java
// ❌ pointless and dangerous — don't do this
try {
    riskyOperation();
} catch (OutOfMemoryError e) {
    // JVM is out of memory — you probably can't even create the log message
    System.out.println("Out of memory"); // might fail too
}
```

They only acceptable exception is catching it at the very top to log it before shoutdown, and even then you should rethrow it rather than swallowing it.

```java
// ✅ only acceptable use — top level logging before shutdown
try {
    application.run();
} catch (Error e) {
    logger.fatal("JVM level failure — shutting down", e); // log and let it crash
    throw e; // rethrow — don't swallow it
}
```

</details>

<details>
<summary>Describe some exception handling best practices?</summary>

* Always handle or propagate checked exceptions - never ignore them
* Never swallow exceptions silently - always log or handle meaningfully
* Don't catch `Error` - let the JVM crash, errors are unrecoverable
* Use specifig exception types over generic ones - catch `FileFoundException` not just `Exception`
* Create custom exceptions for domain specific failures with meaningful names and messages
* Don't use exceptions for flow control - they are expensive and make code hard to read
* Always clean up resources in finally or use try-with-resources

```java
// ❌ swallowing silently — worst practice
try {
    readFile();
} catch (IOException e) {
    // empty — exception completely ignored
}

// ❌ too generic
catch (Exception e) { ... }

// ✅ specific
catch (FileNotFoundException e) { ... }

// ✅ try-with-resources — auto closes resources
try (FileReader fr = new FileReader("file.txt")) {
    // file automatically closed even if exception occurs
}

// work flow

// ❌ using exceptions for flow control — don't do this
public boolean isNumber(String value) {
    try {
        Integer.parseInt(value);
        return true;
    } catch (NumberFormatException e) {
        return false; // using the exception as an if/else
    }
}

// ✅ correct way — use proper conditional logic
public boolean isNumber(String value) {
    return value.matches("-?\\d+");
}

// ❌ using exception to check if list is empty
try {
    String first = list.get(0);
} catch (IndexOutOfBoundsException e) {
    System.out.println("List is empty");
}

// ✅ correct way
if (!list.isEmpty()) {
    String first = list.get(0);
}
```

</details>

<details>
<summary>What are the pitfalls of suppressing an exception?</summary>

```java
try {
    readFile("config.txt");
} catch (IOException e) {
    // empty — exception completely swallowed
}
```

Suppresing exceptions forces the program to keep running in a broken state, which leads to three specific pitfalls: data corruption - operations failed are treated as succesful leading to wrong results; impossible debugging - with no log or message there is no trace of what went wrong or where; and a false sense of stability - the program appears to work normally while silently producing icorrect behavior. Always at minimum log the exception so there is a record of what failed.

```java
// ✅ minimum acceptable handling — at least log it
try {
    readFile("config.txt");
} catch (IOException e) {
    logger.error("Failed to read config", e); // at least you know it happened
}
```

</details>

<details>
<summary>What is the problem with showing a generic error message?</summary>

Showing generic error messages has two problems depending on the audience:

* Developer Perspective - generic messages make debugging harder because you lose the specific context of what failed, where and why.

* User and security perspective - however, showing overly specific error messages to end users can be dangerous, as they may expose sensitive internal details like stack traces, database structure or file paths that attackers could exploit.

```java
// ❌ too generic for developers — useless for debugging
logger.error("Something went wrong");

// ✅ specific for developers — logged internally
logger.error("Failed to connect to database at {}:{}", host, port, e);

// ❌ too specific for users — exposes internals
response.send("SQLException: Table 'users' doesn't exist at db.internal:5432");

// ✅ appropriate for users — friendly and safe
response.send("We're experiencing technical difficulties. Please try again later.");
```

Summarizing:

Generic messages hurt developers by hiding what failed — but overly specific messages hurt security by exposing internals to users. The best practice is to log specific details internally and show friendly generic messages externally.

</details>

<details>
<summary>What are the pitfalls of handling multiple exceptions in a single catch block?</summary>

Handling multiple exceptions in a single broad catch block hurts readability, prevents you from responding differently to different failures, and risks accidentally swallowing unintended exceptions like programming mistakes. Use specific blocks for each failure type, or multi-catch `|` syntax only when two exceptions genuinely need identical handling.

```java
// ❌ catching everything in one block
try {
    readFile("config.txt");
    parseData();
    saveToDatabase();
} catch (Exception e) {
    System.out.println("Something went wrong"); // which operation failed? no idea
}

// ❌ all failures get the same response — makes no sense
} catch (Exception e) {
    showUserError("File not found"); // what if it was actually a database error?
}

// ✅ each failure handled appropriately
} catch (FileNotFoundException e) {
    showUserError("Config file missing");
} catch (ParseException e) {
    showUserError("Config file is corrupted");
} catch (SQLException e) {
    showUserError("Could not save data");
}

// ❌ catches RuntimeExceptions and programming mistakes too
} catch (Exception e) {
    // NullPointerException would be silently swallowed here
    // masking a bug in your code
}

// ✅ acceptable when two exceptions genuinely need identical handling
} catch (FileNotFoundException | SQLException e) {
    logger.error("Resource not available", e);
}
```

</details>

<details>
<summary>What would you do if an exception is thrown from a source that contains sensitive information?
What would you log in such case and what message would you show to the user?</summary>

In that case i would log something meaningful enough for developers to understand easily what failed, without beign too revealing. To the end user i would show a generic friendly message that communicates something went wrong without revealing any internal details that attackers could exploit.

```java
try {
    User user = database.findUser(creditCardNumber);
} catch (SQLException e) {
    // ✅ log specific details internally — developers can debug
    logger.error("Database query failed for transaction ID: {} — {}", 
                  transactionId, e.getMessage());

    // ✅ show generic message to user — no sensitive details exposed
    throw new UserFriendlyException("We could not process your request. Please try again later.");
}
```

</details>

<details>
<summary>What is the pitfall of wrapping all the exceptions into a Generic exception class?</summary>

If something fails, and you are expecting a Generic exception class like `Exception` you are unable to know what specifically failed, making debugging harde; and you risk accidentally catching unintended exceptions like `NullPointerException` or other programming mistakes - masking real bugs in your code that get silently swallowed and misreported as a different kind of failure. Always catch the most specific exception type possible.

```java
// ❌ wrapping everything into generic Exception
try {
    processPayment(order);
} catch (Exception e) {
    logger.error("Payment failed"); // looks like a payment issue
}

// but what if the real cause was a NullPointerException — a bug in your code?
// it gets swallowed and logged as a payment failure
// you spend hours debugging the payment system when the bug is elsewhere
```

</details>

<details>
<summary>Why you shouldn’t use exceptions to control the flow of program execution?</summary>

Because they are expensive, the JVM needs to captures the entire stack trace even if you catch it and ignore it, and it hurts readability - exceptions signal unexpected conditions, so using them for predictable branching logic misleads anyone reading the code into thinking something unexpected is happening when it's just a normal condition flow.

```java
// ❌ expensive and misleading — exception used as if/else
try {
    Integer.parseInt(value);
    return true;
} catch (NumberFormatException e) {
    return false;
}

// ✅ cheap and clear — proper conditional logic
return value.matches("-?\\d+");
```

</details>

<details>
<summary>Should you log all the exceptions? Why?</summary>

Not necessarily - logging every exception can create excessive noise that makes logs hard to read and buries truly important errors. The decision should be based on the severity and context of the exception.

```java
// ❌ too verbose — expected user behavior logged as error
try {
    authenticateUser(username, password);
} catch (AuthenticationException e) {
    logger.error("Authentication failed"); // happens thousands of times a day
}

// ✅ log at appropriate level based on severity
try {
    authenticateUser(username, password);
} catch (AuthenticationException e) {
    logger.debug("Failed login attempt for user: {}", username); // expected — debug level
} catch (DatabaseException e) {
    logger.error("Database unavailable during authentication", e); // unexpected — error level
} catch (Exception e) {
    logger.error("Unexpected error during authentication", e); // always log unexpected
}
```

Log exceptions based on severity - always log unexpected and unrecoverable failures at error level, but avoid login expected conditions like failed logins at error level since it create noise that buries real problems. The goal is logs that are meaninful and actionable, not exhaustive.

</details>

<details>
<summary>Why you shouldn’t use Throwable or some other root level class to catch exceptions?</summary>

You shouldn't catch `Throwable` or other root level classes like `Exception` for two reasons, you lose specifity - catching everything in one broad net means you can't respond differently to different failures and risk accidentally swallowing programming mistakes like `NullPointerException`; second, and specific to `Throwable`, you would also be catching Error - which represents uncoverable JVM level failures like `OutOfMemoryError` and `StackOverflowError` that should never try to handle.

```java
// ❌ catches everything including Errors — never do this
try {
    riskyOperation();
} catch (Throwable t) {
    // swallows OutOfMemoryError, StackOverflowError
    // and every programming mistake
}

// ❌ still too broad — swallows programming mistakes
try {
    riskyOperation();
} catch (Exception e) { ... }

// ✅ catch the most specific exception possible
try {
    riskyOperation();
} catch (FileNotFoundException e) { ... }
  catch (SQLException e) { ... }
```

</details>

<details>
<summary>What should be the criteria to select the code block that should be enclosed into a try block?</summary>

The code enclosed in a `try` block should be the minimun block that can produce the exception - not more, not less. Wrapping too much code makes it hard to identify which specific operation failed. Wrapping too little risks missing code that could throw the same exception.

```java
// ❌ too broad — which line failed? impossible to tell
try {
    readConfig();
    connectToDatabase();
    processPayment();
    sendConfirmationEmail();
} catch (IOException e) {
    logger.error("Something failed"); // no idea which operation
}

// ✅ specific — each risky operation isolated
try {
    readConfig();
} catch (IOException e) {
    logger.error("Failed to read config", e);
}

try {
    connectToDatabase();
} catch (SQLException e) {
    logger.error("Failed to connect to database", e);
}
```

Any resource htat needs to be closed should be inside the `try` block - preferable using try-with-resources so cleanup is guaranteed even if an exception occurs.

```java
// ✅ multiple resources — both closed automatically in reverse order
try (FileReader fr = new FileReader("input.txt");
     FileWriter fw = new FileWriter("output.txt")) {
    // use both fr and fw
} catch (IOException e) {
    logger.error("Failed to process files", e);
}
```

</details>

<details>
<summary>If a nested call is made, which passes through multiple methods, would you implement try-catch in each method? Why?</summary>

No, because you either handle the exception where it occurs with an `try-catch` block or you propagate it up the call stack with `throws` so its the caller that needs to handle it. Adding `try-catch` in every method creates unnecessary boilerplate and defeats the purporse of exception propagation.

```java
// ❌ try-catch in every method — redundant and noisy
void methodC() {
    try {
        readFile();
    } catch (IOException e) {
        throw new RuntimeException(e); // just rethrowing anyway — pointless
    }
}

void methodB() {
    try {
        methodC();
    } catch (RuntimeException e) {
        throw e; // still just rethrowing — pointless
    }
}

// ✅ propagate naturally — handle at the level where it makes sense
void methodC() throws IOException {
    readFile(); // let it propagate
}

void methodB() throws IOException {
    methodC(); // let it propagate
}

void methodA() {
    try {
        methodB(); // handle it here where you can do something meaningful
    } catch (IOException e) {
        logger.error("Failed to read file", e);
        showUserError("Could not load data");
    }
}
```

</details>

<details>
<summary>Which java construct can you use to close the system resources automatically?</summary>

The try-with-resources closes the resources automatically, after the `try` block is executed. It closes them in reverse order they were created.

The resource must implement the `AutoClosable` interface - that's what guarantees it has a `close()` method that try-with-resources ca n call automatically.

```java
// ✅ works — FileReader implements AutoCloseable
try (FileReader fr = new FileReader("file.txt")) {
    // fr.close() called automatically
}

// ❌ won't work — MyClass doesn't implement AutoCloseable
try (MyClass obj = new MyClass()) { // compile error
    ...
}

// ✅ fix — implement AutoCloseable
class MyClass implements AutoCloseable {
    @Override
    public void close() {
        // cleanup logic
    }
}
```

</details>

<details>
<summary>What is `exception chaining`?</summary>

Exception chaining is when an exception is caught and and wrapped inside another exception before beign rethrown - preserving the original exception as the cause. This allows you to add context at each layer of the application without losing the original root cause.

```java
void readConfig() throws IOException {
    throw new IOException("File not found");
}

void loadApplication() throws ApplicationException {
    try {
        readConfig();
    } catch (IOException e) {
        // ✅ wrapping original exception — chain preserved
        throw new ApplicationException("Failed to load application", e);
    }
}

void main() {
    try {
        loadApplication();
    } catch (ApplicationException e) {
        // you can see both exceptions in the stack trace
        e.printStackTrace();
        // ApplicationException: Failed to load application
        //     Caused by: IOException: File not found  ← original cause preserved
    }
}
```

</details>

<details>
<summary>What is a `stacktrace` and how does it relate to an exception?</summary>

The stacktrace is a snapshot of the stack call at the moment an exception arises, it helps to follow the execution flow that lead to the exception.

```java
void methodC() { throw new NullPointerException("value is null"); }
void methodB() { methodC(); }
void methodA() { methodB(); }

// Stack trace output — read from top to bottom
// Exception in thread "main" java.lang.NullPointerException: value is null
//     at methodC(Main.java:1)   ← where it was thrown
//     at methodB(Main.java:2)   ← called methodC
//     at methodA(Main.java:3)   ← called methodB
//     at main(Main.java:4)      ← called methodA
```

The stack trace is read from top to bottom — the top line is where the exception was thrown, and each line below shows the chain of method calls that led to it

</details>
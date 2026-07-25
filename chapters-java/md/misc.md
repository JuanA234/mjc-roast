<details>
<summary>What is a framework?</summary>



</details>

<details>
<summary>Why to write against interfaces?</summary>

Writing against interfaces hides the implementation detail behind a contract, making your code flexible, testable and easy to maintain - changing the underlying data structure becomes a one line decision instead a system-wide refactor.

</details>

<details>
<summary>What is the difference between a side effect and a result?</summary>

Result - the operation returns a value you can use.
Side effect - the operation does something but returns nothing useful.

```java
// Terminal operations that return a RESULT
long count    = stream.count();                    // returns a number
List<String> list = stream.collect(toList());      // returns a collection
Optional<String> first = stream.findFirst();       // returns an element
boolean match = stream.anyMatch(s -> s.equals("Carlos")); // returns boolean

// Terminal operations that produce a SIDE EFFECT
stream.forEach(System.out::println); // returns void — just prints
```

You already know pure functions have no side effects — `forEach` is the classic impure stream operation because it does something (prints, saves, sends) without returning anything. `collect`, `count`, `findFirst` are closer to pure — they return a value without modifying external state.

</details>

<details>
<summary>What are bean scopes?</summary>

In spring defines the lifecycle and visibility of a bean (managed by teh spring container) - basically, it controls how many instances of a bean get created and how long they live.

The most common:

Singleton (default) - Only one instane of the bean is created for the entire Spring container/application context. Every time you inject or request that bean, you get the same shared instance.

Prototype - A new instance is created every time the bean is requested/injected. No sharing.

Request (web apps only) - a new instance is created for each HTTP request. Lives only for the duration of that request.

Session (web apps only) - a new instance is created for each HTTP session, and lives as long that session does.

Application (web apps only) - One instace per ServletContext - similar to singleton, but scoped to the whole web application rather tan the Spring container.

</details>

<details>
<summary>What is inversion of control and dependency injection?</summary>

Inversion of Control(IoC) is a design principle where the control of creating and managing objects is taken away from your code and handed to a framework.

Dependency injection (DI) is the most common way IoC is actually implemented. Instead of a class creating its dependencies itself, the dependencies are "injecte" into it form outside - via constructor, setter or field injection.

Simple way to connect the two:
"IoC is the principle — 'don't create your own dependencies.' DI is the technique — 'here, I'll hand you your dependencies instead.'"

The three ways DI is usually done (good to name):
* Constructor injection - dependencies passed through contructor (most recommended - makes dependencies explicit and allows immutability with final fields)

* Setter Injection - depedencies are set via setter methods after object creation, util for optional dependencies

* Field injection - dependencies injected directly into fields - convenient but generally discouraged since it hides dependencies and makes testing harder.

</details>

<details>
<summary>What is CI/CD?</summary>

Are a set of practices for automating how codes goes from written to being released.

Continuous Integration (CI) — Developers frequently merge their code changes into a shared repository (often multiple times a day). Every time code is pushed, an automated pipeline runs to build the project and run tests automatically. This catches integration problems and bugs early, instead of finding out weeks later that everyone's changes conflict or break something.

Continuous Delivery (CD) — Extends CI by automatically preparing the code for release after it passes tests — packaging it, running additional checks — so it's always in a deployable state. Actually deploying to production still requires a manual trigger/approval.

Continuous Deployment (CD) — Goes one step further: if the code passes all automated tests and checks, it's automatically deployed to production with no manual step at all.

CI/CD automates the path from code to production - CI catches problems early by building and testing on every commit, and CD keeps it ready to release, either with manual approval or fully automatically.

</details>

<details>
<summary>What is DTO and DAO?</summary>



</details>
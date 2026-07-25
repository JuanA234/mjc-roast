# TESTING

## Fundamentals

<details>
<summary>What is unit testing, and how does it differ from integration testing?</summary>

**Unit testing:** Means testing a single unit of code, usually one method or one class in complete isolation from everything else it depends on (database, other classes). Any dependencies are replaced with mock or stubs, so you are only verifying that this piece of logic behaves correctly.

**Integration testing:** Means testing how multiple units work together, for example if a service class correctly talk to the real dabase, or if your controller correctly call the service layer. Checking that the pieces integrat correctly

</details>

<details>
<summary>What is an end-to-end test?</summary>

Means testing the entire application flow from start to finish, exactly the way a real user would experiencie it - through the actual UI or API, hitting the real databse, real services, everything wired together as it would be in productin (or a production-like environment).

</details>

<details>
<summary>What makes a good unit test? (think: independence, speed, repeatability)</summary>

A good unit test generally has these qualities - often remembered by the acronym F.I.R.S.T:

**Fast** - Should run in milliseconds. If your test suite takes forever, developers stop running it often, which defeats the purpose.

**Isolated** - A test shouldn't depend on another test running first, and shouldn't depend on external systems. Each test should be able to run alone, in any order.

**Repeatable** - Running 100 times should give the same result everytime, regardless of the environment (your machine, CI server, whatever). No reliance on things like current date/time, random values, or network state unless you control them.

**Self-validating** - The test itself should clearly say pass or fail (via assertions) - no manually reading logs or output to decide if it worked.

**Timely** - Ideally written close to when you write the actual code (or before it, in TDD) - not weeks later as an afterthought.

additionally:

* Test one thing - aunit test should only verify one specific behavior/scenario. If it fails you should immediately know what broke, without digging.

* Readable - test names and structure should clearly describe whats being test and what's expected

* No side effects - a test shouldn't leave behind state that affect other tests.

</details>

<details>
<summary>What is the AAA pattern (Arrange-Act-Assert)?</summary>

The AAA pattern is a simple way to structure a unit test into three clear sections, makng tests easier to read and maintain:

**Arrange** - Set up everything the test needs: create objects, define input values, configure mocks, etc.

**Act** - Execute the actual method or behavior you're testing. Usually just one line/action.

**Assert** - Verify the result is what you expect, using assertions.

Why it matters: It keeps tests consistent and readable - anyone looking at the test can immediately tell what's beign set up, what's being test, and what the expected outcome is, without having to trace through tangled logic.

</details>

<details>
<summary>What is Test-Driven Development (TDD)? Can you describe the red-green-refactor cycle?</summary>

Test-Driven Developtment is a development approach where you write the test before you write the actual implementation code. Instead of coing first and testing after, you let the test define what the code should do, then write just enough code to satisfy it.

The cycle is called Red-Green-Refactor:

🔴 Red - Write a test for a feature/behavior that doesn't exist yet. Run it - it fails (red). because there's no implementation yet. This confirms the test is actually testing something.

🟢 Green - Write the minimunm amount of code needed to make that test pass. Not the most elegant solution - just enough to turn the test green. Run the test - it passes.

🔵 Refactor - Now that you have a passing test as a safety net, clean up the code: remove duplication, improve naming, restructure - without changing behavior. Re-run the test after each change to make sure it still pass.

Then you repeat the cycle for the next small piece of functionality.

</details>

<details>
<summary>What is code coverage, and why isn't 100% coverage the same as "well tested"?</summary>

Code coverage is a metric that tells you what percentage of your code was executed while running your test suite. It's usually measured in different ways:

* Line coverage - % of lines of code executed
* Branch coverage - % of braches (if/else, switch cases) executed
* Method coverage - % of methods called at least once

**Why 100% coverage ≠ "well tested":**

Coverage only tells you that a line ran during a test - it says nothing about whether you actally asserted the correct behavior happened. You can have a test that calls a method and checks nothing meaningful, and it'll still count as "covered"

</details>

<details>
<summary>What is a test pyramid, and why is it shaped that way?</summary>

Basically, the pyramid is about tradeoffs. Unit tests are cheap and fast, so you write a lot of them. But as you move up to integration and end-to-end tests, they get slower, and brittle, and more expensive to maintain - so you only want a few of those, just enough to cover the most critical flows.

The idea is to test as much as you can, cheaply and fast, and only do more than that when needed

</details>

## JUnit

- What is JUnit, and what version does the Spring ecosystem currently use by default?
- What's the purpose of `@Test`, `@BeforeEach`, `@AfterEach`, `@BeforeAll`, and `@AfterAll`?
- What's the difference between `@BeforeEach`/`@AfterEach` and `@BeforeAll`/`@AfterAll`?
- How do you write a parameterized test in JUnit 5? What's `@ParameterizedTest` for?
- What does `@Disabled` do, and when would you use it?
- What's the difference between `assertEquals`, `assertTrue`, and `assertThrows`?
- How do you test that a method throws a specific exception?
- What is `@DisplayName` used for?

## Mocking

- What is a mock, and why do we use mocks in unit tests?

<details>
<summary>What's the difference between a mock, a stub, and a spy?</summary>

These three are "test doubles" - fake objects that stand in for real dependencies during testing.

Stub - A fake object that returns predifined ansers when called. It doesn't care how it's sued or how many times - it just gives fixed responses. Used purely to feed input into the thing you are testing.

Mock - A fake object where you also verify behavior/interactions - that certain methods were actually called, how many times, with what arguments, etc.

Spy - Wraps a real object, letting the real methods actually execute, but also lets you track calls made to it.

</details>

- What is Mockito, and how do you create a mock with it?
- What's the difference between `@Mock`, `@Spy`, and `@InjectMocks`?
- What does `when(...).thenReturn(...)` do?
- What's the difference between `mock()` and `@Mock` (Mockito annotation vs. manual creation)?
- How do you verify that a mocked method was called, and how many times?
- What is `ArgumentCaptor` used for?
- Why shouldn't you mock the class you're actually testing?

## Assertions & Libraries

- Have you used AssertJ? What advantage does it offer over plain JUnit assertions?
- What's the difference between `assertEquals(expected, actual)` and `assertEquals(actual, expected)` — does order matter?

## Spring Boot Testing

- What does `@SpringBootTest` do, and when would you use it vs. a plain unit test?
- What is `@WebMvcTest`, and how does it differ from `@SpringBootTest`?
- What is `@DataJpaTest` used for?
- What's the purpose of `MockMvc`?
- What's the difference between `@MockBean` and `@Mock`?
- How do you test a REST controller without starting a full embedded server?
- What is `TestRestTemplate` or `WebTestClient` used for?

## Test Types & Strategy

- What is an integration test, and how does it differ from a unit test in scope and setup?
- What is a "test double," and what are the different kinds (dummy, fake, stub, spy, mock)?
- What is regression testing?
- What's the difference between black-box and white-box testing?
- Why is it considered bad practice for unit tests to depend on a real database or external API?
- What is Testcontainers, and what problem does it solve?

## General / Behavioral

- How do you decide what to test and what not to test?
- What makes tests "flaky," and how would you deal with a flaky test?
- Have you worked with any CI pipeline that runs tests automatically? What happens if a test fails?

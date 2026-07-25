# COLLECTIONS

<details>
<summary>Explain collections?</summary>

A collection in Java is an object that groups multiple elements into a single unit. The Java collection Framework (JCF) is a unified architecture introduced in Java 2 that provides a set of interfaces, implementations and algorithms to store, retrieve, and manipulate data as a group

| Interface | Common Implementations | Characteristic |
| :--- | :--- | :--- |
| **List** | ArrayList, LinkedList | • Ordered<br>• Index-based<br>• Allows duplicates |
| **Set** | HashSet, TreeSet, LinkedHashSet | • No duplicates |
| **Queue** | LinkedList, PriorityQueue | • FIFO (First-In-First-Out) processing |
| **Map** | HashMap, TreeMap, LinkedHashMap | • Key-value pairs<br>• No duplicate keys |

Utility methods in Collections class (sort(), shuffle(), binarySearch())

</details>

<details>
<summary>How collections and Generics related?</summary>

Collections provide the data structures, Generics provide the type safety by introducing type parameters, making collections the primary beneficiary and most common use case of Generics in Java

</details>

<details>
<summary>Can you use collections with the primitive types? How can you use collection with the primitive types?</summary>

Collections are meant to be used with Objects, but you can use them with primitive types through heir corresponding wrapper classes, like Integer, Float, Character, Boolean, etc. Java handles the conversion automatically through autoboxing and unboxing. This convenience comes with a minor performance cost since each primitive gets wrapped in an Object.

```java
List<Integer> numbers = new ArrayList<>();

numbers.add(42);        // autoboxing   — int → Integer automatically
int n = numbers.get(0); // unboxing     — Integer → int automatically
```

</details>

<details>
<summary>Explain difference between collections and arrays?</summary>

Both arrays and collections are used to store groups of elements, but they differ significantly in flexibilty, type safety, and built-in fuctionality.

Arrays are faster and more memory efficient but rigid - fixed size, covariant, and no built-in algorithms. Collections are more flexible and feature rich but carry extra overhead. In modern Java you default to collections unless performance or interoperability with low-level APIs requires an array. 

</details>

<details>
<summary>What is the benefit of collection framework?</summary>

The framework provides a feature-rich architecture with ready-to-use data structures, that are type-safe through Generics, built-in algorithms like sort, search, and shuffle, and a consistent interchangeable API.

</details>

<details>
<summary>On collections being an API:</summary>

In this context API doesn't mean HTTP endpoints - it means a defined set of interfaces and methods that you call without knowing or caring about the implementation behind them.

The collections framework is an API in the sense that it exposes a contract - a set of interfaces and methods with defined behavior - and hides the implementation details. You just call `add()`, `get()`, `sort()` and the framework handles the rest.

Think of it as any tool you use without knowing its internals - that's an API.

</details>

<details>
<summary>What are the different components of collection framework?</summary>

The Java Collections framework is built from four main components, each playing a distinc role in the architecture:

1. **Interfaces - the contracts:** Define what a collection can do without specifying how. They allow collections to be used and swapped independently of their implementation.

Iterable
    └── Collection
            ├── List
            ├── Set
            └── Queue
Map                     // separate hierarchy

2. **Implementations - the concrete classes:** The actual data structures you instantiate. Each interface has multiple implementations with different characteristics.

| Interface | Implementations |
| :--- | :--- |
| **List** | ArrayList, LinkedList, Vector |
| **Set** | HashSet, TreeSet, LinkedHashSet |
| **Queue** | LinkedList, PriorityQueue, ArrayDeque |
| **Map** | HashMap, TreeMap, LinkedHashMap |

3. **Algorithms - the utility methods:** Static methods in the Collections utility class that operate on any collection through their interfaces.

```java
Collections.sort(list);
Collections.shuffle(list);
Collections.binarySearch(list, "Carlos");
Collections.reverse(list);
Collections.min(list);
Collections.max(list);
```

4. **Iterators - the traversal mechanism:** A standart way to traverse any collection without knowing its internal structure.

```java
List<String> names = new ArrayList<>();
Iterator<String> it = names.iterator();

while (it.hasNext()) {
    System.out.println(it.next());
}

// Or the cleaner modern equivalent — enhanced for loop
for (String name : names) {
    System.out.println(name);
}
```

**In summary:**
The four components are `Interfaces` (what a collection do), `Implementations` (how they do it), `Algorithms` (utilities that operate on them), and `Iterators` (how you traverse them) - together they form a complete and consistent architecture for working with groups of data.

</details>

<details>
<summary>Why should you write code against the collection interface and not concrete implementation?</summary>

Because it decouples the code from a specific implementation, giving you the freedom to swap it out without changing anything else. This is a fundamental principle in Java known as "Programming to an interface"

**Example:**
```java
// Start with ArrayList for fast random access
List<String> logs = new ArrayList<>();

// Later requirements change — lots of insertions at the front
// Just swap the implementation, nothing else changes
List<String> logs = new LinkedList<>();
```

</details>

<details>
<summary>What are the different algorithms supported by the collection framework?</summary>

The Collections Framework provides a rich set of ready-made algorithms through the `Collections` utility class - sorting, searching, shuffling, reversing, min/max, frequency, copying and filling - all operating through collection interfaces so they work consistently across any implementation.

1. **Sorting:**
```java
List<Integer> numbers = new ArrayList<>(List.of(3, 1, 4, 1, 5));

Collections.sort(numbers);                              // ascending
Collections.sort(numbers, Comparator.reverseOrder());   // descending
```

2. **Searching:**
```java
List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));

// List must be sorted first
int index = Collections.binarySearch(numbers, 3); // returns index 2
```

3. **Shuffling:**
```java
List<String> cards = new ArrayList<>(List.of("A", "K", "Q", "J"));

Collections.shuffle(cards);        // random order
Collections.shuffle(cards, new Random(42)); // seeded random — reproducible
```

4. **Reversing:**
```java
List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));

Collections.reverse(numbers); // [5, 4, 3, 2, 1]
```

5. **Min and Max:**
```java
List<Integer> numbers = new ArrayList<>(List.of(3, 1, 4, 1, 5));

Collections.min(numbers); // 1
Collections.max(numbers); // 5
```

6. **Frequency:**
```java
List<String> names = new ArrayList<>(List.of("Carlos", "Maria", "Carlos"));

Collections.frequency(names, "Carlos"); // 2
```

7. **Copying:**
```java
List<String> source      = new ArrayList<>(List.of("a", "b", "c"));
List<String> destination = new ArrayList<>(List.of("x", "x", "x")); // must be same size

Collections.copy(destination, source); // destination = ["a", "b", "c"]
```

8. **Filling:**
```java
List<String> names = new ArrayList<>(List.of("a", "b", "c"));

Collections.fill(names, "default"); // ["default", "default", "default"]
```

9. **Unmodifiable and synchronized:**
```java
// Unmodifiable — read only wrapper
List<String> locked = Collections.unmodifiableList(names);
locked.add("x"); // ❌ UnsupportedOperationException at runtime

// Synchronized — thread safe wrapper
List<String> safe = Collections.synchronizedList(names);
```

</details>

<details>
<summary>Which search algorithm is used to search keys?</summary>

The search algorithm depends on the collection — Lists use binary search, HashMaps use hashing for O(1) lookup, and TreeMaps use a Red-Black tree for O(log n) sorted key access.

| Collection | Search Algorithm | Time Complexity |
| :--- | :--- | :--- |
| **ArrayList + binarySearch** | Binary search | $O(\log n)$ |
| **HashMap / HashSet** | Hashing | $O(1)$ average |
| **TreeMap / TreeSet** | Red-Black tree | $O(\log n)$ |
| **Unsorted ArrayList** | Linear search | $O(n)$ |

</details>

<details>
<summary>How is the shuffling algorithm used?</summary>

`Collections.shuffle()` uses the **Fisher-Yates** algorithm internally. It iterates from the last element backwards, and for each position swaps the current element with a randomly chosen element from the remaining unshuffled portion.

</details>

## Types of the collections

<details>
<summary>What are the different collection types?</summary>

The Java collection framework organizes collections into four main types, each designed for a different use case:

1. **List - ordered, index-based, allows duplicates:**
```java
List<String> names = new ArrayList<>();
names.add("Carlos");
names.add("Carlos"); // ✅ duplicates allowed
names.get(0);        // ✅ index-based access
```
Common implementations: `ArrayList`, `LinkedList`, `Vector`

2. **Set - unordered, no duplicates:**
```java
Set<String> names = new HashSet<>();
names.add("Carlos");
names.add("Carlos"); // silently ignored — no duplicates
names.get(0);        // ❌ no index-based access
```
Common implementations: `HashSet`, `TreeSet`, `LinkedHashSet`

3. **Queue - ordered for processing, FIFO:**
```java
Queue<String> tasks = new LinkedList<>();
tasks.offer("task1"); // add to tail
tasks.offer("task2");
tasks.poll();         // removes and returns head → "task1"
tasks.peek();         // reads head without removing → "task2"
```
Common implementations: `LinkedList`, `PriorityQueue`, `ArrayDeque`

4. **Map - Key-Value pairs, no duplicate keys:**
```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Carlos", 95);
scores.put("Carlos", 100); // overwrites previous value
scores.get("Carlos");      // 100
```
Common implementations: `HashMap`, `TreeMap`, `LinkedHashMap`

`Map` is technically not part of the `Collection` hierarchy — it doesn't extend the `Collection` interface. However it is considered part of the Collections Framework because it ships with it and follows the same design principles.

One more worth metioning:

5. **Deque (Double Ended Queue) - insertions and removal at both ends:**
A `Deque` (pronounced "deck") extends `Queue` and allows you to add and remove elements from both the head and the tail, making it work as both a `queue (FIFO)` and a `stack (LIFO)` depending on how you use it.

```java
Deque<String> deque = new ArrayDeque<>();

// Adding at both ends
deque.addFirst("B");  // [B]
deque.addLast("C");   // [B, C]
deque.addFirst("A");  // [A, B, C]

// Removing from both ends
deque.pollFirst(); // removes A → [B, C]
deque.pollLast();  // removes C → [B]

// Used as a stack (LIFO)
Deque<String> stack = new ArrayDeque<>();
stack.push("first");   // addFirst
stack.push("second");
stack.pop();           // removeFirst → "second" — last in, first out
```
ArrayDeque is actually the recommended replacement for Stack in modern Java — Stack is a legacy class with synchronization overhead you rarely need.

Common Implementations: `ArrayDeque`, `LinkedList`

</details>

<details>
<summary>Define Set collection Type?</summary>

An un ordered collection that does not allow duplicate elements. Useful when you only care whether something is preset or not, not its position.

```java
Set<String> names = new HashSet<>();
names.add("Carlos");
names.add("Carlos"); // silently ignored
System.out.println(names.size()); // 1
```

</details>

<details>
<summary>Define List collection Type?</summary>

An ordered collection that allows duplicates and index-based access. Elements maintain their insertion order, and can be accesed by position.

```java
List<String> names = new ArrayList<>();
names.add("Carlos");
names.add("Carlos"); // ✅ allowed
System.out.println(names.get(0)); // "Carlos"
```

</details>

<details>
<summary>Define Queue collection Type?</summary>

A collection designed for FIFO (First In First Out) processing - elements are added at the tail and removed from the head. Used when order of processing matters.

```java
Queue<String> queue = new LinkedList<>();
queue.add("first");
queue.add("second");
queue.poll(); // removes and returns "first"
```

</details>

<details>
<summary>Define Deque collection Type?</summary>

Short for Double Ended Queue - extends Queue by allowing elements to be added and removed from both ends. Can act as both a queue (FIFO) and stack (LIFO).

```java
Deque<String> deque = new ArrayDeque<>();
deque.addFirst("first");
deque.addLast("last");
deque.pollFirst(); // removes "first"
deque.pollLast();  // removes "last"
```

</details>

<details>
<summary>Define Map collection Type?</summary>

Not technically a `Collection` subtype, but part of the framework. Stores key-value pairs where keys are unique - no duplicate keys are allowed, but values can repeat.

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Carlos", 95);
scores.put("Carlos", 100); // overwrites previous value
System.out.println(scores.get("Carlos")); // 100
```

LinkedHashMap and TreeMap are exceptions that maintain order.

</details>

<details>
<summary>What is the difference between Queue and Deque?</summary>

`Queues` have FIFO behavior - elements enter at the tail and leave from the head. `Deques` can have FIFO or LIFO behavior based on which methods you call. 

```java
Deque<String> deque = new ArrayDeque<>();

// Used as a FIFO queue
deque.addLast("first");
deque.addLast("second");
deque.pollFirst(); // "first" — same as regular queue behavior

// Used as a LIFO stack
deque.addFirst("first");
deque.addFirst("second");
deque.pollFirst(); // "second" — last in, first out
```

</details>

<details>
<summary>Operating times for collections:</summary>

### Java collections — time complexity reference


| Collection | Access by index | Search | Insert (end) | Insert (middle) | Delete (middle) |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **List** | | | | | |
| `ArrayList` | O(1) | O(n) | O(1) amortized | O(n) | O(n) |
| `LinkedList` | O(n) | O(n) | O(1) | O(1)* | O(1)* |
| **Set** | | | | | |
| `HashSet` | N/A | O(1) avg | O(1) avg | N/A | O(1) avg |
| `TreeSet` | N/A | O(log n) | O(log n) | N/A | O(log n) |
| `LinkedHashSet` | N/A | O(1) avg | O(1) avg | N/A | O(1) avg |
| **Map** | | | | | |
| `HashMap` | N/A | O(1) avg | O(1) avg | N/A | O(1) avg |
| `TreeMap` | N/A | O(log n) | O(log n) | N/A | O(log n) |
| `LinkedHashMap` | N/A | O(1) avg | O(1) avg | N/A | O(1) avg |
| **Queue / Deque** | | | | | |
| `ArrayDeque` | N/A | O(n) | O(1) amortized | O(n) | O(n) |
| `PriorityQueue` | N/A | O(n) | O(log n) | N/A | O(log n) |


> \* `LinkedList` insert/delete in the middle is O(1) only once you already hold the node reference — reaching that position still costs O(n) traversal.

> "avg" means O(1) under normal load but degrades to O(n) on hash collision worst case.

> N/A = the collection has no concept of positional index for that operation.


</details>

### Sets

<details>
<summary>Can you add duplicate elements in a Set?</summary>

No, sets don't allow to insert duplicate elements, when you try to add an element that already exists, it is ignored silently. duplicates are detected using `hashcode()` and `equals()` - which means for custom objects you must override methods, otherwise the Set compares references instead of content and won't detect duplicates correctly.

</details>

<details>
<summary>Which collection type should you use when ordering is not a requirement? Why?</summary>

```java
// If you need no duplicates and no order → HashSet
Set<String> tags = new HashSet<>();

// If you need duplicates allowed and no order → ArrayList is fine
// but if you also don't need index access → don't overthink it

// If you need key-value and no order → HashMap
Map<String, Integer> scores = new HashMap<>();
```

</details>

<details>
<summary>Can you add a null element to a Set?</summary>

It depends on the implementation:

`HashSet` uses `hashCode()` to find the bucket to store the elements - and Java simply treats `null` as having hash code of 0, so it stores it without issues. Only one null is allowed because it's still a Set - duplicates aren't permitted.

`TreeSet` sorts its elements by calling `compareTo()` on them. When you try to insert `null`, it attempts to compare it with existing elements and throws a `NullPointerException` because `null` has no `compareTo()` method.

This null behavior pattern — Hash implementations allow it, Tree implementations reject it — also applies to Map, so it's worth remembering as a general rule for the interview.

</details>

<details>
<summary>Which Set class should you use to maintain order of insertion?</summary>

Use `LinkedHashSet` when you need a Set that remembers the order elements were inserted. It combines the no-duplicates guarantee of a `HashSet` with a linked list runnig through its entries to maintain insertion order.

```java
Set<String> set = new LinkedHashSet<>();
set.add("Banana");
set.add("Apple");
set.add("Mango");

System.out.println(set); // [Banana, Apple, Mango] — insertion order preserved
```

Whenever you see Linked prefix → insertion order. Whenever you see Tree prefix → sorted order. Whenever you see just Hash → no order, best performance.

</details>

<details>
<summary>What happens to ordering, if same element is inserted again in a Set? Will it maintain its original position or inserted at the end?</summary>

If you try to insert a duplicate element into a LinkedHashSet (that are the `Set` implementation that keeps the insertion order), the insertion is silently ignored and the element maintains its original position.

</details>

<details>
<summary>Which Set class should you use to ensure ordering of the elements?</summary>

`TreeSet` keeps elements sorted using `compareTo()` from the `Comparable`interface for natural ordering, or a custom `Comparator` provided at construction time. Because it relies on comparison for every insertion, it rejects `null` elementsand costs `O(log n)` per operation - the price paid for maintaning sorted order.

```java
// Natural ordering — uses compareTo() from Comparable interface
TreeSet<String> set = new TreeSet<>();
set.add("Banana");
set.add("Apple");
set.add("Mango");
System.out.println(set); // [Apple, Banana, Mango] — alphabetical

// Custom ordering — uses a Comparator you provide
TreeSet<String> set = new TreeSet<>(Comparator.reverseOrder());
set.add("Banana");
set.add("Apple");
set.add("Mango");
System.out.println(set); // [Mango, Banana, Apple] — reverse alphabetical
```

</details>

<details>
<summary>Which Set class should you use when you need to traverse in both the directions?</summary>

Use `TreeSet` when you need to traverse in both directions. TreeSet implements the `NavigableSet` interface which provides methods to traverse elements in both ascending and descending order.

</details>

### Lists

<details>
<summary>Can you add duplicate elements to a List?</summary>

Yes, duplicate elements are allowed in the `List` - unlike `Set` which silently ignores them. Both the value an the position of each element are preserved independently. 

</details>

<details>
<summary>Can you add null element to a list?</summary>

Yes, `List` allows null elements - and unlike `Set` which only allows one null, a `List` can hold multiple nulls since duplicates are permitted.

</details>

<details>
<summary>In which scenarios would you use ArrayList: frequent add/update or frequent reading?</summary>

Use `ArrayList` when you need frequent random access - retrieving elements by index is `O(1)` because `ArrayList` is backed by a contiguous array in memory, so the JVM can jump directly to any position. Avoid it when you need frequent insertions or deletions in the middle - those cost `O(n)` because every element after the insertion point needs to be shifted to make room or close the gap.

The process has two specific names depending on the direction:

* **Shifting right** — when inserting, elements move right to make room
* **Shifting left** — when deleting, elements move left to close the gap

The general term for both is just array shifting or element shifting.

</details>

<details>
<summary>In which scenarios would you use LinkedList: frequent add/update or frequent reading?</summary>

Use `LinkedList` when you need frequent insertions or deletions in the middle of the list. Because each element is a node that holds a reference to the next and previous nodes, Inserting or deleting only requires updating a few references - no shifting needed. Avoid fot frequent random access since reaching a position by index costs `O(n)` - the list has to traverse from the head until it finds it.

</details>

<details>
<summary>Between ArrayList and LinkedList, which one consumes more memory? Why?</summary>

`LinkedList` consumes more memory because each element is a node, that not only contains the element itself, but also the references for the next and previous element

`ArrayList` just stores the element references in a plain array — no extra overhead per element. The only extra memory it occasionally uses is reserved but empty slots when the array grows (it doubles in capacity when full), but that's temporary and far cheaper than LinkedList's per-node overhead.

</details>

<details>
<summary>Between ArrayList and LinkedList, which one would you prefer for frequent random access? Why?</summary>

`ArrayList` because it works with a contiguous array in memory so the JVM can jump directly to any position in `O(1)`

</details>

### Maps

<details>
<summary>How Map is different from other collections?</summary>

`Map` doesn't extend the Collection `interface` and it doesn't just stores a set of values alone, it stores key-value pairs. Keys must be unique - adding a second entry with the same key overwrites the existing value instead of adding a new entry. Values however can repeat freely.

</details>

<details>
<summary>Can you add a null value as key to a Map?</summary>

Similar to `Set`, most `Map` implementations accept just one null key, since keys must be unique. However `TreeMap` rejects null keys because it relies on `compareTo()` to sort keys, and comparing null throws a `NullPointerException`

</details>

<details>
<summary>Can you guarantee ordering of elements in HashMap?</summary>

No, you cannot guarantee ordering in a `HashMap`. Elements are stored in buckets determined by the key's `hashCode()` - not by insertion order or any natural ordering. The iteration order can even change when the map grows and rehashes entries. If ordering matters use LinkedHashMap for insertion order or TreeMap for sorted order.

</details>

<details>
<summary>How HashMap is able to provide constant-time performance for get and put operations?</summary>

This is because it stores the elements in buckets based on the hashcode of the keys, so when you try to get or add an element it quickly knows where to look at based on its hashcode.

Within the bucket it uses `equals()` to find the exact entry. As long as keys are well distributed across buckets this is effectively constant time. In the worst case where many keys collide into the same bucket it degrades to O(n) — which is why overriding both hashCode() and equals() correctly on custom key objects matters.

</details>

<details>
<summary>What is the difference between HashMap and LinkedHashMap?</summary>

The main difference is that `LinkedHashMap` preserves the order of insertion while `HashMap` makes no ordering guarantees. `LinkedHashMap` achieves this by maintaining a doubly linked list running through all its entries in addition to the hash table structure - so iteration always returns entries in the order they were inserted. This comes with a slight memory and performance overhead compared to `HashMap`.

</details>

<details>
<summary>If insertion order is to be maintained, which one would you use: LinkedHashMap or HashMap? Why?</summary>

`LinkedHashMap` because it works with a doubly linked list in addition to the hash table structure - so iteration always returns entries in the order they were inserted.

</details>

<details>
<summary>When you want to maintain ordering of key, which Map class would you use?</summary>

`TreeMap` because this works with `compareTo()` from the `Comparable` interface for natural ordering (alphabetical for Strings, numerical for Integers), or a custom `Comparator` provided at construction time for custom sorting. This costs O(log n) per operation since it needs to find the correct position for each key.

</details>

<details>
<summary>Which Map class would you use to access it in both ascending and descending order?</summary>

`TreeMap` is the one that allows you to traverse it from both ends, since it implements the `NavigableMap` interface which provides methods to traverse entires in both ascending and descending order.

</details>

<details>
<summary>Why the key used for Map and the value used for Set should be of immutable type?</summary>

This is for ensuring that their `hashCode()` remains the same after insertion and it doesn't get lost within the `HashMap`. This is because the collection uses the hash to know which bucket the entry lives in - if the key mutates after insertion then its hash changes, but the entry stays in the old bucket. The collection can't find it anymore even though it's still there.

</details>

<details>
<summary>What are the disadvantages of exposing internal collection object to the caller?</summary>

When you expose an internal collection directly, the caller can manipulate the object freely - adding, removing, or modifiying elements without the class knowing. Becase this breaks encapsulation, because the class loses control over its own state.

```java
// Option 1 — return an unmodifiable view - shallow copy
public List<String> getStudents() {
    return Collections.unmodifiableList(students); // ❌ UnsupportedOperationException if caller tries to modify
}

// Option 2 - return a copy of the collection with copies of its elements - deep copy

public List<Student> getStudents() {
    return students.stream()
        .map(s -> new Student(s.name)) // copy each object individually
        .collect(Collectors.toList());
}
```

A shallow copy protects the container but shares the same object references. A deep copy creates brand new objects — nothing is shared with the original.

</details>

<details>
<summary>Which collection is best if you want to traverse the collection in sorted order?</summary>

Use `TreeSet` if you're sorting individual values, use `TreeMap` if you're sorting key-value pairs. Both maintain sorted order using `compareTo()` or a custom `Comparator`, and both cost `O(log n)` per operation. The choice is purely about your data structure need, not performance.

Worth adding for the interview — if you already have an unsorted collection and just need to traverse it once in sorted order, you don't need to switch to TreeSet or TreeMap:

```java
List<String> names = new ArrayList<>(List.of("Banana", "Apple", "Mango"));
Collections.sort(names); // sort in place
// or
names.stream().sorted().forEach(System.out::println); // sort for traversal only
```

If sorting is a one-time need, `Collections.sort()` is simpler. If you always need sorted order on every insertion, `TreeSet` or `TreeMap` is the right choice.

</details>

### Hashing

<details>
<summary>What are the different collections types in Java that use hashCode and equals methods? Why do these collections type use hashCode and equals method?</summary>

They are `HashSet`, `LinkedHashset`, `HashMap`, `LinkedHashMap`. They use them because they store their items in buckets based on the `hashCode()` of each element, and when two elements have the same hash inside a bucket they use `equals()` to get the specific entry.

They use `hashCode()` to determine which bucket to store or look up an element in, and `equals()` to confirm whether two elements are truly the same — both when detecting duplicates on insertion and when resolving collisions within the same bucket. `TreeSet` and `TreeMap` are the notable exclusions — they use `compareTo()` instead since they maintain sorted order rather than hash-based storage.

</details>

<details>
<summary>When you override hashCode() method, why should you also override equals() method?</summary>

Because `hashCode()` and `equals()` work as two-step chain - `hashCode()` finds the bucket, then `equals()` confirms whether two objects within that bucket are truly the same.

If you override `hashCode()` without `equals()`, two objects with the same hash land in the same bucket but `equals()` still compares references and treats them as different. If you override `equals()` without `hashCode()`, two logically equal objects get different hashes and land in different buckets, so `equals()` never even gets called. Both must be overridden together to maintain the contract: if two objects are equal according to `equals()`, they must return the same `hashCode()`.

</details>

<details>
<summary>Why do you need to override hashCode() method, when hashCode() method is already present in the java.lang.Object class?</summary>

Because the default Object `hashCode()` is based on object references, so two objects only return the same hash if they are literally the same object.

</details>

<details>
<summary>Why should an object return the same hash-code value every time during its lifetime?</summary>

To ensure the collection always knows which bucket to look in. If the hash changes, the object may remain in the old bucket, and the collection lose the ability to find it. This is why keys in `Map` and values in a `Set` should be immutable - immutabilty guarantees that the hash never changes after insertion.

</details>

<details>
<summary>Is it necessary that an object returns the same hash-code every time program runs?</summary>

No, it's not ncessary. An object only needs to return the same hash code within a single run of the program (i.e., during its lifetime in memory)

</details>

<details>
<summary>Is it necessary that two equal object return the same hash-code?</summary>

Yes it is, due that `hashCode()` and `equals()` work together within `Map` and `Set`, `hashCode()` for knowing which bucket look in or store, and `equals()` to know if two objects are logically the same.

</details>

<details>
<summary>Can two objects, even when they are not equal, still have the same hash-code?</summary>

Yes, it is possible for two different objects to have the same hash, this is called a collision. When it happens, both objects land in the same bucket and the collection uses `equals()` to tell them apart.

That's why its important to override both methods within a class, so `Map` and `Set` can differentiate two logically different objects.

</details>

<details>
<summary>What is the purpose of calculating hash-code?</summary>

The purpose of this is for `Map` and `Set` to know in which bucket look in or store an element - instead of scanning every element in the collection, the hash acts as a direct address that takes the collection straight to the right bucket in `O(1)`. Without hashingthe collection would have to compare every element one by one, costing `O(n)`

</details>

<details>
<summary>What is the purpose of equals method?</summary>

It's purpose is to know if two objects are logically the same. In the context of `Map` and `Set`, it is used in two specific scenarios: when detecting duplicates on insertion - if two objects land in the same bucket, `equals()` confirms whether they are truly the same and the insertion should be ignored; and when resolving collision - if two different objects share the same hash code, `equals()` is what tells them apart within a bucket.

</details>

<details>
<summary>If two objects have the same hash code, how are these stored in hash buckets?</summary>

Then they land in the same bucket and the `equals()` is used to tell them apart.

Within the bucket, entries are stored as a linked list — the new entry is chained to the existing one. When retrieving, the collection traverses that linked list and uses `equals()` to find the exact entry. This is why many collisions in the same bucket degrade performance from `O(1)` to `O(n)` — the linked list gets longer and longer to traverse.

</details>

<details>
<summary>If two objects have different hash code, do you still need equals method?</summary>

No, equals() is not needed when hash codes differ — different hashes are sufficient proof of inequality.

but,

Yes, it's necessary for comparing and prevent duplicates when inserting

</details>

<details>
<summary>If two objects have the same hash code, then how equals method is used?</summary>

If two objects have the same hash code they land in the same bucket — a collision. `equals()` is then used in two scenarios: on insertion, to check if the new object is a duplicate of an existing entry and should be ignored; and on retrieval, to traverse the bucket's linked list and identify the exact entry being looked for.

</details>

### Comparing & sorting

<details>
<summary>What is the difference between Comparable Interface and Comparator Class?</summary>

`Comparable` is an interface that implements the class itself and defines how its objects should be compared. This is called the natural ordering of the class.

```java
class Person implements Comparable<Person> {
    String name;
    int age;

    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name); // natural order = alphabetical by name
    }
}

List<Person> people = new ArrayList<>();
Collections.sort(people); // uses compareTo() automatically
```

`Comparator:` A separate class implements `Comparator` and defines its own comparison logic, independent of the class beign compared.  Useful when you need multiple different ways to sort the same class or when you can't modify the class itself.

```java
class AgeComparator implements Comparator<Person> {
    @Override
    public int compare(Person a, Person b) {
        return Integer.compare(a.age, b.age); // custom order = by age
    }
}

Collections.sort(people, new AgeComparator()); // sort by age
Collections.sort(people, Comparator.comparing(p -> p.name)); // sort by name
```

</details>

<details>
<summary>When would you implement Comparable Interface to sort a collection?</summary>

I would implement `Comparable` to a class when i want a collection of its objects to be ordered in its natural order.

Implement `Comparable` when the class has one obvious, universally agreed-upon way to be sorted - its natural ordering. For example, `String` sorts alphabetically, `Integer` sorts numerically. You would implement it when you own the class and there is one default ordering that makes sense in most contexts, allowing `Collection.sort()` and `TreeSet`/`TreeMap` to sort objects of that class automatically without any extra configuration.

</details>

<details>
<summary>When do you use Comparator Class to sort a collection?</summary>

When i need alternatives to the natural sorting of a class, or when i need multiple options for sorting the same class, or when you can't modify the original class - either because you don't own it or because it already has a Comparable implementation you don't want to change.

```java
// Scenario 1 — alternative to natural ordering
Collections.sort(products, Comparator.comparing(p -> p.name)); // sort by name instead of price

// Scenario 2 — multiple sorting strategies
Comparator<Product> byPrice = Comparator.comparing(p -> p.price);
Comparator<Product> byName  = Comparator.comparing(p -> p.name);
Comparator<Product> byPriceThenName = byPrice.thenComparing(byName); // chain them

// Scenario 3 — can't modify the class
// String already implements Comparable (alphabetical)
// but you need reverse order without touching String
Collections.sort(names, Comparator.reverseOrder());
```

</details>

### Iterating

<details>
<summary>Explain different ways to iterate over collection?</summary>

The traditional `for` loop, that is index-based that works for `List`:

```java
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}
```

The Enhanced `for`loop - works for any `Iterable`:

```java
for (String name : names) {
    System.out.println(name);
}
```

`Iterator` - explicit traversal, allows safe removal:

```java
Iterator<String> it = names.iterator();
while (it.hasNext()) {
    String name = it.next();
    if (name.equals("Carlos")) {
        it.remove(); // ✅ safe removal during iteration
    }
}
```

`ListIterator` - bidirectional, only for `List`:

```java
ListIterator<String> it = names.listIterator();
while (it.hasNext())     { it.next(); }     // forward
while (it.hasPrevious()) { it.previous(); } // backward
```

`forEach()` with lambda - modern, concise:

```java
names.forEach(name -> System.out.println(name));
```

`stream` - functional style, supports filtering and mapping:

```java
names.stream()
     .filter(name -> name.startsWith("C"))
     .forEach(System.out::println);
```

</details>

<details>
<summary>Iterating methods:</summary>

| Method | Works on | Bidirectional? | Safe removal? |
| :--- | :--- | :---: | :---: |
| Traditional `for` | `List` only | ❌ | ⚠️ risky |
| Enhanced `for` | Any `Iterable` | ❌ | ❌ |
| `Iterator` | Any `Collection` | ❌ | ✅ |
| `ListIterator` | `List` only | ✅ | ✅ |
| `forEach()` | Any `Iterable` | ❌ | ❌ |
| `Stream` | Any `Collection` | ❌ | ❌ |

</details>

<details>
<summary>Why would you use Iterator over an enhanced for loop?</summary>

For same removal during iteration - the enhanced `for` loop throws a `ConcurrendModificationException` if you try to remove elements while iterating, while `Iterator.remove()` handles it safely. 

</details>

<details>
<summary>Can you modify collection structure while iterating using for-each loop?</summary>

You cannot structurally modify a collection during a for-each loop - neither adding or removing. Use `Iterator.remove()` for safe removal, or `removeIf()` for a cleaner modern alternative.

```java
// ✅ Iterator — safe removal
Iterator<String> it = names.iterator();
while (it.hasNext()) {
    if (it.next().equals("Carlos")) {
        it.remove(); // updates modCount correctly
    }
}

// ✅ removeIf() — modern, clean
names.removeIf(name -> name.equals("Carlos"));

// ✅ collect into new list — safe adding
List<String> result = new ArrayList<>();
for (String name : names) {
    result.add(name + " modified"); // modifying a separate list, not the one being iterated
}
```

</details>

<details>
<summary>Why you can't modify a collection while iterating it with a for-each loop?</summary>

Collections have an internal modification counter (`modCount`). The enhaned for loop checks this counter on every iteration - if it detects the collection was structurally modified during iteration it immediately throws `ConcurrentModificationException`. This is called `fail-fast` behavior

This also happens with `forEach()` with a lambda and with `stream().forEach()`

</details>

<details>
<summary>Can you modify a collection element value while iterating using for-each loop?</summary>

Yeah, this is safe because you are not modifying the structure of the collection so with `fail-fast` iterator methods,  `ConcurrentModificationException` is never thrown, and when traversing the collection with a traditional `for` you are not risking to skip elements.

However there is a subtle trap: reassigning the loop variable itself only changes the local copy, not the actual element in the collection. To truly modify elements you need to modify the object's fields directly or use an index-based approach.

```java
// Traditional for loop — same problem
for (int i = 0; i < names.size(); i++) {
    String name = names.get(i);
    name = name.toUpperCase(); // ❌ local copy only — list unchanged
    names.set(i, name);        // ✅ this is what actually updates the list
}

// Iterator — same problem
Iterator<String> it = names.iterator();
while (it.hasNext()) {
    String name = it.next();
    name = name.toUpperCase(); // ❌ local copy only
}

// Only works directly with mutable object fields
for (Person p : people) {
    p.name = p.name.toUpperCase(); // ✅ modifying the actual object, not the reference
}
```

Reassigning a loop variable never modifies the collection — regardless of which iteration method you use. It's a Java reference behavior, not an iterator behavior.

</details>

<details>
<summary>What are the limitations with for-each loop, with respect to navigation direction?</summary>

It's unidirectional, you only can traverse from the head to the tail, forward only. If you need to traverse in both directions you need `ListIterator`, which provides `hasPrevious()` and `previous()` methods.

</details>

<details>
<summary>What is difference between for-each loop and Iterator, with respect to navigation</summary>

Both are unidirectional — the difference isn't navigation direction but rather that `Iterator` gives you explicit control and safe removal, while the enhanced `for` loop is cleaner syntax for the same forward traversal.

</details>

<details>
<summary>Can you modify collection structure while iterating using an Iterator?Can you modify a collection element value while iterating using an Iterator?</summary>

`Iterator` supports safe removal during iteration through `it.remove()` - it updates `modCount` correctly so no `ConcurrentModificationException` is thrown. However it does not support adding elements - `Iterator` has no `add()` mehod. If you need to safely add elements during iteration use `ListIterator` instead. Modifiying values is also safe for the same reason as with any other iteration method - it doesn't touch the collection structure. 

</details>

<details>
<summary>Between for-each loop and for loop, which one would you prefer? Why?</summary>

It depends on what i need to do, but if its just for traversing a collection a for-each loop because it's a cleaner way that allows you to traverse any `Iterable`. I'd switch to the traditional `for` loop when i need the index, when i need to modify elements position or when i need to iterate backwards.

</details>

<details>
<summary>What is the meaning of term fail-fast and fail-safe in context of collection iteration?</summary>

* **Fail-fast:** Iterators that monitor the collection's `modCount`on every `next()` call and throw `ConcurrentModificationException` immediately if they detect a structural modification was made outside the iterator. They "fail fast" rather than risk producing inconsistent results.

```java
List<String> names = new ArrayList<>(List.of("Carlos", "Maria"));
Iterator<String> it = names.iterator();
names.add("Juan");       // modCount incremented
it.next();               // ❌ detects modCount mismatch → ConcurrentModificationException
```

* **Fail-safe:** Iterators that work on a copy of the collection instead of the original - so structural modifications to the original don't affect the ongoing iteration. They never throw `ConcurrentModificationException` but the tradeoff is they may not reflect the latest state of the collection.

```java
CopyOnWriteArrayList<String> names = new CopyOnWriteArrayList<>(List.of("Carlos", "Maria"));
Iterator<String> it = names.iterator(); // iterates over a snapshot copy
names.add("Juan");       // modifies original — copy is unaffected
it.next();               // ✅ no exception — still sees original snapshot
```

</details>

<details>
<summary>Why Iterators are considered more thread safe?</summary>

Not all iterators are thread safe - fail-fast iterators from regular collections like `ArrayList` and `HashMap` are not thread safe and throw `ConcurrentModificationException` under concurrent modification. Only fail-safe iterators form concurrent collections like `CopyOnWriteArrayList` and `ConcurrentHashMap` are thread safe, because they work on a snapshot copy of the collection instead of the original.

`Iterator.remove()` solves the single-thread modification problem. Fail-safe iterators solve the multi-thread modification problem. They address different scenarios — don't confuse them.

</details>

<details>
<summary>What is the benefit of a fail-fast iterator?</summary>

That it preserves the consistency in the collection by preferring to immediately fail (`ConcurrentModificationException`) the moment it detects a structural modification, rather than producing unpredictable or inconsistent results when altering a collection structure while iterating.

</details>

<details>
<summary>Data independent access:</summary>

Data indepentdent access mmeans the caller interacts with the collection through an interface rather a concrete implementation, and receives either an unmodifiable view or a defensive copy instead of the internal collection directly. This way the caller doesn't depent on which specific implementation you use, and can't manipulate your internal state freely.

```java
class StudentRegistry {
    private List<String> students = new ArrayList<>(); // concrete internally

    // ✅ data independent access — caller sees only the interface
    // and gets an unmodifiable view
    public List<String> getStudents() {
        return Collections.unmodifiableList(students);
    }
}
```

</details>
# Collection of Log Utils #
Provides a collection of utilities for logging related use cases.

Requires:

- Java >= `1.8`

## Declarative Logging MDC Management ##
Allows clients to _declaratively_ add relevant method arguments to the Mapped Diagnostic Context **(MDC)** of a logging framework. 
The clients should mark the method with a **method marker** annotation and arguments with an **argument marker** annotation.

The following **method marker** annotations are available _(corresponds to the supported logging framework)_

- `@Slf4jMdcAware`
- `@Log4j2MdcAware`

The following **argument marker** annotations are available _(corresponds to the supported argument type)_

- `@MdcMap`
- `@MdcValue`
- `@MdcParam`
- `@MdcHolder`

Additionally, two _holder_ annotations, `@MdcMaps` and `@MdcParams`, allow clients to specify multiple `@MdcMap` and `@MdcParam` annotations respectively.

### Quickstart ###
To add all the entries from a `Map` to the `Slf4j` MDC:
```
@Slf4jMdcAware
public void doSomething(@MdcMap Map input) {
    // do something
}
```
To add all the entries from a `Map` to the `Log4j2` MDC:
```
@Log4j2MdcAware
public void doSomething(@MdcMap Map input) {
    // do something
}
```

### MdcMap ###
- Used for `Map` objects

For a `Map` as below:-
```
{
    "one": "two",
    "foo": "bar",
    "this": "that",
    "nested_one": {
        "inner_foo": "inner_bar",
        "inner_this": "inner_that"
    },
    "nested_two": {
        "inner_foo_again": "inner_bar_again",
        "inner_this_again": "inner_that_again"
    }
}
```
- `@MdcMap(filter = { "one", "this" })` will add the following to the `Log4j2` MDC.
```
{
    "one": "two",
    "this": "that"
}
```
- `@MdcMap(nestedMapKeys = { "nested_one" })` will add the following to the `Log4j2` MDC.
```
{
    "inner_foo": "inner_bar",
    "inner_this": "inner_that"
}
```
- `@MdcMap(mdcKeyPrefix = "my", filter = { "one", "foo", "this" })` will add the following to the `Log4j2` MDC.
```
{
    "my-one": "two",
    "my-foo": "bar",
    "my-this": "that"
}
```

#### Notes ####
1. `mdcKeyPrefix` is added to the keys in **MDC** that allows different `Map`s to supply the same keys to the MDC.
2. `@MdcMap` can be used:-
    1. on its own _(entire `Map` is copied to the MDC)_, or 
    2. with any one of `filter`, `nestedMapKeys`, or `mdcKeyPrefix` values, or 
    3. any combination of them _(`nestedMapKeys` is applied first to get the qualifying inner `Map`)_.
3. `@MdcMap` `filter` values are **NOT** _case-sensitive_; so a `foo` will match `Foo`, `FOO` or any other case variants of `foo`.
4. `@MdcMap` annotations are **repeatable** on their own or can be used via `@MdcMaps` holder annotation as shown below.
```
public void doSomething(@MdcMaps({ @MdcMap(filter = { "this" }),
                                   @MdcMap(nestedMapKeys = { "nested_one" }, filter = { "one", "foo"}) })
                        Map map) {}

```
or
```
public void doSomething(@MdcMap(filter = { "this" })
                        @MdcMap(nestedMapKeys = { "nested_one" }, filter = { "one", "foo"})
                        Map map) {}
```

### MdcValue ###
- Used for objects that have useful `toString()` implementations

For an `Object` parameter where its `toString()` method returns `777`:- 

- `@MdcValue(name = "foo")` will add `{ "foo": "777" }` to the MDC.

If the annotated object is `null`:

- `@MdcValue(name = "foo")` will **NOT** add anything to the MDC.
- `@MdcValue(name = "foo", nullable = true)` will add `{ "foo": "null" }` to the MDC.

### MdcHolder ###
- Used for objects that implement `com.aspirecsl.log.HasDiagnosticContext` interface

For a `HasDiagnosticContext` implementation that returns the following `Map` via its `mappedDiagnosticContext()` method:-
```
{
    "one": "two",
    "foo": "bar",
    "this": "that"
}
```
- `@MdcHolder` annotation will add the following to the `Log4j2` MDC.
```
{
    "one": "two",
    "foo": "bar",
    "this": "that"
}
```
- `@MdcHolder(filter = { "one", "this" })` annotation will add the following to the `Log4j2` MDC.
```
{
    "one": "two",
    "this": "that"
}
```
- `@MdcHolder(mdcKeyPrefix = "my")` will add the following to the `Log4j2` MDC.
```
{
    "my-one": "two",
    "my-foo": "bar",
    "my-this": "that"
}
```

### MdcParams ###
- Used for third-party objects where properties added to the MDC are resolved using Reflection API

For an object of the class:-
```
public class Foo{

    private String bar = "BAR";
    private String foo = "FOO";

    // constructors, setters and 
    // other getters omitted for brevity

    public String getBar(){
        return bar;
    }
}
```

- `@MdcParams(@MdcParam(field = "foo"), @MdcParam(field = "bar"))` will add the following to the MDC.
```
{
    "foo": "FOO",
    "bar": "BAR"
}
```

- `@MdcParams(@MdcParam(getter = "getBar", label = "bar"))` will add the following to the MDC.
```
{
    "bar": "BAR"
}
```
#### Notes ####
1. Only one __(and exactly one)__ of `field` or `getter` should be specified in `@MdcParam`
2. If `label` is not specified in `@MdcParam` then the specified `field` or `getter` is used as the key in the MDC
3. The holder annotation `@MdcParams` is optional. Both of the following produce the same results.
```
public void doSomething(@MdcParams({ @MdcParam(field = "foo", label = "FOO"),
                                     @MdcParam(getter = "getBar", label = "BAR") })
                        Foo foo) {}

```
```
public void doSomething(@MdcParam(field = "foo", label = "FOO")
                        @MdcParam(getter = "getBar", label = "BAR")
                        Foo foo) {}
```
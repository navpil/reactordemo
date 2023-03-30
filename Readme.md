# Notes

## Spring Boot notes

Spring Boot has annotation for everything.

Also it has a configuration for everything in application.properties

Just google "How to in Spring Boot."
This is my most important lesson from working with Spring Boot.

Here I'm using Spring WebFlux and R2DBC (plus H2 in memory DB).

## What is reactive

Theoretically it's very different to how it is practically used.
You will read about "pushbacks" and other stuff, but it's abstracted away.

Either reactive or not.
Example: we could not add an OAuth server inside a reactive application.

## What is monad

Schroedinger monad (similar to Optional, Completable Future, List (though a strange case for monad))

Most important: 
 - Mono.just(),
 - map
 - flatMap 

Explain the difference between map and flatMap (in `saveEmployee`);

Kind advice: Reactor interface is very big. 
Understanding all the methods can be daunting.
Try to only use map/flatMap whenever possible.
Calling some methods can have unexpected consequences.

Lazy execution, you build the chain and then it gets executed

## Some API

### Flux to mono and back

Transforming Flux to Mono

        //.collectList()
        //.flatMapMany(Flux::fromIterable)

### Subscribe

What is subscribe, what is subscribeOn and what is publishOn

How to do the blocking calls:
https://egkatzioura.com/2021/10/11/executing-blocking-calls-on-a-reactor-based-application/

## Unusual way of writing code

### Reverse coding

Check the `getEmployeeById`

    UserEntity user = repository.findById(id);
    return mappingFunction(user);

nor

    return mappingFunction( repository.findById(id)));

but:

    return repository.findById(id)
        .map(this::mappingFunction);

### Combining results

Check `getEmployeeByIdWithManager`.
This no longer works:
    
    User user = repository.findById(id);
    User manager = repository.findById(user.getManager());
    return new UserWithManager(user, manager);

Lock the context (`getEmployeeByIdWithManager`) or use Zip (`ReactorTest::zipTest`);

### Exception handling

You won't try-catch exceptions, you will have to work with `Mono.error()` and Reactive API such as `retry` and `onError`
Be careful: API is tricky to get right.

## Gotchas

Remember: all execution is lazy

Reactive repositories do not use Hibernate under the hood, don't expect `@OneToMany` work out of the box;

### Mono.defer

Mono.defer is our savior for:
 - Repeat
 - switchIfEmpty (`createDefaultUser()`)

Beware of behavior of "repeat"
Because Mono.just() will not be called again.

### Mono.empty

Mono.empty() will often mean end of execution, it's not the same as empty object

### Logging

Log.info won't always work as expected (`showUnusualLogging()` example)

## References:

 - [Project Reactor](https://projectreactor.io/docs/core/release/reference/)
 - [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
 - [Reactive Manifesto](https://www.reactivemanifesto.org/)
 - [Get Started with Reactor Blog Post](https://developer.okta.com/blog/2018/09/21/reactive-programming-with-spring)


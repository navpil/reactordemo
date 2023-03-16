package ua.navpil.reactordemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class ReactorTest {

    private static final Logger LOG = LogManager.getLogger(ReactorTest.class);

    @Test
    public void showSubscribe() {
        Mono.just("Jim")
                .map(s -> {
                    LOG.info(s);
                    return s;
                })
//                .subscribe()
        ;
    }

    @Test
    public void zipTest() {
        Mono<String> data1 = Mono.just("Data1");
        Mono<String> data2 = Mono.just("Data2");

        Mono.zip(data1, data2).map(d -> d.getT1() + d.getT2())
                .map(s -> {
                    LOG.info(s);
                    return s;
                })
                .subscribe();
    }

    @Test
    public void repeatTest() {
        class UserRepository {
            private int counter = 0;
            public Mono<User> getUser() {
                if (counter == 0) {
                    counter++;
                    return Mono.error(new RuntimeException("Could not load the user"));
                } else {
                    return Mono.just(new User("1", "John", "2"));
                }
            }
        }

        UserRepository userRepository = new UserRepository();

        //Mono is not reevaluated!!! Use Mono.defer()
        Mono.defer(() -> userRepository.getUser())
                .map(User::getName)
                //If you don't remember this API, you can always switch to map()
                .doOnNext(name -> LOG.info("Got User " + name))
//                .map((name) -> {
//                    LOG.info("Found user " + name);
//                    return name;
//                })
                .retry(3)
                .subscribe();

    }

    @Test
    public void showUnusualLogging() {
        LOG.info("The service is called");
        Mono<String> john = Mono.just("John")
                .flatMap(this::anotherEntity);

        LOG.info("And now I will map it");
        john
                .flatMap(this::anotherEntity2)
                .subscribe();
    }

    public Mono<String> anotherEntity(String name) {
        LOG.info("Mapping to entity 1");
        return Mono.just(name + " 1");
    }

    public Mono<String> anotherEntity2(String name) {
        LOG.info("Inside 2");
        return Mono.just(name + " 2");
    }

}

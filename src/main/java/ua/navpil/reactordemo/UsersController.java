package ua.navpil.reactordemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersRepository repository;

    @Autowired
    public UsersController(UsersRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public Mono<User> getEmployeeById(@PathVariable String id) {
        //Unusual way of writing code, not mappingFunction(repository.findById(id));
        // but repository.findById(id).map(this::mappingFunction);
        return repository.findById(id)
                .map(UsersController::map)
                .switchIfEmpty(Mono.just(createDefaultUser()));
    }

    @GetMapping("/withmanager/{id}")
    public Mono<UserWithManager> getEmployeeByIdWithManager(@PathVariable String id) {
        return repository.findById(id)
                .map(UsersController::map)
                .flatMap(user -> {
                    if (user.getManager() != null) {
                        return repository.findById(user.getManager())
                                .map(UsersController::map)
                                .map(manager -> new UserWithManager(user, manager));
                    } else {
                        return Mono.just(new UserWithManager(user, null));
                    }
                });
    }

    public record UserWithManager (User user, User manager) {

    }

    private static User createDefaultUser() {
        System.out.println("I'm creating a default user");
        return new User("-1", "Not Found", null);
    }

    @PostMapping
    public Mono<User> saveEmployee(@RequestBody Mono<User> user) {
        return user
                //Map, because method returns normal object
                .map(UsersController::map)
                //FlatMap because method returns Mono
                .flatMap(repository::save)
                .map(UsersController::map);
    }
    @GetMapping
    public Flux<User> list() {
        //.collectList()
        //.flatMapMany(Flux::fromIterable)
        return repository.findAll()
                .map(UsersController::map)

                .collectList()
                .flatMapMany(Flux::fromIterable);
    }

    private static UserEntity map(User user) {
        return new UserEntity(user.getId(), user.getName(), user.getManager());
    }
    private static User map(UserEntity user) {
        return new User(user.getId(), user.getName(), user.getManager());
    }
}

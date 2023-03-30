package ua.navpil.reactordemo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UsersRepository extends ReactiveCrudRepository<UserEntity, String> {

}

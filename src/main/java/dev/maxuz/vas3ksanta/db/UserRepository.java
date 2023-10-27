package dev.maxuz.vas3ksanta.db;

import dev.maxuz.vas3ksanta.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByTelegramId(String tid);
}

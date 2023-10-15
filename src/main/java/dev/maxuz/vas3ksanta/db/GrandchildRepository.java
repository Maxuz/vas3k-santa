package dev.maxuz.vas3ksanta.db;

import dev.maxuz.vas3ksanta.model.GrandchildEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GrandchildRepository extends CrudRepository<GrandchildEntity, Long> {
    Optional<GrandchildEntity> findByTelegramId(String tid);
}

package dev.maxuz.vas3ksanta.db;

import dev.maxuz.vas3ksanta.model.Grandchild;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GrandchildRepository extends CrudRepository<Grandchild, Long> {
    Optional<Grandchild> findByTelegramId(String tid);
}

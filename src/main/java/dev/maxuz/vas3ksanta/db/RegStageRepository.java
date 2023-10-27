package dev.maxuz.vas3ksanta.db;

import dev.maxuz.vas3ksanta.model.UserEntity;
import dev.maxuz.vas3ksanta.model.RegStageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegStageRepository extends CrudRepository<RegStageEntity, Long> {
    Optional<RegStageEntity> findByUser(UserEntity user);
}

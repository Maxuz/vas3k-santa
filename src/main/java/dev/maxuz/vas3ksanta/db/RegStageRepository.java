package dev.maxuz.vas3ksanta.db;

import dev.maxuz.vas3ksanta.model.GrandchildEntity;
import dev.maxuz.vas3ksanta.model.RegStageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegStageRepository extends CrudRepository<RegStageEntity, Long> {
    Optional<RegStageEntity> findByGrandchild(GrandchildEntity grandchild);
}

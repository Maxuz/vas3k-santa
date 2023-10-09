package dev.maxuz.vas3ksanta.db;

import dev.maxuz.vas3ksanta.model.Grandchild;
import dev.maxuz.vas3ksanta.model.RegStage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegStageRepository extends CrudRepository<RegStage, Long> {
    Optional<RegStage> findByGrandchild(Grandchild grandchild);
}

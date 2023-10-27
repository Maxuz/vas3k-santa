package dev.maxuz.vas3ksanta.service;

import dev.maxuz.vas3ksanta.db.UserRepository;
import dev.maxuz.vas3ksanta.db.RegStageRepository;
import dev.maxuz.vas3ksanta.model.UserEntity;
import dev.maxuz.vas3ksanta.model.RegStageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegStageService {
    private static final Logger log = LoggerFactory.getLogger(RegStageService.class);

    private final UserRepository userRepository;
    private final RegStageRepository regStageRepository;

    private final Set<RegStageEntity.Stage> updatableStages = Arrays.stream(RegStageEntity.Stage.values()).filter( s -> !s.equals(RegStageEntity.Stage.DECLINED)).collect(Collectors.toSet());

    public RegStageService(UserRepository userRepository, RegStageRepository regStageRepository) {
        this.userRepository = userRepository;
        this.regStageRepository = regStageRepository;
    }

    public void updateStageByTelegramId(String telegramId, RegStageEntity.Stage stage) {
        Optional<UserEntity> opUser = userRepository.findByTelegramId(telegramId);
        if (opUser.isPresent()) {
            RegStageEntity regStage = regStageRepository.findByUser(opUser.get())
                .orElse(new RegStageEntity(stage, opUser.get()));
            if (canUpdate(regStage.getStage())) {
                regStageRepository.save(regStage);
            } else {
                log.warn("The stage {} can't be updated", stage);
            }
        } else {
            log.warn("User with telegramId {} is not found", telegramId);
        }
    }

    private boolean canUpdate(RegStageEntity.Stage stage) {
        return updatableStages.contains(stage);
    }

}

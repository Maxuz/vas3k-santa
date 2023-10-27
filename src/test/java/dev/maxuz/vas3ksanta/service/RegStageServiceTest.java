package dev.maxuz.vas3ksanta.service;

import dev.maxuz.vas3ksanta.db.GrandchildRepository;
import dev.maxuz.vas3ksanta.db.RegStageRepository;
import dev.maxuz.vas3ksanta.model.GrandchildEntity;
import dev.maxuz.vas3ksanta.model.RegStageEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegStageServiceTest {
    private final GrandchildRepository grandchildRepository = mock(GrandchildRepository.class);
    private final RegStageRepository regStageRepository = mock(RegStageRepository.class);

    private final RegStageService service = new RegStageService(grandchildRepository, regStageRepository);

    @ParameterizedTest
    @EnumSource(RegStageEntity.Stage.class)
    void updateStageByTelegramId_StageExist(RegStageEntity.Stage stage) {
        GrandchildEntity grandchild = mock(GrandchildEntity.class);
        when(grandchildRepository.findByTelegramId(any())).thenReturn(Optional.of(grandchild));

        RegStageEntity regStageEntity = mock(RegStageEntity.class);
        when(regStageEntity.getStage()).thenReturn(stage);
        when(regStageRepository.findByGrandchild(grandchild)).thenReturn(Optional.of(regStageEntity));

        service.updateStageByTelegramId("1234", RegStageEntity.Stage.STARTED);

        if (stage.equals(RegStageEntity.Stage.DECLINED)) {
            verify(regStageRepository, never()).save(any());
        } else {
            verify(regStageRepository).save(any());
        }
    }
}
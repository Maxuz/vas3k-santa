package dev.maxuz.vas3ksanta.model;

import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
@Entity(name = "reg_stage")
public class RegStageEntity {
    public enum Stage {
        STARTED,
        AUTH,

        FINAL,
        DECLINED,
        ERROR;

        public static Stage fromString(String s) {
            if (StringUtils.isBlank(s)) {
                return null;
            }
            for (Stage stage : values()) {
                if (stage.toString().equals(s)) {
                    return stage;
                }
            }
            throw new IllegalArgumentException("Unknown stage: " + s);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "reg_stage_id_seq")
    private Long id;

    @Column(name = "stage")
    private String stage;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public RegStageEntity() {
    }

    public RegStageEntity(Stage stage, UserEntity user) {
        this.stage = stage.toString();
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stage getStage() {
        return Stage.fromString(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage.toString();
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}

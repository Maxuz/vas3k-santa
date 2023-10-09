package dev.maxuz.vas3ksanta.model;

import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

@Entity(name = "reg_stage")
public class RegStage {
    public enum Stage {
        STARTED,
        AUTH,

        FINAL,
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
    @JoinColumn(name = "grandchild_id")
    private Grandchild grandchild;

    public RegStage() {
    }

    public RegStage(Stage stage, Grandchild grandchild) {
        this.stage = stage.toString();
        this.grandchild = grandchild;
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

    public Grandchild getGrandchild() {
        return grandchild;
    }

    public void setGrandchild(Grandchild grandchild) {
        this.grandchild = grandchild;
    }
}

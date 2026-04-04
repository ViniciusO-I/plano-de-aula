package br.com.fiap.skill_hub.repository.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_skills")
public class SkillEntity extends AuditDataEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String category;

    public SkillEntity() {}
}
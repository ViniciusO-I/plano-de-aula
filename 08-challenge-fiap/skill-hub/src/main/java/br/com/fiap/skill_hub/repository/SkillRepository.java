package br.com.fiap.skill_hub.repository;

import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<SkillEntity, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<SkillEntity> findByNameIgnoreCase(String name);
}
package br.com.fiap.skill_hub.repository;

import br.com.fiap.skill_hub.repository.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
}

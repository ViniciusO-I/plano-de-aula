package br.com.fiap.skill_hub.repository;

import br.com.fiap.skill_hub.repository.entities.GroupEntity;
import br.com.fiap.skill_hub.repository.entities.GroupMember;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByGroupAndUser(GroupEntity group, UserEntity user);

    int countByGroup(GroupEntity group);

    List<GroupMember> findByUser(UserEntity user);

    List<GroupMember> findByGroup(GroupEntity group);

    Optional<GroupMember> findByGroupAndUser(GroupEntity group, UserEntity user);
}
package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.GroupDto;
import br.com.fiap.skill_hub.repository.GroupMemberRepository;
import br.com.fiap.skill_hub.repository.GroupRepository;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.GroupEntity;
import br.com.fiap.skill_hub.repository.entities.GroupMember;
import br.com.fiap.skill_hub.repository.entities.MemberRole;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import br.com.fiap.skill_hub.response.GroupResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository,
                        GroupMemberRepository groupMemberRepository,
                        UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    // Cria um novo grupo — criador entra automaticamente como OWNER
    public GroupResponse create(String email, GroupDto dto) {
        UserEntity owner = findUserByEmail(email);

        GroupEntity group = new GroupEntity();
        group.setName(dto.name());
        group.setDescription(dto.description());
        group.setMaxNumbers(dto.maxMembers());
        group.setCreatedAt(LocalDateTime.now());
        group.setOwner(owner);

        GroupEntity saved = groupRepository.save(group);

        // Criador entra automaticamente como OWNER
        GroupMember member = new GroupMember();
        member.setGroup(saved);
        member.setUser(owner);
        member.setRole(MemberRole.OWNER);
        member.setJoinedAt(LocalDateTime.now());
        groupMemberRepository.save(member);

        return toResponse(saved);
    }

    // Lista todos os grupos
    public List<GroupResponse> listAll() {
        return groupRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca grupo por ID
    public GroupResponse findById(Long id) {
        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado: " + id));
        return toResponse(group);
    }

    // Lista grupos do usuário autenticado
    public List<GroupResponse> myGroups(String email) {
        UserEntity user = findUserByEmail(email);
        return groupMemberRepository.findByUser(user)
                .stream()
                .map(gm -> toResponse(gm.getGroup()))
                .toList();
    }

    // Entra em um grupo
    public GroupResponse join(String email, Long groupId) {
        UserEntity user = findUserByEmail(email);
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado: " + groupId));

        // Verifica se já é membro
        if (groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("Usuário já é membro deste grupo");
        }

        // Verifica se o grupo está cheio
        int currentMembers = groupMemberRepository.countByGroup(group);
        if (currentMembers >= group.getMaxNumbers()) {
            throw new RuntimeException("Grupo está cheio");
        }

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        member.setRole(MemberRole.MEMBER);
        member.setJoinedAt(LocalDateTime.now());
        groupMemberRepository.save(member);

        return toResponse(group);
    }

    // Sai de um grupo
    public void leave(String email, Long groupId) {
        UserEntity user = findUserByEmail(email);
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado: " + groupId));

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new RuntimeException("Usuário não é membro deste grupo"));

        // OWNER não pode sair sem excluir o grupo
        if (MemberRole.OWNER.equals(member.getRole())) {
            throw new RuntimeException("O dono não pode sair do grupo. Delete o grupo.");
        }

        groupMemberRepository.delete(member);
    }

    // Exclui o grupo — apenas OWNER
    public void delete(String email, Long groupId) {
        UserEntity user = findUserByEmail(email);
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado: " + groupId));

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new RuntimeException("Usuário não é membro deste grupo"));

        if (!MemberRole.OWNER.equals(member.getRole())) {
            throw new RuntimeException("Apenas o dono pode excluir o grupo");
        }

        groupMemberRepository.deleteAll(
                groupMemberRepository.findByGroup(group)
        );
        groupRepository.delete(group);
    }

    // Converte entidade para response
    private GroupResponse toResponse(GroupEntity group) {
        int currentMembers = groupMemberRepository.countByGroup(group);
        String ownerName = group.getOwner() != null ?
                group.getOwner().getName() : "Desconhecido";

        return new GroupResponse(
                group.getName(),
                group.getDescription(),
                group.getMaxNumbers(),
                group.getCreatedAt(),
                ownerName,
                currentMembers
        );
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
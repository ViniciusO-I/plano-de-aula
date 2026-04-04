package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.GroupDto;
import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.exception.group.*;
import br.com.fiap.skill_hub.exception.skill.SkillNotFoundException;
import br.com.fiap.skill_hub.exception.user.UserNotFoundException;
import br.com.fiap.skill_hub.mapper.GroupMapper;
import br.com.fiap.skill_hub.repository.GroupRepository;
import br.com.fiap.skill_hub.repository.SkillRepository;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.GroupEntity;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository,
                        UserRepository userRepository,
                        SkillRepository skillRepository,
                        GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.groupMapper = groupMapper;
    }

    public GroupDto create(GroupDto groupDto) {
        UserEntity owner = userRepository.findById(groupDto.ownerId())
                .orElseThrow(() -> new GroupOwnerNotFoundException("Owner não encontrado"));

        List<SkillEntity> requiredSkills = skillRepository.findAllById(extractSkillIds(groupDto.skills()));
        if (requiredSkills.size() != groupDto.skills().size()) {
            throw new SkillNotFoundException("Uma ou mais skills obrigatórias não foram encontradas");
        }

        GroupEntity group = new GroupEntity();
        group.setDescription(groupDto.description().trim());
        group.setMaxMembers(groupDto.maxMembers());
        group.setOwner(owner);
        group.setSkills(new HashSet<>(requiredSkills));
        group.getUsers().add(owner);

        GroupEntity saved = groupRepository.save(group);
        return groupMapper.toDto(saved);
    }

    public List<GroupDto> list() {
        List<GroupDto> groups = new ArrayList<>();
        for (GroupEntity groupEntity : groupRepository.findAll()) {
            groups.add(groupMapper.toDto(groupEntity));
        }
        return groups;
    }

    public GroupDto findById(Integer id) {
        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Grupo não encontrado"));
        return groupMapper.toDto(group);
    }

    public GroupDto update(Integer id, GroupDto groupDto) {
        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Grupo não encontrado"));

        UserEntity owner = userRepository.findById(groupDto.ownerId())
                .orElseThrow(() -> new GroupOwnerNotFoundException("Owner não encontrado"));

        List<SkillEntity> requiredSkills = skillRepository.findAllById(extractSkillIds(groupDto.skills()));
        if (requiredSkills.size() != groupDto.skills().size()) {
            throw new SkillNotFoundException("Uma ou mais skills obrigatórias não foram encontradas");
        }

        group.setDescription(groupDto.description().trim());
        group.setMaxMembers(groupDto.maxMembers());
        group.setOwner(owner);
        group.setSkills(new HashSet<>(requiredSkills));
        group.getUsers().add(owner);

        GroupEntity saved = groupRepository.save(group);
        return groupMapper.toDto(saved);
    }

    public void delete(Integer id) {
        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Grupo não encontrado"));
        groupRepository.delete(group);
    }

    public GroupDto join(Integer groupId, Integer userId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Grupo não encontrado"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (group.getUsers().stream().anyMatch(member -> member.getId().equals(userId))) {
            throw new UserAlreadyGroupMemberException("Usuário já é membro do grupo");
        }

        if (group.getUsers().size() >= group.getMaxMembers()) {
            throw new GroupNoAvailableSeatsException("Grupo sem vagas disponíveis");
        }

        if (!userHasAllRequiredSkills(user, group.getSkills())) {
            throw new UserLacksRequiredSkillsException("Usuário não possui as skills mínimas exigidas");
        }

        group.getUsers().add(user);
        GroupEntity saved = groupRepository.save(group);
        return groupMapper.toDto(saved);
    }

    private boolean userHasAllRequiredSkills(UserEntity user, Set<SkillEntity> requiredSkills) {
        Set<Integer> userSkillIds = new HashSet<>();
        for (SkillEntity skill : user.getSkills()) {
            userSkillIds.add(skill.getId());
        }

        for (SkillEntity requiredSkill : requiredSkills) {
            if (!userSkillIds.contains(requiredSkill.getId())) {
                return false;
            }
        }

        return true;
    }

    private List<Integer> extractSkillIds(List<SkillDto> skills) {
        List<Integer> skillIds = new ArrayList<>();
        for (SkillDto skill : skills) {
            skillIds.add(skill.id());
        }
        return skillIds;
    }

}


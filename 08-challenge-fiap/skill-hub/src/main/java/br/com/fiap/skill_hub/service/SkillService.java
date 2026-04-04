package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.exception.skill.SkillAlreadyRegisteredException;
import br.com.fiap.skill_hub.exception.skill.SkillDescriptionAlreadyInUseException;
import br.com.fiap.skill_hub.exception.skill.SkillNotFoundException;
import br.com.fiap.skill_hub.mapper.SkillMapper;
import br.com.fiap.skill_hub.repository.SkillRepository;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SkillService {

	private final SkillRepository skillRepository;
	private final SkillMapper skillMapper;

	public SkillService(SkillRepository skillRepository, SkillMapper skillMapper) {
		this.skillRepository = skillRepository;
		this.skillMapper = skillMapper;
	}

	public SkillDto create(SkillDto skillDto) {
		skillRepository.findByDescriptionIgnoreCase(skillDto.description())
				.ifPresent(skill -> {
					throw new SkillAlreadyRegisteredException("Skill já cadastrada");
				});

		SkillEntity skillEntity = new SkillEntity();
		skillEntity.setDescription(skillDto.description().trim());
		SkillEntity saved = skillRepository.save(skillEntity);
		return skillMapper.toDto(saved);
	}

	public List<SkillDto> list() {
		List<SkillDto> skills = new ArrayList<>();
		for (SkillEntity skill : skillRepository.findAll()) {
			skills.add(skillMapper.toDto(skill));
		}
		return skills;
	}

	public SkillDto findById(Integer id) {
		SkillEntity skill = skillRepository.findById(id)
				.orElseThrow(() -> new SkillNotFoundException("Skill não encontrada"));
		return skillMapper.toDto(skill);
	}

	public SkillDto update(Integer id, SkillDto skillDto) {
		SkillEntity skill = skillRepository.findById(id)
				.orElseThrow(() -> new SkillNotFoundException("Skill não encontrada"));

		skillRepository.findByDescriptionIgnoreCase(skillDto.description())
				.filter(existing -> !existing.getId().equals(id))
				.ifPresent(existing -> {
					throw new SkillDescriptionAlreadyInUseException("Descrição de skill já em uso");
				});

		skill.setDescription(skillDto.description().trim());
		SkillEntity saved = skillRepository.save(skill);
		return skillMapper.toDto(saved);
	}

	public void delete(Integer id) {
		SkillEntity skill = skillRepository.findById(id)
				.orElseThrow(() -> new SkillNotFoundException("Skill não encontrada"));
		skillRepository.delete(skill);
	}

}

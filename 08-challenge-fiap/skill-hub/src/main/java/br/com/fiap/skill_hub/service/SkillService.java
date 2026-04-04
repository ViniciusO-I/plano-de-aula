package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.SkillDto;
import br.com.fiap.skill_hub.repository.SkillRepository;
import br.com.fiap.skill_hub.repository.entities.SkillEntity;
import br.com.fiap.skill_hub.response.SkillResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    // Cria uma nova skill — só ADMINISTRATOR pode fazer isso
    public SkillResponse create(SkillDto dto) {

        // Verifica se já existe skill com esse nome
        if (skillRepository.existsByNameIgnoreCase(dto.name())) {
            throw new RuntimeException("Skill já cadastrada: " + dto.name());
        }

        SkillEntity entity = new SkillEntity();
        entity.setName(dto.name());
        entity.setCategory(dto.category());

        SkillEntity saved = skillRepository.save(entity);
        return toResponse(saved);
    }

    // Lista todas as skills disponíveis
    public List<SkillResponse> listAll() {
        return skillRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca skill por ID
    public SkillResponse findById(Long id) {
        SkillEntity entity = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill não encontrada: " + id));
        return toResponse(entity);
    }

    // Converte entidade para response
    private SkillResponse toResponse(SkillEntity entity) {
        return new SkillResponse(entity.getId(), entity.getName(), entity.getCategory());
    }
}
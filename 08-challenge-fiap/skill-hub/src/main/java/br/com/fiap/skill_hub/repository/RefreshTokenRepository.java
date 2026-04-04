package br.com.fiap.skill_hub.repository;

import br.com.fiap.skill_hub.repository.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    Optional<RefreshTokenEntity> findByJtiAndRevokedFalse(String jti);
    List<RefreshTokenEntity> findAllByUserIdAndRevokedFalse(Integer userId);
}


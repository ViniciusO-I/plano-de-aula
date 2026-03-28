package br.com.fiap.skill_hub.repository.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class UserEntity extends AuditDataEntity implements Serializable {
    private String name;
    private String email;
    private String password;


}

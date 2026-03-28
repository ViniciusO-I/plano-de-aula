package br.com.fiap.skill_hub.repository.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public class AuditDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dtCreated;
    private Date dtUpdated;
    private StatusEnum status;


    public AuditDataEntity() {
        this.status = StatusEnum.ACTIVE;
        this.dtCreated = new Date();
    }
}

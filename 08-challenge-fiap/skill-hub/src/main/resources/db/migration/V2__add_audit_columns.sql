-- Adiciona colunas de auditoria na tabela de skills
ALTER TABLE tb_skills ADD COLUMN dt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tb_skills ADD COLUMN dt_updated TIMESTAMP;
ALTER TABLE tb_skills ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';

-- Adiciona colunas de auditoria na tabela de grupos
ALTER TABLE tb_groups ADD COLUMN dt_created_audit TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tb_groups ADD COLUMN dt_updated TIMESTAMP;
ALTER TABLE tb_groups ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';
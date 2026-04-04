-- Corrige nome da coluna de auditoria na tabela de grupos
ALTER TABLE tb_groups DROP COLUMN IF EXISTS dt_created_audit;
ALTER TABLE tb_groups ADD COLUMN IF NOT EXISTS dt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tb_groups ADD COLUMN IF NOT EXISTS dt_updated TIMESTAMP;
ALTER TABLE tb_groups ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE';

-- Garante que tb_group_members também tem as colunas de auditoria
ALTER TABLE tb_group_members ADD COLUMN IF NOT EXISTS dt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tb_group_members ADD COLUMN IF NOT EXISTS dt_updated TIMESTAMP;
ALTER TABLE tb_group_members ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE';
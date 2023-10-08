-- -----------------------------------------------------
-- Banco bdNetminders
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS bdNetminders;
USE bdNetminders;

-- -----------------------------------------------------
-- Tabela `empresa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS empresa
(
    id           INT          NOT NULL,
    nomeFantasia VARCHAR(100) NULL,
    razaoSocial  VARCHAR(100) NULL,
    CNPJ         VARCHAR(18)  NULL,
    CEP          VARCHAR(10)  NULL,
    telefone     VARCHAR(20)  NULL,
    emailEmpresa VARCHAR(255) NULL,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabela `endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS endereco
(
    id          INT         NOT NULL,
    fkEmpresa   INT         NOT NULL,
    isFilial    TINYINT,
    CEP         VARCHAR(10) NULL,
    numero      INT         NULL,
    complemento VARCHAR(45),
    PRIMARY KEY (id, fkEmpresa),
    INDEX fk_usuario_empresa_idx (fkEmpresa ASC),
    CONSTRAINT fk_endereco_empresa
        FOREIGN KEY (fkEmpresa)
            REFERENCES empresa (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabela `usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario
(
    id           INT          NOT NULL,
    fkEmpresa    INT          NOT NULL,
    nome  VARCHAR(45),
    email VARCHAR(255) NOT NULL,
    senha        VARCHAR(45)  NOT NULL,
    isAdmin      TINYINT      NOT NULL,
    PRIMARY KEY (id, fkEmpresa),
    CONSTRAINT fk_usuario_empresa
        FOREIGN KEY (fkEmpresa)
            REFERENCES empresa (id)
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabela `dispositivo`
-- -----------------------------------------------------
drop table log;
drop table dispositivo;
CREATE TABLE IF NOT EXISTS dispositivo
(
    id         INT      NOT NULL AUTO_INCREMENT,
    endMAC     CHAR(17) NOT NULL,
    fkEmpresa INT NOT NULL,
    titulo     VARCHAR(45),
    descricao  VARCHAR(255),
    isOn       TINYINT,
    hasCPU     TINYINT,
    hasRAM     TINYINT,
    hasDisk    TINYINT,
    hasNetwork TINYINT,
    delayInMs INT,
    CONSTRAINT fkEmpresa
        FOREIGN KEY (fkEmpresa)
            REFERENCES empresa (id),
    PRIMARY KEY (id)
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabela `log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS log
(
    id                   INT AUTO_INCREMENT,
    fkDispositivo        INT      NOT NULL,
    dataHora             DATETIME NOT NULL,
    uso                  SMALLINT NOT NULL,
    componenteMonitorado VARCHAR(7)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fkDispositivo
        FOREIGN KEY (fkDispositivo)
            REFERENCES dispositivo (id)
)
    ENGINE = InnoDB;
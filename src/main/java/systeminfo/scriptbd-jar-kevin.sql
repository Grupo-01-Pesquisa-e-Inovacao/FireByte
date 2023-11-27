CREATE DATABASE jar_individual_kevin;
use jar_individual_kevin;
CREATE TABLE informacoes_sistema (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sistema_operacional VARCHAR(100),
    arquitetura VARCHAR(50),
    versao VARCHAR(50),
    total_memoria_ram BIGINT,
    livre_memoria_ram BIGINT,
    max_memoria_ram BIGINT,
    informacoes_discos TEXT
);
select * from informacoes_sistema;
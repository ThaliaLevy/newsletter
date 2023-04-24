CREATE TABLE tb_etiquetas (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(255) NOT NULL);

INSERT INTO tb_etiquetas (nome)
VALUES
   ('Tecnologia'),
   ('Musica'),
   ('Teatro');
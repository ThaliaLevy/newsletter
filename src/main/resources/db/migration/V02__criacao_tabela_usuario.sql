CREATE TABLE tb_usuario (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE KEY,
senha VARCHAR(255) NOT NULL,
perfil_id BIGINT NOT NULL,
FOREIGN KEY (perfil_id) REFERENCES tb_perfil(id));

INSERT INTO tb_usuario (nome, email, senha, perfil_id)
VALUES
   ('Admin', 'admin@teste', '$2a$10$Ueu8/ocgoGeoljwf6QA2IuXn5wWIggBYYgqbkanieZ5.vlf8VmnZa', '1'),
   ('Guilherme', 'guilherme@teste', '$2a$10$Ueu8/ocgoGeoljwf6QA2IuXn5wWIggBYYgqbkanieZ5.vlf8VmnZa', '1'),
   ('Samara', 'samara@teste', '$2a$10$Ueu8/ocgoGeoljwf6QA2IuXn5wWIggBYYgqbkanieZ5.vlf8VmnZa', '2'),
   ('Leandro', 'leandro@teste', '$2a$10$Ueu8/ocgoGeoljwf6QA2IuXn5wWIggBYYgqbkanieZ5.vlf8VmnZa', '2');
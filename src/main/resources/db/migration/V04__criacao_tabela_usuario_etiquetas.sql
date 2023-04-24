CREATE TABLE tb_usuario_etiquetas (
usuario_id BIGINT NOT NULL,
etiqueta_id BIGINT NOT NULL,
FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id),
FOREIGN KEY (etiqueta_id) REFERENCES tb_etiquetas(id));

INSERT INTO tb_usuario_etiquetas (usuario_id, etiqueta_id)
VALUES
   ('3', '2'),
   ('3', '1'),
   ('3', '3'),
   ('4', '3'),
   ('4', '1');

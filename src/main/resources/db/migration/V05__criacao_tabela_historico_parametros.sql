CREATE TABLE tb_historico_parametros (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
etiqueta varchar(255) NOT NULL,
data varchar(255) NOT NULL,
id_usuario varchar(255));

INSERT INTO tb_historico_parametros (etiqueta, data, id_usuario)
VALUES
   ('teatro', '10/11/2022', '3'),
   ('tecnologia', '10/11/2022', '3'),
   ('tecnologia', '09/11/2022', '4'),
   ('musica', '22/09/2022', '3'),
   ('teatro', '13/10/2022', '4');
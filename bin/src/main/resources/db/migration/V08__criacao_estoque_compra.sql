CREATE TABLE tb_estoque(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
produto_id BIGINT NOT NULL,
filial_id BIGINT NOT NULL,
quantidade INT NOT NULL,
FOREIGN KEY (produto_id) REFERENCES tb_produto(id),
FOREIGN KEY (filial_id) REFERENCES tb_filial(id)
);

CREATE TABLE tb_compra(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
filial_id BIGINT NOT NULL,
fornecedor_id BIGINT NOT NULL,
FOREIGN KEY (filial_id) REFERENCES tb_filial(id),
FOREIGN KEY (fornecedor_id) REFERENCES tb_fornecedor(id)
);

CREATE TABLE tb_item_compra(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
compra_id BIGINT,
produto_id BIGINT NOT NULL,
quantidade INT NOT NULL,
valor DECIMAL(19,2) NOT NULL,
FOREIGN KEY (produto_id) REFERENCES tb_produto(id),
FOREIGN KEY (compra_id) REFERENCES tb_compra(id)
);
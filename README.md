<h2>Newsletter</h2>
O projeto tem como objetivo principal o envio de notícias do dia de forma personalizada, de acordo com etiquetas (assuntos) que são cadastradas pelo próprio usuário. A lista de notícias é enviada via e-mail. <br>

<h5>Tecnologias utilizadas:</h5>
- Java 17; <br>
- Maven; <br>
- Spring boot 2.7.5; <br>
- Spring Web; <br>
- Spring DevTools; <br>
- Spring Data JPA; <br>
- Spring Security; <br>
- Spring Webflux; <br>
- Spring Mail; <br>
- Flyway; <br>
- MySQL; <br>
- Swagger 2.9.2; <br>
- JWT 3.18.1; <br>
- Flyway. <br>

<h5>Funcionalidades:</h5>
- Cadastro de usuário informando se possui perfil de administrador ou não, realizado somente por perfil administrador; <br>
- Cadastro de etiqueta por usuário, realizado somente pelo próprio usuário sem administrador; <br>
- Consumo de api externa, de onde serão retornadas as listagens de notícias (https://apinoticias.tedk.com.br/); <br>
- Busca de listagem de notícias ao informar etiqueta relacionada ao assunto que deseja visualizar, realizada somente por usuários sem administrador; <br>
- Histórico de parâmetros (etiqueta e data) acessados no dia atual, que pode ser visualizado pelo próprio usuário que fez os acessos ou um perfil administrador que visualiza os acessos de todos os usuários; <br>
- Histórico de etiquetas mais acessadas independente da data, que pode ser visualizado somente por perfil administrador; <br>
- Banco de dados é populado no ato de criação das tabelas. <br>
- JWT para gerar token; <br>
- Swagger para documentar os endpoints da API; <br>
- Projeto no Postman para documentar os endpoints da API; <br>
- Envio de e-mail com confirmação de sucesso no ato de cadastro de um usuário; <br>
- Envio de e-mail contendo todas as notícias da data atual, de acordo com as etiquetas dos usuários, para cada usuário, realizado por um perfil administrador; <br>
- Busca de listagem de notícias ao informar data da notícia e etiqueta relacionada ao assunto que deseja visualizar, realizada somente por usuários sem administrador; <br>
- Exceptions com retorno de status http personalizados de acordo com o erro, dentro da classe customizada handler; <br>
- Uso de DTOs para facilitar a transferência de dados de uma camada para outra; <br>
- Spring Security para permitir restringir acessos. <br>

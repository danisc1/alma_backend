ALMA - Backend
Backend da aplicação ALMA (Apoio no Levantamento de Mudanças Afetivas), plataforma para registro e acompanhamento do humor e bem-estar dos pacientes, facilitando o trabalho de psicólogos.

Tecnologias
Java 17+

Spring Boot

Spring Security (JWT para autenticação)

PostgreSQL

Maven (gerenciador de dependências)

JPA/Hibernate (mapeamento objeto-relacional)

Flyway (versionamento do banco)

Docker (opcional para containerizar)

Funcionalidades principais
Cadastro de usuários (pacientes e psicólogos) com validação

Login com autenticação JWT e autorização por perfil

CRUD de registros de humor (avaliação e anotações diárias)

Recuperação de senha via token por e-mail

Upload e exibição de foto de perfil

Exclusão completa de conta com remoção dos dados relacionados

Auditoria básica de alterações (via triggers no banco)

API RESTful estruturada para integração com frontend

Requisitos
Java 17 ou superior instalado

PostgreSQL 14+ rodando localmente ou remotamente

Maven 3.8+

Docker (opcional)

Configuração
Clone este repositório:

bash
Copiar
Editar
git clone https://github.com/seuusuario/alma-backend.git
cd alma-backend
Configure o banco de dados PostgreSQL:

Crie o banco alma_bd

Configure as credenciais no arquivo src/main/resources/application.properties:

properties
Copiar
Editar
spring.datasource.url=jdbc:postgresql://localhost:5432/alma_bd
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
Execute as migrações do banco com Flyway (automaticamente no start do app).

Como rodar
Via Maven:

bash
Copiar
Editar
mvn spring-boot:run
Ou compile e execute o JAR:

bash
Copiar
Editar
mvn clean package
java -jar target/alma-backend-0.0.1-SNAPSHOT.jar
Endpoints principais
Método	Endpoint	Descrição
POST	/api/auth/register	Cadastro de usuário
POST	/api/auth/login	Login e retorno do token JWT
POST	/api/auth/forgot-password	Solicitação de redefinição de senha
POST	/api/auth/reset-password	Redefinição de senha com token
GET	/api/usuarios/me	Dados do usuário autenticado
PUT	/api/usuarios/me	Atualizar dados do usuário
DELETE	/api/usuarios/me	Excluir conta do usuário
POST	/api/humor	Criar registro de humor
GET	/api/humor	Listar registros do usuário


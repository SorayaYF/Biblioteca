# 📚 Biblioteca

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-0079BF?style=for-the-badge&logo=java&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-9C84C7?style=for-the-badge&logo=hibernate&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit&logoColor=white)

**Biblioteca** é uma aplicação Java para gerenciamento de biblioteca, utilizando o Spring Framework para uma estrutura robusta e escalável. O projeto é integrado com PostgreSQL, utiliza JWT para segurança e JPA/Hibernate para o mapeamento objeto-relacional. Inclui testes unitários para garantir a qualidade do código.

---

## 📚 Descrição

O **Biblioteca** permite:

- **Gerenciar Livros**: Adicionar, atualizar, consultar e remover livros.
- **Gerenciar Usuários**: Cadastrar e autenticar usuários.
- **Segurança**: Utiliza JWT para autenticação e autorização segura.

---

## 🚀 Como Executar

Para executar o **Biblioteca** em sua máquina, siga os passos abaixo:

1. **Clone o repositório:**

    ```bash
    git clone https://github.com/SeuUsuario/Biblioteca.git
    cd Biblioteca
    ```

2. **Configure o banco de dados PostgreSQL:**

   - Crie um banco de dados PostgreSQL para a aplicação.
   - Atualize as configurações de conexão no arquivo `src/main/resources/application.properties`:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    ```

3. **Compile e execute a aplicação:**

    ```bash
    ./mvnw spring-boot:run
    ```

## 🧪 Executando Testes

Para executar os testes unitários, use o seguinte comando:

```bash
./mvnw test

# Projeto de Gerenciamento de Boards de Tarefas 🚀

## Sobre o projeto 💡

### Contexto 📋

Este projeto tem como objetivo criar um sistema para gerenciamento de boards de tarefas. O usuário poderá adicionar boards, incluir colunas nesses boards, criar cards para as colunas e movimentá-los entre elas. Além disso, será possível bloquear e desbloquear cards.

### Objetivo 📚

O propósito do projeto é praticar, explorar e aprender conceitos e boas práticas de persistência de dados em bancos de dados relacionais.

### Tecnologias Utilizadas 🖥️

- **Java** com **Gradle** como gerenciador de dependências
- Banco de dados **MySQL**
- **Liquibase** para versionamento e migração de banco de dados
- **Lombok** para reduzir código boilerplate

### Modelo de Dados 📊
<img src="/assets/DomainModel.png" width="100%">


---

## Funcionalidades
- Criar um board
- Criar colunas
- Criar cards com títulos e descrições
- Mover cards para coluna posterior
- Bloquear e desbloquear cards

## Como Utilizar 💻

### Iniciar o Projeto na IDE 🏗️

1. Clone o repositório do projeto:
   ```shell
   git clone https://github.com/AlissonLimaG/DecolaTech-Desafio-2.git
   ```
2. Acesse o diretório do projeto:
   ```
   cd nome-do-projeto
   ```
3. Crie um banco de dados MySQL com o nome "decola_board" ou qual preferir



4. Coloque o username e a senha da sua conexão com o MySQL no arquivo `connection.properties`
   ```properties
   DB_URL=jdbc:mysql://localhost:3306/nome_do_seu_banco
   DB_USERNAME=seu_username
   DB_PASSWORD=sua_senha
   ```
5. Execute a classe principal "Main"

#### *Obs: O banco foi populado com um pequeno conjunto de dados para facilitar o teste das funcionalidades.*



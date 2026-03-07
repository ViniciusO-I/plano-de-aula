# FIAP Connect – Descrição do Projeto

## Contexto do Projeto

O projeto FIAP Connect foi desenvolvido com o objetivo de criar uma plataforma que facilite a conexão entre estudantes com diferentes habilidades técnicas para a formação de grupos de trabalho em projetos acadêmicos.

Em cursos da área de tecnologia, é comum que atividades e desafios práticos sejam realizados em grupo. No entanto, muitas vezes a formação dessas equipes acontece de maneira improvisada, sem considerar as competências técnicas de cada integrante. Isso pode gerar grupos com habilidades semelhantes ou até mesmo ausência de conhecimentos importantes para o desenvolvimento do projeto.

Diante desse cenário, o FIAP Connect surge como uma solução que permite organizar melhor a colaboração entre estudantes, promovendo a formação de equipes mais equilibradas e produtivas.

---

## Objetivo do Sistema

O objetivo principal do sistema é permitir que estudantes cadastrem suas habilidades técnicas e possam se conectar com outros usuários que possuam competências complementares para o desenvolvimento de projetos.

A proposta da aplicação é incentivar a colaboração entre alunos, facilitar a criação de grupos de trabalho e promover o compartilhamento de conhecimentos dentro da comunidade acadêmica.

---

## Funcionamento do Sistema

O sistema funciona como uma API REST responsável por gerenciar usuários, habilidades (skills) e grupos de projeto.

Inicialmente o usuário realiza seu cadastro informando nome, e-mail e senha. Após o cadastro, o usuário pode registrar suas habilidades técnicas no sistema.

Exemplos de habilidades que podem ser cadastradas:

- Java  
- Spring Boot  
- React  
- SQL  
- DevOps  
- UX/UI  

Essas habilidades ficam associadas ao perfil do usuário e ajudam na formação de grupos com competências complementares.

Além disso, o sistema permite a criação de grupos de projeto, onde usuários podem se organizar para desenvolver atividades acadêmicas.

---

## Arquitetura da Aplicação

O projeto foi desenvolvido utilizando Java com o framework Spring Boot e segue uma arquitetura em camadas composta por:

- Controller – responsável pelos endpoints da API REST  
- Service – responsável pelas regras de negócio  
- Repository – responsável pela comunicação com o banco de dados  
- DTO – objetos de transferência de dados  
- Security – responsável pela autenticação utilizando JWT  

---

## Tecnologias Utilizadas

- Java 17  
- Spring Boot  
- Spring Security  
- JWT (JSON Web Token)  
- Spring Data JPA  
- H2 Database para ambiente de desenvolvimento  
- Oracle Database  
- Maven  
- Swagger / OpenAPI  

---

## Resultado Esperado

Ao final do desenvolvimento, o sistema deverá permitir:

- cadastro de usuários  
- autenticação segura  
- registro de habilidades técnicas  
- associação de skills aos usuários  
- criação de grupos de projeto  
- gerenciamento de membros em grupos  

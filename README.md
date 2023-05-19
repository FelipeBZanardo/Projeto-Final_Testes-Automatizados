# Projeto Final Testes Automatizados I - API de Livros

> *Turma 938 - Santander Coders - Trilha Web FullStack*

API (Application Programming Interface) desenvolvida com o intuito de Criar, Ler, Atualizar e Deletar um Livro conforme instruções passadas no enunciado do projeto.

## ✒️Autores 
- [Felipe Zanardo](https://github.com/FelipeBZanardo)
- [Ramon Carvalho](https://github.com/rcarvalho-pb)

## 📋Enunciado do Projeto

Implementar uma API básica(CRUD) com as camadas de Repository, Service e Controller de um cadastro de livros, utilizando um banco de dados em memória.

## 📝 Requisitos Funcionais
1 - É necessário cadastrar um novo livro no sistema;
2 - Todo livro deve ter os seguintes atributos abaixo:
	-   Um título;
	-   Um resumo do que vai ser encontrado no livro;
	-   Um sumário de tamanho livre;
	-   Preço do livro;
	-   Número de páginas;
	-   Isbn(identificador do livro);
	-   Data que ele deve entrar no ar(de publicação).
	
3 - Deve ser possível realizar a atualização dos dados de um livro cadastrado no sistema;
4 - Deve ser possível buscar um livro pelo id;
5 - Deve possível listar todos os livros;
6 - Deve ser possível excluir um livro do cadastro.

## 🔒Restrições

1 - Título é obrigatório;
2 - Resumo é obrigatório e tem no máximo 500 caracteres;
3 - O sumário é de tamanho livre;
4 - Preço é obrigatório e o mínimo é de 20;
5 - Número de páginas é obrigatória e o mínimo é de 100;
6 - Isbn é obrigatório, formato livre;
7 - Data que vai entrar no ar precisa ser no futuro;

## 🔐 Requisitos não funcionais obrigatórios

1 - A aplicação precisa ter cobertura mínima de 80% do código da API;

2 - Realizar pelo menos 2 testes de integração, com um banco em memória a sua escolha.

## 🔓 Requisitos Opcionais

1 - Criar uma interface simples para todos os end-points da API e realizar testes de interface com Selenium.

## 🌎 URI principal da API

`http://localhost:8080/rest/livros`

## 📌 Endpoints da API REST
Para realizar testes com requisições HTTP, faça o download do arquivo do Insomnia presente no projeto e siga as orientações a seguir.

### 📗 Criar Livro

[POST] http://localhost:8080/rest/livros
```
Exemplo Request:
{
	"titulo": "Título",
	"resumo": "Resumo",
	"sumario": "Sumário",
	"preco": "20.00",
	"numeroPaginas": 100,
	"isbn": "8532530788",
	"dataPublicacao": "2023-08-19"	
}
```
```
Exemplo Response:
HTTPStatus = 201
{	
	"id": "5b8654fa-3898-420a-8773-9c010bfa80d3",
	"titulo": "Título",
	"resumo": "Resumo",
	"sumario": "Sumário",
	"preco": "20.00",
	"numeroPaginas": 100,
	"isbn": "8532530788",
	"dataPublicacao": "2023-08-19"	
}
```

### 📙 Buscar Livro por Id

[GET] http://localhost:8080/rest/livros/{id}
🚨Id do tipo UUID

```
Exemplo:
http://localhost:8080/livros/5b8654fa-3898-420a-8773-9c010bfa80d3
```
```
Exemplo Response:
HTTPStatus = 200
{	
	"id": "5b8654fa-3898-420a-8773-9c010bfa80d3",
	"titulo": "Título",
	"resumo": "Resumo",
	"sumario": "Sumário",
	"preco": "20.00",
	"numeroPaginas": 100,
	"isbn": "8532530788",
	"dataPublicacao": "2023-08-19"	
}
```

###  📚 Buscar Todos os Livros

[GET] http://localhost:8080/rest/livros

```
HTTPStatus = 200
Exemplo Response:
[
	{	
		"id": "5b8654fa-3898-420a-8773-9c010bfa80d3",
		"titulo": "Título",
		"resumo": "Resumo",
		"sumario": "Sumário",
		"preco": "20.00",
		"numeroPaginas": 100,
		"isbn": "8532530788",
		"dataPublicacao": "2023-08-19"	
	},
	{	
		"id": "892aedda-c18d-4d22-a83e-b1616c5e74da",
		"titulo": "Título 2",
		"resumo": "Resumo 2",
		"sumario": "Sumário 2",
		"preco": "30.00",
		"numeroPaginas": 200,
		"isbn": "1234567810",
		"dataPublicacao": "2023-06-25"	
	},
]
```
### 📘 Atualizar Livro

[PUT] http://localhost:8080/rest/livros/{id}
🚨Id do tipo UUID

```
Exemplo:
http://localhost:8080/livros/5b8654fa-3898-420a-8773-9c010bfa80d3
```
```
Exemplo Request:
HTTPStatus = 200
{
	"titulo": "Título Modificado",
	"resumo": "Resumo",
	"sumario": "Sumário Modificado",
	"preco": "20.00",
	"numeroPaginas": 100,
	"isbn": "8532530788",
	"dataPublicacao": "2023-08-19"	
}
```
```
Exemplo Response:
{	
	"id": "5b8654fa-3898-420a-8773-9c010bfa80d3",
	"titulo": "Título Modificado",
	"resumo": "Resumo",
	"sumario": "Sumário Modificado",
	"preco": "20.00",
	"numeroPaginas": 100,
	"isbn": "8532530788",
	"dataPublicacao": "2023-08-19"	
}
```

### 📕 Remover Livro

[DELETE] http://localhost:8080/rest/livros/{id}
🚨Id do tipo UUID

```
Exemplo:
http://localhost:8080/livros/5b8654fa-3898-420a-8773-9c010bfa80d3
```
```
Exemplo Response:
HTTPStatus = 200

Livro cujo Id - 5b8654fa-3898-420a-8773-9c010bfa80d3 - foi deletado com sucesso!
```
## 🖥️ Páginas para realização de teste com o Selenium

Para realização dos Testes com Selenium, basta "rodar" a aplicação principal e posteriormente "rodar" SeleniumTest presente na API.

### Tela Principal / Listar Livros:
http://localhost:8080/livros

![Tela Listar Livros](https://github.com/FelipeBZanardo/Projeto-Final_Testes-Automatizados/blob/master/livros-api/src/main/resources/static/tela-principal.png)

### Tela Cadastro / Tela Editar:
http://localhost:8080/livros/cadastrar
http://localhost:8080/livros/editar/{id}

![Tela Cadastro/Editar](https://github.com/FelipeBZanardo/Projeto-Final_Testes-Automatizados/blob/master/livros-api/src/main/resources/static/tela-cadastro-edicao.png)

### Resultado dos Testes
🚨**Foi excluído a Classe Main na cobertura dos testes!**

![Cobertura-Testes](https://github.com/FelipeBZanardo/Projeto-Final_Testes-Automatizados/blob/master/livros-api/src/main/resources/static/cobertura-testes.png)

## 🛠️ Tecnologias Utilizadas

* [IntelliJ IDEA](https://www.jetbrains.com/pt-br/idea/) - IDE
* [Spring Initializer](https://start.spring.io/)
* [Maven](https://maven.apache.org/) - Gerenciador de Dependência
* [H2 Database](https://www.h2database.com/html/main.html) - Banco de Dados em memória
* [JUnit 5](https://junit.org/junit5/) - Testes unitários
* [Mockito](https://site.mockito.org/) - Testes unitários, Testes de Integração
* [Selenium](https://www.selenium.dev/pt-br/) - Testes de aceitação

## 📈 Melhorias futuras

- Implementar uma API reativa utilizando WebFlux;
- Fazer o deploy do projeto para estar disponível para qualquer usuário;
- Implementação de segurança utilizando Spring Security;
- Fazer comunicação com outras API's externas;
- Melhorar a qualidade das páginas do site.


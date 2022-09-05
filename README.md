# Movie Battle

  API Criada para teste da empresa Ada - let's code.
  Crie uma API REST para uma aplicação ao estilo card game, onde serão informados dois
  filmes e o jogador deve acertar aquele que possui melhor avaliação no IMDB.
  
  REQUISITOS:
  1. O jogador deve fazer login para iniciar uma nova partida. Portanto, cada partida sempre será identificada pela autenticação do usuário.
      a. Não há restrições onde armazenar os usuários: em memória, ou em banco, etc.
  2. Cada rodada do jogo consiste em informar um par de filmes, observando para não repetir o mesmo par nem formar um par com um único filme.
      a. São sequências não-válidas: [A-A] o mesmo filme repetido; [A-B, A-B] pares repetidos – considere iguais os pares do tipo A-B e B-A.
      b. Os seguintes pares são válidos: [A-B, B-C] o filme B é usado em pares diferentes.
  3. O jogador deve tentar acertar qual filme possui maior pontuação, composta pela nota (0.0-10.0) multiplicado pelo total de votos.
  4. Se escolher o vencedor correto, conta 1 ponto. São permitidos até três erros. Após responder, terá acesso a novo par de filmes quando acessar o endpoint do quiz.
  5. Forneça endpoints específicos para iniciar e encerrar a partida a qualquer momento. Valide o momento em que cada funcionalidade pode ser acionada.
  6. Não deve ser possível avançar para o próximo par sem responder o atual.
  7. Deve existir uma funcionalidade de ranking, exibindo os melhores jogadores e suas pontuações.
  8. A pontuação é obtida multiplicando a quantidade de quizzes respondidos pela porcentagem de acerto.

# Tecnologias e Frameworks
  - Springboot
  - Java 11
  - Swagger
  - Junit
  - Mockito
  - H2
  - Resfull
  - Spring Security
  - JWT
  - Lombok
  - Spring data JPA
  - Querydsl
  - hateoas
  - FeignClient

  * Requisitos para rodar Localmente
    - Primeiro faça clone do projeto no Github
    - Tenha Java 11 ou superior instalado
    - Caso não tenha, instale o plug-in do LOMBOK em sua IDE
    - Starte a aplicação
    - Digite no seu navegador localhost:8080/ada/swagger-ui/index.html
    - Ou Teste via Postman importando a collection que está na raiz do projeto Movies Battle - ADA.postman_collection.json

  Ao Iniciar o projeto o execute o endpoint para gerar alguns dados de teste
  ```html
  curl --location --request GET 'http://localhost:8080/ada/init/create-data'
  ```
  
  
  Antes de utilizar os endpoins do game você precisa gerar um token fazendo login por exemplo: 
  ```html
    curl --location --request POST 'localhost:8080/ada/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login":"player1",
    "password":1234
}'
  ```
  
  E após isso pegar o token no header do response na key Authorization, que vai conter algo como:
  ```html
  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwbGF5ZXIxIiwiZXhwIjoxNjYyMzkwNzgzfQ.CmrsG3Nu4qyQXcGRVzDWGr4EyS15tba-TnI07ryRaB4
   ```
  Com o token o Token você pode fazer requisições que precisam de autenticação. como por exemplo:
   ```html
  curl --location --request POST 'localhost:8080/ada/battle/fight' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwbGF5ZXIxIiwiZXhwIjoxNjYyMzkwNzgzfQ.CmrsG3Nu4qyQXcGRVzDWGr4EyS15tba-TnI07ryRaB4'
 ```
 E as escolhas do filme passando o ID do filme
 ```html
 curl --location --request PATCH 'localhost:8080/ada/battle/choose/1' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwbGF5ZXIxIiwiZXhwIjoxNjYyMzkwNzgzfQ.CmrsG3Nu4qyQXcGRVzDWGr4EyS15tba-TnI07ryRaB4'
  ```




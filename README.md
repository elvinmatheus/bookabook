# book-a-book

- Elvin Matheus Souda da Silva - 497516
- Renan de Castro Silva Cordeiro - 495354
- Victor Pereira de Araújo - 493722

## Instruções

Compilação: `javac -encoding utf8 ./framework/App.java`

Execução: `java framework/App`

Limpeza (Windows): `del /S *.class`

Limpeza (Ubuntu): `find . -name '*.class' -type f -delete`

## IMPORTANTE: Dados pré-cadastrados

Já pré-cadastramos alguns dados para fins de teste.
Isso é bem importante, pois a maioria das páginas só são acessíveis por meio de um login.
Use as contas de usuário e de admin descritas a seguir.

- Um usuário comum de matrícula 0 e senha `aaaaaa`.
- Um admin de matrícula 1 e senha `bbbbbb`.
- 6 livros são pré-cadastrados
- Algumas reservas são pré-cadastradas para o usuário de mátricula 0

## Organização

Pastas

- controller: Classes associadas ao controller. A pasta commands contém vários comandos, objetos que podem ser invocados. A pasta handlers contém várias classes que implementam ActionListener e portanto conseguem responder a eventos. RefreshID define várias constantes que representam alterações de estado do modelo, permitindo às views observadoras saber que tipo de alteração ocorreu e reagir apropriadamente.
- framework: Classes que definem o framework utilizado (MVC, Páginas...). App é a classe principal (contém main).
- helpers: Classes ajudantes que simplificam operações frequentemente utilizadas (como Margin), ou que são úteis para o próprio desenvolvimento do projeto (loggers).
- images: Contém imagens utilizadas pelo projeto (como a logo da UFC).
- model: Define classes responsáveis por lidar com a lógica interna da aplicação.
- view: Define classes responsáveis pela geração de componentes gráficos. Na pasta pages há classes que geram as páginas da aplicação. Na pasta components estão definidos componentes reusáveis.
 
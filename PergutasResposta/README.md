# Jogo de Perguntas e Respostas - História (70s, 80s, 90s)

## 📋 Descrição
Um jogo educativo de múltipla escolha com perguntas sobre história dos anos 70, 80 e 90. As perguntas e respostas são armazenadas em um banco de dados **(SQLite ou PostgreSQL)**.

## 📦 Requisitos
- Java 8 ou superior
- **SQLite** OU **PostgreSQL** (escolha uma das opções)

## 🗂️ Estrutura do Projeto

```
PerguntasResposta/
├── PerguntaResposta.java       # Classe principal (ponto de entrada)
├── JogoPerguntas.java           # Lógica do jogo
├── Pergunta.java                # Modelo de pergunta
├── GerenciadorBD.java           # Gerencimento do banco de dados
├── setup_perguntas.sql          # Script SQL para criar tabelas
├── SETUP_POSTGRESQL.md          # Guia de configuração PostgreSQL
├── README.md                    # Este arquivo
└── perguntas.db                 # Banco SQLite (criado automaticamente)
```

## 🚀 Opção 1: Usando SQLite (Mais Simples)

### Passo 1: Baixar o Driver SQLite
Baixe `sqlite-jdbc-3.44.0.0.jar` de: https://central.sonatype.com/search?q=sqlite-jdbc

Coloque na mesma pasta do projeto.

### Passo 2: Compilar
```bash
# Windows
javac -cp ".;sqlite-jdbc-3.44.0.0.jar" *.java

# Linux/Mac
javac -cp ".:sqlite-jdbc-3.44.0.0.jar" *.java
```

### Passo 3: Executar
```bash
# Windows
java -cp ".;sqlite-jdbc-3.44.0.0.jar" PerguntaResposta

# Linux/Mac
java -cp ".:sqlite-jdbc-3.44.0.0.jar" PerguntaResposta
```

**Pronto! O banco será criado automaticamente! ✅**

---

## 🚀 Opção 2: Usando PostgreSQL (Recomendado)

### ⚠️ Pré-requisitos
- PostgreSQL instalado e rodando no PC
- Senha do usuário `postgres`

### Passo 1: Criar o Banco de Dados

**Via pgAdmin (GUI):**
1. Abra pgAdmin
2. Clique em **Servers** → **PostgreSQL**
3. Clique direito em **Databases** → **Create** → **Database**
4. Nome: `perguntas`
5. Clique em **Save**

**Via Linha de Comando:**
```bash
psql -U postgres -h localhost
CREATE DATABASE perguntas;
\q
```

### Passo 2: Configurar Credenciais

Edite `GerenciadorBD.java` e altere a senha do PostgreSQL:
```java
private static final String USUARIO = "postgres";
private static final String SENHA = "sua_senha_aqui"; // ← Altere aqui com sua senha
```

### Passo 3: Inserir Perguntas no Banco

**Via pgAdmin (Query Tool):**
1. Clique em **Databases** → **perguntas** → **Query Tool**
2. Copie e cole o conteúdo de `setup_perguntas.sql`
3. Clique em **Execute** (F5)

**Via Linha de Comando:**
```bash
psql -U postgres -d perguntas -f setup_perguntas.sql
```

### Passo 4: Baixar o Driver PostgreSQL

Baixe `postgresql-42.7.3.jar` de: https://jdbc.postgresql.org/download/

Coloque na mesma pasta do projeto.

### Passo 5: Compilar e Executar

```bash
# Windows
javac -cp ".;postgresql-42.7.3.jar" *.java
java -cp ".;postgresql-42.7.3.jar" PerguntaResposta

# Linux/Mac
javac -cp ".:postgresql-42.7.3.jar" *.java
java -cp ".:postgresql-42.7.3.jar" PerguntaResposta
```

📚 **Consulte `SETUP_POSTGRESQL.md` para mais detalhes!**

---

## 🎮 Como Jogar

1. Execute o programa com um dos comandos acima
2. Escolha uma das opções:
   - **Opção 1**: Jogar com TODAS as perguntas
   - **Opção 2**: Apenas perguntas dos anos 70
   - **Opção 3**: Apenas perguntas dos anos 80
   - **Opção 4**: Apenas perguntas dos anos 90

3. Para cada pergunta, digite:
   - **A** para a opção A
   - **B** para a opção B
   - **C** para a opção C
   - **D** para a opção D

4. Veja seu resultado final e percentual de acertos

## 📝 Funcionalidades

✅ Suporte para **SQLite** e **PostgreSQL**
✅ 15 perguntas iniciais (5 de cada década)
✅ Filtragem por década
✅ Embaralhamento aleatório de perguntas
✅ Pontuação em tempo real
✅ Resultado final com percentual
✅ Avaliação de desempenho
✅ Opção de jogar novamente
✅ Interface amigável e intuitiva

## 🤖 Adicionando Novas Perguntas

### Com SQLite
1. Delete o arquivo `perguntas.db` (se existir)
2. Edite o método `inserirPerguntasIniciais()` em `GerenciadorBD.java`
3. Execute o programa novamente

### Com PostgreSQL
Execute no pgAdmin ou terminal:
```sql
INSERT INTO perguntas (enunciado, opcaoA, opcaoB, opcaoC, opcaoD, respostaCorreta, decada) 
VALUES ('Pergunta aqui', 'Opção A', 'Opção B', 'Opção C', 'Opção D (correta)', 'D', 1990);
```

---

## 📊 Critérios de Desempenho

- **90-100%**: ⭐ EXCELENTE! Você é um especialista!
- **70-89%**: 🌟 BOM! Muito bom conhecimento!
- **50-69%**: 👍 RAZOÁVEL! Continue estudando!
- **< 50%**: 📚 PRECISA ESTUDAR! Tente novamente!

---

## 🔧 Troubleshooting

### ❌ "Driver não encontrado!"
- Certifique-se que o arquivo `.jar` está na mesma pasta do projeto
- Verifique se a classpath está correta

### ❌ "Conexão recusada" (PostgreSQL)
- Verifique se PostgreSQL está rodando (Windows: Services → PostgreSQL → Start)
- Confirme usuario e senha em `GerenciadorBD.java`
- Banco de dados `perguntas` foi criado?

### ❌ "Banco de dados vazio" (SQLite)
- Delete `perguntas.db` e execute novamente

### ❌ "FATAL: password authentication failed"
- Altere a senha em `GerenciadorBD.java` para a correta

### ❌ "FATAL: database 'perguntas' does not exist"
- Crie o banco de dados seguindo o Passo 1 da Opção 2

---

## 📚 Tópicos das Perguntas

### Anos 70
- Fim da Guerra Fria
- The Beatles se separa
- Revolução Verde
- Catástrofes aéreas
- Presidentes dos EUA

### Anos 80
- Gorbachev e a URSS
- Cinema e prêmios
- Invasões militares
- Consoles de videogame
- Desastres nucleares

### Anos 90
- Fim oficial da Guerra Fria
- Apartheid na África do Sul
- Terrorismo internacional
- Acidentes industriais
- Fenômenos do cinema

---

**Divirta-se aprendendo história! 🎓**

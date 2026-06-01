# Jogo de Perguntas - Versão SEM Banco de Dados

## 📋 Descrição
Um jogo educativo de múltipla escolha com perguntas sobre história dos anos 70, 80 e 90. **Sem necessidade de banco de dados!**

## 📦 Requisitos
- Java 8 ou superior
- ✅ Nenhuma dependência externa!

## 🗂️ Estrutura do Projeto

```
PerguntasResposta/
├── JogoSimples.java           # Classe principal do jogo
├── PerguntaSimples.java       # Modelo de pergunta
├── RepositorioPerguntas.java  # Armazena as perguntas em memória
└── README_SIMPLES.md          # Este arquivo
```

## 🚀 Como Usar (Super Simples!)

### Passo 1: Compilar
```bash
javac *.java
```

### Passo 2: Executar
```bash
java JogoSimples
```

**Pronto! Nenhum driver, nenhuma configuração!** 🎮

## 🎮 Como Jogar

1. Execute o programa
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

✅ 15 perguntas sobre história (anos 70, 80 e 90)
✅ Filtragem por década
✅ Embaralhamento aleatório de perguntas
✅ Pontuação em tempo real
✅ Resultado final com percentual
✅ Avaliação de desempenho
✅ Opção de jogar novamente
✅ **ZERO dependências externas!**

## 📊 Critérios de Desempenho

- **90-100%**: ⭐ EXCELENTE! Você é um especialista!
- **70-89%**: 🌟 BOM! Muito bom conhecimento!
- **50-69%**: 👍 RAZOÁVEL! Continue estudando!
- **< 50%**: 📚 PRECISA ESTUDAR! Tente novamente!

## 🤖 Adicionando Novas Perguntas

Edite `RepositorioPerguntas.java` e adicione novas perguntas assim:

```java
perguntas.add(new PerguntaSimples(16,
    "Sua pergunta aqui?",
    "Opção A", "Opção B", "Opção C", "Opção D (correta)",
    'D', 1990));
```

A estrutura é:
- **ID**: Número único
- **Enunciado**: A pergunta
- **Opções**: A, B, C, D
- **Resposta correta**: A letra da resposta (A, B, C ou D)
- **Década**: 1970, 1980 ou 1990

## 📚 Perguntas Incluídas

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

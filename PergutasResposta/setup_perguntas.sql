-- Script SQL para criar banco de dados e tabela no PostgreSQL
-- Execute este script no pgAdmin ou via linha de comando

-- Criar banco de dados (execute primeiro como superuser)
-- CREATE DATABASE perguntas;

-- Criar tabela de perguntas
CREATE TABLE IF NOT EXISTS perguntas (
    id SERIAL PRIMARY KEY,
    enunciado TEXT NOT NULL,
    opcaoA TEXT NOT NULL,
    opcaoB TEXT NOT NULL,
    opcaoC TEXT NOT NULL,
    opcaoD TEXT NOT NULL,
    respostaCorreta VARCHAR(1) NOT NULL,
    decada INTEGER NOT NULL
);

-- Limpar dados existentes (CUIDADO!)
--DELETE FROM perguntas;

-- Inserir perguntas dos anos 70
INSERT INTO perguntas (enunciado, opcaoA, opcaoB, opcaoC, opcaoD, respostaCorreta, decada) VALUES
('Em que ano Pelé conquistou sua 3ª Copa do Mundo com o Brasil?', '1970', '1962', '1958', '1978', 'A', 1970),
('Qual foi o programa de TV mais assistido no Brasil nos anos 70?', 'Televisão de Sábado à Noite', 'Globo de Ouro', 'Plantão das Notícias', 'Estúdio Aberto', 'A', 1970),
('Em qual ano o Brasil sediou a Copa do Mundo?', '1950', '1970', '1978', '1994', 'B', 1970),
('Qual time ganhou o Campeonato Carioca em 1970?', 'Flamengo', 'Vasco da Gama', 'Fluminense', 'Botafogo', 'A', 1970),
('Qual filme brasileiro foi lançado em 1970 com grande sucesso?', 'Macunaíma', 'São Bernardo', 'Dona Flor e Seus Dois Maridos', 'Pixote', 'A', 1970);

-- Inserir perguntas dos anos 80
INSERT INTO perguntas (enunciado, opcaoA, opcaoB, opcaoC, opcaoD, respostaCorreta, decada) VALUES
('Em que ano Zé Maria da Paraíba conquistou o Campeonato Carioca?', '1985', '1980', '1988', '1982', 'A', 1980),
('Qual filme brasileiro foi sucesso em 1985?', 'Pixote', 'Dona Flor e Seus Dois Maridos', 'O Cortiço', 'Grande Sertão Veredas', 'B', 1980),
('Em qual ano Sócrates liderou a Democracia Corinthiana?', '1983', '1980', '1985', '1987', 'A', 1980),
('Qual programa de TV marcou gerações de crianças brasileiras nos anos 80?', 'Sessão da Tarde', 'Estúdio Aberto', 'Praça da Alegria', 'TV Globinha', 'B', 1980),
('Qual time paulista ganhou a Libertadores em 1982?', 'Corinthians', 'Palmeiras', 'São Paulo', 'Santos', 'C', 1980);

-- Inserir perguntas dos anos 90
INSERT INTO perguntas (enunciado, opcaoA, opcaoB, opcaoC, opcaoD, respostaCorreta, decada) VALUES
('Em que ano o Brasil conquistou a 4ª Copa do Mundo?', '1994', '1990', '1998', '2002', 'A', 1990),
('Qual foi o maior programa de auditório do Brasil nos anos 90?', 'Domingão do Faustão', 'Planeta TV', 'Circo do Criatiano', 'Show da Gravidade', 'A', 1990),
('Em qual ano Ayrton Senna faleceu em um acidente?', '1994', '1991', '1992', '1995', 'A', 1990),
('Qual filme brasileiro foi um fenômeno em 1995?', 'Cidade de Deus', 'Carlota Joaquina', 'Isadora Duncan', 'O Cortiço', 'B', 1990),
('Em que ano Flamengo conquistou sua última Libertadores?', '1981', '1990', '1988', '1995', 'A', 1990);

-- Verificar inserção
SELECT COUNT(*) as total_perguntas FROM perguntas;
SELECT COUNT(*) as perguntas_70 FROM perguntas WHERE decada = 1970;
SELECT COUNT(*) as perguntas_80 FROM perguntas WHERE decada = 1980;
SELECT COUNT(*) as perguntas_90 FROM perguntas WHERE decada = 1990;

-- Visualizar todas as perguntas
SELECT * FROM perguntas ORDER BY decada, id;

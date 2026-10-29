# Triple Triad

Jogo para dois participantes, implementado em Java 21 e executado exclusivamente
no terminal.

## Compilar e executar

No PowerShell, a partir da raiz do repositório:

```powershell
Set-Location game
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
java -cp out Main
```

No início, responda `s` ou `n` para ativar cada regra adicional. Durante a
partida, escolha uma carta pelo número e indique a linha e a coluna de uma
célula vazia, usando posições de 0 a 2. Sem All Open, o terminal pede que o
jogador pressione ENTER antes de exibir sua mão.

## Regras básicas

- Dois jogadores recebem cinco cartas cada; cada carta tem quatro valores de
  lado, de 0 a 10, com 10 exibido como `A`.
- Em turnos alternados, cada jogador coloca uma carta em uma célula vazia do
  tabuleiro 3×3.
- Uma carta adversária adjacente é capturada quando o lado correspondente da
  carta recém-colocada tem valor maior.
- A partida termina quando as nove células estão preenchidas. Vence quem
  controlar mais cartas no tabuleiro.
- Cada jogador começa com cinco pontos. Capturar uma carta concede um ponto ao
  capturador e remove um ponto do dono anterior; a soma permanece 10.

## Regras adicionais

As quatro regras podem ser ativadas ou desativadas individualmente no início:

- **All Open:** exibe as mãos de ambos os jogadores em cada turno.
- **Swap:** troca uma carta aleatória entre as mãos no início da partida e
  informa quais cartas foram trocadas.
- **Plus:** quando duas ou mais somas de lados adjacentes são iguais, captura
  os vizinhos dessas somas; a regra pode capturar mesmo quando a comparação
  normal não venceria.
- **Sudden Death:** em caso de empate no controle do tabuleiro, inicia uma nova
  rodada com as cartas controladas por cada jogador como suas novas mãos e
  limpa o tabuleiro. O primeiro jogador alterna entre rodadas.

O documento da disciplina não define um limite de rodadas para Sudden Death.
Os pontos são acumulados entre rodadas e sua soma permanece 10; por isso, após
o descarte das cartas que ficaram nas mãos no fim de uma rodada, a pontuação
acumulada não representa apenas as cartas da rodada atual.

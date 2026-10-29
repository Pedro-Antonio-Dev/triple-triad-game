package ui;

import board.Board;
import board.Position;
import model.Card;
import model.Direction;
import model.Player;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;

public class TerminalUI {

    private final Scanner scanner = new Scanner(System.in);

    public Optional<Boolean> solicitarAtivacaoRegra(String nomeRegra) {
        while (true) {
            Optional<String> entrada = lerLinha(
                    "Ativar " + nomeRegra + "? (s/n): "
            );
            if (entrada.isEmpty()) {
                return Optional.empty();
            }

            String resposta = entrada.get().strip().toLowerCase(Locale.ROOT);
            if (resposta.equals("s")) {
                return Optional.of(true);
            }
            if (resposta.equals("n")) {
                return Optional.of(false);
            }
            exibirMensagem("Resposta inválida. Digite s ou n.");
        }
    }

    public boolean aguardarConfirmacaoDeTurno(Player jogador) {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
        while (true) {
            Optional<String> entrada = lerLinha(
                    jogador.getNome() + ", pressione ENTER para ver sua mão: "
            );
            if (entrada.isEmpty()) {
                return false;
            }
            if (entrada.get().isEmpty()) {
                return true;
            }
            exibirMensagem("Pressione ENTER para continuar.");
        }
    }

    public void exibirTitulo() {
        exibirMensagem("==================================");
        exibirMensagem("          TRIPLE TRIAD");
        exibirMensagem("==================================");
    }

    public void exibirEstadoJogo(
            Player jogadorAtual,
            Player oponente,
            Board tabuleiro,
            boolean mostrarMaos
    ) {
        exibirMensagem("\nVez de: " + jogadorAtual.getNome());
        exibirTabuleiro(tabuleiro);
        if (mostrarMaos) {
            exibirMao(jogadorAtual);
            exibirMao(oponente);
        } else {
            exibirMao(jogadorAtual);
        }
    }

    public void exibirMao(Player jogador) {
        exibirMensagem("\nMão de " + jogador.getNome() + ":");
        List<Card> mao = jogador.getMao();
        for (int i = 0; i < mao.size(); i++) {
            exibirMensagem((i + 1) + " - " + formatarCarta(mao.get(i)));
        }
        if (mao.isEmpty()) {
            exibirMensagem("(sem cartas)");
        }
    }

    public Optional<Card> solicitarCarta(Player jogador) {
        if (jogador.getMao().isEmpty()) {
            exibirMensagem("Este jogador não possui cartas disponíveis.");
            return Optional.empty();
        }

        while (true) {
            OptionalInt escolha = solicitarInteiro(
                    "Escolha uma carta pelo número: ",
                    1,
                    jogador.getMao().size()
            );
            if (escolha.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(jogador.getMao().get(escolha.getAsInt() - 1));
        }
    }

    public Optional<Position> solicitarPosicao(Board tabuleiro) {
        while (true) {
            OptionalInt linha = solicitarInteiro("Linha (0-2): ", 0, 2);
            if (linha.isEmpty()) {
                return Optional.empty();
            }
            OptionalInt coluna = solicitarInteiro("Coluna (0-2): ", 0, 2);
            if (coluna.isEmpty()) {
                return Optional.empty();
            }
            if (tabuleiro.isPosicaoVazia(
                    linha.getAsInt(), coluna.getAsInt()
            )) {
                return Optional.of(new Position(
                        linha.getAsInt(), coluna.getAsInt()
                ));
            }
            exibirMensagem(
                    "Essa posição está ocupada. Escolha uma célula vazia."
            );
        }
    }

    public void exibirJogada(Player jogador, Card carta, Position posicao) {
        exibirMensagem(
                jogador.getNome() + " colocou " + carta.getNome()
                        + " em " + posicao + "."
        );
    }

    public void exibirCartaCapturada(
            Player capturador,
            Card carta,
            Player donoAnterior
    ) {
        exibirMensagem(
                capturador.getNome() + " capturou " + carta.getNome()
                        + " de " + donoAnterior.getNome() + "."
        );
    }

    public void exibirTabuleiro(Board tabuleiro) {
        exibirMensagem("\nTabuleiro:");
        exibirMensagem(tabuleiro.imprimirTabuleiro());
    }

    public void exibirPontuacao(Player jogador1, Player jogador2) {
        exibirMensagem(
                "Pontuação: " + jogador1.getNome() + " "
                        + jogador1.getPontos() + " x "
                        + jogador2.getPontos() + " "
                        + jogador2.getNome()
        );
        if (jogador1.getPontos() + jogador2.getPontos() != 10) {
            throw new IllegalStateException(
                    "A soma dos pontos dos jogadores deve permanecer 10."
            );
        }
    }

    public void exibirInicioNovaRodada(
            int rodada,
            Player jogadorInicial,
            String nomeRegra
    ) {
        exibirMensagem(
                "\nEmpate: regra " + nomeRegra
                        + " iniciou a rodada " + rodada + "."
        );
        exibirMensagem(
                jogadorInicial.getNome() + " começa esta rodada."
        );
    }

    public void exibirResultado(
            Player jogador1,
            Player jogador2,
            Board tabuleiro
    ) {
        int cartasNoTabuleiro1 = tabuleiro.contarCartasJogador(jogador1);
        int cartasNoTabuleiro2 = tabuleiro.contarCartasJogador(jogador2);
        int cartasControladas1 =
                cartasNoTabuleiro1 + jogador1.getMao().size();
        int cartasControladas2 =
                cartasNoTabuleiro2 + jogador2.getMao().size();

        exibirMensagem("\n========== FIM DE JOGO ==========");
        exibirTabuleiro(tabuleiro);
        exibirMensagem(
                jogador1.getNome() + ": " + cartasNoTabuleiro1
                        + " cartas no tabuleiro, "
                        + cartasControladas1 + " controladas no total."
        );
        exibirMensagem(
                jogador2.getNome() + ": " + cartasNoTabuleiro2
                        + " cartas no tabuleiro, "
                        + cartasControladas2 + " controladas no total."
        );
        exibirPontuacao(jogador1, jogador2);

        if (cartasNoTabuleiro1 > cartasNoTabuleiro2) {
            exibirMensagem("Vencedor: " + jogador1.getNome());
        } else if (cartasNoTabuleiro2 > cartasNoTabuleiro1) {
            exibirMensagem("Vencedor: " + jogador2.getNome());
        } else {
            exibirMensagem("A partida terminou empatada!");
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    private String formatarCarta(Card carta) {
        return carta.getNome()
                + " | T:" + Card.formatarValor(carta.getValor(Direction.TOP))
                + " E:" + Card.formatarValor(carta.getValor(Direction.LEFT))
                + " D:" + Card.formatarValor(carta.getValor(Direction.RIGHT))
                + " B:" + Card.formatarValor(carta.getValor(Direction.BOTTOM));
    }

    private OptionalInt solicitarInteiro(
            String prompt,
            int minimo,
            int maximo
    ) {
        while (true) {
            Optional<String> entrada = lerLinha(prompt);
            if (entrada.isEmpty()) {
                return OptionalInt.empty();
            }

            int valor;
            try {
                valor = Integer.parseInt(entrada.get().strip());
            } catch (NumberFormatException excecao) {
                exibirMensagem("Entrada inválida. Digite um número inteiro.");
                continue;
            }

            if (valor < minimo || valor > maximo) {
                exibirMensagem(
                        "Valor fora do intervalo. Digite um número entre "
                                + minimo + " e " + maximo + "."
                );
                continue;
            }
            return OptionalInt.of(valor);
        }
    }

    private Optional<String> lerLinha(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            exibirMensagem("\nEntrada encerrada. A partida será interrompida.");
            return Optional.empty();
        }
        return Optional.of(scanner.nextLine());
    }
}

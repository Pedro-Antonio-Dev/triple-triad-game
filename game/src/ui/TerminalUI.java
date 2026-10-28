package ui;

import board.Board;
import board.Position;
import model.Card;
import model.Direction;
import model.Player;

import java.util.List;
import java.util.Scanner;

public class TerminalUI {

    private final Scanner scanner;

    public TerminalUI() {
        this.scanner = new Scanner(System.in);
    }

    public void exibirTitulo() {
        System.out.println("==================================");
        System.out.println("          TRIPLE TRIAD");
        System.out.println("==================================");
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void exibirTabuleiro(Board tabuleiro) {
        System.out.println("\nTabuleiro:");
        System.out.println(tabuleiro.imprimirTabuleiro());
    }

    public void exibirJogadorAtual(Player jogador) {
        System.out.println("\nVez de: " + jogador.getNome());
    }

    public void exibirMao(Player jogador) {
        List<Card> mao = jogador.getMao();

        System.out.println("\nCartas disponíveis:");

        for (int i = 0; i < mao.size(); i++) {
            Card carta = mao.get(i);

            System.out.printf(
                    "%d - %s | TOP: %d | RIGHT: %d"
                            + " | BOTTOM: %d | LEFT: %d%n",
                    i + 1,
                    carta.getNome(),
                    carta.getValor(Direction.TOP),
                    carta.getValor(Direction.RIGHT),
                    carta.getValor(Direction.BOTTOM),
                    carta.getValor(Direction.LEFT)
            );
        }
    }

    public Card solicitarCarta(Player jogador) {
        List<Card> mao = jogador.getMao();

        if (mao.isEmpty()) {
            exibirMensagem("Não existem cartas disponíveis.");
            return null;
        }

        exibirMao(jogador);

        while (true) {
            System.out.print("Escolha o número da carta: ");
            int escolha = lerInteiro();

            if (escolha >= 1 && escolha <= mao.size()) {
                return mao.get(escolha - 1);
            }

            exibirMensagem("Número inválido. Tente novamente.");
        }
    }

    public Position solicitarPosicao() {
        while (true) {
            System.out.print("Linha (0-2): ");
            int linha = lerInteiro();

            System.out.print("Coluna (0-2): ");
            int coluna = lerInteiro();

            if (linha >= 0 && linha <= 2
                    && coluna >= 0 && coluna <= 2) {
                return new Position(linha, coluna);
            }

            exibirMensagem(
                    "Posição inválida. Informe valores entre 0 e 2."
            );
        }
    }

    public void exibirResultado(
            Player jogador1,
            Player jogador2,
            Board tabuleiro
    ) {
        int cartas1 = tabuleiro.contarCartasJogador(jogador1);
        int cartas2 = tabuleiro.contarCartasJogador(jogador2);

        exibirMensagem("\n========== FIM DE JOGO ==========");
        exibirTabuleiro(tabuleiro);

        exibirMensagem(
                jogador1.getNome() + ": " + cartas1 + " cartas"
        );

        exibirMensagem(
                jogador2.getNome() + ": " + cartas2 + " cartas"
        );

        if (cartas1 > cartas2) {
            exibirMensagem("Vencedor: " + jogador1.getNome());
        } else if (cartas2 > cartas1) {
            exibirMensagem("Vencedor: " + jogador2.getNome());
        } else {
            exibirMensagem("A partida terminou empatada!");
        }
    }

    private int lerInteiro() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Digite um número inteiro válido: ");
        }

        return scanner.nextInt();
    }
}


package game;

import board.Board;
import board.Cell;
import model.Card;
import model.Direction;
import model.Player;
import ui.TerminalUI;

import java.util.List;
import java.util.Scanner;

public class GameController {

    private final Game game;
    private final Scanner scanner;

    private Player jogadorAtual;
    private boolean partidaEncerrada;

    public GameController(Game game, TerminalUI ui) {
        if (game == null) {
            throw new IllegalArgumentException(
                    "O jogo não pode ser nulo."
            );
        }

        this.game = game;
        this.scanner = new Scanner(System.in);
        this.partidaEncerrada = false;
    }

    public void iniciarPartida() {
        game.distribuirCartas();

        jogadorAtual = game.getJogador1();

        System.out.println("================================");
        System.out.println("         TRIPLE TRIAD");
        System.out.println("================================");

        while (!partidaEncerrada) {
            executarTurno();

            if (verificarFimDePartida()) {
                partidaEncerrada = true;
            }
        }

        finalizarPartida();
    }

    public void executarTurno() {
        if (partidaEncerrada) {
            return;
        }

        exibirEstadoJogo();

        Card carta = selecionarCarta();

        if (carta == null) {
            return;
        }

        int linha;
        int coluna;

        while (true) {
            System.out.print("Linha (0-2): ");
            linha = scanner.nextInt();

            System.out.print("Coluna (0-2): ");
            coluna = scanner.nextInt();

            if (validarJogada(carta, linha, coluna)) {
                break;
            }

            System.out.println(
                    "Posição inválida ou ocupada. Tente novamente."
            );
        }

        realizarJogada(carta, linha, coluna);

        if (!verificarFimDePartida()) {
            alternarJogador();
        }
    }

    private void exibirEstadoJogo() {
        System.out.println("\n--------------------------------");
        System.out.println(
                "Vez de: " + jogadorAtual.getNome()
        );

        System.out.println("\nTabuleiro:");
        System.out.println(
                game.getTabuleiro().imprimirTabuleiro()
        );

        System.out.println("Sua mão:");
        exibirMao();
    }

    private void exibirMao() {
        List<Card> mao = jogadorAtual.getMao();

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

    private Card selecionarCarta() {
        List<Card> mao = jogadorAtual.getMao();

        if (mao.isEmpty()) {
            System.out.println(
                    "Este jogador não possui cartas disponíveis."
            );
            return null;
        }

        while (true) {
            System.out.print("Escolha uma carta pelo número: ");

            int escolha = scanner.nextInt();

            if (escolha >= 1 && escolha <= mao.size()) {
                return mao.get(escolha - 1);
            }

            System.out.println(
                    "Número inválido. Escolha novamente."
            );
        }
    }

    public boolean validarJogada(
            Card carta,
            int linha,
            int coluna
    ) {
        if (carta == null || jogadorAtual == null) {
            return false;
        }

        if (!jogadorAtual.possuiCarta(carta)) {
            return false;
        }

        Board tabuleiro = game.getTabuleiro();

        return tabuleiro.isPosicaoValida(linha, coluna)
                && tabuleiro.isPosicaoVazia(linha, coluna);
    }

    private void realizarJogada(
            Card carta,
            int linha,
            int coluna
    ) {
        Board tabuleiro = game.getTabuleiro();

        carta.setDono(jogadorAtual);

        tabuleiro.colocarCarta(linha, coluna, carta);

        jogadorAtual.removerCarta(carta);

        System.out.println(
                jogadorAtual.getNome()
                        + " colocou a carta "
                        + carta.getNome()
                        + " na posição ("
                        + linha + ", " + coluna + ")."
        );

        aplicarCapturas(carta, linha, coluna);

        System.out.println("\nTabuleiro após a jogada:");
        System.out.println(tabuleiro.imprimirTabuleiro());
    }

    private void aplicarCapturas(
            Card carta,
            int linha,
            int coluna
    ) {
        Board tabuleiro = game.getTabuleiro();

        compararVizinho(
                carta, linha - 1, coluna,
                Direction.TOP, Direction.BOTTOM
        );

        compararVizinho(
                carta, linha + 1, coluna,
                Direction.BOTTOM, Direction.TOP
        );

        compararVizinho(
                carta, linha, coluna - 1,
                Direction.LEFT, Direction.RIGHT
        );

        compararVizinho(
                carta, linha, coluna + 1,
                Direction.RIGHT, Direction.LEFT
        );
    }

    private void compararVizinho(
            Card cartaJogada,
            int linhaVizinho,
            int colunaVizinho,
            Direction direcaoJogada,
            Direction direcaoVizinho
    ) {
        Board tabuleiro = game.getTabuleiro();

        if (!tabuleiro.isPosicaoValida(
                linhaVizinho, colunaVizinho
        )) {
            return;
        }

        Cell celula = tabuleiro.getCell(
                linhaVizinho, colunaVizinho
        );

        Card cartaVizinha = celula.getCarta();

        if (cartaVizinha == null) {
            return;
        }

        if (cartaVizinha.getDono() == null) {
            return;
        }

        if (cartaVizinha.getDono().equals(jogadorAtual)) {
            return;
        }

        int valorJogada = cartaJogada.getValor(direcaoJogada);
        int valorVizinha = cartaVizinha.getValor(direcaoVizinho);

        System.out.printf(
                "Comparando %s (%d) contra %s (%d).%n",
                direcaoJogada,
                valorJogada,
                direcaoVizinho,
                valorVizinha
        );

        if (valorJogada > valorVizinha) {
            cartaVizinha.setDono(jogadorAtual);

            jogadorAtual.ganharPonto();

            cartaVizinha.getDono().ganharPonto();

            System.out.println(
                    "Carta capturada: "
                            + cartaVizinha.getNome()
            );
        }
    }

    private void alternarJogador() {
        if (jogadorAtual.equals(game.getJogador1())) {
            jogadorAtual = game.getJogador2();
        } else {
            jogadorAtual = game.getJogador1();
        }
    }

    public boolean verificarFimDePartida() {
        return game.getTabuleiro().todasAsCelulasPreenchidas();
    }

    public void finalizarPartida() {
        Board tabuleiro = game.getTabuleiro();

        Player jogador1 = game.getJogador1();
        Player jogador2 = game.getJogador2();

        int cartasJogador1 =
                tabuleiro.contarCartasJogador(jogador1);

        int cartasJogador2 =
                tabuleiro.contarCartasJogador(jogador2);

        System.out.println("\n================================");
        System.out.println("          FIM DE JOGO");
        System.out.println("================================");

        System.out.println(tabuleiro.imprimirTabuleiro());

        System.out.println(
                jogador1.getNome()
                        + " | Cartas: " + cartasJogador1
                        + " | Pontos: " + jogador1.getPontos()
        );

        System.out.println(
                jogador2.getNome()
                        + " | Cartas: " + cartasJogador2
                        + " | Pontos: " + jogador2.getPontos()
        );

        if (cartasJogador1 > cartasJogador2) {
            System.out.println(
                    "Vencedor: " + jogador1.getNome()
            );
        } else if (cartasJogador2 > cartasJogador1) {
            System.out.println(
                    "Vencedor: " + jogador2.getNome()
            );
        } else {
            System.out.println("A partida terminou empatada!");
        }
    }

    public Player getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean isPartidaEncerrada() {
        return partidaEncerrada;
    }
}

package game;

import board.Board;
import board.Position;
import model.Card;
import model.Direction;
import model.Player;
import rules.GameRule;
import ui.TerminalUI;

import java.util.List;
import java.util.Optional;

public class GameController {

    private final Game game;
    private final TerminalUI ui;

    private Player jogadorAtual;
    private boolean partidaEncerrada;
    private String regraDaNovaRodada;

    public GameController(Game game, TerminalUI ui) {
        if (game == null) {
            throw new IllegalArgumentException("O jogo não pode ser nulo.");
        }
        if (ui == null) {
            throw new IllegalArgumentException("A interface não pode ser nula.");
        }

        this.game = game;
        this.ui = ui;
        this.partidaEncerrada = false;
    }

    public void iniciarPartida() {
        game.distribuirCartas();
        jogadorAtual = game.getJogador1();
        game.setEstado(GameState.EM_ANDAMENTO);
        ui.exibirTitulo();

        for (GameRule regra : game.getRegrasAtivas()) {
            regra.aoIniciarPartida(game).forEach(ui::exibirMensagem);
        }
        ui.exibirPontuacao(game.getJogador1(), game.getJogador2());

        int rodada = 1;
        while (!partidaEncerrada) {
            while (!verificarFimDePartida()) {
                if (!executarTurno()) {
                    partidaEncerrada = true;
                    return;
                }
            }

            if (!iniciarNovaRodadaSeNecessario()) {
                finalizarPartida();
                return;
            }

            rodada++;
            jogadorAtual = jogadorAtual == game.getJogador1()
                    ? game.getJogador2()
                    : game.getJogador1();
            ui.exibirInicioNovaRodada(
                    rodada, jogadorAtual, regraDaNovaRodada
            );
        }
    }

    private boolean executarTurno() {
        Player oponente = obterOponente(jogadorAtual);
        boolean mostrarMaos = game.getRegrasAtivas().stream()
                .anyMatch(regra ->
                        regra.podeExibirMaoDoOponente(game, jogadorAtual));

        if (!mostrarMaos && !ui.aguardarConfirmacaoDeTurno(jogadorAtual)) {
            return false;
        }

        ui.exibirEstadoJogo(
                jogadorAtual,
                oponente,
                game.getTabuleiro(),
                mostrarMaos
        );

        Optional<Card> cartaEscolhida = ui.solicitarCarta(jogadorAtual);
        if (cartaEscolhida.isEmpty()) {
            return false;
        }

        Optional<Position> posicaoEscolhida =
                ui.solicitarPosicao(game.getTabuleiro());
        if (posicaoEscolhida.isEmpty()) {
            return false;
        }

        Card carta = cartaEscolhida.get();
        Position posicao = posicaoEscolhida.get();
        realizarJogada(carta, posicao);

        if (!verificarFimDePartida()) {
            jogadorAtual = oponente;
        }
        return true;
    }

    public boolean validarJogada(Card carta, int linha, int coluna) {
        return carta != null
                && jogadorAtual != null
                && jogadorAtual.possuiCarta(carta)
                && game.getTabuleiro().isPosicaoVazia(linha, coluna);
    }

    private void realizarJogada(Card carta, Position posicao) {
        Board tabuleiro = game.getTabuleiro();
        if (!validarJogada(carta, posicao.getLinha(), posicao.getColuna())) {
            throw new IllegalArgumentException("Jogada inválida.");
        }

        carta.setDono(jogadorAtual);
        tabuleiro.colocarCarta(posicao.getLinha(), posicao.getColuna(), carta);
        jogadorAtual.removerCarta(carta);
        ui.exibirJogada(jogadorAtual, carta, posicao);

        aplicarCapturas(carta, posicao);
        ui.exibirTabuleiro(tabuleiro);
        ui.exibirPontuacao(game.getJogador1(), game.getJogador2());
    }

    private void aplicarCapturas(Card carta, Position posicao) {
        for (GameRule regra : game.getRegrasAtivas()) {
            List<Position> capturasEspeciais =
                    regra.aplicarCapturasEspeciais(game, posicao, carta)
                            .stream()
                            .distinct()
                            .toList();
            if (!capturasEspeciais.isEmpty()) {
                ui.exibirMensagem(
                        "Regra " + regra.getNome()
                                + " ativada: vizinhos com somas iguais foram capturados."
                );
                for (Position posicaoCapturada : capturasEspeciais) {
                    capturarCarta(posicaoCapturada);
                }
            }
        }

        compararVizinho(carta, posicao, -1, 0,
                Direction.TOP, Direction.BOTTOM);
        compararVizinho(carta, posicao, 1, 0,
                Direction.BOTTOM, Direction.TOP);
        compararVizinho(carta, posicao, 0, -1,
                Direction.LEFT, Direction.RIGHT);
        compararVizinho(carta, posicao, 0, 1,
                Direction.RIGHT, Direction.LEFT);
    }

    private void compararVizinho(
            Card cartaJogada,
            Position posicao,
            int deslocamentoLinha,
            int deslocamentoColuna,
            Direction direcaoJogada,
            Direction direcaoVizinho
    ) {
        int linha = posicao.getLinha() + deslocamentoLinha;
        int coluna = posicao.getColuna() + deslocamentoColuna;
        Board tabuleiro = game.getTabuleiro();
        if (!tabuleiro.isPosicaoValida(linha, coluna)) {
            return;
        }

        Card cartaVizinha = tabuleiro.getCell(linha, coluna).getCarta();
        if (cartaVizinha == null
                || cartaVizinha.getDono() == null
                || cartaVizinha.getDono() == jogadorAtual) {
            return;
        }

        if (cartaJogada.getValor(direcaoJogada)
                > cartaVizinha.getValor(direcaoVizinho)) {
            capturarCarta(new Position(linha, coluna));
        }
    }

    private void capturarCarta(Position posicao) {
        Card carta = game.getTabuleiro()
                .getCell(posicao.getLinha(), posicao.getColuna())
                .getCarta();
        if (carta == null || carta.getDono() == null
                || carta.getDono() == jogadorAtual) {
            return;
        }

        Player donoAnterior = carta.getDono();
        carta.setDono(jogadorAtual);
        jogadorAtual.ganharPonto();
        donoAnterior.perderPonto();
        verificarConservacaoDaPontuacao();
        ui.exibirCartaCapturada(jogadorAtual, carta, donoAnterior);
    }

    private void verificarConservacaoDaPontuacao() {
        if (game.getJogador1().getPontos()
                + game.getJogador2().getPontos() != 10) {
            throw new IllegalStateException(
                    "A soma dos pontos dos jogadores deve permanecer 10."
            );
        }
    }

    private Player obterOponente(Player jogador) {
        return jogador == game.getJogador1()
                ? game.getJogador2()
                : game.getJogador1();
    }

    private boolean iniciarNovaRodadaSeNecessario() {
        for (GameRule regra : game.getRegrasAtivas()) {
            if (regra.aoFinalizarPartida(game)) {
                regraDaNovaRodada = regra.getNome();
                return true;
            }
        }
        return false;
    }

    public boolean verificarFimDePartida() {
        return game.getTabuleiro().todasAsCelulasPreenchidas();
    }

    public void finalizarPartida() {
        partidaEncerrada = true;
        game.setEstado(GameState.FINALIZADO);
        ui.exibirResultado(
                game.getJogador1(),
                game.getJogador2(),
                game.getTabuleiro()
        );
    }

    public Player getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean isPartidaEncerrada() {
        return partidaEncerrada;
    }
}

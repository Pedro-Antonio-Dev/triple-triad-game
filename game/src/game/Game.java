package game;

import board.Board;
import model.Card;
import model.Player;
import rules.GameRule;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Player jogador1;
    private Player jogador2;
    private Board tabuleiro;
    private List<Card> baralho;
    private List<GameRule> regrasAtivas;
    private GameState estado;

    public Game(GameState estado, List<GameRule> regrasAtivas, List<Card> baralho, Board tabuleiro, Player jogador2, Player jogador1) {
        this.estado = estado;
        this.regrasAtivas = regrasAtivas;
        this.baralho = baralho;
        this.tabuleiro = tabuleiro;
        this.jogador2 = jogador2;
        this.jogador1 = jogador1;
    }

    public void distribuirCartas() {
        if (baralho.size() < 10) {
            throw new IllegalStateException(
                    "O baralho precisa conter pelo menos 10 cartas."
            );
        }

        for (int i = 0; i < 10; i += 2) {
            Card cartaJogador1 = baralho.get(i);
            Card cartaJogador2 = baralho.get(i + 1);
            cartaJogador1.setDono(jogador1);
            cartaJogador2.setDono(jogador2);
            jogador1.adicionarCarta(cartaJogador1);
            jogador2.adicionarCarta(cartaJogador2);
        }
    }

    public void prepararProximaRodadaMorteSubita() {
        List<Card> cartasJogador1 = new ArrayList<>();
        List<Card> cartasJogador2 = new ArrayList<>();

        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                Card carta = tabuleiro.getCell(linha, coluna).getCarta();
                if (carta == null) {
                    continue;
                }
                if (carta.getDono() == jogador1) {
                    cartasJogador1.add(carta);
                } else if (carta.getDono() == jogador2) {
                    cartasJogador2.add(carta);
                }
            }
        }

        jogador1.getMao().clear();
        jogador1.getMao().addAll(cartasJogador1);
        jogador2.getMao().clear();
        jogador2.getMao().addAll(cartasJogador2);
        tabuleiro = new Board();
        estado = GameState.EM_ANDAMENTO;
    }

    public GameState getEstado() {
        return estado;
    }

    public List<GameRule> getRegrasAtivas() {
        return regrasAtivas;
    }

    public List<Card> getBaralho() {
        return baralho;
    }

    public Board getTabuleiro() {
        return tabuleiro;
    }

    public Player getJogador2() {
        return jogador2;
    }

    public Player getJogador1() {
        return jogador1;
    }

    public void setEstado(GameState estado) {
        this.estado = estado;
    }
}

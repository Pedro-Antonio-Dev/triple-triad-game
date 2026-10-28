package game;

import board.Board;
import model.Card;
import model.Player;
import rules.GameRule;

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

        int cont = 0;

        while(jogador1.getMao().size() < 5 && jogador2.getMao().size() < 5) {

            jogador1.getMao().add(this.baralho.get(cont));
            jogador2.getMao().add(this.baralho.get(cont+1));

            cont+=2;

        }

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
}

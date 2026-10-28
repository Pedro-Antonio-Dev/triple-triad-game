package model;

import java.util.List;
import java.util.Objects;

public class Player {

    private int id;
    private String nome;
    private int pontos;
    private List<Card> mao;

    public Player(int id, List<Card> mao, int pontos, String nome) {
        this.id = id;
        this.mao = mao;
        this.pontos = pontos;
        this.nome = nome;
    }

    public void adicionarCarta(Card carta) {

        this.mao.add(carta);

    }

    public void removerCarta(Card carta) {

        this.mao.remove(carta);

    }

    public Card getCarta(String nome) {

        for(Card carta: this.mao) {

            if(Objects.equals(carta.getNome(), nome)) {
                return carta;
            }

        }

        return null;

    }

    public boolean possuiCarta(Card carta) {

        for(Card c: this.mao) {

            if(c.equals(carta)) {
                return true;
            }

        }

        return false;

    }

    public void ganharPonto() {

        this.pontos++;

    }

    public void perderPonto() {

        this.pontos--;

    }

    public int getPontos() {
        return this.pontos;
    }

    public String getNome() {
        return this.nome;
    }
}

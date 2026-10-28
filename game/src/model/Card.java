package model;

public class Card {

    private String id;
    private String nome;
    private int topo;
    private int baixo;
    private int esquerda;
    private int direita;
    private Player dono;
    private CardType tipo;

    public Card(CardType tipo, Player dono, int direita, int esquerda, int baixo, int topo, String nome, String id) {
        this.tipo = tipo;
        this.dono = dono;
        this.direita = direita;
        this.esquerda = esquerda;
        this.baixo = baixo;
        this.topo = topo;
        this.nome = nome;
        this.id = id;
    }

    public int getValor(Direction direcao) {

        switch (direcao) {

            case TOP:
                return this.topo;

            case BOTTOM:
                return this.baixo;

            case LEFT:
                return this.esquerda;

            case RIGHT:
                return this.direita;

            default:
                throw new Exception("")

        }

    }

}

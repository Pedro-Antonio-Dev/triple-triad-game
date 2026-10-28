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
                return 0;

        }

    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Player getDono() {
        return dono;
    }

    public CardType getTipo() {
        return tipo;
    }

    public void setDono(Player dono) {
        this.dono = dono;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", topo=" + topo +
                ", baixo=" + baixo +
                ", esquerda=" + esquerda +
                ", direita=" + direita +
                ", dono=" + dono +
                ", tipo=" + tipo +
                '}';
    }
}

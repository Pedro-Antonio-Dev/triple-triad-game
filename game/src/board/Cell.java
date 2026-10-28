package board;

import model.Card;

public class Cell {

    private final int linha;
    private final int coluna;
    private Card carta;

    public Cell(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        this.carta = null;
    }

    public boolean isVazia() {
        return carta == null;
    }

    public void setCarta(Card carta) {
        this.carta = carta;
    }

    public Card getCarta() {
        return carta;
    }

}

package board;

public class Position {

    private final int linha;
    private final int coluna;

    public Position(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return linha == p.linha && coluna == p.coluna;
    }

    @Override
    public int hashCode() { return 31 * linha + coluna; }

    @Override
    public String toString() { return "(" + linha + "," + coluna + ")"; }

}

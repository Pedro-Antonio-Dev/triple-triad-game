package board;

import model.Card;
import model.Player;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Cell[][] grade;
    private final int linhas = 3;
    private final int colunas = 3;

    public Board() {
        grade = new Cell[linhas][colunas];
        for (int l = 0; l < linhas; l++)
            for (int c = 0; c < colunas; c++)
                grade[l][c] = new Cell(l, c);
    }

    public boolean isPosicaoValida(int l, int c) {
        return l >= 0 && l < linhas && c >= 0 && c < colunas;
    }

    public boolean isPosicaoVazia(int l, int c) {
        return isPosicaoValida(l, c) && grade[l][c].isVazia();
    }

    public Cell getCell(int linha, int coluna) {
        if (linha < 0 || linha >= linhas || coluna < 0 || coluna >= colunas) {
            throw new IndexOutOfBoundsException("Posição inválida: (" + linha + "," + coluna + ")");
        }
        return grade[linha][coluna];
    }

    public void colocarCarta(int linha, int coluna, Card carta){
        if (!isPosicaoVazia(linha, coluna)) {
            throw new IllegalStateException("A célula já está ocupada.");
        }
        grade[linha][coluna].setCarta(carta);
    }

    public List<Position> getVizinhos(int linha, int coluna) {
        List<Position> vizinhos = new ArrayList<>();
        int[] dLinhas = {-1, 0, 1, 0};
        int[] dColunas = {0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            int novaLinha = linha + dLinhas[i];
            int novaColuna = coluna + dColunas[i];
            if (isPosicaoValida(novaLinha, novaColuna)) {
                vizinhos.add(new Position(novaLinha, novaColuna));
            }
        }
        return vizinhos;
    }

    public boolean todasAsCelulasPreenchidas() {
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                if (grade[l][c].isVazia()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int contarCartasJogador(Player p){
        int count = 0;
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                Card carta = grade[l][c].getCarta();
                if (carta != null && carta.getDono().equals(p)) {
                    count++;
                }
            }
        }
        return count;
    }

    public String imprimirTabuleiro() {
        StringBuilder sb = new StringBuilder();
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                Card carta = grade[l][c].getCarta();
                if (carta != null) {
                    sb.append(carta.getDono().getNome().charAt(0)).append(" ");
                } else {
                    sb.append(". ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}

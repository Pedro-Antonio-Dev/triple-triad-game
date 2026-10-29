package rules;

import board.Board;
import board.Position;
import game.Game;
import model.Card;
import model.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlusRule implements GameRule {

    @Override
    public String getNome() {
        return "Plus";
    }

    @Override
    public List<Position> aplicarCapturasEspeciais(
            Game game,
            Position posicaoJogada,
            Card cartaJogada
    ) {
        Board tabuleiro = game.getTabuleiro();
        Map<Integer, List<Position>> posicoesPorSoma = new HashMap<>();
        Direction[] direcoesJogada = {
                Direction.TOP, Direction.RIGHT,
                Direction.BOTTOM, Direction.LEFT
        };
        Direction[] direcoesVizinho = {
                Direction.BOTTOM, Direction.LEFT,
                Direction.TOP, Direction.RIGHT
        };
        int[] deslocamentosLinha = {-1, 0, 1, 0};
        int[] deslocamentosColuna = {0, 1, 0, -1};

        for (int i = 0; i < direcoesJogada.length; i++) {
            int linha = posicaoJogada.getLinha() + deslocamentosLinha[i];
            int coluna = posicaoJogada.getColuna() + deslocamentosColuna[i];
            if (!tabuleiro.isPosicaoValida(linha, coluna)
                    || tabuleiro.getCell(linha, coluna).getCarta() == null) {
                continue;
            }

            Card cartaVizinha = tabuleiro.getCell(linha, coluna).getCarta();
            int soma = cartaJogada.getValor(direcoesJogada[i])
                    + cartaVizinha.getValor(direcoesVizinho[i]);
            posicoesPorSoma.computeIfAbsent(soma, ignorada -> new ArrayList<>())
                    .add(new Position(linha, coluna));
        }

        List<Position> capturas = new ArrayList<>();
        for (List<Position> posicoes : posicoesPorSoma.values()) {
            if (posicoes.size() >= 2) {
                capturas.addAll(posicoes);
            }
        }
        return capturas;
    }
}

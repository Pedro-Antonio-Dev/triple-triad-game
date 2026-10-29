package rules;

import board.Position;
import game.Game;
import model.Card;
import model.Player;

import java.util.List;

public interface GameRule {

    default String getNome() {
        return getClass().getSimpleName();
    }

    default List<String> aoIniciarPartida(Game game) {
        return List.of();
    }

    default boolean podeExibirMaoDoOponente(Game game, Player jogadorAtual) {
        return false;
    }

    default List<Position> aplicarCapturasEspeciais(
            Game game,
            Position posicaoJogada,
            Card cartaJogada
    ) {
        return List.of();
    }

    default boolean aoFinalizarPartida(Game game) {
        return false;
    }
}

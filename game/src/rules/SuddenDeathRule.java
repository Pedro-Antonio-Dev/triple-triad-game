package rules;

import game.Game;

public class SuddenDeathRule implements GameRule {

    @Override
    public String getNome() {
        return "Sudden Death";
    }

    @Override
    public boolean aoFinalizarPartida(Game game) {
        int cartasJogador1 =
                game.getTabuleiro().contarCartasJogador(game.getJogador1());
        int cartasJogador2 =
                game.getTabuleiro().contarCartasJogador(game.getJogador2());
        if (cartasJogador1 != cartasJogador2) {
            return false;
        }

        // A pontuação é acumulada entre rodadas e sua soma permanece 10.
        game.prepararProximaRodadaMorteSubita();
        return true;
    }
}

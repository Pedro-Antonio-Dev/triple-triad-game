package rules;

import game.Game;
import model.Player;

public class AllOpenRule implements GameRule {

    @Override
    public String getNome() {
        return "All Open";
    }

    @Override
    public boolean podeExibirMaoDoOponente(Game game, Player jogadorAtual) {
        return true;
    }
}

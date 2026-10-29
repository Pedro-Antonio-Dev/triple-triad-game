package rules;

import game.Game;
import model.Card;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SwapRule implements GameRule {

    @Override
    public String getNome() {
        return "Swap";
    }

    @Override
    public List<String> aoIniciarPartida(Game game) {
        List<Card> maoJogador1 = game.getJogador1().getMao();
        List<Card> maoJogador2 = game.getJogador2().getMao();
        if (maoJogador1.isEmpty() || maoJogador2.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível trocar cartas de mãos vazias."
            );
        }

        int indiceJogador1 = ThreadLocalRandom.current()
                .nextInt(maoJogador1.size());
        int indiceJogador2 = ThreadLocalRandom.current()
                .nextInt(maoJogador2.size());
        Card cartaJogador1 = maoJogador1.get(indiceJogador1);
        Card cartaJogador2 = maoJogador2.get(indiceJogador2);
        maoJogador1.set(indiceJogador1, cartaJogador2);
        maoJogador2.set(indiceJogador2, cartaJogador1);
        cartaJogador1.setDono(game.getJogador2());
        cartaJogador2.setDono(game.getJogador1());
        return List.of(
                "Regra Swap: " + game.getJogador1().getNome()
                        + " trocou " + cartaJogador1.getNome()
                        + " por " + cartaJogador2.getNome()
                        + " de " + game.getJogador2().getNome() + "."
        );
    }
}

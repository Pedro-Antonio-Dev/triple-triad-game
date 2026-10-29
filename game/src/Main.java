
import board.Board;
import game.Game;
import game.GameController;
import game.GameState;
import model.Card;
import model.CardType;
import model.Player;
import rules.AllOpenRule;
import rules.GameRule;
import rules.PlusRule;
import rules.SuddenDeathRule;
import rules.SwapRule;
import ui.TerminalUI;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TerminalUI ui = new TerminalUI();
        List<GameRule> regrasAtivas = new ArrayList<>();

        if (!selecionarRegras(ui, regrasAtivas)) {
            return;
        }

        List<Card> maoJogador1 = new ArrayList<>();
        List<Card> maoJogador2 = new ArrayList<>();

        Player jogador1 = new Player(
                1,
                maoJogador1,
                5,
                "Jogador 1"
        );

        Player jogador2 = new Player(
                2,
                maoJogador2,
                5,
                "Jogador 2"
        );

        List<Card> baralho = new ArrayList<>();
        CardType tipo = CardType.NEUTRAL;
        for (int i = 0; i < 10; i++) {
            Card carta = new Card(
                    tipo,
                    null,
                    i % 11,
                    (i + 1) % 11,
                    (i + 2) % 11,
                    (i + 3) % 11,
                    "Carta " + (i + 1),
                    String.valueOf(i + 1)
            );
            baralho.add(carta);
        }

        Board tabuleiro = new Board();
        Game game = new Game(
                GameState.AGUARDANDO,
                regrasAtivas,
                baralho,
                tabuleiro,
                jogador2,
                jogador1
        );

        GameController controller = new GameController(game, ui);
        controller.iniciarPartida();
    }

    private static boolean selecionarRegras(
            TerminalUI ui,
            List<GameRule> regrasAtivas
    ) {
        var allOpen = ui.solicitarAtivacaoRegra("All Open");
        if (allOpen.isEmpty()) {
            return false;
        }
        if (allOpen.get()) {
            regrasAtivas.add(new AllOpenRule());
        }

        var swap = ui.solicitarAtivacaoRegra("Swap");
        if (swap.isEmpty()) {
            return false;
        }
        if (swap.get()) {
            regrasAtivas.add(new SwapRule());
        }

        var plus = ui.solicitarAtivacaoRegra("Plus");
        if (plus.isEmpty()) {
            return false;
        }
        if (plus.get()) {
            regrasAtivas.add(new PlusRule());
        }

        var suddenDeath = ui.solicitarAtivacaoRegra("Sudden Death");
        if (suddenDeath.isEmpty()) {
            return false;
        }
        if (suddenDeath.get()) {
            regrasAtivas.add(new SuddenDeathRule());
        }
        return true;
    }
}

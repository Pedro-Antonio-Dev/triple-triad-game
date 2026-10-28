
import board.Board;
import game.Game;
import game.GameController;
import game.GameState;
import model.Card;
import model.CardType;
import model.Player;
import rules.GameRule;
import ui.TerminalUI;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Criar as mãos dos jogadores
        List<Card> maoJogador1 = new ArrayList<>();
        List<Card> maoJogador2 = new ArrayList<>();

        // 2. Criar os jogadores
        Player jogador1 = new Player(
                1,
                maoJogador1,
                0,
                "Jogador 1"
        );

        Player jogador2 = new Player(
                2,
                maoJogador2,
                0,
                "Jogador 2"
        );

        // 3. Criar o baralho
        List<Card> baralho = new ArrayList<>();

        CardType tipo = CardType.values()[0];

        for (int i = 1; i <= 10; i++) {
            Card carta = new Card(
                    tipo,
                    null,
                    1 + (i % 10), // direita
                    1 + ((i + 1) % 10), // esquerda
                    1 + ((i + 2) % 10), // baixo
                    1 + ((i + 3) % 10), // topo
                    "Carta " + i,
                    String.valueOf(i)
            );

            baralho.add(carta);
        }

        // 4. Criar o tabuleiro
        Board tabuleiro = new Board();

        // 5. Configurar as regras ativas
        List<GameRule> regrasAtivas = new ArrayList<>();

        /*
         * Adicione aqui as instâncias das regras concretas
         * que você já implementou.
         */

        // 6. Criar o estado inicial
        GameState estado = GameState.values()[0];

        // 7. Criar o jogo
        Game game = new Game(
                estado,
                regrasAtivas,
                baralho,
                tabuleiro,
                jogador2,
                jogador1
        );

        // 8. Criar a interface e o controlador
        TerminalUI ui = new TerminalUI();
        GameController controller = new GameController(game, ui);

        // 9. Iniciar a partida
        controller.iniciarPartida();
    }
}

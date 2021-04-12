package bo;

import bo.game.Difficulty;
import bo.game.Game;
import bo.game.location.LocationName;
import bo.game.player.Player;
import bo.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;

        view.getMainMenuPanel().getBtnNewGame().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Game game = new Game();
                model.setGame(game);

                // TODO Allow user to set difficulty and player roles
                game.setDifficulty(Difficulty.STANDARD);
                game.getPlayers().add(new Player(Player.BECK));
                game.getPlayers().add(new Player(Player.OSTER));

                // Add the player board panels
                for (Player player: game.getPlayers()){
                    view.getGamePanel().getPlayerBoardsPanel().addPlayerBoard(player);
                    game.getBoard().getLocation(LocationName.TRAIN_STATION).getPlayers().add(player);
                }

                view.showGame();
                view.refresh();
            }
        });
    }
}

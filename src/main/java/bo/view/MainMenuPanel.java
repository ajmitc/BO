package bo.view;

import bo.Model;

import javax.swing.*;

public class MainMenuPanel extends JPanel {
    private JButton btnNewGame;

    public MainMenuPanel(Model model, View view){
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        btnNewGame = new JButton("New Game");
        add(btnNewGame);
    }

    public JButton getBtnNewGame() {
        return btnNewGame;
    }
}

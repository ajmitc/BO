package bo.view;

import bo.Model;

import javax.swing.*;
import java.awt.*;

public class View {
    private static final String MAINMENU = "mainmenu";
    private static final String GAME = "game";

    private Model model;
    private JFrame frame;

    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;

    public View(Model model){
        this.model = model;

        frame = new JFrame();
        frame.setTitle("Black Orchestra");
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainMenuPanel = new MainMenuPanel(model, this);
        gamePanel = new GamePanel(model, this);

        frame.getContentPane().setLayout(new CardLayout());
        frame.getContentPane().add(mainMenuPanel, MAINMENU);
        frame.getContentPane().add(gamePanel, GAME);
    }

    public void refresh(){
        gamePanel.refresh();
    }

    public void showMainMenu(){
        showCard(MAINMENU);
    }

    public void showGame(){
        showCard(GAME);
    }

    private void showCard(String name){
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), name);
    }

    public JFrame getFrame() {
        return frame;
    }

    public MainMenuPanel getMainMenuPanel() {
        return mainMenuPanel;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}

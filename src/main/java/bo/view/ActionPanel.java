package bo.view;

import bo.Model;
import bo.view.util.GridBagLayoutHelper;

import javax.swing.*;
import java.awt.*;

public class ActionPanel extends JPanel {
    private Model model;
    private View view;

    private JLabel lblPlayerTurn;
    private JLabel lblNumActionsLeft;
    private JButton btnPlayCard;
    private JButton btnCollectItem;
    private JButton btnConspire;
    private JButton btnDeliverItem;
    private JButton btnDrawCard;
    private JButton btnMove;
    private JButton btnRelease;  // From Jail
    private JButton btnRevealItem;
    private JButton btnTransferCardTile;
    private JButton btnPlayLightningCard;  // This button is always available and allows the user to play a lightning card from any player's dossier
    private JButton btnPlayerSpecialAbility;

    public ActionPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        btnPlayCard          = new JButton("Act (Play Card)");
        btnPlayLightningCard = new JButton("Play Lightning Card");
        btnDrawCard          = new JButton("Draw Card");

        btnCollectItem      = new JButton("Collect Item");
        btnDeliverItem      = new JButton("Deliver Item");
        btnRevealItem       = new JButton("Reveal Item");

        btnConspire         = new JButton("Conspire");
        btnMove             = new JButton("Move");
        btnTransferCardTile = new JButton("Transfer a Card/Tile");
        btnRelease          = new JButton("Release From Jail");

        btnPlayerSpecialAbility = new JButton("Act (Player Special)");

        lblPlayerTurn = new JLabel("");
        lblNumActionsLeft = new JLabel("0");
        JPanel displayNumActionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        displayNumActionsPanel.add(lblPlayerTurn);
        displayNumActionsPanel.add(new JLabel("  Actions Left:"));
        displayNumActionsPanel.add(lblNumActionsLeft);

        JPanel commonActionPanel = new JPanel();
        commonActionPanel.setBorder(BorderFactory.createTitledBorder("Common Actions"));
        commonActionPanel.setLayout(new BoxLayout(commonActionPanel, BoxLayout.LINE_AXIS));
        commonActionPanel.add(btnConspire);
        commonActionPanel.add(btnMove);

        JPanel cardActionPanel = new JPanel();
        cardActionPanel.setBorder(BorderFactory.createTitledBorder("Dossier Actions"));
        cardActionPanel.setLayout(new BoxLayout(cardActionPanel, BoxLayout.LINE_AXIS));
        cardActionPanel.add(btnPlayCard);
        cardActionPanel.add(btnPlayLightningCard);
        cardActionPanel.add(btnDrawCard);

        JPanel itemActionPanel = new JPanel();
        itemActionPanel.setBorder(BorderFactory.createTitledBorder("Item Actions"));
        itemActionPanel.setLayout(new BoxLayout(itemActionPanel, BoxLayout.LINE_AXIS));
        itemActionPanel.add(btnRevealItem);
        itemActionPanel.add(btnCollectItem);
        itemActionPanel.add(btnDeliverItem);

        JPanel otherActionPanel = new JPanel();
        otherActionPanel.setBorder(BorderFactory.createTitledBorder("Other Actions"));
        otherActionPanel.setLayout(new BoxLayout(otherActionPanel, BoxLayout.LINE_AXIS));
        otherActionPanel.add(btnTransferCardTile);
        otherActionPanel.add(btnRelease);
        otherActionPanel.add(btnPlayerSpecialAbility);

        new GridBagLayoutHelper(this, true)
                .setAnchor(GridBagConstraints.NORTHWEST)
                .add(displayNumActionsPanel).nextRow()
                .add(commonActionPanel).nextRow()
                .add(cardActionPanel).nextRow()
                .add(itemActionPanel).nextRow()
                .add(otherActionPanel);
    }

    public void refresh(){
        lblPlayerTurn.setText(model.getGame().getCurrentPlayer().getName() + "'s Turn");
        int actionsLeft = model.getGame().getCurrentPlayerActionsAllowed() - model.getGame().getCurrentPlayerActionsTaken();
        lblNumActionsLeft.setText(actionsLeft + "/" + model.getGame().getCurrentPlayerActionsAllowed());
    }

    public JButton getBtnPlayCard() {
        return btnPlayCard;
    }

    public JButton getBtnCollectItem() {
        return btnCollectItem;
    }

    public JButton getBtnConspire() {
        return btnConspire;
    }

    public JButton getBtnDeliverItem() {
        return btnDeliverItem;
    }

    public JButton getBtnDrawCard() {
        return btnDrawCard;
    }

    public JButton getBtnMove() {
        return btnMove;
    }

    public JButton getBtnRelease() {
        return btnRelease;
    }

    public JButton getBtnRevealItem() {
        return btnRevealItem;
    }

    public JButton getBtnTransferCardTile() {
        return btnTransferCardTile;
    }

    public JButton getBtnPlayLightningCard() {
        return btnPlayLightningCard;
    }

    public JButton getBtnPlayerSpecialAbility() {
        return btnPlayerSpecialAbility;
    }
}

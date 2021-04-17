package bo.view;

import bo.Model;
import bo.view.util.GridBagLayoutHelper;

import javax.swing.*;
import java.awt.*;

public class ActionPanel extends JPanel {
    private Model model;
    private View view;

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
        otherActionPanel.add(btnConspire);
        otherActionPanel.add(btnMove);
        otherActionPanel.add(btnTransferCardTile);
        otherActionPanel.add(btnRelease);
        otherActionPanel.add(btnPlayerSpecialAbility);

        new GridBagLayoutHelper(this, true)
                .setAnchor(GridBagConstraints.NORTHWEST)
                .add(cardActionPanel).nextRow()
                .add(itemActionPanel).nextRow()
                .add(otherActionPanel);
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

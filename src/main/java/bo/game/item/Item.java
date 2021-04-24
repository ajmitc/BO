package bo.game.item;

public class Item {
    private ItemType type;
    private boolean revealed = false;

    // Used with Concealed Pistol conspirator card
    // Is a Weapon type, but doesn't take up item slot
    private boolean concealed = false;

    public Item(ItemType type){
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isConcealed() {
        return concealed;
    }

    public void setConcealed(boolean concealed) {
        this.concealed = concealed;
    }

    public String toString(){
        if (type != null)
            return "" + type;
        return "";
    }
}

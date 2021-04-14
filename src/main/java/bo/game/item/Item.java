package bo.game.item;

public class Item {
    private ItemType type;
    private boolean revealed = false;

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

    public String toString(){
        if (type != null)
            return "" + type;
        return "";
    }
}

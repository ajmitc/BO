package bo.game.item;

public class Item {
    private ItemType type;

    public Item(ItemType type){
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }
}

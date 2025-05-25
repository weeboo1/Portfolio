package lifesim;

public class Item {
    private final String name;
    private final double price;
    private final ItemType type;

    
    public enum ItemType { 
        TRANSPORT, FOOD, TECH, GAME, BED;
    }

    public Item(String name, double price, ItemType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }


    public ItemType getType() {
        return type;
    }



}

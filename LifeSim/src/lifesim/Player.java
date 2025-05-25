package lifesim;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {

    private String name;
    private float balance;
    private short stamina;
    private short maxStamina;
    private List<Item> inventory;
    private Set<Item.ItemType> boughtItems;

    public Player(String name, float balance, short stamina, short maxStamina, List<Item> inventory){
        this.name = name;
        this.balance = balance;
        this.maxStamina = maxStamina;
        this.stamina = maxStamina;
        this.inventory = inventory;
        this.boughtItems = new HashSet<>();
    }

    public Player( float balance, short stamina, short maxStamina, List<Item> inventory){
        this.balance = balance;
        this.maxStamina = maxStamina;
        this.stamina = maxStamina;
        this.inventory = inventory;
    }
        
    //getters and setters

    public String getName() {
        return name;
    }
    
 
    public void setName(String name) {
        this.name = name;
    }
        

    public float getBalance() {
        return balance;
    }
    
    public void setBalance(float balance) { 
        this.balance = balance;
    }
    
    public short getStamina() {
        return stamina;
    }
    
    public void setStamina(short stamina) {
        this.stamina = stamina;
    }
    
    public short getMaxStamina() {
        return maxStamina;
    }
    
    public void setMaxStamina(short amount) {
        this.maxStamina = amount;
    }
    
    public List<Item> getInventory() {
        return inventory;
    }
    
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
    
    public Set<Item.ItemType> getBoughtItems() {
        return boughtItems;
    }

    //methods

    public void earnMoney(float amount) {
        this.balance += amount;
    }

    public boolean spendMoney(float amount){
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void decreaseStamina(short amount){
        stamina = (short) Math.max(0, stamina - amount);
    }

    public void recoverStamina(short amount) {
        stamina = (short) Math.min(stamina + amount, maxStamina);
    }

    public static void showStatsAndInventory(Player player) {
        System.out.println("Balance: " + player.getBalance() + " EUR");
        System.out.println("Stamina: " + player.getStamina() + "/" + player.getMaxStamina());
        System.out.println("Inventory:");
        for (Item item : player.getInventory()) {
            System.out.println("- " + item.getName() + "\t(Type: " + item.getType() + ")");
        }
    }

    public void addBoughtItem(Item.ItemType itemType) {
        boughtItems.add(itemType);
    }

    

}

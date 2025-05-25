package lifesim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Shop {

    private final List<Item> items;

    public Shop(Item.ItemType category) {
        items = new ArrayList<>();
        populateItems(category);
    }

    private void populateItems(Item.ItemType category) {
        if (category == Item.ItemType.TRANSPORT) {
            items.add(new Item("Bike", 80, Item.ItemType.TRANSPORT));
            items.add(new Item("Car", 1000, Item.ItemType.TRANSPORT));
            items.add(new Item("Ferrari", 19000, Item.ItemType.TRANSPORT));
        } else if (category == Item.ItemType.FOOD) {
            items.add(new Item("Instant noodles", 5.50, Item.ItemType.FOOD));
            items.add(new Item("Pizza", 20.75, Item.ItemType.FOOD));
            items.add(new Item("Salad", 15.25, Item.ItemType.FOOD));
            items.add(new Item("Pasta", 30, Item.ItemType.FOOD));
            items.add(new Item("Coffe", 8.50, Item.ItemType.FOOD));
            items.add(new Item("Redbull", 14.50, Item.ItemType.FOOD));
        } else if (category == Item.ItemType.TECH) {
            items.add(new Item("Laptop", 270, Item.ItemType.TECH));
            items.add(new Item("Gaming PC", 900, Item.ItemType.TECH));
            items.add(new Item("iPhone 15", 1350, Item.ItemType.TECH));
        } else if (category == Item.ItemType.GAME) {
            items.add(new Item("Minecraft", 20, Item.ItemType.GAME));
            items.add(new Item("GTA V", 30, Item.ItemType.GAME));
            items.add(new Item("Life Sim", 30, Item.ItemType.GAME));
        } else if (category == Item.ItemType.BED) {
            items.add(new Item("Bed Level 2", 200, Item.ItemType.BED));
            items.add(new Item("Bed Level 3", 450, Item.ItemType.BED));
            items.add(new Item("Bed Level 4", 700, Item.ItemType.BED));
        }
    }

    public void open(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the store!");

        Map<Integer, Item> displayed = new HashMap<>();
        int index = 1;

        for (Item item : items) {
            System.out.printf("%d. %s (%.2f EUR) [%s]%n", index, item.getName(), item.getPrice(), item.getType());
            displayed.put(index, item);
            index++;
        }

        while (true) {
            System.out.print("Enter item number.\n(0 to exit) ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 0) {
                    System.out.println("Exiting the store.");
                    break;
                }

                Item selectedItem = displayed.get(choice);

                if (selectedItem == null) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

                double price = selectedItem.getPrice();
                Item.ItemType itemType = selectedItem.getType();

                if (itemType != Item.ItemType.FOOD) {
                    boolean alreadyOwned = false;
                    for (Item item : player.getInventory()) {
                        if (item.getName().equals(selectedItem.getName())) {
                            alreadyOwned = true;
                            break;
                        }
                    }

                    if (alreadyOwned) {
                        System.out.println("You already own this item: " + selectedItem.getName());
                        continue;
                    }
                }

                if (itemType == Item.ItemType.BED) {
                    switch (selectedItem.getName()) {
                        case "Bed Level 2" -> player.setMaxStamina((short) 150);
                        case "Bed Level 3" -> player.setMaxStamina((short) 200);
                        case "Bed Level 4" -> player.setMaxStamina((short) 250);
                    }
                }

                if (player.spendMoney((float) price)) {
                    player.getInventory().add(selectedItem);
                    System.out.println("You bought: " + selectedItem.getName());
                    System.out.printf("Your balance: %.2f EUR%n", player.getBalance());
                } else {
                    System.out.println("Not enough money to buy " + selectedItem.getName());
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }
}

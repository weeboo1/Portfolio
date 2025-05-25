package lifesim;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Kiril Halahan
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean playing = true;
        List<Item> inventory = new ArrayList<>();
        
        Item bedLevel1 = new Item("Bed Level 1", 0, Item.ItemType.BED);
        inventory.add(bedLevel1);
        Player player = new Player("", 50.0f, (short) 100, (short) 100, inventory);
        Item phone = new Item("iPhone 11", 400.0, Item.ItemType.TECH);
        player.getInventory().add(phone);

        // Beginning
        System.out.println("""
                           Welcome to the LifeSim: Work and Play
                           In this game you have to work by completing interesting minigames and earn money. You can spend your money on different items like food or a better bed, that gives you more stamina, new phone, or a new car, etc.""");
        System.out.println("First of all, whats your name?");

        String playerName = scanner.nextLine();
        player.setName(playerName);

        System.out.println("Nice to meet you " + player.getName() + "!");

        System.out.println("Lets give you some loot, so it is easier for you to start");

        System.out.println("You received 50 Euro, a level 1 bed and a phone");

        //Playing

        mainLoop:
        while (playing) {
            System.out.println("\n\nWhat would you like to do?");
            System.out.println("1. Sleep (recover stamina)");
            System.out.println("2. Work");
            System.out.println("3. Go shopping");
            System.out.println("4. Check my stats and inventory");
            System.out.println("10. Leave the game");

            System.out.print("Enter action number: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.println("\nYou are sleeping... Stamina is recovering...");
                    player.recoverStamina(player.getMaxStamina());
                }
                case "2" -> {
                    System.out.println("You decided to work. Here are your options:");
                    System.out.println(" - 1. Math equations");
                    System.out.println(" - 2. Clicker");
                    System.out.println(" - 3. To be developed");
                    
                    String workChoice = scanner.nextLine();
                    
                    switch (workChoice) {
                        case "1" -> {
                            MathWork mathWork = new MathWork();
                            mathWork.start(player);
                        }
                        case "2" -> {
                            ClickerWork clickerWork = new ClickerWork();
                            clickerWork.start(player);
                        }
                        case "3" -> System.out.println("~~Work in progress~~");
                    
                    
                    }
                }
                case "3" -> {
                    while (true) {
                        System.out.println("You entered a foreign supermarket.");
                        System.out.println(" - 1. Transport dealership");
                        System.out.println(" - 2. Groceries store");
                        System.out.println(" - 3. Tech store");
                        System.out.println(" - 4. Games store");
                        System.out.println(" - 5. Bed shop");
                        System.out.println(" - 0. Leave supermarket");

                        String storeChoice = scanner.nextLine();

                        switch (storeChoice) {
                            case "1" -> new Shop(Item.ItemType.TRANSPORT).open(player);
                            case "2" -> new Shop(Item.ItemType.FOOD).open(player);
                            case "3" -> new Shop(Item.ItemType.TECH).open(player);
                            case "4" -> new Shop(Item.ItemType.GAME).open(player);
                            case "5" -> new Shop(Item.ItemType.BED).open(player);
                            case "0" -> {
                                System.out.println("You left the supermarket.");
                                continue mainLoop;
                            }
                            default -> System.out.println("Invalid choice.");
                        }
                    }
                }
                case "4" -> {
                    Player.showStatsAndInventory(player);
                }
                case "10" -> {
                    System.out.println("You are leaving the game! Thanks for playing LifeSim: Work and Play");
                    playing = false;
                }
                default -> System.out.println("Wrong input! Try again.");
            }
        }

        scanner.close();
    }

}

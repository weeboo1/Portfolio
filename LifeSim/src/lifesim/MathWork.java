package lifesim;

import java.util.Random;
import java.util.Scanner;

public class MathWork {

    public void start(Player player) {
        
        System.out.println("You chose Math equations");
        
        if (player.getStamina() < 30) {
            System.out.println("\nYou are too tired to work. You need at least 30 stamina.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        player.decreaseStamina((short) 30);
        System.out.println("You spent 30 stamina to work!");
        System.out.println("Now you have " + player.getStamina() + " stamina");


        System.out.println("You will have to solve 5 math equations");

        for (int i = 0; i < 5; i++) {

            Random random = new Random();

            int numCount = random.nextInt(3) + 2;

            int[] numbers = new int[numCount];

            for (int j = 0; j < numCount; j++) {
                numbers[j] = random.nextInt(21);
            }

            String[] operations = {"+", "-", "*", "/"};
            String operation = operations[random.nextInt(4)];

            StringBuilder equation = new StringBuilder();
            double result = numbers[0];

            for (int j = 0; j < numCount; j++) {
                equation.append(numbers[j]);
                if (j < numCount - 1) {
                    equation.append(" " + operation + " ");
                }

                if (j > 0) {
                    switch (operation) {
                        case "+":
                            result += numbers[j];
                            break;
                        case "-":
                            result -= numbers[j];
                            break;
                        case "*":
                            result *= numbers[j];
                            break;
                        case "/":
                            if (numbers[j] != 0) {
                                result /= numbers[j];
                            } else {
                                result = 0;
                            }
                            break;
                    }
                }
            }

            double roundedResult = Math.round(result * 100.0) / 100.0;

            System.out.println("Solve this equation: " + equation);

            Double answer = null;
            while (answer == null) {
                System.out.print("Enter your answer: ");
                if (scanner.hasNextDouble()) {
                    answer = scanner.nextDouble();
                    answer = Math.round(answer * 100.0) / 100.0;
                } else {
                    System.out.println("Invalid input! Please enter a valid number.");
                    scanner.next();
                }
            }

            if (answer.equals(roundedResult)) {
                System.out.println("Correct! You earned 10 Euro!");
                player.earnMoney(10);
            } else {
                System.out.println("Wrong! The correct answer was: " + roundedResult);
            }
        }

        //scanner.close();
    }
}

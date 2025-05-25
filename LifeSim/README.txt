# Life Simulation Game (Java, Console + Swing)

**Life Simulation Game** is a text-based simulation game written in Java, where the player controls a character, works, earns money, manages stamina, and can shop in a store. The game features two mini-games: one is a logic-based (solving equations), and the other is an arcade-style (clicker with balls).

---

## Features

- **Jobs**:
  - **Math Work** — solve simple math equations by entering answers via the console.
  - **Clicker Work** — a speed mini-game using Swing: click on green circles and avoid red ones!

- **Shop**:
  - You can buy tech, food, and drinks (though the hunger functionality hasn't been added yet).
  - You can buy different levels of beds to increase maximum stamina.

- **Player**:
  - The player has money and stamina.
  - Work requires a certain amount of stamina.

- **Progress and Limitations**:
  - If the player runs out of stamina, they cannot work.
  - To restore stamina, the player needs to sleep.

---

## Technologies

- Java 17+
- Swing (for ClickerWork)
- Console-based interface (text-based)

---

### Project Structure

- **Player.java** — the player class. Stores money, stamina, and contains methods for earning money, spending, and restoring energy.
- **Item.java** — the item class. Stores item categories, getters, and setters.
- **Shop.java** — shop implementation. Allows the player to buy items and restore stamina.
- **ClickerWork.java** — a mini-game with a graphical interface using Swing: click on green circles, avoid red ones, and earn money.
- **MathWork.java** — a text-based mini-game: solve random equations and earn money.
- **Main.java** — the main class with the game menu and logic for launching various functions.

---
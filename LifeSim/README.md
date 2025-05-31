# Life Simulation Game (Java, Console + Swing)

**Life Simulation Game** is a text-based simulation game written in Java, where player can work, earn money, buy items in a store, and manage stamina. The game features two works (mini-games): one is a logic-based (solving equations), and the other is an arcade-style (clicker with balls).

---

# Features

- **Jobs**:
  - Math Work – a console mini-game where you solve simple math equations.
  - Clicker Work – a Swing-based mini-game where you click on green circles to earn money while avoiding red ones.

- **Shop**:
  - You can buy electronics, food, and drinks (though the hunger functionality hasn't been added yet).
  - Beds increase your maximum stamina, which helps you work longer.
  - Hunger mechanics are planned but not yet implemented.
  
- **Player**:
  - The player has money and stamina.
  - Working costs stamina, so you’ll need to manage your energy.
  - You can sleep to restore stamina when it runs out.

### Project Structure

- `Player.java` – handles player data: money, stamina, and related methods like working and sleeping.
- `Item.java` – defines items, their categories, and properties.
- `Shop.java` – allows the player to buy items and handles stamina-related effects.
- `ClickerWork.java` – the graphical clicker game where timing and reaction matter.
- `MathWork.java` – the console-based math game for earning money by solving equations.
- `Main.java` – contains the game menu and ties everything together.

by Halahan Kiril

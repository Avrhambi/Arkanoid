# Arkonaid Game
This project is a implementation of the classic Arkanoid game. It uses the biuoop library for graphical user interface (GUI) handling and animations. The game consists of several levels that the player can progress through by breaking bricks with a ball controlled by a paddle.

## Features
- Multiple Levels: The game includes several predefined levels that can be selected by command-line arguments or defaults.
- Smooth Animations: The game is rendered with smooth animations running at 60 frames per second.
- Sprites and Game Flow: Various game objects (such as bricks, balls, and paddles) are represented as sprites, and the game flow is managed by a dedicated GameFlow class.
- Keyboard Input: The game detects and reacts to keyboard input, allowing players to control the paddle.




## Installation
## Quick access (recommended)
1. Click here for download: [Arkanoid.jar](https://github.com/Avrhambi/Arkanoid_Game/raw/refs/heads/main/Arkanoid.jar) 
2. Ensure that Java SE 10 or higher is installed on your machine.
3. The game will launch automatically and you can start playing immediately.

## Full installation
1. Clone the repository to your local machine:

2. Include the biuoop library in your project (either download or add it as a dependency).

3. Compile and run the Java classes.

##  How to Play
- Game Controls: Use the arrow keys to control the paddle.

- Game Flow: The game starts with a specific level and continues to the next after the player clears the current level. 
  If no levels are specified, the game defaults to a set of predefined levels.
   You can run the game with different levels by passing arguments:
   ```bash
   ./mvnw package
   java -jar target/Arkanoid.jar 1 2 3

This will run levels 1, 2, and 3. If no arguments are provided, all default levels will be played.

## level 1
![image](https://github.com/user-attachments/assets/1d247f0b-28ef-4988-adf3-16a5226f2af6)

## level 2
![image](https://github.com/user-attachments/assets/a3e8c57c-21ba-4cd0-b8c9-b8e4c2813537)

## level 3 
![image](https://github.com/user-attachments/assets/53d8d495-94ed-44d3-af43-9c23f757e59e)





## Project Structure
- ArkanoidGame.java: The main class responsible for initializing the game and running levels.
- AnimationRunner.java: Handles running animations in the game at 60 frames per second.
- BuildingAnimation.java: A sample animation representing a building sprite in the game.
- GameFlow.java: Manages the game flow, including level transitions and the game loop.
- Level Classes: Define different game levels with unique layouts (e.g., DirectHit1Level, WideEasy2Level).

## Requirements
- Java 17 or later.
- biuoop library for GUI and animation.

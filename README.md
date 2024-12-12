# Mower 🌊🎋🚤

Mower is an engaging 2D tile-based game where players control a mower tasked with cutting bamboos while avoiding dangerous boulders. Precision, strategy, and quick reflexes are essential to survive and achieve the highest score!


## 🎮 Game Dynamics

- Objective: Move the mower to cut as many bamboos as possible.
- Challenge: Avoid boulders to stay alive.
- Game Over: Health reaches zero when you hit too many boulders.

## ⚙️ Mechanics

- Movement: Navigate the mower across the grid.
- Collisions: Colliding with boulders reduces health.
- Bamboo Cutting: Earn points by cutting bamboos.

## 🖼️ Assets

Graphics
- Textures: Bamboo and tile textures.
- Sprites: Animated mower sprites.

Sound Effects
- Bamboo cutting sounds.
- Mower operational sounds (start, stop, and continuous operation).
- Collision impact sounds.

Fonts
- Custom font used for score display and UI elements.

## ✨ Features

- Bamboo Cutting Action: Strategically mow bamboos to score points.
- Health System: Each boulder collision reduces health. The game ends when health reaches zero.
- Immersive Sounds: Enjoy dynamic sound effects for each in-game event.
- Challenging Obstacles: Maneuver strategically to avoid boulders.

## 🕹️ How to Play

Controls:
- Use arrow keys for movement on desktops.
- Use on-screen controls on touch devices.

Gameplay:
- Cut bamboos to gain points.
- Avoid boulders to maintain your health.

End Condition:
- The game ends when your health bar depletes completely.

## 🛠️ Built With

- LibGDX: Cross-platform game development framework.
- Java: Core programming language for game logic.
- Tiled: Map editor used for creating TMX format levels.

## 🚀 Getting Started

Prerequisites
- Java JDK 8+
- Gradle: Optional but included via wrapper.

Installation

Clone this repository:

`git clone https://github.com/zbrdarovski/mower.git`  

`cd mower`  

Build the project:

`./gradlew build`

Run the game:

`./gradlew desktop:run`

## 📂 Project Structure

Mower  
├── android/        # Android-specific files for mobile deployment  
├── core/           # Core game logic and assets  
│   ├── assets/     # Game assets (textures, sounds, fonts)  
│   └── src/        # Game source code  
├── desktop/        # Desktop launcher  
├── gradle/         # Gradle wrapper  
└── build.gradle    # Build configuration

## 🌟 Future Features

- Power-ups: Add health kits or temporary shields.
- New Levels: Introduce more diverse and challenging environments.
- Leaderboard: Display top player scores for competitive play.

## 👏 Acknowledgments

- Community Support: Thanks to the LibGDX community for tools and guidance.
- Assets Credits: Graphics and sound effects are credited to their respective creators.

## ⚖️ License
This project is not licensed for public use. The code is protected by copyright law.  
Viewing is permitted for evaluation purposes only. Copying, modifying, or distributing the code is strictly prohibited.

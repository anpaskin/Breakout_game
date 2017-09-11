game
====

First project for CompSci 308 Fall 2017

This game was created by Aaron Paskin and is loosely based on the design plan outlined by Carl Dea
in his javafx tutorial blog (https://carlfx.wordpress.com/2012/03/29/javafx-2-gametutorial-part-1/).
The game was started on August 29th, 2017 and completed on September 10th, 2017. It took approximately
40 hours to complete.

Open the game by running MainGame.java. MainGame.java uses the classes GameDriver.java, BlockManager.java,
SlashScreen.java, and Block.java.

Refer to the instructions on the home screen (displayed when first opening the game) for game play. Cheat codes 
include pressing the 'enter' key to advance to the next level, pressing the 'D' key to deactivate an active power-up,
pressing the 'A' key to add a life, pressing the 'tab' key to increase the ball speed by a factor of 1.5, pressing
the 'shift' key to decrease the ball speed by a factor of 1.5, and pressing the 'R' key to reset the paddle and
ball positions.

Known bugs include:
- Traveling blocks sometimes travel to an occupied location
- Sticky paddle sometimes "drops" the ball on movement. Ball also moves into sticky paddle when moving the paddle
against a wall.
- When ball collides with corner of window, it falls down the side.
- Ball changes direction when a lazer hits a Traveling Block.

Final impressions:
The game layout, including level setup and navigation, and game play, including moving the paddle and receiving/using
power-ups, are very smooth. I am pleased with the overall functionality of the game, and there are only a few minor
bugs which I could resolve with more time. The two areas in which I could have improved most were design and style. I 
feel like I started off strong with the design of my code, but I didn't adapt well later on and implemented many 
features in GameDriver.java and Block.java which should have been done in their own classes. For example, I should have 
made my power-ups and block types their own classes and subclasses so that future programmers could more easily 
implement new features. Overall, however, the game works well and I am proud of my product.



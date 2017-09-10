package game_anp36;

import java.util.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/** BlockManager manages the statuses and positions of the blocks in a given level. It uses
 BLOCK_LIST to keep track of all existing blocks, COLLISIONS to keep track of all hit blocks
 in a given step, and CLEAN_UP_BLOCKS to keep track of the blocks that are destroyed in a given step.
 It also gets a ball from the GameDriver (or whatever initializes the BlockManager).
 *
 *@author Aaron Paskin
 *
 */
public class BlockManager {

	private List<Block> BLOCK_LIST;	
	private List<Block> COLLISIONS;
	private Set<Block> CLEAN_UP_BLOCKS;
	private Circle BALL;
	
	public BlockManager(Circle ball) {
		BLOCK_LIST = new ArrayList<Block>();
		COLLISIONS = new ArrayList<Block>();
		CLEAN_UP_BLOCKS = new HashSet<Block>();
		BALL = ball;
	}

	public void addBlock(Block e) {
		BLOCK_LIST.add(e);
	}
	
	public List<Block> getBlockList() {
		return BLOCK_LIST;
	}
	
	public List<Block> getCollisions() {
		return COLLISIONS;
	}
	
	public Set<Block> getCleanUp() {
		return CLEAN_UP_BLOCKS;
	}
	
	/** addCollisions iterates through the BLOCK_LIST and adds each block that
	 * has been hit by the ball or a lazer to the COLLISIONS list. Method also changes
	 * block color upon collision depending on block type.
	 * @param lazer		Rectangle that represents the lazer to check for lazer-block collisions*/
	public void addCollisions(Rectangle lazer) {
		for(Block x : BLOCK_LIST) {
			if(x.ballCollide(BALL) || x.lazerCollide(lazer)) {
				if(x.getType().equals("Traveling") && x.getCollisions() == 1) {
					moveToOpenSpace(x);
					x.getRectangle().setFill(Color.GRAY);
				}
				else if(x.getType().equals("Two Hit") && x.getCollisions() == 1) {
					x.getRectangle().setFill(Color.ORANGE);
				}
				COLLISIONS.add(x);
			}
		}
	}
	
	/** moveToOpenSpace moves the passed block to a random position that does not contain a block and is
	 * used to relocate a Traveling Block upon first collision.
	 * @param blockToMove		Traveling Block to relocate*/
	private void moveToOpenSpace(Block blockToMove) {
		ArrayList<Integer> openSpaces = getOpenSpaces();
		Random rand = new Random();
		int xy = openSpaces.get(rand.nextInt(openSpaces.size()));
		int x = (xy % 9) * Block.WIDTH;
		int y = (xy / 9) * Block.HEIGHT;
		blockToMove.getRectangle().setX(x);
		blockToMove.getRectangle().setY(y);
	}
	
	/** getOpenSpaces creates and returns a list of all locations that do not contain a block, to be used
	 * in moveToOpenSpace.
	 * @return		An ArrayList of Integers representing open locations for a traveling block to
	 * 				be moved to. Each location is represented by a number 0-44, with 0-8 being the top
	 * 				row locations from left to right, 9-17 being the second row from left to right, etc.*/
	private ArrayList<Integer> getOpenSpaces() {
		ArrayList<Integer> openSpaces = new ArrayList<Integer>();
		boolean add = true;
		int space = 0;
		for(int x = 0; x < GameDriver.NUM_COLS * Block.WIDTH; x += Block.WIDTH) {
			for(int y = 0; y < GameDriver.NUM_ROWS * Block.HEIGHT; y += Block.HEIGHT) {
				for(Block b : BLOCK_LIST) {
					if(b.getRectangle().getX() == x && b.getRectangle().getY() == y) {
						add = false;
					}
				}
				if(add) openSpaces.add(space);
				add = true;
				space++;
			}
		}
		return openSpaces;
	}
	
	/** cleanUp checks if each block in COLLISIONS is destroyed and, if it is, adds it to CLEAN_UP_BLOCKS.*/
	public void cleanUp() {
		CLEAN_UP_BLOCKS.clear();
		for(Block x : COLLISIONS) {
			if(x.getIsDestroyed()) {
				CLEAN_UP_BLOCKS.add(x);
			}
		}
	}
	
	public void removeBlocks() {
		BLOCK_LIST.removeAll(CLEAN_UP_BLOCKS);
		COLLISIONS.clear();
	}
	
	public void destroy(Block block) {
		BLOCK_LIST.remove(block);
	}
	
}

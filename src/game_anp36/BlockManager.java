package game_anp36;

import java.util.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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
	
	public void addCollisions(Rectangle lazer) {
		for(Block x : BLOCK_LIST) {
			if(x.ballCollide(BALL) || x.lazerCollide(lazer)) {
				if(x.getType().equals("Traveling") && x.getCollisions() == 1) {
					moveToOpenSpace(x);
					x.getRectangle().setFill(Color.GRAY);
				}
				COLLISIONS.add(x);
			}
		}
	}
	
	private void moveToOpenSpace(Block blockToMove) {
		System.out.println("Old Position: (" + blockToMove.getRectangle().getX() + ", " + blockToMove.getRectangle().getY() + ")");
		ArrayList<Integer> openSpaces = getOpenSpaces();
		Random rand = new Random();
		int xy = openSpaces.get(rand.nextInt(openSpaces.size()));
		int x = (xy % 9) * Block.WIDTH;
		int y = (xy / 9) * Block.HEIGHT;
		blockToMove.getRectangle().setX(x);
		blockToMove.getRectangle().setY(y);
		System.out.println("New Position: (" + blockToMove.getRectangle().getX() + ", " + blockToMove.getRectangle().getY() + ")");
	}
	
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
		System.out.println(openSpaces);
		return openSpaces;
	}
	
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

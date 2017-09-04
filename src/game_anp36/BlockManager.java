package game_anp36;

import java.util.*;

import javafx.scene.shape.Circle;

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
		System.out.println("Block List: " + BLOCK_LIST);
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
	
	public void addCollisions() {
		for(Block x : BLOCK_LIST) {
			if(x.ballCollide(BALL)) {
				COLLISIONS.add(x);
			}
		}
		System.out.println("Collisions: " + COLLISIONS);
	}
	
	public void cleanUp() {
		System.out.println("Number of Blocks: " + BLOCK_LIST.size());
		CLEAN_UP_BLOCKS.clear();
		for(Block x : COLLISIONS) {
			if(x.isDestroyed()) {
				CLEAN_UP_BLOCKS.add(x);
			}
		}
	}
	
	public void removeBlocks() {
		BLOCK_LIST.removeAll(CLEAN_UP_BLOCKS);
		COLLISIONS.clear();
		System.out.println("Clean Up: " + CLEAN_UP_BLOCKS);
		System.out.println("Number of Blocks: " + BLOCK_LIST.size());
		//return CLEAN_UP_BLOCKS;
	}
	
}

package game_anp36;

import java.util.*;

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
				COLLISIONS.add(x);
			}
		}
	}
	
	public void cleanUp() {
		CLEAN_UP_BLOCKS.clear();
		for(Block x : COLLISIONS) {
			if(x.checkIfDestroyed()) {
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

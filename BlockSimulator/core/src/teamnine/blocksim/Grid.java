package teamnine.blocksim;

import com.badlogic.gdx.math.Vector3;

public class Grid {
	public Block grid[][][];
	
	public Grid(int gridSize) {
		grid = new Block[gridSize][gridSize][gridSize];
	}
	
	public void addBlock(Block block) {
		Vector3 blockPosition = block.getPosition();
		grid[(int) blockPosition.x][(int) blockPosition.y][(int) blockPosition.z] = block;
	}
	
	public void removeBlock(Block block) {
		Vector3 blockPosition = block.getPosition();
		grid[(int) blockPosition.x][(int) blockPosition.y][(int) blockPosition.z] = null;
	}
	
	public int size() {
		return grid.length;
	}
	
	public Block[][][] getGrid() {
		return grid;
	}
}

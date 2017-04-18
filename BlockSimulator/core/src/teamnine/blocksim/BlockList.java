package teamnine.blocksim;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class BlockList implements Disposable {
	
	private int floorSize;
	private Grid grid;
	private ArrayList<Block> blockList = new ArrayList<Block>();
	private Block selectorBlock;

	public BlockList(int floorSize, Grid grid) {
		this.grid = grid;
		this.floorSize = floorSize;
		createFloor();
		
		
	}
	
	public int size() {
		return blockList.size();
	}
	
	public ArrayList<Block> getBlockList() {
		return blockList;
	}
	
	public Block getBlock(int i) {
		return blockList.get(i);
	}
	
	public void addBlock(Block block) {
		grid.addBlock(block);
		blockList.add(block);
	}
	
	public Block createBlock(Vector3 vector, Block.Type type) {
		Block newBlock = new Block(vector, type);
		grid.addBlock(newBlock);
		blockList.add(newBlock);
		return newBlock;
	}
	
	public Block createBlock (Vector3 vector, Block.Type type, double ID){
		Block newBlock = new Block(vector, type);
		grid.addBlock(newBlock);
		blockList.add(newBlock);
		newBlock.setID(ID);
		return newBlock;
	}
	
	public void removeBlock(Block block) {
		grid.removeBlock(block);
		blockList.remove(block);
	}
	
	public void removeBlock(int i) {
		grid.removeBlock(blockList.remove(i));
		blockList.remove(i);
	}
	
	public Block blockAtPoint(Vector3 point) {
		for(int i = 0; i < blockList.size(); i++) {
			if(blockList.get(i).getPosition().equals(point)) {
				return blockList.get(i);
			}
		}
		return null;
	}
	
	public void createFloor() {
		for(int x = 0; x < floorSize; x++) {
			for(int z = 0; z < floorSize; z++) {
				createBlock(new Vector3(x, 0, z), Block.Type.Floor);
			}
		}
	}
	
	/**
	 * Please take into account that Y and Z coordinates are interchanged in this 
	 */
	public void createRobot(String[][] data) { 
		
		
		for(int i = 0; i<data.length; i++){
			//System.out.println(Float.parseFloat(data[i][1])+"; "+Float.parseFloat(data[i][3])+"; "+Float.parseFloat(data[i][2]));
			
			try{
				double newID = Double.parseDouble(data[i][0]);
				createBlock(new Vector3(Float.parseFloat(data[i][1]), 
						   (Float.parseFloat(data[i][3])+1), //WATCH OUT: INCREASED BY 1!!!!!!!!!!!!!!!!!!!!!!!!!
							Float.parseFloat(data[i][2])), 
							Block.Type.Robot, newID);
			}
			catch (NumberFormatException e){
				createBlock(new Vector3(Float.parseFloat(data[i][1]), 
						   (Float.parseFloat(data[i][3])+1), //WATCH OUT: INCREASED BY 1!!!!!!!!!!!!!!!!!!!!!!!!!
							Float.parseFloat(data[i][2])), 
							Block.Type.Robot);
			}
		}
	}
	
	/**
	 * Please take into account that Y and Z are interchanged in this method
	 */
	public void createTarget(String[][] data) {
		
		for (int i = 0; i<data.length; i++){
			try{
				double newID = Double.parseDouble(data[i][0]);
				createBlock(new Vector3(Float.parseFloat(data[i][1]), 
						   (Float.parseFloat(data[i][3])+1), //WATCH OUT: INCREASED BY 1!!!!!!!!!!!!!!!!!!!!!!!!!
							Float.parseFloat(data[i][2])), 
							Block.Type.Goal, newID);
			}
			catch (NumberFormatException e){
				createBlock(new Vector3(Float.parseFloat(data[i][1]), 
						   (Float.parseFloat(data[i][3])+1), //WATCH OUT: INCREASED BY 1!!!!!!!!!!!!!!!!!!!!!!!!!
							Float.parseFloat(data[i][2])), 
							Block.Type.Goal);
			}
		}
	}
	
	/**
	 * Please take into account that Y and Z are interchanged in this method
	 * Obstacles don't have ID's ?????
	 */
	public void createObstacles(String[][] data) {
		
		for (int i = 0; i<data.length; i++){
			createBlock(new Vector3(Float.parseFloat(data[i][0]),
								   (Float.parseFloat(data[i][2])+1),
									Float.parseFloat(data[i][1])),
									Block.Type.Obstacle);
		}
	}
	
	public void render(ModelBatch modelBatch, Environment environment) {
		for(int i = 0; i < blockList.size(); i++) {
			blockList.get(i).moveModel();
			modelBatch.render(blockList.get(i).getModelInstance(), environment);
		}
	}
	
	public int getFloorsize() {
		return this.floorSize;
	}
	
	
	public void setFloorsize(int floorSize) {
		this.floorSize = floorSize;
		createFloor();
	}
	
	public void setSelectorBlock(Block selectorBlock) {
		this.selectorBlock = selectorBlock;
	}
	
	@Override
	public void dispose() {
		//Dispose all Blocks
		for(int i = 0; i < blockList.size(); i++) {
			blockList.get(i).dispose();
		}
	}
	
	public void editBoxByRayCast(Vector3 start_point, Vector3 direction, Block.Type type) {
		int last_point_x = 0;
		int last_point_y = 0;
		int last_point_z = 0;
		for (int i = 1; i < floorSize * 2; i++) {
			Vector3 tmp_start = new Vector3(start_point);
			Vector3 tmp_direction = new Vector3(direction);
			tmp_direction.nor();
			tmp_direction.scl(i);
			Vector3 line = tmp_start.add(tmp_direction);
			int x = Math.round(line.x);
			int y = Math.round(line.y);
			int z = Math.round(line.z);
			if (x > (floorSize - 1) || y > (floorSize - 1) || z > (floorSize - 1) || x < 0 || y < 0 || z < 0) {
				break;
			}
			if (grid.getGrid()[x][y][z] != null) {
				if (type == null) {
					if (grid.getGrid()[x][y][z] != null && grid.getGrid()[x][y][z].getType() != Block.Type.Floor) {
						removeBlock(blockAtPoint(new Vector3(x, y, z)));
					}
				} else {
					createBlock(new Vector3(last_point_x, last_point_y, last_point_z), type);
					selectorBlock.setPosition(new Vector3(last_point_x, last_point_y, last_point_z));
				}
				break;
			}
			last_point_x = x;
			last_point_y = y;
			last_point_z = z;
		}
	}
	
	public void moveSelectorBlock(Vector3 start_point, Vector3 direction) {
		int last_point_x = 0;
		int last_point_y = 0;
		int last_point_z = 0;
		for (int i = 1; i < floorSize * 2; i++) {
			Vector3 tmp_start = new Vector3(start_point);
			Vector3 tmp_direction = new Vector3(direction);
			tmp_direction.nor();
			tmp_direction.scl(i);
			Vector3 line = tmp_start.add(tmp_direction);
			int x = Math.round(line.x);
			int y = Math.round(line.y);
			int z = Math.round(line.z);
			if (x > (floorSize - 1) || y > (floorSize - 1) || z > (floorSize - 1) || x < 0 || y < 0 || z < 0) {
				break;
			}
			if (grid.getGrid()[x][y][z] != null) {
				selectorBlock.setPosition(new Vector3(last_point_x, last_point_y, last_point_z));
				break;
			}
			last_point_x = x;
			last_point_y = y;
			last_point_z = z;
		}
	}
	
	// for collition detection...
	public boolean hittingBox(Vector3 point) {
		point.scl(1 / floorSize);
		int x = Math.round(point.x);
		int y = Math.round(point.y);
		int z = Math.round(point.z);
		
		if(grid.getGrid()[x][y][z] != null){
			return true;
		} else {
			return false;
		}
	}
	
}

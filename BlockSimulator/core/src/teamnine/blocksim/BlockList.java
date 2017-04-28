package teamnine.blocksim;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import java.util.Stack;
import com.badlogic.gdx.utils.Disposable;

public class BlockList implements Disposable {

	// Block Simulator
	BlockSimulator blockSimulator;

	// Grid
	private int gridSize;

	// Block Lists
	private ArrayList<Block> blockList = new ArrayList<Block>();
	private ArrayList<Block> floorList = new ArrayList<Block>();
	private ArrayList<Block> obstacleList = new ArrayList<Block>();
	private ArrayList<Block> goalList = new ArrayList<Block>();
	private ArrayList<RobotBlock> robotList = new ArrayList<RobotBlock>();

	// Selector Block
	private Block selectorBlock;

	// Stacks
	private Stack<Action> undoQueue = new Stack<Action>();
	private Stack<Action> redoQueue = new Stack<Action>();

	// Register Action
	boolean registerAction = true;

	public BlockList(int gridSize, BlockSimulator blockSimulator) {
		this.gridSize = gridSize;
		this.blockSimulator = blockSimulator;
		createFloor();
	}

	public int size() {
		return blockList.size();
	}

	public ArrayList<Block> getBlockList() {
		return blockList;
	}

	public ArrayList<Block> getBlockList(Block.Type type) {
		if (type == Block.Type.Floor) {
			return floorList;
		} else if (type == Block.Type.Goal) {
			return goalList;
		} else if (type == Block.Type.Obstacle) {
			return obstacleList;
		}
		return null;
	}
	public int getGridSize()
	{
		return gridSize;
	}
	public ArrayList<RobotBlock> getRobotBlockList() {
		return robotList;
	}
	public ArrayList<Block> getObstacleList() {
		return obstacleList;
	}
	public ArrayList<Block> getTargetList() {
		return goalList;
	}
	public Block getBlock(int i) {
		return blockList.get(i);
	}

	public void addBlock(Block block) {

		// Add Adder Action To Queue
		if (registerAction) {
			undoQueue.push(new Action(block.getPosition(), block.getType(), block.getID(), false));
			redoQueue.clear();
		}

		// Add Block to different List
		if (block.getType() == Block.Type.Floor) {
			floorList.add(block);
		} else if (block.getType() == Block.Type.Goal) {
			goalList.add(block);
		} else if (block.getType() == Block.Type.Obstacle) {
			obstacleList.add(block);
		}

		registerAction = true;
		blockList.add(block);
	}

	public Block createBlock(Vector3 vector, Block.Type type) {
		if (type == Block.Type.Robot) {
			RobotBlock robotBlock = new RobotBlock(vector, type);
			robotList.add(robotBlock);
			addBlock((Block) robotBlock);
			return robotBlock;
		} else {
			Block block = new Block(vector, type);
			addBlock(block);
			return block;
		}
	}

	public Block createBlock(Vector3 vector, Block.Type type, double ID) {
		if (type == Block.Type.Robot) {
			RobotBlock robotBlock = new RobotBlock(vector, type);
			robotList.add(robotBlock);
			addBlock((Block) robotBlock);
			robotBlock.setID(ID);
			return robotBlock;
		} else {
			Block block = new Block(vector, type);
			addBlock(block);
			block.setID(ID);
			return block;
		}
	}

	public void removeBlock(Block block) {

		// Add Removal Action To Queue
		if (registerAction) {
			undoQueue.push(new Action(block.getPosition(), block.getType(), block.getID(), true));
			redoQueue.clear();
		}

		// Add Block to different List
		if (block.getType() == Block.Type.Floor) {
			floorList.remove(block);
		} else if (block.getType() == Block.Type.Goal) {
			goalList.remove(block);
		} else if (block.getType() == Block.Type.Obstacle) {
			obstacleList.remove(block);
		}

		// If it's a robot block
		if (block.getType() == Block.Type.Robot) {
			robotList.remove(block);
		}

		registerAction = true;
		blockList.remove(block);
	}

	public void removeBlock(int i) {
		Block block = blockList.get(i);

		// Add Removal Action To Queue
		if (registerAction) {
			undoQueue.push(new Action(block.getPosition(), block.getType(), block.getID(), true));
			redoQueue.clear();
		}
		registerAction = true;
		blockList.remove(block);

		// If it's a robot block
		if (block.getType() == Block.Type.Robot) {
			robotList.remove(block);
		}
	}

	public Block blockAtPoint(Vector3 point) {
		for (int i = 0; i < blockList.size(); i++) {
			if (blockList.get(i).getPosition().cpy().sub(point).isZero()) {
				return blockList.get(i);
			}
		}
		return null;
	}

	public void resizeFloor(int gridSize) {
		if (gridSize <= 0) {
			blockSimulator.gridSize = 0;
			return;
		}

		this.gridSize = gridSize;

		ArrayList<Block> floorBlocks = new ArrayList<Block>();
		for (Block block : blockList) {
			if (block.getType() == Block.Type.Floor) {
				floorBlocks.add(block);
			}
		}
		blockList.removeAll(floorBlocks);
		createFloor();
	}

	public void createFloor() {
		for (int x = 0; x < gridSize; x++) {
			for (int z = 0; z < gridSize; z++) {
				registerAction = false;
				createBlock(new Vector3(x, 0, z), Block.Type.Floor);
			}
		}
	}

	/**
	 * Please take into account that Y and Z coordinates are interchanged in
	 * this
	 */
	// TODO: IMPLEMENT ID RECOGNITION
	public void createRobot(String[][] data) {

		for (int i = 0; i < data.length; i++) {
			// System.out.println(Float.parseFloat(data[i][1])+";
			// "+Float.parseFloat(data[i][3])+";
			// "+Float.parseFloat(data[i][2]));

			try {
				double newID = Double.parseDouble(data[i][0]);
				createBlock(new Vector3(Float.parseFloat(data[i][1]), (Float.parseFloat(data[i][3]) + 1), // WATCH
																											// OUT:
																											// INCREASED
																											// BY
																											// 1!!!!!!!!!!!!!!!!!!!!!!!!!
						Float.parseFloat(data[i][2])), Block.Type.Robot, newID);
			} catch (NumberFormatException e) {
				System.out.println("Exception");
				createBlock(new Vector3(Float.parseFloat(data[i][1]), (Float.parseFloat(data[i][3]) + 1), // WATCH
																											// OUT:
																											// INCREASED
																											// BY
																											// 1!!!!!!!!!!!!!!!!!!!!!!!!!
						Float.parseFloat(data[i][2])), Block.Type.Robot);
			}
		}
	}

	/**
	 * Please take into account that Y and Z are interchanged in this method
	 */
	// TODO: IMPLEMENT ID RECOGNITION/leave it out properly
	public void createTarget(String[][] data) {

		for (int i = 0; i < data.length; i++) {
			try {
				double newID = Double.parseDouble(data[i][0]);
				createBlock(new Vector3(Float.parseFloat(data[i][1]), (Float.parseFloat(data[i][3]) + 1), // WATCH
																											// OUT:
																											// INCREASED
																											// BY
																											// 1!!!!!!!!!!!!!!!!!!!!!!!!!
						Float.parseFloat(data[i][2])), Block.Type.Goal, newID);
			} catch (NumberFormatException e) {
				System.out.println("Exception");
				createBlock(new Vector3(Float.parseFloat(data[i][1]), (Float.parseFloat(data[i][3]) + 1), // WATCH
																											// OUT:
																											// INCREASED
																											// BY
																											// 1!!!!!!!!!!!!!!!!!!!!!!!!!
						Float.parseFloat(data[i][2])), Block.Type.Goal);
			}
		}
	}

	/**
	 * Please take into account that Y and Z are interchanged in this method
	 * Obstacles don't have ID's ?????
	 */
	// TODO: IMPLEMENT ID RECOGNITION FOR OBSTACLES
	public void createObstacles(String[][] data) {

		for (int i = 0; i < data.length; i++) {
			createBlock(new Vector3(Float.parseFloat(data[i][0]), (Float.parseFloat(data[i][2]) + 1),
					Float.parseFloat(data[i][1])), Block.Type.Obstacle);
		}
	}

	public void render(ModelBatch modelBatch, Environment environment) {
		for (int i = 0; i < blockList.size(); i++) {
			Block block = blockList.get(i);
			block.moveModel();

			// Check for collision
			if (block.getType() == Block.Type.Robot) {
				for (int j = 0; j < blockList.size(); j++) {
					// Don't check if block is same
					if (block == blockList.get(j)) {
						continue;
					}

					// Check for intersection between blocks
					if (block.intersect(blockList.get(j))) {
						// Detecting Collision
						//TODO Stop movement
					}
				}
			}

			modelBatch.render(blockList.get(i).getModelInstance(), environment);
		}
	}

	public void setSelectorBlock(Block selectorBlock) {
		this.selectorBlock = selectorBlock;
	}

	public void editBoxByRayCast(Vector3 start_point, Vector3 direction, Block.Type type) {
		int last_point_x = 0;
		int last_point_y = 0;
		int last_point_z = 0;
		for (int i = 1; i < gridSize * 2; i++) {
			Vector3 tmp_start = new Vector3(start_point);
			Vector3 tmp_direction = new Vector3(direction);
			tmp_direction.nor();
			tmp_direction.scl(i);
			Vector3 line = tmp_start.add(tmp_direction);
			int x = Math.round(line.x);
			int y = Math.round(line.y);
			int z = Math.round(line.z);
			if (x > (gridSize - 1) || y > (gridSize - 1) || z > (gridSize - 1) || x < 0 || y < 0 || z < 0) {
				break;
			}
			if (blockAtPoint(new Vector3(x, y, z)) != null) {
				if (type == null) {
					if (blockAtPoint(new Vector3(x, y, z)) != null
							&& blockAtPoint(new Vector3(x, y, z)).getType() != Block.Type.Floor) {
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
		for (int i = 1; i < gridSize * 2; i++) {
			Vector3 tmp_start = new Vector3(start_point);
			Vector3 tmp_direction = new Vector3(direction);
			tmp_direction.nor();
			tmp_direction.scl(i);
			Vector3 line = tmp_start.add(tmp_direction);
			int x = Math.round(line.x);
			int y = Math.round(line.y);
			int z = Math.round(line.z);
			if (x > (gridSize - 1) || y > (gridSize - 1) || z > (gridSize - 1) || x < 0 || y < 0 || z < 0) {
				break;
			}
			if (blockAtPoint(new Vector3(x, y, z)) != null) {
				selectorBlock.setPosition(new Vector3(last_point_x, last_point_y, last_point_z));
				break;
			}
			last_point_x = x;
			last_point_y = y;
			last_point_z = z;
		}
	}

	// for collision detection...
	public boolean hittingBox(Vector3 point) {
		point.scl(1 / gridSize);
		int x = Math.round(point.x);
		int y = Math.round(point.y);
		int z = Math.round(point.z);

		if (blockAtPoint(new Vector3(x, y, z)) != null) {
			return true;
		} else {
			return false;
		}
	}

	public void undo() {
		if (undoQueue.isEmpty()) {
			blockSimulator.notification.setNotification("Can't Undo", Notification.Type.Error);
			return;
		}

		Action undoAction = undoQueue.pop();

		// If it was removal Action
		if (undoAction.wasRemoved()) {
			registerAction = false;
			createBlock(undoAction.getPosition(), undoAction.getBlockType(), undoAction.getID());
			redoQueue.push(new Action(undoAction.getPosition(), undoAction.getBlockType(), undoAction.getID(), false));
		} else {
			registerAction = false;
			removeBlock(blockAtPoint(undoAction.getPosition()));
			redoQueue.push(new Action(undoAction.getPosition(), undoAction.getBlockType(), undoAction.getID(), true));
		}
	}

	public void redo() {
		if (redoQueue.isEmpty()) {
			blockSimulator.notification.setNotification("Can't Redo", Notification.Type.Error);
			return;
		}

		Action redoAction = redoQueue.pop();

		// If it was removal Action
		if (redoAction.wasRemoved()) {
			createBlock(redoAction.getPosition(), redoAction.getBlockType(), redoAction.getID());
		} else {
			removeBlock(blockAtPoint(redoAction.getPosition()));
		}
	}

	@Override
	public void dispose() {
		// Dispose all Blocks
		for (int i = 0; i < blockList.size(); i++) {
			blockList.get(i).dispose();
		}
	}

}

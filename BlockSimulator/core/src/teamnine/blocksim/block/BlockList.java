package teamnine.blocksim.block;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import teamnine.blocksim.Action;
import teamnine.blocksim.BlockSimulator;
import teamnine.blocksim.hud.Notification;

public class BlockList implements Disposable
{

	// Block Simulator
	BlockSimulator blockSimulator;

	// Grid
	private int gridSize;

	// Block Lists
	private ArrayList<Block> blockList = new ArrayList<Block>();
	private ArrayList<Block> floorList = new ArrayList<Block>();
	private ArrayList<Block> obstacleList = new ArrayList<Block>();
	private ArrayList<Block> goalList = new ArrayList<Block>();
	private ArrayList<Block> pathList = new ArrayList<Block>();
	private ArrayList<RobotBlock> robotList = new ArrayList<RobotBlock>();

	// Register Action
	boolean registerAction = true;

	// Speed for Robot Blocks
	private float speed = 5;

	public BlockList(int gridSize, BlockSimulator blockSimulator)
	{
		this.gridSize = gridSize;
		this.blockSimulator = blockSimulator;
		createFloor();

		// Temporary
		preconfig();
	}

	public ArrayList<Block> getFloor()
	{
		return floorList;
	}

	public void preconfig()
	{
		// Robots
		createBlock(new Vector3(0, 1, 10), Block.Type.Robot);
		createBlock(new Vector3(1, 1, 10), Block.Type.Robot);
		createBlock(new Vector3(0, 2, 10), Block.Type.Robot);
		createBlock(new Vector3(0, 3, 10), Block.Type.Robot);
		createBlock(new Vector3(1, 2, 10), Block.Type.Robot);
		createBlock(new Vector3(1, 3, 10), Block.Type.Robot);
		createBlock(new Vector3(1, 4, 10), Block.Type.Robot);
		createBlock(new Vector3(1, 5, 10), Block.Type.Robot);
		createBlock(new Vector3(1, 6, 10), Block.Type.Robot);

		// Obstacle
		createBlock(new Vector3(15, 1, 15), Block.Type.Obstacle);

		// Goal
		createBlock(new Vector3(12, 1, 12), Block.Type.Goal);
		createBlock(new Vector3(12, 1, 13), Block.Type.Goal);
	}

	public ArrayList<Block> getObstacleList()
	{
		return obstacleList;
	}

	public ArrayList<Block> getGoalList()
	{
		return goalList;
	}

	public int size()
	{
		return blockList.size();
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(boolean increase)
	{
		for (RobotBlock block : robotList)
		{
			if (!increase && speed - 1 <= 0)
			{
				speed = 1;
				block.setSpeed(speed);
				continue;
			}
			block.setSpeed(increase ? (speed = speed + 1) : (speed = speed - 1));
		}
	}

	public ArrayList<RobotBlock> getRobotBlockList()
	{
		return robotList;
	}

	public ArrayList<Block> getBlockList(Block.Type type)
	{
		if (type == null)
		{
			return blockList;
		}
		if (type == Block.Type.Floor)
		{
			return floorList;
		}
		else if (type == Block.Type.Goal)
		{
			return goalList;
		}
		else if (type == Block.Type.Obstacle)
		{
			return obstacleList;
		}
		else if (type == Block.Type.Path)
		{
			return pathList;
		}
		return null;
	}

	public int getGridSize()
	{
		return gridSize;
	}

	public Block getBlock(int i)
	{
		return blockList.get(i);
	}

	public void addBlock(Block block)
	{
		// Add Block to different List
		if (block.getType() == Block.Type.Floor)
		{
			floorList.add(block);
		}
		else if (block.getType() == Block.Type.Goal)
		{
			goalList.add(block);
		}
		else if (block.getType() == Block.Type.Obstacle)
		{
			obstacleList.add(block);
		}
		else if (block.getType() == Block.Type.Path)
		{
			pathList.add(block);
		}

		registerAction = true;
		blockList.add(block);
	}

	public Block createBlock(Vector3 vector, Block.Type type)
	{
		if (type == Block.Type.Robot)
		{
			RobotBlock robotBlock = new RobotBlock(vector, type, speed, this);
			robotList.add(robotBlock);
			addBlock((Block) robotBlock);
			return robotBlock;
		}
		else
		{
			Block block = new Block(vector, type);
			addBlock(block);
			return block;
		}
	}

	public Block createBlock(Vector3 vector, Block.Type type, double ID)
	{
		if (type == Block.Type.Robot)
		{
			RobotBlock robotBlock = new RobotBlock(vector, type, speed, this);
			robotList.add(robotBlock);
			addBlock((Block) robotBlock);
			robotBlock.setID(ID);
			return robotBlock;
		}
		else
		{
			Block block = new Block(vector, type);
			addBlock(block);
			block.setID(ID);
			return block;
		}
	}

	public void removeBlock(Block block)
	{
		// Add Block to different List
		if (block.getType() == Block.Type.Floor)
		{
			floorList.remove(block);
		}
		else if (block.getType() == Block.Type.Goal)
		{
			goalList.remove(block);
		}
		else if (block.getType() == Block.Type.Obstacle)
		{
			obstacleList.remove(block);
		}

		// If it's a robot block
		if (block.getType() == Block.Type.Robot)
		{
			robotList.remove(block);
		}

		registerAction = true;
		blockList.remove(block);
	}

	public void removeBlockType(Block.Type type)
	{
		if (type == Block.Type.Floor)
		{
			blockList.removeAll(floorList);
			floorList.clear();
		}
		else if (type == Block.Type.Goal)
		{
			blockList.removeAll(goalList);
			goalList.clear();
		}
		else if (type == Block.Type.Obstacle)
		{
			blockList.removeAll(obstacleList);
			obstacleList.clear();
		}
		else if (type == Block.Type.Path)
		{
			blockList.removeAll(pathList);
			pathList.clear();
		}
	}

	public void removeBlock(int i)
	{
		Block block = blockList.get(i);

		registerAction = true;
		blockList.remove(block);

		// If it's a robot block
		if (block.getType() == Block.Type.Robot)
		{
			robotList.remove(block);
		}
	}

	public Block blockAtPoint(Vector3 point)
	{
		for (int i = 0; i < blockList.size(); i++)
		{
			if (blockList.get(i).getPosition().cpy().sub(point).isZero())
			{
				return blockList.get(i);
			}
		}
		return null;
	}

	public void resizeFloor(int gridSize)
	{
		if (gridSize <= 0)
		{
			blockSimulator.gridSize = 0;
			return;
		}

		this.gridSize = gridSize;

		ArrayList<Block> floorBlocks = new ArrayList<Block>();
		for (Block block : blockList)
		{
			if (block.getType() == Block.Type.Floor)
			{
				floorBlocks.add(block);
			}
		}
		blockList.removeAll(floorBlocks);
		createFloor();
	}

	public void createFloor()
	{
		for (int x = 0; x < gridSize; x++)
		{
			for (int z = 0; z < gridSize; z++)
			{
				registerAction = false;
				createBlock(new Vector3(x, 0, z), Block.Type.Floor);
			}
		}
	}

	public void render(ModelBatch modelBatch, Environment environment)
	{
		for (Block block : blockList)
		{
			if (block.getType() == Block.Type.Robot)
				block.moveModel();

			modelBatch.render(block.getModelInstance(), environment);
		}
	}

	public void editBoxByRayCast(Vector3 start_point, Vector3 direction, Block.Type type, boolean blockAction)
	{
		int last_point_x = 0;
		int last_point_y = 0;
		int last_point_z = 0;
		for (int i = 1; i < gridSize * 2; i++)
		{
			Vector3 tmp_start = new Vector3(start_point);
			Vector3 tmp_direction = new Vector3(direction);
			tmp_direction.nor();
			tmp_direction.scl(i);
			Vector3 line = tmp_start.add(tmp_direction);
			int x = Math.round(line.x);
			int y = Math.round(line.y);
			int z = Math.round(line.z);
			if (x > (gridSize - 1) || y > (gridSize - 1) || z > (gridSize - 1) || x < 0 || y < 0 || z < 0)
			{
				break;
			}
			if (blockAtPoint(new Vector3(x, y, z)) != null)
			{
				if (type == null && blockAction)
				{
					if (blockAtPoint(new Vector3(x, y, z)) != null && blockAtPoint(new Vector3(x, y, z)).getType() != Block.Type.Floor)
					{
						removeBlock(blockAtPoint(new Vector3(x, y, z)));
					}
				}
				else if (blockAction)
				{
					createBlock(new Vector3(last_point_x, last_point_y, last_point_z), type);
				}

				blockSimulator.selectorBlock.setPosition(new Vector3(last_point_x, last_point_y, last_point_z));
				break;
			}
			last_point_x = x;
			last_point_y = y;
			last_point_z = z;
		}
	}

	// for collision detection...
	public boolean hittingBox(Vector3 point)
	{
		point.scl(1 / gridSize);
		int x = Math.round(point.x);
		int y = Math.round(point.y);
		int z = Math.round(point.z);

		if (blockAtPoint(new Vector3(x, y, z)) != null)
			return true;
		else
			return false;
	}

	@Override
	public void dispose()
	{
		// Dispose all Blocks
		for (Block block : blockList)
		{
			block.dispose();
		}
	}

	public void disposeExceptFloor()
	{
		// Dispose all Blocks, except the floor
		for (int i = 0; i < blockList.size(); i++)
		{
			Block block = blockList.get(i);
			if (block.type != Block.Type.Floor)
			{
				blockList.remove(block);
				i--;
			}
		}
	}
}

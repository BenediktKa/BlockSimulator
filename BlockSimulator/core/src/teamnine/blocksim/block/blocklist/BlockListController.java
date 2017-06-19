package teamnine.blocksim.block.blocklist;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockType;
import teamnine.blocksim.block.RobotBlock;

/**
 * The BlockListController.
 */
public class BlockListController implements Disposable
{

	/** The block list controller. */
	private static BlockListController blockListController;
	/** The block list factory. */
	private BlockListFactory blockListFactory;

	/** The floor grid size. */
	private int floorGridSize;

	/**
	 * Instantiates a new block list controller.
	 */
	private BlockListController()
	{
		blockListFactory = new BlockListFactory();
		createFloor();
	}

	/**
	 * Gets the single instance of BlockListController.
	 *
	 * @return single instance of BlockListController
	 */
	public static BlockListController getInstance()
	{
		if (blockListController == null)
			return (blockListController = new BlockListController());
		else
			return blockListController;
	}

	/**
	 * Initialize.
	 *
	 * @param floorGridSize the floor grid size
	 */
	public void initialize(int floorGridSize)
	{
		this.floorGridSize = floorGridSize;
		createFloor();
	}

	/**
	 * Gets the floor grid size.
	 *
	 * @return the floor grid size
	 */
	public int getFloorGridSize()
	{
		return floorGridSize;
	}

	/**
	 * Sets the floor grid size.
	 *
	 * @param floorGridSize the new floor grid size
	 */
	public void setFloorGridSize(int floorGridSize)
	{
		this.floorGridSize = floorGridSize;
		removeAllBlocksOfType(BlockType.Floor);
	}

	/**
	 * Creates the floor.
	 */
	public void createFloor()
	{
		for (int x = 0; x < floorGridSize; x++)
			for (int z = 0; z < floorGridSize; z++)
				createBlock(new Vector3(x, 0, z), BlockType.Floor);
	}

	/**
	 * Gets the block list of specified block type.
	 *
	 * @param blockType the block type
	 * @return the block list
	 */
	public ArrayList<Block> getBlockList(BlockType blockType)
	{
		return blockListFactory.getBlockList(blockType);
	}

	/**
	 * Gets the full block list.
	 *
	 * @return the full block list
	 */
	public ArrayList<Block> getFullBlockList()
	{
		ArrayList<Block> blockList = new ArrayList<Block>();

		for (BlockType blockType : BlockType.values())
		{
			blockList.addAll(getBlockList(blockType));
		}

		return blockList;
	}

	/**
	 * Gets the combined block list.
	 *
	 * @param blockTypes the block types
	 * @return the combined block list
	 */
	public ArrayList<Block> getCombinedBlockList(BlockType... blockTypes)
	{
		ArrayList<Block> blockList = new ArrayList<Block>();

		for (BlockType blockType : blockTypes)
		{
			blockList.addAll(getBlockList(blockType));
		}

		return blockList;
	}

	/**
	 * Gets the excluded block list.
	 *
	 * @param blockTypes the block types
	 * @return the excluded block list
	 */
	public ArrayList<Block> getExcludedBlockList(BlockType... blockTypes)
	{
		ArrayList<Block> blockList = new ArrayList<Block>();
		ArrayList<BlockType> excludedTypes = new ArrayList<BlockType>(Arrays.asList(blockTypes));

		for (BlockType blockType : BlockType.values())
		{
			if (excludedTypes.contains(blockType))
				continue;

			blockList.addAll(getBlockList(blockType));
		}

		return blockList;
	}

	/**
	 * Creates a block of specified position and type.
	 *
	 * @param position the position
	 * @param blockType the block type
	 */
	public void createBlock(Vector3 position, BlockType blockType)
	{
		if (position == null)
			return;
		Block block;
		if(blockType == BlockType.Robot)
			block = new RobotBlock(position, blockType);
		else
			block = new Block(position, blockType);
		
		getBlockList(blockType).add(block);
	}
	
	/**
	 * Creates a block of specified position and type and ID.
	 *
	 * @param position the position
	 * @param blockType the block type
	 * @param ID the id
	 */
	public void createBlock(Vector3 position, BlockType blockType, double ID)
	{
		if (position == null)
			return;

		Block block;
		if(blockType == BlockType.Robot)
			block = new RobotBlock(position, blockType, ID);
		else
			block = new Block(position, blockType, ID);
		getBlockList(blockType).add(block);
	}

	/**
	 * Removes a block by it's reference.
	 *
	 * @param block the block
	 */
	public void removeBlock(Block block)
	{
		if (block == null)
			return;

		getBlockList(block.getType()).remove(block);
	}

	/**
	 * Removes the all blocks of the specified type(s).
	 *
	 * @param blockTypes the block types
	 */
	public void removeAllBlocksOfType(BlockType... blockTypes)
	{
		for (BlockType blockType : blockTypes)
			getBlockList(blockType).clear();
	}
	
	/**
	 * Removes the all blocks ignoring the specified type(s).
	 *
	 * @param blockTypes the block types
	 */
	public void removeAllBlocksIgnoreType(BlockType... blockTypes)
	{
		ArrayList<BlockType> excludedTypes = new ArrayList<BlockType>(Arrays.asList(blockTypes));

		for (BlockType blockType : BlockType.values())
		{
			if (excludedTypes.contains(blockType))
				continue;
			
			getBlockList(blockType).clear();
		}
	}

	/**
	 * Gets a block at a specified point.
	 *
	 * @param position the position
	 * @return the block at point
	 */
	public Block getBlockAtPoint(Vector3 position)
	{
		for (Block block : getFullBlockList())
			if (block.getPosition().equals(position))
				return block;

		return null;
	}
	
	/**
	 * Gets a block type at a specified point.
	 *
	 * @param position the position
	 * @return the block type at point
	 */
	public BlockType getBlockTypeAtPoint(Vector3 position)
	{
		for (Block block : getFullBlockList())
			if (block.getPosition().equals(position))
				return block.getType();

		return null;
	}

	/**
	 * Gets a block at a specified point which is not any of the specified block
	 * types.
	 *
	 * @param position the position
	 * @param blockTypes the block types
	 * @return the block at point ignore type
	 */
	public Block getBlockAtPointIgnoreType(Vector3 position, BlockType... blockTypes)
	{
		for (Block block : getExcludedBlockList(blockTypes))
			if (block.getPosition().equals(position))
				return block;

		return null;
	}

	/**
	 * Renders the blocks.
	 *
	 * @param modelBatch the model batch
	 * @param environment the environment
	 */
	public void render(ModelBatch modelBatch, Environment environment)
	{
		for (Block block : getFullBlockList())
		{
			if (block.getType() == BlockType.Robot)
				block.moveModel();

			modelBatch.render(block.getModelInstance(), environment);
		}
	}

	/**
	 * Gets the position at ray cast.
	 *
	 * @param startPoint the start point
	 * @param direction the direction
	 * @param getBlockPointingAt the get block pointing at
	 * @return the position at ray cast
	 */
	public Vector3 getPositionAtRayCast(Vector3 startPoint, Vector3 direction, boolean getBlockPointingAt)
	{
		int last_point_x = 0;
		int last_point_y = 0;
		int last_point_z = 0;
		for (int i = 1; i < floorGridSize * 2; i++)
		{
			Vector3 tmp_start = new Vector3(startPoint);
			Vector3 tmp_direction = new Vector3(direction);
			tmp_direction.nor();
			tmp_direction.scl(i);
			Vector3 line = tmp_start.add(tmp_direction);
			int x = Math.round(line.x);
			int y = Math.round(line.y);
			int z = Math.round(line.z);
			if (x > (floorGridSize - 1) || y > (floorGridSize - 1) || z > (floorGridSize - 1) || x < 0 || y < 0 || z < 0)
				break;
			if (getBlockAtPoint(new Vector3(x, y, z)) != null)
			{
				if (getBlockPointingAt)
					return new Vector3(x, y, z);
				else
					return new Vector3(last_point_x, last_point_y, last_point_z);
			}
			last_point_x = x;
			last_point_y = y;
			last_point_z = z;
		}

		return null;
	}
	
	@Override
	public void dispose()
	{
		// Dispose all Blocks
		for (Block block : getFullBlockList())
			block.dispose();
	}

}

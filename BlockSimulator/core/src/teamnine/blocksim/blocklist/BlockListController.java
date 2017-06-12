package teamnine.blocksim.blocklist;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import teamnine.blocksim.block.Block;

/**
 * The BlockListController
 */
public class BlockListController implements Disposable
{
	private static BlockListController blockListController;
	/** The block list factory. */
	private BlockListFactory blockListFactory;

	private int floorGridSize;

	/**
	 * Instantiates a new block list controller.
	 */
	private BlockListController()
	{
		blockListFactory = new BlockListFactory();
		createFloor();
	}

	public static BlockListController getInstance()
	{
		if (blockListController == null)
			return (blockListController = new BlockListController());
		else
			return blockListController;
	}

	public void initialize(int floorGridSize)
	{
		this.floorGridSize = floorGridSize;
		createFloor();
	}

	public int getFloorGridSize()
	{
		return floorGridSize;
	}

	public void setFloorGridSize(int floorGridSize)
	{
		this.floorGridSize = floorGridSize;
		removeAllBlocksOfType(Block.Type.Floor);
	}
	
	public void createFloor()
	{
		for (int x = 0; x < floorGridSize; x++)
			for (int z = 0; z < floorGridSize; z++)
				createBlock(new Vector3(x, 0, z), Block.Type.Floor);
	}

	/**
	 * Gets the block list of specified block type.
	 *
	 * @param blockType the block type
	 * @return the block list
	 */
	public ArrayList<Block> getBlockList(Block.Type blockType)
	{
		ArrayList<Block> blockList;
		if ((blockList = blockListFactory.getBlockList(blockType)) == null)
		{
			throw new NullPointerException("BlockList of requested type could not be found");
		}
		else
		{
			return blockList;
		}
	}

	/**
	 * Gets the full block list.
	 *
	 * @return the full block list
	 */
	public ArrayList<Block> getFullBlockList()
	{
		ArrayList<Block> blockList = new ArrayList<Block>();

		for (Block.Type blockType : Block.Type.values())
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
	public ArrayList<Block> getCombinedBlockList(Block.Type... blockTypes)
	{
		ArrayList<Block> blockList = new ArrayList<Block>();

		for (Block.Type blockType : blockTypes)
		{
			blockList.addAll(getBlockList(blockType));
		}

		return blockList;
	}

	public ArrayList<Block> getExcludedBlockList(Block.Type... blockTypes)
	{
		ArrayList<Block> blockList = new ArrayList<Block>();
		ArrayList<Block.Type> excludedTypes = new ArrayList<Block.Type>(Arrays.asList(blockTypes));
				
		for (Block.Type blockType : Block.Type.values())
		{
			if(excludedTypes.contains(blockType))
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
	public void createBlock(Vector3 position, Block.Type blockType)
	{
		if (position == null)
			return;

		Block block = new Block(position, blockType);
		getBlockList(blockType).add(block);
	}

	/**
	 * Removes a block by it's reference.
	 *
	 * @param block the block
	 */
	public void removeBlock(Block block)
	{
		if(block == null)
			return;
		
		getBlockList(block.getType()).remove(block);
	}

	/**
	 * Removes the all blocks of the specified type(s).
	 *
	 * @param blockTypes the block types
	 */
	public void removeAllBlocksOfType(Block.Type... blockTypes)
	{
		for (Block.Type blockType : blockTypes)
			getBlockList(blockType).clear();
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
	 * Gets a block at a specified point which is not any of the specified block
	 * types.
	 *
	 * @param position the position
	 * @param blockTypes the block types
	 * @return the block at point ignore type
	 */
	public Block getBlockAtPointIgnoreType(Vector3 position, Block.Type... blockTypes)
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
			if (block.getType() == Block.Type.Robot)
				block.moveModel();

			modelBatch.render(block.getModelInstance(), environment);
		}
	}

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
			if(getBlockAtPoint(new Vector3(x, y, z)) != null)
			{
				if(getBlockPointingAt)
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

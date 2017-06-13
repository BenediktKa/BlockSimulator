package teamnine.blocksim.block.blocklist;

import java.util.ArrayList;
import java.util.HashMap;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockType;

/**
 * A factory for creating BlockList objects.
 */
public class BlockListFactory
{

	/** BlockList HashMap */
	private static final HashMap<BlockType, ArrayList<Block>> blockLists = new HashMap<BlockType, ArrayList<Block>>();

	/**
	 * Gets the block list of a specified block type
	 *
	 * @param blockType the block type
	 * @return the block list
	 */
	public ArrayList<Block> getBlockList(BlockType blockType)
	{
		// Check if BlockArray of certain type already exists
		ArrayList<Block> blockArray = blockLists.get(blockType);

		// If BlockArray of certain type doesn't exists
		if (blockArray == null)
		{
			blockArray = new ArrayList<Block>();

			// Add new BlockArray to HashMap
			blockLists.put(blockType, blockArray);
		}

		return blockArray;
	}
}

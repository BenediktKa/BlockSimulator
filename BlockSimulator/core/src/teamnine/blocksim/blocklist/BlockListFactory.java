package teamnine.blocksim.blocklist;

import java.util.ArrayList;
import java.util.HashMap;

import teamnine.blocksim.block.Block;

/**
 * A factory for creating BlockList objects.
 */
public class BlockListFactory
{

	/** BlockList HashMap */
	private static final HashMap<Block.Type, ArrayList<Block>> blockLists = new HashMap<Block.Type, ArrayList<Block>>();

	/**
	 * Gets the block list of a specified block type
	 *
	 * @param blockType the block type
	 * @return the block list
	 */
	public ArrayList<Block> getBlockList(Block.Type blockType)
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

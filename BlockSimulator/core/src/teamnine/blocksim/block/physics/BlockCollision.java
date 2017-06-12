package teamnine.blocksim.block.physics;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.blocklist.BlockListController;

public class BlockCollision extends BlockPhysics
{
	private BlockListController blockListController = null;

	public BlockCollision(Vector3 position, Type type)
	{
		super(position, type);
		this.blockListController = BlockListController.getInstance();
	}

	public boolean isColliding(Block blockNotToCheck, Vector3 position)
	{
		BlockHitbox hitbox = new BlockHitbox(position);
		for (Block block : blockListController.getExcludedBlockList(Block.Type.Path, Block.Type.Goal))
		{
			if (block == blockNotToCheck)
				continue;

			if (hitbox.intersect(block.getHitbox()))
				return true;
		}
		return false;
	}
}

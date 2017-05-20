package teamnine.blocksim.block;

import com.badlogic.gdx.math.Vector3;

public class BlockCollision extends Block
{
	private BlockList blockList;

	public BlockCollision(Vector3 position, Type type, BlockList blockList)
	{
		super(position, type);
		this.blockList = blockList;
	}

	public boolean isColliding(Block blockNotToCheck, Vector3 position)
	{
		BlockHitbox hitbox = new BlockHitbox(position);
		for (Block block : blockList.getBlockList())
		{
			if (block.getType() == Block.Type.Path || block.getType() == Block.Type.Goal)
				continue;

			if (block == blockNotToCheck)
				continue;

			if (hitbox.intersect(block.getHitbox()))
				return true;
		}
		return false;
	}
}
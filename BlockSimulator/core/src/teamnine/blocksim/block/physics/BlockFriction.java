package teamnine.blocksim.block.physics;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.BlockList;

public class BlockFriction extends BlockCollision
{

	public BlockFriction(Vector3 position, Type type, BlockList blockList)
	{
		super(position, type, blockList);
	}

	public float calcFriction(float speed)
	{
		if (speed == 0)
			return 0;
		else if (speed > 0)
			return getFrictionConstant() * -getGravity();
		else
			return getFrictionConstant() * getGravity();

	}
}

package teamnine.blocksim.block.physics;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.BlockList;

public class BlockFriction extends BlockCollision
{
	// Physic constants
	protected final float GRAVITY = 0.098f;
	protected final float TERMINALVELOCITY = 10;
	protected final float FRICTIONCONSTANT = 0.5f;
	
	public BlockFriction(Vector3 position, Type type, BlockList blockList)
	{
		super(position, type, blockList);
	}
	
	public float calcFriction(float speed)
	{
		if(speed == 0)
			return 0;
		else if(speed > 0)
			return FRICTIONCONSTANT * -GRAVITY;
		else
			return FRICTIONCONSTANT * GRAVITY;
	}
}

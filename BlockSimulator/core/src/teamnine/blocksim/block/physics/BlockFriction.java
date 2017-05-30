package teamnine.blocksim.block.physics;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockList;

public class BlockFriction extends BlockCollision
{

	private BlockList blockList;
	
	public BlockFriction(Vector3 position, Type type, BlockList blockList)
	{
		super(position, type, blockList);
		this.blockList = blockList;
	}

	public float calcFriction(float speed)
	{
		float friction = 0;
		
		if (speed == 0)
			return 0;
		else if (speed > 0)
			return -(constantFriction() + dynamicFriction());
		else
			return (constantFriction() + dynamicFriction());
	}
	
	public float constantFriction()
	{	
		return getFrictionConstant() * getGravity();
	}
	
	public float dynamicFriction()
	{
		return touchingSurface() * getFrictionConstant() * getGravity();
	}
	
	public float touchingSurface()
	{
		Block blockUnder = blockList.blockAtPointIgnoreGoal(new Vector3(Math.round(position.x), Math.round(position.y) - 1, Math.round(position.z)));
		
		if(blockUnder == null)
			return 0;
		
		float xDifference = Math.abs(position.x + 1 - blockUnder.getPosition().x);
		float zDifference =  Math.abs(position.z + 1 - blockUnder.getPosition().z);
		return xDifference * zDifference;
	}
}

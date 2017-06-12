package teamnine.blocksim.block.physics;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.blocklist.BlockListController;

public class BlockFriction extends BlockCollision
{

	private BlockListController blockListController = null;
	
	public BlockFriction(Vector3 position, Type type)
	{
		super(position, type);
		this.blockListController = BlockListController.getInstance();
	}

	public float calcFriction(float speed)
	{		
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
		Block blockUnder = blockListController.getBlockAtPointIgnoreType(new Vector3(Math.round(position.x), Math.round(position.y) - 1, Math.round(position.z)), Block.Type.Goal);
		
		if(blockUnder == null)
			return 0;
		
		float xDifference = Math.abs(position.x + 1 - blockUnder.getPosition().x);
		float zDifference =  Math.abs(position.z + 1 - blockUnder.getPosition().z);
		return xDifference * zDifference;
	}
}

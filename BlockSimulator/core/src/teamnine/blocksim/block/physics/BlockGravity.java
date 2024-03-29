package teamnine.blocksim.block.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.BlockModel;
import teamnine.blocksim.block.BlockType;

public class BlockGravity extends BlockFriction
{

	// Fall velocity
	private float fallVel;
	protected boolean gravity = false;

	// Timer to recalculate gravity
	private float gravityTime = 0;
	
	private Vector3 originalPos;

	public BlockGravity(Vector3 position, BlockType type)
	{
		super(position, type);
	}

	public void setGravity(boolean gravity)
	{
		// Reset Gravity
		fallVel = 0;

		// Reset Time
		gravityTime = 0;
		
		// Set Gravity Boolean
		this.gravity = gravity;
		
		rbText.setVerticalText(fallVel);
	}

	public void calcFallVel()
	{
		fallVel += getGravity() * gravityTime;

		// Can't go faster than terminal velocity
		if (fallVel > getTerminalVelocity())
			fallVel = getTerminalVelocity();
		
		rbText.setVerticalText(fallVel);
	}

	public void moveModel()
	{
		if (gravity)
		{
			// This will add to a timer, which once it hits 1 will increase
			// gravity
			gravityTime += Gdx.graphics.getDeltaTime();

			calcFallVel();

			// Check if the next update downwards will lead to a collision
			if (isColliding(this, new Vector3(position.x, position.y - fallVel, position.z)))
			{
				position = new Vector3(Math.round(position.x), (float)Math.floor(position.y), Math.round(position.z));
				setGravity(false);
				blockModel = new BlockModel(BlockType.Robot);
			}
			else 
				position.y -= fallVel;
		}
		super.moveModel();
	}

	public Vector3 getOriginalPos()
	{
		return originalPos;
	}

	public void setOriginalPos(Vector3 originalPos)
	{
		this.originalPos = originalPos;
	}
}

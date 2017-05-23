package teamnine.blocksim.block.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.BlockList;

public class BlockGravity extends BlockCollision
{

	// Physic constants
	private final float GRAVITY = 0.098f;
	private final float TERMINALVELOCITY = 10;

	// Fall velocity
	private float fallVel = GRAVITY;
	protected boolean gravity = false;

	// Timer to recalculate gravity
	private float gravityTime = 0;

	private BlockList blockList;
	private Vector3 originalPos;

	public BlockGravity(Vector3 position, Type type, BlockList blockList)
	{
		super(position, type, blockList);
		this.blockList = blockList;
	}

	public void setGravity(boolean gravity)
	{
		// Reset Gravity
		fallVel = GRAVITY;

		// Reset Time
		gravityTime = 0;
		
		// Set Gravity Boolean
		this.gravity = gravity;
	}

	public void calcFallVel()
	{
		fallVel += GRAVITY * gravityTime;

		// Can't go faster than terminal velocity
		if (fallVel > TERMINALVELOCITY)
			fallVel = TERMINALVELOCITY;
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
				position = new Vector3(Math.round(position.x), Math.round(position.y), Math.round(position.z));
				setGravity(false);
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

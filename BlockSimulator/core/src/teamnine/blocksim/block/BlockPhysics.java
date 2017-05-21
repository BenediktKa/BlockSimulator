package teamnine.blocksim.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.physics.BlockCollision;

public class BlockPhysics extends BlockCollision
{

	// Physic constants
	private final float GRAVITY = 0.1f;
	private final float TERMINALVELOCITY = 10;

	// Fall velocity
	private float fallVel = GRAVITY;
	protected boolean gravity = false;

	// Timer to recalculate gravity
	private float gravityTimer = 0;

	private BlockList blockList;
	private Vector3 originalPos;

	public BlockPhysics(Vector3 position, Type type, BlockList blockList)
	{
		super(position, type, blockList);
		this.blockList = blockList;
	}

	public void setGravity(boolean gravity)
	{
		// Reset Gravity
		fallVel = GRAVITY;

		this.gravity = gravity;
	}

	public void calcFallVel()
	{
		fallVel += GRAVITY;

		// Can't go faster than terminal velocity
		if (fallVel > TERMINALVELOCITY)
			fallVel = TERMINALVELOCITY;
	}

	public void moveModel()
	{
		if (gravity)
		{
			// This will add to a timer, which once it hits 1 will increase gravity
			gravityTimer += Gdx.graphics.getDeltaTime();
			if (gravityTimer >= 1)
			{
				gravityTimer -= 1;
				calcFallVel();
			}

			//Check if the next update downwards will lead to a collision
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

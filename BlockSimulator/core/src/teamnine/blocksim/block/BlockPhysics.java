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
			gravityTimer += Gdx.graphics.getDeltaTime();
			if (gravityTimer >= 1)
			{
				gravityTimer -= 1;
				calcFallVel();
			}

			if (isColliding(this, new Vector3(position.x, position.y - fallVel, position.z)))
			{
				setGravity(false);
				Vector3 newPos = new Vector3(Math.round(position.x), Math.round(position.y), Math.round(position.z));
				position = newPos;
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

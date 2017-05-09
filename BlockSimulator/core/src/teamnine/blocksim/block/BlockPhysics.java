package teamnine.blocksim.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class BlockPhysics extends Block
{
	
	//Physic constants
	private final float GRAVITY = 0.1f;
	private final float TERMINALVELOCITY = 10;
	
	//Fall velocity
	private float fallVel = GRAVITY;
	private boolean gravity = false;
	
	//Timer to recalculate gravity
	private float gravityTimer = 0;

	public BlockPhysics(Vector3 position, Type type)
	{
		super(position, type);
	}
	
	public void setGravity(boolean gravity)
	{
		//Reset Gravity
		fallVel = GRAVITY;
		
		this.gravity = gravity;
	}
	
	public void calcFallVel()
	{
		fallVel += GRAVITY;
		
		//Can't go faster than terminal velocity
		if(fallVel > TERMINALVELOCITY)
		{
			fallVel = TERMINALVELOCITY;
		}
	}
	
	public void moveModel()
	{
		if(gravity)
		{
			gravityTimer += Gdx.graphics.getDeltaTime();
			if(gravityTimer >= 1)
			{
				gravityTimer -= 1;
				calcFallVel();
			}
			position.y -= fallVel;
		}
	}
}

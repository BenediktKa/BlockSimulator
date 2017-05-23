package teamnine.blocksim.block.physics;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;

public class BlockPhysics extends Block
{

	private float GRAVITY = 0.098f;
	private float TERMINALVELOCITY = 10;
	private float FRICTIONCONSTANT = 0.5f;

	public BlockPhysics(Vector3 position, Type type)
	{
		super(position, type);
	}

	public float getGravity()
	{
		return GRAVITY;
	}

	public void setGravity(float GRAVITY)
	{
		this.GRAVITY = GRAVITY;
	}

	public float getTerminalVelocity()
	{
		return TERMINALVELOCITY;
	}

	public void setTerminalVelocity(float TERMINALVELOCITY)
	{
		this.TERMINALVELOCITY = TERMINALVELOCITY;
	}

	public float getFrictionConstant()
	{
		return FRICTIONCONSTANT;
	}

	public void setFrictionConstant(float FRICTIONCONSTANT)
	{
		this.FRICTIONCONSTANT = FRICTIONCONSTANT;
	}
}

package teamnine.blocksim.block.physics;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockType;
import teamnine.blocksim.hud.RobotBlockText;

public class BlockPhysics extends Block
{

	private float SPEED, ANIMATIONSPEED = 5;
	private float GRAVITY = 0.098f;
	private float TERMINALVELOCITY = 10;
	private float FRICTIONCONSTANT = 0.25f;

	protected static RobotBlockText rbText;

	public BlockPhysics(Vector3 position, BlockType type)
	{
		super(position, type);

		rbText = RobotBlockText.getInstance();

		calcSpeed();
	}

	public void calcSpeed()
	{
		float maxFriction = 2 * (FRICTIONCONSTANT * GRAVITY);
		SPEED = Math.abs(maxFriction + ANIMATIONSPEED);
	}

	public float getSpeed()
	{
		return SPEED;
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

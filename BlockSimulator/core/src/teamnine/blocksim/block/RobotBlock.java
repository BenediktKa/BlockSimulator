package teamnine.blocksim.block;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.block.physics.BlockGravity;
import teamnine.blocksim.hud.RobotBlockText;

public class RobotBlock extends BlockGravity
{

	private Vector3 moveTo, movement;
	private float speed;
	private boolean moving = false;
	private BlockList blockList;
	private Block target;
	private boolean visited = false;
	private int counter = 0;
	private ArrayList<RobotBlock> connections = new ArrayList<RobotBlock>();

	public RobotBlock(Vector3 position, Type type, float speed, BlockList blockList)
	{
		super(position, type, blockList);

		this.speed = speed;
		this.blockList = blockList;
	}

	public void setCounter(int c)
	{
		counter = c;
	}

	public int getCounter()
	{
		return counter;
	}

	public void addConnection(RobotBlock b)
	{
		connections.add(b);
	}

	public ArrayList<RobotBlock> getConnections()
	{
		return connections;
	}

	public void clearConnections()
	{
		connections = new ArrayList<RobotBlock>();
	}

	public void setVisited(boolean v)
	{
		visited = v;
	}

	public boolean getVisited()
	{
		return visited;
	}

	public boolean getMoving()
	{
		return moving || gravity;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public void setTarget(Block targetBlock)
	{
		target = targetBlock;
	}

	public Block getTarget()
	{
		return target;
	}

	public void moveLeft()
	{
		if (getMoving())
			return;

		setOriginalPos(position.cpy());
		moveTo = new Vector3(position.x - 1, position.y, position.z);
		movement = new Vector3(-1, 0, 0);
		moving = true;
	}

	public void moveRight()
	{
		if (getMoving())
			return;

		setOriginalPos(position.cpy());
		moveTo = new Vector3(position.x + 1, position.y, position.z);
		movement = new Vector3(1, 0, 0);
		moving = true;
	}

	public void moveForward()
	{
		if (getMoving())
			return;

		setOriginalPos(position.cpy());
		moveTo = new Vector3(position.x, position.y, position.z + 1);
		movement = new Vector3(0, 0, 1);
		moving = true;
	}

	public void moveBackwards()
	{
		if (getMoving())
			return;

		setOriginalPos(position.cpy());
		moveTo = new Vector3(position.x, position.y, position.z - 1);
		movement = new Vector3(0, 0, -1);
		moving = true;
	}

	public void climb()
	{
		if (getMoving())
			return;

		setOriginalPos(position.cpy());
		moveTo = new Vector3(position.x, position.y + 1, position.z);
		movement = new Vector3(0, 1, 0);
		moving = true;
	}

	public void moveModel()
	{
		if(StateManager.state == StateManager.SimulationState.PAUSE)
		{
			rbText.render();
			return;
		}
		
		if (movement == null)
		{
			super.moveModel();
			return;
		}

		if ((position.equals(moveTo) && getOriginalPos().y + 1 != moveTo.y && !gravity) || gravity)
		{
			movement = null;
			setGravity(true);
			System.out.println("Called");
		}
		else if ((position.cpy().sub(moveTo).isZero(0.01f) || getOriginalPos().dst(position) > 1 || getOriginalPos().dst(position) < -1) && moving)
		{
			position.x = moveTo.x;
			position.y = moveTo.y;
			position.z = moveTo.z;
			moving = false;
			rbText.setHorizontalText(0);
		}
		else if (moving)
		{
			float speedX = movement.x * speed * Gdx.graphics.getDeltaTime() + calcFriction(movement.x);
			float speedY = movement.y * speed * Gdx.graphics.getDeltaTime();
			float speedZ = movement.z * speed * Gdx.graphics.getDeltaTime() + calcFriction(movement.z);
			position.x += speedX;
			position.y += speedY;
			position.z += speedZ;
			
			rbText.setFrictionText(calcFriction(movement.x) + calcFriction(movement.z));
			rbText.setHorizontalText(speedX + speedZ);
		}
		super.moveModel();
	}
}

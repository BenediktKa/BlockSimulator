package teamnine.blocksim.block;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.block.physics.BlockGravity;

public class RobotBlock extends BlockGravity
{

	private Vector3 moveTo, movement;
	private boolean moving = false;
	private Block target;
	private boolean visited = false;
	private boolean inFinalPosition = false;
	private int counter = 0;
	private ArrayList<RobotBlock> connections = new ArrayList<RobotBlock>();
	private ArrayList<Vector3> unpassableVectors= new ArrayList<Vector3>();
	private ArrayList<Integer> numOfPass= new ArrayList<Integer>();

	public RobotBlock(Vector3 position, BlockType type)
	{
		super(position, type);
	}
	public void addUnpassableVector(Vector3 v)
	{
		unpassableVectors.add(v);
	}
	public ArrayList<Vector3> getUnpassableVectors()
	{
		return unpassableVectors;
	}
	public void addNumOfpass()
	{
		numOfPass.add(0);
	}
	public ArrayList<Integer> getNumOfPass()
	{
		return numOfPass;
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
	
	public void setInFinalPosition(boolean v)
	{
		inFinalPosition = v;
	}
	
	public boolean isInFinalPosition()
	{
		return inFinalPosition;
	}

	public boolean getVisited()
	{
		return visited;
	}

	public boolean getMoving()
	{
		return (moving || gravity);
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
			if(getModelInstance() != null)
				super.moveModel();
			return;
		}

		if ((position.equals(moveTo) && getOriginalPos().y + 1 != moveTo.y && !gravity) || gravity)
		{
			movement = null;
			setGravity(true);
			blockModel = new BlockModel(BlockType.RobotMoving);
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
			float speedX = movement.x * getSpeed() * Gdx.graphics.getDeltaTime() + calcFriction(movement.x);
			float speedY = movement.y * getSpeed() * Gdx.graphics.getDeltaTime();
			float speedZ = movement.z * getSpeed() * Gdx.graphics.getDeltaTime() + calcFriction(movement.z);
			position.x += speedX;
			position.y += speedY;
			position.z += speedZ;
			
			blockModel = new BlockModel(BlockType.RobotMoving);
			
			rbText.setFrictionText(calcFriction(movement.x) + calcFriction(movement.z));
			rbText.setHorizontalText(speedX + speedZ);
		}
		super.moveModel();
	}
}

package teamnine.blocksim.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class RobotBlock extends Block
{

	private Vector3 originalPos, moveTo, movement;
	private float speed;
	private boolean moving = false;
	private BlockList blockList;
	private Block target;

	public RobotBlock(Vector3 position, Type type, float speed, BlockList blockList)
	{
		super(position, type);
		
		this.speed = speed;
		this.blockList = blockList;
	}
	
	public boolean getMoving()
	{
		return moving;
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
		if (moving)
		{
			return;
		}

		originalPos = position.cpy();
		moveTo = new Vector3(position.x - 1, position.y, position.z);
		movement = new Vector3(-1, 0, 0);
		moving = true;
	}

	public void moveRight()
	{
		if (moving)
		{
			return;
		}

		originalPos = position.cpy();
		moveTo = new Vector3(position.x + 1, position.y, position.z);
		movement = new Vector3(1, 0, 0);
		moving = true;
	}

	public void moveForward()
	{
		if (moving)
		{
			return;
		}

		originalPos = position.cpy();
		moveTo = new Vector3(position.x, position.y, position.z + 1);
		movement = new Vector3(0, 0, 1);
		moving = true;
	}

	public void moveBackwards()
	{
		if (moving)
		{
			return;
		}

		originalPos = position.cpy();
		moveTo = new Vector3(position.x, position.y, position.z - 1);
		movement = new Vector3(0, 0, -1);
		moving = true;
	}

	public void climb()
	{
		if (moving)
		{
			return;
		}

		originalPos = position.cpy();
		moveTo = new Vector3(position.x, position.y + 1, position.z);
		movement = new Vector3(0, 1, 0);
		moving = true;
	}

	public void fall()
	{
		if (moving)
		{
			return;
		}

		originalPos = position.cpy();
		moveTo = new Vector3(position.x, position.y - 1, position.z);
		movement = new Vector3(0, -1, 0);
		moving = true;
	}
	
	public Vector3 getOriginalPos()
	{
		if(originalPos == null)
		{
			return position;
		}
		else
		{
			return originalPos;
		}
	}

	public void moveModel()
	{
		if (movement == null)
		{
			modelInstance.transform = new Matrix4().translate(position.x, position.y, position.z);
			return;
		}

		if (position.equals(moveTo))
		{
			return;
		}
		
		if (position.cpy().sub(moveTo).isZero(0.01f) || originalPos.dst(position) > 1 || originalPos.dst(position) < -1)
		{
			position.x = moveTo.x;
			position.y = moveTo.y;
			position.z = moveTo.z;
			moving = false;
			
			//Temporary Gravity
			if(moveTo.y != originalPos.y + 1)
			{
				Block block = blockList.blockAtPoint(new Vector3(position.x, position.y - 1, position.z));
				if(block == null || block.getType() == Block.Type.Path || block.getType() == Block.Type.Goal)
				{
					fall();
				}
			}
			
		}
		else
		{
			position.x += movement.x * speed * Gdx.graphics.getDeltaTime();
			position.y += movement.y * speed * Gdx.graphics.getDeltaTime();
			position.z += movement.z * speed * Gdx.graphics.getDeltaTime();
		}
		super.moveModel();
	}
}

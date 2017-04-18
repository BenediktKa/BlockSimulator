package teamnine.blocksim;

import com.badlogic.gdx.math.Vector3;

public class RobotBlock<T> extends Block {
	
	public RobotBlock(Vector3 position, Type type) {
		super(position, type);
		
	}
	public void moveLeft()
	{
		Vector3 v= getPosition();
		setPosition(v.x-1,v.y,v.z);
	}
	public void moveRight()
	{
		Vector3 v= getPosition();
		setPosition(v.x+1,v.y,v.z);
	}
	public void moveForward()
	{
		Vector3 v= getPosition();
		setPosition(v.x,v.y,v.z+1);
	}
	public void moveBackwards()
	{
		Vector3 v= getPosition();
		setPosition(v.x,v.y,v.z-1);
	}
	public void climb()
	{
		Vector3 v= getPosition();
		setPosition(v.x,v.y+1,v.z);
	}
	public void fall()
	{
		Vector3 v= getPosition();
		setPosition(v.x,v.y-1,v.z);
	}
}

package teamnine.blocksim.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.ai.Move;

public class RobotBlock extends Block {

	private Vector3 moveTo, movement;
	private float speed = 5;
	private boolean moving = false;

	public RobotBlock(Vector3 position, Type type) {
		super(position, type);
	}
	
	public boolean getMoving() {
		return moving;
	}

	public void moveLeft() {
		if (moving) {
			return;
		}
		
		System.out.println("Left");
		moveTo = new Vector3(position.x - 1, position.y, position.z);
		movement = new Vector3(-1, 0, 0);
		moving = true;
	}

	public void moveRight() {
		if (moving) {
			return;
		}

		System.out.println("Right");
		moveTo = new Vector3(position.x + 1, position.y, position.z);
		movement = new Vector3(1, 0, 0);
		moving = true;
	}

	public void moveForward() {
		if (moving) {
			return;
		}

		System.out.println("Forward");
		moveTo = new Vector3(position.x, position.y, position.z + 1);
		movement = new Vector3(0, 0, 1);
		moving = true;
	}

	public void moveBackwards() {
		if (moving) {
			return;
		}

		System.out.println("Backwards");
		moveTo = new Vector3(position.x, position.y, position.z - 1);
		movement = new Vector3(0, 0, -1);
		moving = true;
	}

	public void climb() {
		if (moving) {
			return;
		}

		System.out.println("Climb");
		moveTo = new Vector3(position.x, position.y + 1, position.z);
		movement = new Vector3(0, 1, 0);
		moving = true;
	}

	public void fall() {
		if (moving) {
			return;
		}

		moveTo = new Vector3(position.x, position.y - 1, position.z);
		movement = new Vector3(0, -1, 0);
		moving = true;
	}
	
	public void setPosition(float x, float y, float z) {
		position = new Vector3(x, y, z);
		moveModel();
	}
	
	public void setPosition(Vector3 vector) {
		position = vector;
		moveModel();
	}

	public void moveModel() {
		System.out.println("Called");
		if (movement == null) {
			modelInstance.transform = new Matrix4().translate(position.x, position.y, position.z);
			return;
		}

		if (position.equals(moveTo)) {
			return;
		}
		
		if (position.cpy().sub(moveTo).isZero(0.01f)) {
			position.x = moveTo.x;
			position.y = moveTo.y;
			position.z = moveTo.z;
			moving = false;
		} else {
			position.x += movement.x * speed * Gdx.graphics.getDeltaTime();
			position.y += movement.y * speed * Gdx.graphics.getDeltaTime();
			position.z += movement.z * speed * Gdx.graphics.getDeltaTime();
		}
		super.moveModel();
	}
}

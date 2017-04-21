package teamnine.blocksim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class RobotBlock<T> extends Block {

	private Vector3 moveTo, movement;
	private float speed = 5;
	private boolean moving = false;

	public RobotBlock(Vector3 position, Type type) {
		super(position, type);
	}

	public void moveLeft() {
		if (moving) {
			return;
		}

		moveTo = new Vector3(position.x - 1, position.y, position.z);
		movement = new Vector3(-1, 0, 0);
		moving = true;
	}

	public void moveRight() {
		if (moving) {
			return;
		}

		moveTo = new Vector3(position.x + 1, position.y, position.z);
		movement = new Vector3(1, 0, 0);
		moving = true;
	}

	public void moveForward() {
		if (moving) {
			return;
		}

		moveTo = new Vector3(position.x, position.y, position.z + 1);
		movement = new Vector3(0, 0, 1);
		moving = true;
	}

	public void moveBackwards() {
		if (moving) {
			return;
		}

		moveTo = new Vector3(position.x, position.y, position.z - 1);
		movement = new Vector3(0, 0, -1);
		moving = true;
	}

	public void climb() {
		if (moving) {
			return;
		}

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

	public void moveModel() {
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

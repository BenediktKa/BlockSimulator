package teamnine.blocksim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.hud.Notification;

public class FPSControl extends FirstPersonCameraController
{
	private BlockSimulator blockSimulator;
	private Camera camera;
	private Block.Type blockType = Block.Type.Obstacle;

	// Mouse Variables
	private int mouseX = 0;
	private int mouseY = 100;
	private float rotSpeed = 0.2f;

	public FPSControl(Camera camera, BlockSimulator blockSimulator)
	{
		super(camera);
		this.blockSimulator = blockSimulator;
		this.camera = camera;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		if (StateManager.state == SimulationState.BUILD)
		{
			int magX = Math.abs(mouseX - screenX);
			int magY = Math.abs(mouseY - screenY);

			if (mouseX > screenX)
			{
				camera.rotate(Vector3.Y, 1 * magX * rotSpeed);
				camera.update();
			}

			if (mouseX < screenX)
			{
				camera.rotate(Vector3.Y, -1 * magX * rotSpeed);
				camera.update();
			}

			if (mouseY < screenY)
			{
				if (camera.direction.y > -0.965)
				{
					camera.rotate(camera.direction.cpy().crs(Vector3.Y), -1 * magY * rotSpeed);
					camera.update();
				}
			}

			if (mouseY > screenY)
			{

				if (camera.direction.y < 0.965)
				{
					camera.rotate(camera.direction.cpy().crs(Vector3.Y), 1 * magY * rotSpeed);
					camera.update();
				}
			}
			blockSimulator.blockList.editBoxByRayCast(camera.position, camera.direction, null, false);
		}
		mouseX = screenX;
		mouseY = screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		if (StateManager.state == SimulationState.SIMULATION)
		{
			return false;
		}

		if (amount == 1)
		{
			blockType = blockType.next();
			blockSimulator.levelHUD.setSelectedBlock(blockType);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if (StateManager.state == SimulationState.BUILD)
		{
			if (button == 0)
			{
				blockSimulator.blockList.editBoxByRayCast(camera.position, camera.direction, blockType, true);
			}
			else if (button == 1)
			{
				blockSimulator.blockList.editBoxByRayCast(camera.position, camera.direction, null, true);
			}
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if (StateManager.state == SimulationState.SIMULATION)
		{
			return super.keyDown(keycode);
		}
		if (keycode == Keys.UP)
		{
			if (inGrid(blockSimulator.selectorBlock.getPosition().x + 1))
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(1, 0, 0));
		}
		else if (keycode == Keys.DOWN)
		{
			if (inGrid(blockSimulator.selectorBlock.getPosition().x - 1))
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(-1, 0, 0));
		}
		else if (keycode == Keys.LEFT)
		{
			if (inGrid(blockSimulator.selectorBlock.getPosition().z + 1))
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(0, 0, 1));
		}
		else if (keycode == Keys.RIGHT)
		{
			if (inGrid(blockSimulator.selectorBlock.getPosition().z - 1))
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(0, 0, -1));
		}
		else if (keycode == Keys.X)
		{
			if (inGrid(blockSimulator.selectorBlock.getPosition().y + 1))
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(0, 1, 0));
		}
		else if (keycode == Keys.Z)
		{
			if (inGrid(blockSimulator.selectorBlock.getPosition().y - 1))
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(0, -1, 0));
		}
		else if (keycode == Keys.PLUS)
		{
			blockSimulator.blockList.resizeFloor(blockSimulator.gridSize++);
		}
		else if (keycode == Keys.MINUS)
		{
			blockSimulator.blockList.resizeFloor(blockSimulator.gridSize--);
		}
		else if (keycode == Keys.DEL)
		{
			Block blockToDelete = blockSimulator.blockList.blockAtPoint(blockSimulator.selectorBlock.getPosition());
			if (blockToDelete != null && blockToDelete.getType() != Block.Type.Floor)
				blockSimulator.blockList.removeBlock(blockToDelete);
			else if (blockToDelete == null)
				blockSimulator.notification.setNotification("Block doesn't exist", Notification.Type.Error, 1);
			else
				blockSimulator.notification.setNotification("Can't delete the floor", Notification.Type.Error, 1);
		}
		else if (keycode == Keys.SPACE)
		{
			if (blockSimulator.blockList.blockAtPoint(blockSimulator.selectorBlock.getPosition()) == null)
				blockSimulator.blockList.createBlock(blockSimulator.selectorBlock.getPosition().cpy(), blockType);
		}
		else if (keycode == Keys.ESCAPE)
		{
			if (StateManager.state == SimulationState.BUILD)
			{
				blockSimulator.notification.setNotification("Menu Mode", Notification.Type.ModeChange, 2);
				StateManager.state = SimulationState.MENU;
				Gdx.input.setCursorCatched(false);
			}
			else if (StateManager.state == SimulationState.MENU)
			{
				blockSimulator.notification.setNotification("Build Mode", Notification.Type.ModeChange, 2);
				StateManager.state = SimulationState.BUILD;
				Gdx.input.setCursorCatched(true);
			}
		}
		return super.keyDown(keycode);
	}

	public boolean inGrid(float value)
	{
		if (value >= blockSimulator.gridSize || value < 0)
		{
			blockSimulator.notification.setNotification("Out of bounds", Notification.Type.Error, 1);
			return false;
		}
		return true;
	}
}
package teamnine.blocksim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.SelectorBlock;
import teamnine.blocksim.blocklist.BlockListController;
import teamnine.blocksim.hud.Notification;

public class FPSControl extends FirstPersonCameraController
{
	private Camera camera;

	// Mouse Variables
	private int mouseX = 0;
	private int mouseY = 100;
	private float rotSpeed = 0.2f;
	private BlockListController blockListController = null;
	private Notification notification = null;
	private SelectorBlock selectorBlock = null;

	public FPSControl(Camera camera)
	{
		super(camera);
		this.camera = camera;
		
		blockListController = BlockListController.getInstance();
		notification = Notification.getInstance();
		selectorBlock = SelectorBlock.getInstance();
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		if (StateManager.state == SimulationState.BUILD || StateManager.state == SimulationState.SIMULATIONFPS)
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
			Vector3 vec;
			if((vec = blockListController.getPositionAtRayCast(camera.position, camera.direction, false)) != null)
				selectorBlock.setPosition(vec);
			
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
			selectorBlock.setNextBlock();
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
				Vector3 vec;
				if((vec = blockListController.getPositionAtRayCast(camera.position, camera.direction, false)) != null)
					blockListController.createBlock(vec, selectorBlock.getSelectedBlock());
					
			}
			else if (button == 1)
			{
				Vector3 vec;
				if((vec = blockListController.getPositionAtRayCast(camera.position, camera.direction, true)) != null)
					blockListController.removeBlock(blockListController.getBlockAtPointIgnoreType(vec, Block.Type.Floor));
			}
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if (StateManager.state == SimulationState.SIMULATION || StateManager.state == SimulationState.SIMULATIONFPS)
		{
			if(keycode == Keys.ESCAPE)
			{
				if(StateManager.state == SimulationState.SIMULATION)
				{
					notification.setNotification("Camera Free", Notification.Type.ModeChange, 2);
					StateManager.state = SimulationState.SIMULATIONFPS;
					Gdx.input.setCursorCatched(true);
				}
				else if(StateManager.state == SimulationState.SIMULATIONFPS)
				{
					notification.setNotification("Camera Locked", Notification.Type.ModeChange, 2);
					StateManager.state = SimulationState.SIMULATION;
					Gdx.input.setCursorCatched(false);
				}
			}
			return super.keyDown(keycode);
		}
		if (keycode == Keys.UP)
		{
			if (inGrid(selectorBlock.getPosition().x + 1))
				selectorBlock.setPosition(selectorBlock.getPosition().add(1, 0, 0));
		}
		else if (keycode == Keys.DOWN)
		{
			if (inGrid(selectorBlock.getPosition().x - 1))
				selectorBlock.setPosition(selectorBlock.getPosition().add(-1, 0, 0));
		}
		else if (keycode == Keys.LEFT)
		{
			if (inGrid(selectorBlock.getPosition().z + 1))
				selectorBlock.setPosition(selectorBlock.getPosition().add(0, 0, 1));
		}
		else if (keycode == Keys.RIGHT)
		{
			if (inGrid(selectorBlock.getPosition().z - 1))
				selectorBlock.setPosition(selectorBlock.getPosition().add(0, 0, -1));
		}
		else if (keycode == Keys.X)
		{
			if (inGrid(selectorBlock.getPosition().y + 1))
				selectorBlock.setPosition(selectorBlock.getPosition().add(0, 1, 0));
		}
		else if (keycode == Keys.Z)
		{
			if (inGrid(selectorBlock.getPosition().y - 1))
				selectorBlock.setPosition(selectorBlock.getPosition().add(0, -1, 0));
		}
		else if (keycode == Keys.PLUS)
		{
			blockListController.setFloorGridSize(blockListController.getFloorGridSize() + 1);
		}
		else if (keycode == Keys.MINUS)
		{
			blockListController.setFloorGridSize(blockListController.getFloorGridSize() - 1);
		}
		else if (keycode == Keys.DEL)
		{
			Block blockToDelete = blockListController.getBlockAtPoint(selectorBlock.getPosition());
			if (blockToDelete != null && blockToDelete.getType() != Block.Type.Floor)
				blockListController.removeBlock(blockToDelete);
			else if (blockToDelete == null)
				notification.setNotification("Block doesn't exist", Notification.Type.Error, 1);
			else
				notification.setNotification("Can't delete the floor", Notification.Type.Error, 1);
		}
		else if (keycode == Keys.SPACE)
		{
			if (blockListController.getBlockAtPoint(selectorBlock.getPosition()) == null)
				blockListController.createBlock(selectorBlock.getPosition().cpy(), selectorBlock.getSelectedBlock());
		}
		else if (keycode == Keys.ESCAPE)
		{
			if (StateManager.state == SimulationState.BUILD)
			{
				notification.setNotification("Menu Mode", Notification.Type.ModeChange, 2);
				StateManager.state = SimulationState.MENU;
				Gdx.input.setCursorCatched(false);
			}
			else if (StateManager.state == SimulationState.MENU)
			{
				notification.setNotification("Build Mode", Notification.Type.ModeChange, 2);
				StateManager.state = SimulationState.BUILD;
				Gdx.input.setCursorCatched(true);
			}
		}
		return super.keyDown(keycode);
	}

	public boolean inGrid(float value)
	{
		if (value >= blockListController.getFloorGridSize() || value < 0)
		{
			notification.setNotification("Out of bounds", Notification.Type.Error, 1);
			return false;
		}
		return true;
	}
}
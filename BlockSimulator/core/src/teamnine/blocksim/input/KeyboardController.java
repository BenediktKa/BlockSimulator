package teamnine.blocksim.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.SelectorBlock;
import teamnine.blocksim.blocklist.BlockListController;
import teamnine.blocksim.hud.Notification;

public class KeyboardController extends InputAdapter
{
	private Notification notification = null;
	private BlockListController blockListController = null;
	private SelectorBlock selectorBlock = null;
	
	public KeyboardController()
	{
		notification = Notification.getInstance();
		blockListController = BlockListController.getInstance();
		selectorBlock = SelectorBlock.getInstance();
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		if (StateManager.state == SimulationState.SIMULATION || StateManager.state == SimulationState.SIMULATIONFPS)
		{
			if (keycode == Keys.ESCAPE)
			{
				if (StateManager.state == SimulationState.SIMULATION)
				{
					notification.setNotification("Camera Free", Notification.Type.ModeChange, 2);
					StateManager.state = SimulationState.SIMULATIONFPS;
					Gdx.input.setCursorCatched(true);
				}
				else if (StateManager.state == SimulationState.SIMULATIONFPS)
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

package teamnine.blocksim.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.SelectorBlock;
import teamnine.blocksim.blocklist.BlockListController;

public class CameraController extends FirstPersonCameraController
{
	private Camera camera;

	// Mouse Variables
	private int mouseX = 0;
	private int mouseY = 100;
	private float rotSpeed = 0.2f;
	private BlockListController blockListController = null;
	private SelectorBlock selectorBlock = null;

	public CameraController(Camera camera)
	{
		super(camera);
		this.camera = camera;
		
		blockListController = BlockListController.getInstance();
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
}
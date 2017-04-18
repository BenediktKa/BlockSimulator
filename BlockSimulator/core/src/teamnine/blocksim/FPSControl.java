package teamnine.blocksim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
public class FPSControl extends FirstPersonCameraController {
	private BlockSimulator blockSimulator;
	private Camera camera;
	private Type modeType;
	private Block.Type blockType = Block.Type.Obstacle;
	
	
	public enum Type {
		BuildMode,
		MenuMode,
		SimulationMode;
	}
	
	public FPSControl(Camera camera, BlockSimulator blockSimulator) {
		super(camera);
		this.blockSimulator = blockSimulator;
		this.camera = camera;
		this.modeType = Type.BuildMode;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(modeType == Type.BuildMode) {
			touchDragged(screenX, screenY, 0);
			blockSimulator.blockList.moveSelectorBlock(camera.position, camera.direction);
		}
		return super.mouseMoved(screenX, screenY);
	}
	
	@Override
	public boolean scrolled(int amount) {
		if(modeType == Type.SimulationMode) {
			return false;
		}
		if(amount == 1) {
			blockType = blockType.next();
		}
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(modeType != Type.BuildMode) {
			return super.touchDown(screenX, screenY, pointer, button);
		}
		if (button == 0) {
			blockSimulator.blockList.editBoxByRayCast(camera.position, camera.direction, blockType);
		} else if (button == 1) {
			blockSimulator.blockList.editBoxByRayCast(camera.position, camera.direction, null);
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean keyDown(int keycode) {		
		if(modeType == Type.SimulationMode) {
			return super.keyDown(keycode);
		}
		if(keycode == Keys.UP) {
			if(inGrid(blockSimulator.selectorBlock.getPosition().x + 1)) {
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(1, 0, 0));
			}
		} else if(keycode == Keys.DOWN) {
			if(inGrid(blockSimulator.selectorBlock.getPosition().x - 1)) {
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(-1, 0, 0));
			}
		} else if(keycode == Keys.LEFT) {
			if(inGrid(blockSimulator.selectorBlock.getPosition().z + 1)) {
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(0, 0, 1));
			}
		} else if(keycode == Keys.RIGHT) {
			if(inGrid(blockSimulator.selectorBlock.getPosition().z - 1)) {
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(0, 0, -1));
			}
		} else if(keycode == Keys.X) {
			if(inGrid(blockSimulator.selectorBlock.getPosition().y + 1)) {
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(0, 1, 0));
			}
		} else if(keycode == Keys.Z) {
			if(inGrid(blockSimulator.selectorBlock.getPosition().y - 1)) {
				blockSimulator.selectorBlock.setPosition(blockSimulator.selectorBlock.getPosition().add(0, -1, 0));
			}
		} else if(keycode == Keys.DEL) {
			Block blockToDelete = blockSimulator.grid.getGrid()[(int)blockSimulator.selectorBlock.getPosition().x][(int)blockSimulator.selectorBlock.getPosition().y][(int)blockSimulator.selectorBlock.getPosition().z];
			if(blockToDelete != null && blockToDelete.getType() != Block.Type.Floor) {
				blockSimulator.blockList.removeBlock(blockToDelete);
			} else if(blockToDelete == null) {
				blockSimulator.notification.setNotification("Block doesn't exist", Notification.Type.Error);
			} else {
				blockSimulator.notification.setNotification("Can't delete the floor", Notification.Type.Error);
			}
		} else if(keycode == Keys.SPACE) {
			if(blockSimulator.grid.getGrid()[(int)blockSimulator.selectorBlock.getPosition().x][(int)blockSimulator.selectorBlock.getPosition().y][(int)blockSimulator.selectorBlock.getPosition().z] == null) {
				blockSimulator.blockList.createBlock(new Vector3(blockSimulator.selectorBlock.getPosition().x, blockSimulator.selectorBlock.getPosition().y, blockSimulator.selectorBlock.getPosition().z), blockType);
			}
		} else if(keycode == Keys.C) {
			camera.lookAt(blockSimulator.selectorBlock.getPosition());
		} else if(keycode == Keys.ESCAPE) {
			if(modeType == Type.BuildMode) {
				modeType = Type.MenuMode;
				Gdx.input.setCursorCatched(false);
			} else {
				modeType = Type.BuildMode;
				Gdx.input.setCursorCatched(true);
			}
		}
		return super.keyDown(keycode);
	}
	
	public boolean inGrid(float value) {
		if(value >= blockSimulator.grid.size() || value < 0) {
			blockSimulator.notification.setNotification("Out of bounds", Notification.Type.Error);
			return false;
		}
		return true;
	}
	
	public Block.Type getBlockType() {
		return blockType;
	}
	
	public Type getModeType() {
		return modeType;
	}
	
	public void setModeType(Type modeType) {
		this.modeType = modeType;
	}
}
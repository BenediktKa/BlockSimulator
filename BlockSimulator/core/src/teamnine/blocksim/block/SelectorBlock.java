package teamnine.blocksim.block;

import com.badlogic.gdx.math.Vector3;

public class SelectorBlock extends Block{

	public SelectorBlock(Vector3 position, Type type) {
		super(position, type);
	}
	
	public void setPosition(float x, float y, float z) {
		position = new Vector3(x, y, z);
		moveModel();
	}
	
	public void setPosition(Vector3 vector) {
		position = vector;
		moveModel();
	}

}

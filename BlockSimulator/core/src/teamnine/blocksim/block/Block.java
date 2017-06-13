package teamnine.blocksim.block;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import teamnine.blocksim.block.physics.BlockHitbox;

public class Block implements Disposable
{

	// Model
	protected BlockModel blockModel;

	// Block Variables
	protected Vector3 position;
	protected BlockType type;
	protected double ID;
	private BlockHitbox hitbox;
	protected float distanceToPath = 0;

	public Block(Vector3 position, BlockType type)
	{
		this.position = position;
		this.type = type;
		blockModel = new BlockModel(type);
		hitbox = new BlockHitbox(position);
		moveModel();
	}

	public void setDistanceToPath(float p)
	{
		distanceToPath = p;
	}

	public float getDistanceToPath()
	{
		return distanceToPath;
	}

	public Model getModel()
	{
		return blockModel.getModel();
	}

	public BlockHitbox getHitbox()
	{
		return hitbox;
	}

	public ModelInstance getModelInstance()
	{
		return blockModel.getModelInstance();
	}

	public void moveModel()
	{
		getModelInstance().transform = new Matrix4().translate(position.x, position.y, position.z);
		hitbox = new BlockHitbox(position);
	}

	public void setPosition(float x, float y, float z)
	{
		position = new Vector3(x, y, z);
		moveModel();
	}

	public void setPosition(Vector3 vector)
	{
		position = vector;
		moveModel();
	}

	public Vector3 getPosition()
	{
		return position;
	}

	public BlockType getType()
	{
		return type;
	}

	public void setID(double ID)
	{
		this.ID = ID;
	}

	public double getID()
	{
		return ID;
	}

	@Override
	public void dispose()
	{
		getModel().dispose();
	}
}

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
	private BlockModel blockModel;

	// Block Variables
	protected Vector3 position;
	protected Type type;
	protected double ID;
	private BlockHitbox hitbox;
	protected float distanceToPath = 0;

	public Block(Vector3 position, Type type)
	{
		this.position = position;
		this.type = type;
		blockModel = new BlockModel(type);
		hitbox = new BlockHitbox(position);
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
	}

	public void setPosition(Vector3 vector)
	{
		position = vector;
	}

	public Vector3 getPosition()
	{
		return position;
	}

	public enum Type
	{
		Robot, Obstacle, Goal, Selector, Floor, Path;

		public Type next()
		{
			Type types[] = Type.values();
			int ordinal = this.ordinal();
			ordinal = ++ordinal % types.length;
			if (types[ordinal] == Selector)
				return types[0];

			return types[ordinal];
		}
	}

	public Type getType()
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

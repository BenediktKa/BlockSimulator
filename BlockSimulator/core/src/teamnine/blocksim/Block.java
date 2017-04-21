package teamnine.blocksim;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Block implements Disposable {

	// Model
	protected Model model;
	protected ModelInstance modelInstance;

	// Block Variables
	protected Vector3 position;
	protected Type type;
	protected double ID;

	public Block(Vector3 position, Type type) {
		this.position = position;
		this.type = type;
		createModel();
	}

	public void createModel() {
		if (type == Type.Robot) {
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f,
					new Material(ColorAttribute.createDiffuse(new Color(142f / 255f, 68f / 255f, 173f / 255f, 1.0f))),
					Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		} else if (type == Type.Floor) {
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f,
					new Material(ColorAttribute.createDiffuse(new Color(149f / 255f, 165f / 255f, 166f / 255f, 1.0f))),
					Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		} else if (type == Type.Obstacle) {
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f,
					new Material(ColorAttribute.createDiffuse(new Color(231f / 255f, 76f / 255f, 60f / 255f, 1.0f))),
					Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		} else if (type == Type.Goal) {
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f,
					new Material(ColorAttribute.createDiffuse(new Color(26f / 255f, 188f / 255f, 156f / 255f, 1.0f)),
							new BlendingAttribute(0.4f)),
					Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		} else if (type == Type.Selector) {
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f,
					new Material(ColorAttribute.createDiffuse(new Color(1.0f, 1.0f, 1.0f, 1.0f)),
							new BlendingAttribute(0.4f)),
					Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
	}

	public Model getModel() {
		return this.model;
	}

	public ModelInstance getModelInstance() {
		return this.modelInstance;
	}

	public void moveModel() {
		modelInstance.transform = new Matrix4().translate(position.x, position.y, position.z);
	}

	public void setPosition(float x, float y, float z) {
		position = new Vector3(x, y, z);
		moveModel();
	}

	public void setPosition(Vector3 vector) {
		position = vector;
		moveModel();
	}

	public Vector3 getPosition() {
		return position;
	}

	public enum Type {
		Robot, Floor, Obstacle, Goal, Selector;

		public Type next() {
			Type types[] = Type.values();
			int ordinal = this.ordinal();
			ordinal = ++ordinal % types.length;
			if (types[ordinal] == Selector) {
				return types[0];
			} else if (types[ordinal] == Floor) {
				return types[2];
			}
			return types[ordinal];
		}
	}

	public Type getType() {
		return type;
	}

	public void setID(double ID) {
		this.ID = ID;
	}

	public double getID() {
		return ID;
	}

	@Override
	public void dispose() {
		model.dispose();
	}
}

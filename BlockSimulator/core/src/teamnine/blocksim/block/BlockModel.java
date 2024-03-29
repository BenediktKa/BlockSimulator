package teamnine.blocksim.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class BlockModel
{
	private Model model;
	private ModelInstance modelInstance;

	public BlockModel(BlockType type)
	{
		if (type == BlockType.Robot)
		{
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color(142f / 255f, 68f / 255f, 173f / 255f, 1.0f))), Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
		else if (type == BlockType.RobotMoving)
		{
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color(102f / 255f, 28f / 255f, 133f / 255f, 1.0f))), Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
		else if (type == BlockType.Floor)
		{
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color(149f / 255f, 165f / 255f, 166f / 255f, 1.0f))), Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
		else if (type == BlockType.Obstacle)
		{
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color(231f / 255f, 76f / 255f, 60f / 255f, 1.0f))), Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
		else if (type == BlockType.Goal)
		{
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color(26f / 255f, 188f / 255f, 156f / 255f, 1.0f)), new BlendingAttribute(0.4f)), Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
		else if (type == BlockType.Selector)
		{
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color(1.0f, 1.0f, 1.0f, 1.0f)), new BlendingAttribute(0.4f)), Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
		else if (type == BlockType.Path)
		{
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color(0.0f, 0.0f, 1.0f, 1.0f)), new BlendingAttribute(0.1f)), Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
		else if (type == BlockType.PathFinding)
		{
			ModelBuilder modelBuilder = new ModelBuilder();
			model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color(0.0f, 1.0f, 0.0f, 1.0f)), new BlendingAttribute(0.1f)), Usage.Position | Usage.Normal);
			modelInstance = new ModelInstance(model);
		}
	}

	public ModelInstance getModelInstance()
	{
		return modelInstance;
	}

	public Model getModel()
	{
		return model;
	}
}

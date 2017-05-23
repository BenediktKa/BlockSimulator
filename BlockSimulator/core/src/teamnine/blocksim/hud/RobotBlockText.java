package teamnine.blocksim.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class RobotBlockText implements Disposable
{
	private Skin skin;
	
	private Table table;
	
	private Stage stage;
	
	private Label speedLabel;
	private Label frictionLabel;
	private Label gravityLabel;
	private Label totalSpeedLabel;

	public RobotBlockText()
	{
		stage = new Stage(new ScreenViewport());
		table = new Table();
		table.setWidth(stage.getWidth());
		table.align(Align.left);
		table.setPosition(stage.getWidth() - 400, stage.getHeight() - 75);
		
		skin = new Skin(Gdx.files.internal("interface/skins/uiskin.json"));
		
		speedLabel = new Label("Speed: ",skin);
		frictionLabel = new Label("Friction: ",skin);
		gravityLabel = new Label("Gravity: ",skin);
		totalSpeedLabel = new Label("Speed X: Speed Z: ", skin);
		
		table.add(speedLabel).row();
		table.add(frictionLabel).row();
		table.add(gravityLabel).row();
		table.add(totalSpeedLabel);
		
		
		stage.addActor(table);
	}
	
	public void render()
	{
		stage.draw();
	}
	
	public void setSpeedText(float value)
	{
		speedLabel.setText(String.format("Speed: %.3f", value));
	}
	
	public void setFrictionText(float value)
	{
		frictionLabel.setText(String.format("Friction: %.3f", value));
	}
	
	public void setGravityText(float value)
	{
		gravityLabel.setText(String.format("Gravity: %.3f", value));
	}
	
	public void setTotalSpeedText(Vector3 vector)
	{
		totalSpeedLabel.setText(String.format("Speed X: %.3f Speed Z: %.3f", vector.x, vector.z));
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}
}

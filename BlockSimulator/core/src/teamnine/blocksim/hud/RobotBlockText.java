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

import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;

public class RobotBlockText implements Disposable
{
	private Skin skin;
	
	private Table table;
	
	private Stage stage;
	
	private Label frictionLabel;
	private Label horizontalSpeedLabel;
	private Label verticalSpeedLabel;

	public RobotBlockText()
	{
		stage = new Stage(new ScreenViewport());
		table = new Table();
		table.setWidth(stage.getWidth());
		table.align(Align.left);
		table.setPosition(stage.getWidth() - 400, stage.getHeight() - 75);
		
		skin = new Skin(Gdx.files.internal("interface/skins/uiskin.json"));
		
		frictionLabel = new Label("Friction: ",skin);
		horizontalSpeedLabel = new Label("Horizontal Speed: ",skin);
		verticalSpeedLabel = new Label("Vertical Speed: ", skin);
		
		table.add(frictionLabel).row();
		table.add(horizontalSpeedLabel).row();
		table.add(verticalSpeedLabel);
		
		
		stage.addActor(table);
	}
	
	public void render()
	{
		stage.draw();
	}
	
	public void setFrictionText(float value)
	{
		frictionLabel.setText(String.format("Friction: %.3f", value));
	}
	
	public void setHorizontalText(float value)
	{
		horizontalSpeedLabel.setText(String.format("Horizontal Speed: %.3f", value));
	}
	
	public void setVerticalText(float value)
	{
		verticalSpeedLabel.setText(String.format("Vertical Speed: %.3f", value));
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}
}

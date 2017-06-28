package teamnine.blocksim.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import teamnine.blocksim.BlockSimulator;
import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.ai.BrainAI;
import teamnine.blocksim.block.BlockType;
import teamnine.blocksim.block.SelectorBlock;
import teamnine.blocksim.block.blocklist.BlockListController;
import teamnine.blocksim.configs.configurationLoader;
import teamnine.blocksim.configs.simulationLoader;

public class LevelEditorHUD implements Disposable
{
	
	// Stage
	private Stage stage;

	// Skin
	private Skin skin;

	// Tables
	private Table table;

	// Import Button
	private TextButton importButton;

	// Start Button
	private TextButton startButton;

	// Pause Button
	private TextButton pauseButton;

	// AI Mode Button
	private TextButton aiModeButton;
	private AIMode aiMode = AIMode.Greedy;

	// Block Dialog
	private Label blockLabel;
	
	//Selector Block Position
	private Label selectorPosLabel;
	
	private SelectorBlock selectorBlock = null;

	// Configuration Checker
	// private ConfigurationChecker check;

	public enum AIMode
	{
		Greedy, Dijkstra, Astar;

		public AIMode next()
		{
			AIMode types[] = AIMode.values();
			int ordinal = this.ordinal();
			ordinal = ++ordinal % types.length;
			return types[ordinal];
		}
	}

	public LevelEditorHUD(final BlockSimulator blockSimulator)
	{
		selectorBlock = SelectorBlock.getInstance();
		
		skin = new Skin(Gdx.files.internal("interface/skins/uiskin.json"));
		stage = new Stage(new ScreenViewport(), blockSimulator.spriteBatch);

		// Buttons Table
		table = new Table();
		table.setWidth(stage.getWidth());
		table.align(Align.left | Align.top);
		table.setPosition(0, stage.getHeight() - 5);

		// Create Buttons
		importButton = new TextButton("Import", skin);
		aiModeButton = new TextButton("AI: " + aiMode, skin);
		startButton = new TextButton("Start", skin);
		pauseButton = new TextButton("Pause", skin);
		pauseButton.setVisible(false);
		pauseButton.setTouchable(Touchable.disabled);

		// Create Labels
		blockLabel = new Label("Selected: " + selectorBlock.getSelectedBlock(), skin);
		selectorPosLabel = new Label("", skin, "smallLabel");

		// Add to Table
		table.add(importButton).padRight(20);
		table.add(blockLabel).padRight(20);
		table.add(selectorPosLabel).padRight(20);
		table.row().height(20);
		table.add(aiModeButton).padRight(20);
		table.add(startButton).padRight(20);
		table.add(pauseButton);
		table.row().height(20);

		// Import Button Listener
		importButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (StateManager.state == SimulationState.MENU)
				{
					new configurationLoader();
				}
				super.clicked(event, x, y);
			}
		});

		// AI Button Listener
		aiModeButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				aiMode = aiMode.next();
				aiModeButton.setText("AI: " + aiMode);
				super.clicked(event, x, y);
			}
		});

		// Start Button Listener
		startButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (StateManager.state == SimulationState.SIMULATION || StateManager.state == SimulationState.PAUSE)
				{
					StateManager.state = SimulationState.MENU;
					startButton.setText("Start");
					// Remove Path
					BlockListController.getInstance().removeAllBlocksOfType(BlockType.Path);
					// Hide Pause Button
					pauseButton.setVisible(false);
					pauseButton.setTouchable(Touchable.disabled);
				}
				else
				{
					StateManager.state = SimulationState.SIMULATION;
					startButton.setText("Stop");
					// new Movement(blockSimulator.blockList);
					new BrainAI(aiMode);

					// Display Pause Button
					pauseButton.setVisible(true);
					pauseButton.setTouchable(Touchable.enabled);
				}

				super.clicked(event, x, y);
			}
		});

		// Pause Button Listener
		pauseButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (StateManager.state == StateManager.SimulationState.SIMULATION)
				{
					StateManager.state = StateManager.SimulationState.PAUSE;
					pauseButton.setText("Resume");
				}
				else if (StateManager.state == StateManager.SimulationState.PAUSE)
				{
					StateManager.state = StateManager.SimulationState.SIMULATION;
					pauseButton.setText("Pause");
				}

				super.clicked(event, x, y);
			}
		});

		// Add to Stage
		stage.addActor(table);

		blockSimulator.inputMultiplexer.addProcessor(stage);
	}

	public void render()
	{
		blockLabel.setText("Selected: " + selectorBlock.getSelectedBlock());
		selectorPosLabel.setText("X:" + selectorBlock.getPosition().x + " Y: " + selectorBlock.getPosition().y + " Z: " + selectorBlock.getPosition().z);
		stage.draw();
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}

}
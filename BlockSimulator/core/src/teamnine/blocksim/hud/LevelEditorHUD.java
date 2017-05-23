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
import teamnine.blocksim.block.Block;
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

	// Export Button
	private TextButton exportButton;

	// Undo Button
	private TextButton undoButton;

	// Redo Button
	private TextButton redoButton;

	// Start Button
	private TextButton startButton;

	// Pause Button
	private TextButton pauseButton;

	// Load Simulation Button
	private TextButton simulationButton;

	// Block Dialog
	private Label blockLabel;
	private Block.Type selectedBlock = Block.Type.Obstacle;
	
	// Configuration Checker
	// private ConfigurationChecker check;

	public LevelEditorHUD(final BlockSimulator blockSimulator)
	{
		skin = new Skin(Gdx.files.internal("interface/skins/uiskin.json"));
		stage = new Stage(new ScreenViewport());

		// Buttons Table
		table = new Table();
		table.setWidth(stage.getWidth());
		table.align(Align.left | Align.top);
		table.setPosition(0, stage.getHeight() - 5);

		// Create Buttons
		importButton = new TextButton("Import", skin);
		exportButton = new TextButton("Export", skin);
		undoButton = new TextButton("Undo", skin, "midnight");
		redoButton = new TextButton("Redo", skin, "midnight");
		startButton = new TextButton("Start", skin);
		pauseButton = new TextButton("Pause", skin);
		pauseButton.setVisible(false);
		pauseButton.setTouchable(Touchable.disabled);
		simulationButton = new TextButton("Load Simulation", skin);

		// Create Labels
		blockLabel = new Label("Selected: " + selectedBlock, skin);

		// Add to Table
		table.add(importButton).padRight(20);
		table.add(exportButton).padRight(20);
		table.add(simulationButton).padRight(20);
		table.add(blockLabel);
		table.row().height(20);
		table.add(undoButton).padRight(20);
		table.add(redoButton).padRight(20);
		table.add(startButton).padRight(20);
		table.add(pauseButton);

		// Import Button Listener
		importButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (StateManager.state == SimulationState.MENU)
				{
					new configurationLoader(blockSimulator.blockList);
				}
				super.clicked(event, x, y);
			}
		});

		// Export Button Listener
		exportButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (StateManager.state == StateManager.SimulationState.MENU)
				{
					// check = new
					// ConfigurationChecker(blockSimulator.blockList);

					/*
					 * if (check.checkConfiguration()) {
					 * 
					 * FileCreator fileCreator = new FileCreator(); File
					 * robotFile = new File("myRobot.txt"); File targetFile =
					 * new File("myTarget.txt"); File obstacleFile = new
					 * File("myObstacle.txt");
					 * 
					 * fileChooser.setSelectedFile(robotFile); if
					 * (fileChooser.showSaveDialog(null) ==
					 * JFileChooser.APPROVE_OPTION) {
					 * fileCreator.print(fileChooser.getSelectedFile(),
					 * check.getRobotBlockList()); }
					 * 
					 * fileChooser.setSelectedFile(targetFile); if
					 * (fileChooser.showSaveDialog(null) ==
					 * JFileChooser.APPROVE_OPTION) {
					 * fileCreator.print(fileChooser.getSelectedFile(),
					 * check.getGoalBlockList()); }
					 * 
					 * fileChooser.setSelectedFile(obstacleFile); if
					 * (fileChooser.showSaveDialog(null) ==
					 * JFileChooser.APPROVE_OPTION) {
					 * fileCreator.print(fileChooser.getSelectedFile(),
					 * check.getObstacleBlockList()); }
					 * 
					 * } else { JPanel panel = new JPanel();
					 * JOptionPane.showMessageDialog(panel,
					 * check.getErrorMessage(), "Error",
					 * JOptionPane.ERROR_MESSAGE); }
					 */
				}

				super.clicked(event, x, y);
			}
		});

		// Undo Button Listener
		undoButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (StateManager.state == StateManager.SimulationState.MENU)
				{
					blockSimulator.blockList.undo();
				}
				super.clicked(event, x, y);
			}
		});

		// Redo Button Listener
		redoButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (StateManager.state == StateManager.SimulationState.MENU)
				{
					blockSimulator.blockList.redo();
				}
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
					blockSimulator.blockList.removeBlockType(Block.Type.Path);
					// Hide Pause Button
					pauseButton.setVisible(false);
					pauseButton.setTouchable(Touchable.disabled);
				}
				else
				{
					StateManager.state = SimulationState.SIMULATION;
					startButton.setText("Stop");
					// new Movement(blockSimulator.blockList);
					new BrainAI(blockSimulator.blockList);

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
				if(StateManager.state == StateManager.SimulationState.SIMULATION)
				{
					StateManager.state = StateManager.SimulationState.PAUSE;
					pauseButton.setText("Resume");
				}
				else if(StateManager.state == StateManager.SimulationState.PAUSE)
				{
					StateManager.state = StateManager.SimulationState.SIMULATION;
					pauseButton.setText("Pause");
				}
				
				super.clicked(event, x, y);
			}
		});

		// Simulation Button Listener
		simulationButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (StateManager.state == StateManager.SimulationState.MENU)
				{
					new simulationLoader();
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
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public Block.Type getSelectedBlock()
	{
		return selectedBlock;
	}

	public void setSelectedBlock(Block.Type selectedBlock)
	{
		blockLabel.setText("Selected: " + selectedBlock);
		this.selectedBlock = selectedBlock;
	}

	@Override
	public void dispose()
	{
		stage.dispose();
	}

}
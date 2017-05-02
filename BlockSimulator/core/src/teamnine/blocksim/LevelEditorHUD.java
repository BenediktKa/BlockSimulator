package teamnine.blocksim;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelEditorHUD implements Disposable {

	//Have a nice weekend
	//Have an even better weekend
	// Block Simulator
	// Enjoy everything
	// Test branching again and again and again
	// New test
	
	private BlockSimulator blockSimulator;

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

	// Load Simulation Button
	private TextButton simulationButton;

	// Block Dialog
	private Label blockLabel;

	// File Chooser
	private JFileChooser fileChooser;

	// Configuration Checker
	//private ConfigurationChecker check;

	public LevelEditorHUD(final BlockSimulator blockSimulator, final BlockList blockList) {
		this.blockSimulator = blockSimulator;

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
		simulationButton = new TextButton("Load Simulation", skin);

		// File Chooser
		fileChooser = new JFileChooser();

		// Create Labels
		blockLabel = new Label("Selected: " + blockSimulator.cameraController.getBlockType(), skin);

		// Import Button Listener
		importButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isMenuMode()) {
					fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
					fileChooser.setDialogTitle("Open Start Robot Configuration");

					if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						Reader reader = new Reader(selectedFile);
						blockSimulator.blockList.createRobot(reader.getBlockData());
					}
					fileChooser.setDialogTitle("Open Target Configuration");
					if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						Reader reader = new Reader(selectedFile);
						blockSimulator.blockList.createTarget(reader.getBlockData());
					}
					fileChooser.setDialogTitle("Open Obstacle Configuration");
					if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						Reader reader = new Reader(selectedFile);
						blockSimulator.blockList.createObstacles(reader.getBlockData());
					}

				}
				super.clicked(event, x, y);
			}
		});

		// Export Button Listener
		exportButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isMenuMode()) {
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
		undoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isMenuMode()) {
					blockSimulator.blockList.undo();
				}
				super.clicked(event, x, y);
			}
		});

		// Redo Button Listener
		redoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isMenuMode()) {
					blockSimulator.blockList.redo();
				}
				super.clicked(event, x, y);
			}
		});

		// Start Button Listener
		startButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (blockSimulator.cameraController.getModeType() == FPSControl.Type.SimulationMode) {
					blockSimulator.cameraController.setModeType(FPSControl.Type.BuildMode);
					startButton.setText("Start");

				} else {
					blockSimulator.cameraController.setModeType(FPSControl.Type.SimulationMode);
					startButton.setText("Stop");
					// new Movement(blockSimulator.blockList);
					new BrainAI(blockList.getObstacleList(),blockList.getRobotBlockList(),blockList.getTargetList(),blockList.getGridSize());
				}

				super.clicked(event, x, y);
			}
		});

		// Simulation Button Listener
		simulationButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isMenuMode()) {
					fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
					fileChooser.setDialogTitle("Load a simulation from file");
					if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						//Reader reader = new Reader(selectedFile);
						// new simulationRunner(reader.getSimulationData(),
						// blockSimulator);
					}
				}

				super.clicked(event, x, y);
			}

		});

		// Add to Table
		table.add(importButton).padRight(20);
		table.add(exportButton).padRight(20);
		table.add(simulationButton).padRight(20);
		table.add(blockLabel);
		table.row().height(20);
		table.add(undoButton).padRight(20);
		table.add(redoButton).padRight(20);
		table.add(startButton).padRight(20);

		// Add to Stage
		stage.addActor(table);

		blockSimulator.inputMultiplexer.addProcessor(stage);
	}

	public void render() {
		blockLabel.setText("Selected: " + blockSimulator.cameraController.getBlockType());
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public boolean isMenuMode() {
		if (blockSimulator.cameraController.getModeType() == FPSControl.Type.MenuMode) {
			return true;
		}
		return false;
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
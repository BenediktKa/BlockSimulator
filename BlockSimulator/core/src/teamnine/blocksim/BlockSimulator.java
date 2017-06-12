package teamnine.blocksim;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.SelectorBlock;
import teamnine.blocksim.blocklist.BlockListController;
import teamnine.blocksim.hud.Crosshair;
import teamnine.blocksim.hud.FPSCounter;
import teamnine.blocksim.hud.LevelEditorHUD;
import teamnine.blocksim.hud.Notification;

public class BlockSimulator implements ApplicationListener
{

	// Camera variables
	public final float FIELDOFVIEW = 67;
	public final float CAMERA_NEAR = 1;
	public final float CAMERA_FAR = 300;

	public Environment environment;
	public PerspectiveCamera camera;
	public FPSControl fpsController;
	public SpriteBatch spriteBatch;
	public ModelBatch modelBatch;

	// State Manager
	public StateManager stateManager;

	// Input Multiplexer
	public InputMultiplexer inputMultiplexer;

	// HUD
	public LevelEditorHUD levelHUD;

	// Grid size
	public static int gridSize = 15; //Needed it for smartmovement xxJurriaan

	// BlockList
	private BlockListController blockListController;

	// Notifications
	private Notification notification;

	// Crosshair
	private Crosshair crosshair;

	// FPS Counter
	private FPSCounter fpsText;

	@Override
	public void create()
	{

		// Create State Manager
		stateManager = new StateManager(SimulationState.BUILD);

		// Create Environment
		environment = new Environment();
		spriteBatch = new SpriteBatch();
		modelBatch = new ModelBatch();

		// Notification
		notification = Notification.getInstance();

		// Input Multiplexer
		inputMultiplexer = new InputMultiplexer();

		// Lighting
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// Crosshair
		crosshair = new Crosshair();

		// FPS Counter
		fpsText = new FPSCounter();

		// Create Camera
		camera = new PerspectiveCamera(FIELDOFVIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(12.5f, 12.5f, 12.5f);
		camera.lookAt(0, 0, 0);
		camera.near = CAMERA_NEAR;
		camera.far = CAMERA_FAR;

		// Camera Control
		fpsController = new FPSControl(camera);

		// Interface
		levelHUD = new LevelEditorHUD(this);

		// Input
		inputMultiplexer.addProcessor(fpsController);
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCursorCatched(true);
		
		blockListController = BlockListController.getInstance();
		blockListController.initialize(gridSize);
	}

	@Override
	public void render()
	{
		// Set Background Color
		Gdx.gl.glClearColor(44f / 255f, 62f / 255f, 80f / 255f, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// Rendering Models
		modelBatch.begin(camera);
		blockListController.render(modelBatch, environment);
		modelBatch.render(SelectorBlock.getInstance().getModelInstance(), environment);
		modelBatch.end();

		// Render LevelHUD
		levelHUD.render();

		// Rendering Sprite
		spriteBatch.begin();
		notification.render(spriteBatch);
		crosshair.render(spriteBatch);
		fpsText.render(spriteBatch);
		spriteBatch.end();
		
		// Camera Update
		fpsController.update();
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void dispose()
	{
		crosshair.dispose();
		fpsText.dispose();
		blockListController.dispose();
		notification.dispose();
	}
}

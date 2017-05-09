package teamnine.blocksim;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockList;
import teamnine.blocksim.block.SelectorBlock;
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
	public FPSControl cameraController;
	public ExtendViewport viewport;
	public SpriteBatch spriteBatch;
	public ModelBatch modelBatch;
	public BitmapFont font;

	// Input Multiplexer
	public InputMultiplexer inputMultiplexer;

	// HUD
	public LevelEditorHUD levelHUD;

	// Grid size
	public int gridSize = 25;

	// BlockList
	public BlockList blockList;

	// Notifications
	public Notification notification;

	// Crosshair
	public Texture crosshair;

	// Loading
	public SelectorBlock selectorBlock;

	@Override
	public void create()
	{

		// Create Environment
		environment = new Environment();
		spriteBatch = new SpriteBatch();
		modelBatch = new ModelBatch();

		// Notification
		notification = new Notification();

		// Input Multiplexer
		inputMultiplexer = new InputMultiplexer();

		// Lighting
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		// BlockList
		blockList = new BlockList(gridSize, this);
		selectorBlock = new SelectorBlock(new Vector3(0, 0, 0), Block.Type.Selector);
		selectorBlock.moveModel();
		blockList.setSelectorBlock(selectorBlock);

		// Crosshair
		crosshair = new Texture(Gdx.files.internal("interface/Crosshair.png"));
		
		//Font
		font = new BitmapFont();

		// Create Camera
		camera = new PerspectiveCamera(FIELDOFVIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(12.5f, 12.5f, 12.5f);
		camera.lookAt(0, 0, 0);
		camera.near = CAMERA_NEAR;
		camera.far = CAMERA_FAR;

		// Camera Control
		cameraController = new FPSControl(camera, this);

		// Interface
		levelHUD = new LevelEditorHUD(this, blockList);

		// Input
		inputMultiplexer.addProcessor(cameraController);
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCursorCatched(true);
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
		modelBatch.render(selectorBlock.getModelInstance(), environment);
		blockList.render(modelBatch, environment);
		modelBatch.end();

		float crosshair_x = (Gdx.graphics.getWidth() - 25) / 2;
		float crosshair_y = (Gdx.graphics.getHeight() - 25) / 2;

		// Render LevelHUD
		levelHUD.render();

		// Rendering Sprites
		spriteBatch.begin();
		
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 20);
		
		notification.render(spriteBatch);
		spriteBatch.draw(crosshair, crosshair_x, crosshair_y, 25, 25);
		spriteBatch.end();
		
		// Camera Update
		cameraController.update();
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
		blockList.dispose();
		notification.dispose();
	}
}

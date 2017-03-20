package teamnine.blocksim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;

public class BlockSimulator extends ApplicationAdapter {
	
	//Camera variables
	public final float FIELDOFVIEW = 90;
	public final float CAMERA_NEAR = 1;
	public final float CAMERA_FAR = 300;
	public final float CAMERA_VELOCITY = 15;
	public final float CAMERA_DEGREESPERPIXEL = 0.08f;
	
	//Crosshair
	public final float CROSSHAIR_SIZE = 25;
	
	public Environment environment;
	public PerspectiveCamera camera;
	public SpriteBatch spriteBatch;
	public Texture crosshair;
	
	@Override
	public void create () {
		
		//Create Environment
		environment = new Environment();
		spriteBatch = new SpriteBatch();
		
		//Create Camera
		camera = new PerspectiveCamera(FIELDOFVIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10, 20, 10);
		camera.near = CAMERA_NEAR;
		camera.far = CAMERA_FAR;
		camera.update();
		
		crosshair = new Texture(Gdx.files.internal("interface/Crosshair.png"));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.8f, 1.0f, 0.2f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		float crosshair_x = (Gdx.graphics.getWidth() - CROSSHAIR_SIZE) / 2;
		float crosshair_y = (Gdx.graphics.getHeight() - CROSSHAIR_SIZE) / 2;
		spriteBatch.begin();
		spriteBatch.draw(crosshair, crosshair_x, crosshair_y, CROSSHAIR_SIZE, CROSSHAIR_SIZE);
		spriteBatch.end();
	}
	
	@Override
	public void dispose () {
	}
}

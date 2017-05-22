package teamnine.blocksim.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class FPSCounter implements Disposable
{
	private BitmapFont font;
	
	public FPSCounter()
	{
		font = new BitmapFont();
	}

	public void render(SpriteBatch spriteBatch)
	{
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 20);
	}

	@Override
	public void dispose()
	{
		font.dispose();
	}
}

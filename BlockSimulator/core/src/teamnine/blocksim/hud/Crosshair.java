package teamnine.blocksim.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class Crosshair implements Disposable
{
	private Texture texture;
	float crosshairX, crosshairY;

	public Crosshair()
	{
		crosshairX = (Gdx.graphics.getWidth() - 25) / 2;
		crosshairY = (Gdx.graphics.getHeight() - 25) / 2;
		texture = new Texture(Gdx.files.internal("interface/Crosshair.png"));
	}

	public void render(SpriteBatch spriteBatch)
	{
		spriteBatch.draw(texture, crosshairX, crosshairY, 25, 25);
	}

	@Override
	public void dispose()
	{
		texture.dispose();
	}
}

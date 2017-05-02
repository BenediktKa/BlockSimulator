package teamnine.blocksim.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Class that allows to display Notification messages on screen
 */
public class Notification implements Disposable {

	/** Notification BitmapFont */
	private BitmapFont font;
	
	/** Notification Text */
	private String text;
	
	/** Notification Alpha */
	private float alpha;

	/** Notification Layout */
	private GlyphLayout layout;

	/** Notification Type */
	private Type type;

	/**
	 * Notification Type List
	 */
	public enum Type {
		ModeChange, Error;
	}

	/**
	 * Instantiates a new notification.
	 */
	public Notification() {
		font = new BitmapFont();
		layout = new GlyphLayout();
	}

	/**
	 * Render the notification
	 *
	 * @param spriteBatch
	 */
	public void render(SpriteBatch spriteBatch) {
		//Update Alpha
		if (alpha <= 0) {
			return;
		} else {
			alpha -= 0.015;
		}
		
		//If there is no Text
		if (text == null) {
			return;
		}
		
		//Change Text Color based on Type
		if (type == Type.Error) {
			font.setColor(192f / 255f, 57f / 255f, 43f / 255f, alpha);
		} else if (type == Type.ModeChange) {
			font.setColor(236f / 255f, 240f / 255f, 241f / 255f, alpha);
		}
		
		//Draw Text
		font.draw(spriteBatch, text, Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getWidth() / 2);
	}

	/**
	 * Sets the notification.
	 *
	 * @param text to display
	 * @param type of notification
	 */
	public void setNotification(String text, Type type, float scale) {
		this.text = text;
		this.type = type;
		
		font.getData().setScale(scale);
		
		this.alpha = 1;
		layout.setText(font, text);
	}

	/**
	 * Dispose of the font
	 */
	@Override
	public void dispose() {
		font.dispose();
	}
}

package teamnine.blocksim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class Notification implements Disposable {

	private BitmapFont font;
	private String text;
	private float alpha;
	private GlyphLayout layout;
	private Type type;

	public enum Type {
		Error;
	}

	public Notification() {
		font = new BitmapFont();
		layout = new GlyphLayout();
	}

	public void render(SpriteBatch spriteBatch) {
		if (alpha <= 0) {
			return;
		} else {
			alpha -= 0.015;
		}
		if (text != null) {
			if (type == Type.Error) {
				font.setColor(192f / 255f, 57f / 255f, 43f / 255f, alpha);
			}
			font.draw(spriteBatch, text, Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getWidth() / 2);
		}
	}

	public void setNotification(String text, Type type) {
		this.text = text;
		this.type = type;
		this.alpha = 1;
		layout.setText(font, text);
	}

	@Override
	public void dispose() {
		font.dispose();
	}
}

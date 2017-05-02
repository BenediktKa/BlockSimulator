package teamnine.blocksim.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import teamnine.blocksim.BlockSimulator;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "BlockSimulator";
		config.width = 800;
		config.height = 600;
		config.resizable = false;
		
		new LwjglApplication(new BlockSimulator(), config);
	}
}

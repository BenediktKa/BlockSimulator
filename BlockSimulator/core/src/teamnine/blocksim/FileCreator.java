package teamnine.blocksim;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import teamnine.blocksim.block.Block;

public class FileCreator {

	public void print(File file, ArrayList<Block> blockList) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			for (int i = 0; i < blockList.size(); i++) {
				Block block = blockList.get(i);

				if (block.getType() == Block.Type.Obstacle) { // FOR NOW NO ID'S
																// ARE GIVEN TO
																// OBSTACLE
																// BLOCKS
					writer.println(
							block.getPosition().x + ", " + block.getPosition().z + ", " + (block.getPosition().y - 1));
				} else if ((Double) block.getID() == 0.0) {
					writer.println("X, " + block.getPosition().x + ", " + block.getPosition().z + ", "
							+ (block.getPosition().y - 1));
				} else {
					writer.println(block.getID() + ", " + block.getPosition().x + ", " + block.getPosition().z + ", "
							+ (block.getPosition().y - 1));
				}

			}
			writer.close();
		} catch (IOException e) {
			System.out.println("No file specified");
		}
	}

}
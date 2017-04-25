package teamnine.blocksim;

import java.util.ArrayList;

public class Bucket {
	private ArrayList<Block> bloks = new ArrayList<Block>();
	public void addBlock(Block b)
	{
		bloks.add(b);
	}
	public ArrayList<Block> getBloks()
	{
		return bloks;
	}
}

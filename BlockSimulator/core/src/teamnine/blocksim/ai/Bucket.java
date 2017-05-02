package teamnine.blocksim.ai;

import java.util.ArrayList;

import teamnine.blocksim.block.RobotBlock;

public class Bucket {
	private ArrayList<RobotBlock> bloks = new ArrayList<RobotBlock>();
	public void addBlock(RobotBlock b)
	{
		bloks.add(b);
	}
	public ArrayList<RobotBlock> getBloks()
	{
		return bloks;
	}
}

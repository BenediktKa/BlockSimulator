package teamnine.blocksim;

import java.util.ArrayList;

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

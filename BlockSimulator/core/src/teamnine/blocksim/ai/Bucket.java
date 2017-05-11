package teamnine.blocksim.ai;

import java.util.ArrayList;
import java.util.Collections;

import teamnine.blocksim.block.RobotBlock;

public class Bucket {
	private ArrayList<RobotBlock> bloks = new ArrayList<RobotBlock>();
	public void addBlock(RobotBlock b)
	{
		bloks.add(b);
		sort();
	}
	public void sort()
	{
		boolean change=true;
		while(change)
		{
			change=false;
			for(int i=0;i<bloks.size()-1;i++)
			{
				if(bloks.get(i).getPosition().y<bloks.get(i+1).getPosition().y)
				{
					Collections.swap(bloks, i, i+1);
					change=true;					
				}
			}
		}
	}
	
	public ArrayList<RobotBlock> getBloks()
	{
		
		return bloks;
	}
}

package teamnine.blocksim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Movement {
	private ArrayList<Block> robots = new ArrayList<Block>();
	public Movement(BlockList blockList)
	{
		Block end = new Block(null,null);
		for(int i=0;i<blockList.size();i++)
		{
			if(blockList.getBlockList().get(i).getType().toString().equals("Robot"))
				robots.add( blockList.getBlockList().get(i));						
		}
		ArrayList<Integer> distances= new ArrayList<Integer>();;
		Stack<Block> priority = new Stack();
		for(int i=0;i<robots.size();i++)
		{
			distances.add(robots.get(i).getDistanceToGoal());
		}
		boolean check=true;
		while(check)
		{
			check=false;
			for(int k=0;k<distances.size()-1;k++)
			{
				if(distances.get(k)<distances.get(k+1))
				{
					Collections.swap(distances, k, k+1);
					check=true;
				}
			}
		}
		for(int y=0;y<distances.size();y++)
		{
			for(int x=0;x<robots.size();x++)
			{
				if(distances.get(y)==robots.get(x).getDistanceToGoal())
					priority.push( robots.get(x));
			}
		}
		for(int y=0;y<robots.size();y++)
		{
			PathFinding pf= new PathFinding(blockList, robots.get(y));
			ArrayList<Block> path =pf.getBestPath();
			for(int x=0;x<path.size();x++)
			{
				System.out.println(path.size()+" p: "+path.get(x).getPosition().x+" "+path.get(x).getPosition().y+" "+path.get(x).getPosition().z);
				System.out.println(robots.get(y).getPosition().x+" r: "+robots.get(y).getPosition().y+" "+robots.get(y).getPosition().z);

				
				if(robots.get(y).getPosition().x-1==path.get(x).getPosition().x)
				{
					System.out.println("HITT");
					robots.get(y).setPosition(robots.get(y).getPosition().x-1,robots.get(y).getPosition().y,robots.get(y).getPosition().z);
				}
				else if(robots.get(y).getPosition().x+1==path.get(x).getPosition().x)
				{
					System.out.println("HITT");
					robots.get(y).setPosition(robots.get(y).getPosition().x+1,robots.get(y).getPosition().y,robots.get(y).getPosition().z);
				}
				else if(robots.get(y).getPosition().z-1==path.get(x).getPosition().z)
				{
					System.out.println("HITT");
					robots.get(y).setPosition(robots.get(y).getPosition().x,robots.get(y).getPosition().y,robots.get(y).getPosition().z-1);
				}
				else if(robots.get(y).getPosition().z+1==path.get(x).getPosition().z)
				{
					System.out.println("HITT");
					robots.get(y).setPosition(robots.get(y).getPosition().x,robots.get(y).getPosition().y,robots.get(y).getPosition().z+1);
				}
			}
		}
	}
	
	
}

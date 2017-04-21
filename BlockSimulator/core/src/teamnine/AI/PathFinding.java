package teamnine.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import teamnine.blocksim.Block;
import teamnine.blocksim.BlockList;

public class PathFinding 
{
	private ArrayList<Block> blockList= new ArrayList<Block>();
	private ArrayList<Block> currentPath= new ArrayList<Block>();
	private int steps=-1;
	private Block end;
	private Block start;
	
	public PathFinding(int gs, BlockList bl)
	{
		for(int t=0; t<bl.size();t++)
		{
			blockList.add(bl.getBlock(t));
		}
		
		for(int i=0;i<blockList.size();i++)
		{
			if(blockList.get(i).getType().toString().equals("Goal")&&end==null)
				end=blockList.get(i);
			else if(blockList.get(i).getType().toString().equals("Goal"))
				blockList.remove(i);
			if(blockList.get(i).getType().toString().equals("Robot")&&end==null)
				start=blockList.get(i);
			else if(blockList.get(i).getType().toString().equals("Robot"))
				blockList.remove(i);
		}
		for(int t=0; t<blockList.size();t++)
		{
			blockList.get(t).setDistanceToGoal((int)Math.abs(blockList.get(t).getPosition().x-end.getPosition().x)+(int)Math.abs(blockList.get(t).getPosition().z-end.getPosition().z));
		}
		System.out.println("x: "+end.getPosition().x+" z: "+end.getPosition().z);
		System.out.println("start");
		for(int i =0;i<blockList.size();i++)
		{
			
			
			for(int k=0;k<blockList.size();k++)
			{
				if(blockList.get(i).getPosition().x+1==blockList.get(k).getPosition().x&&blockList.get(i).getPosition().z==blockList.get(k).getPosition().z)
				{
					//.out.println("x: "+blockList.get(k).getPosition().x+" z: "+blockList.get(k).getPosition().z);
					blockList.get(i).addConnection(blockList.get(k));
				}
				else if(blockList.get(i).getPosition().x-1==blockList.get(k).getPosition().x&&blockList.get(i).getPosition().z==blockList.get(k).getPosition().z)
				{
					//System.out.println("x: "+blockList.get(k).getPosition().x+" z: "+blockList.get(k).getPosition().z);
					blockList.get(i).addConnection(blockList.get(k));
				}
				else if(blockList.get(i).getPosition().x==blockList.get(k).getPosition().x&&blockList.get(i).getPosition().z+1==blockList.get(k).getPosition().z)
				{
					//System.out.println("x: "+blockList.get(k).getPosition().x+" z: "+blockList.get(k).getPosition().z);
					blockList.get(i).addConnection(blockList.get(k));
				}
				else if(blockList.get(i).getPosition().x==blockList.get(k).getPosition().x&&blockList.get(i).getPosition().z-1==blockList.get(k).getPosition().z)
				{
					//System.out.println("x: "+blockList.get(k).getPosition().x+" z: "+blockList.get(k).getPosition().z);
					blockList.get(i).addConnection(blockList.get(k));
				}
			}
			
		}
		ArrayList<Block> startList = new ArrayList<Block>();
		startList.add(start);
		findPath(startList);
	}
	public ArrayList<Block> findPath(ArrayList<Block> b)
	{
		currentPath.addAll(b);
		ArrayList<Block> path = new ArrayList<Block>();
		System.out.println("x: "+b.get(0).getPosition().x+" z: "+b.get(0).getPosition().z);
		if(b.get(0).getPosition().x==end.getPosition().x&&b.get(0).getPosition().z==end.getPosition().z)
		{
			System.out.println("target found");
			return b;
		}
		else
		{
			ArrayList<Integer> distances= new ArrayList<Integer>();;
			Stack<Block> priority = new Stack();
			for(int i=0;i<b.get(0).getConnections().size();i++)
			{
				distances.add(b.get(0).getConnections().get(i).getDistanceToGoal());
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
				for(int x=0;x<distances.size();x++)
				{
					if(distances.get(y)==b.get(0).getConnections().get(x).getDistanceToGoal())
						priority.push(b.get(0).getConnections().get(x));
				}
			}
			ArrayList<Block> tmp = new ArrayList<Block>();
			tmp.add(priority.pop());
			path.add(findPath(tmp).get(0));
			return path;
		}
		
		
	}
}
		
				
	
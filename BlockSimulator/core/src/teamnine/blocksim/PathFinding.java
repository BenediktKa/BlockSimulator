package teamnine.blocksim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class PathFinding 
{
	private ArrayList<Block> blockList= new ArrayList<Block>();
	private ArrayList<Block> currentPath= new ArrayList<Block>();
	private ArrayList<Block> obstacles = new ArrayList<Block>();
	private Block lastVisited;
	private Block end;
	private Block start;
	private ArrayList<Block> bestPath = new ArrayList<Block>();
	private int bestLength=-1;
	private int currentLength=0;
	private boolean improved=true;
	
	public PathFinding(BlockList bl, Block block)
	{
		//creates floor
		for(int t=0; t<bl.size();t++)
		{
			blockList.add(bl.getBlock(t));
		}
		//selects obstacles, start block and end block, removes all unneccesary blocks.
		for(int i=0;i<blockList.size();i++)
		{
			
			if(blockList.get(i).getType().toString().equals("Goal")&&end==null)
				{ end=blockList.get(i);
				System.out.println("Set end");
				}
			else if(blockList.get(i).getType().toString().equals("Goal"))
				blockList.remove(i);
			if(blockList.get(i).getType().toString().equals("Robot")&&start==null)
				start=blockList.get(i);
			else if(blockList.get(i).getType().toString().equals("Robot"))
				blockList.remove(i);
			else if(blockList.get(i).getType().toString().equals("Obstacle"))
			{
				obstacles.add(blockList.get(i));	
				blockList.remove(i);
			}
				
		}
		System.out.println("obstacles "+obstacles.size());;
		System.out.println("x: "+end.getPosition().x+" z: "+end.getPosition().z);
		
		
		//sets for each block how far it is removed from tehe goal.
		for(int t=0; t<blockList.size();t++)
		{
			blockList.get(t).setDistanceToGoal((int)Math.abs(blockList.get(t).getPosition().x-end.getPosition().x)+(int)Math.abs(blockList.get(t).getPosition().z-end.getPosition().z));
		}
		
		System.out.println("start");
		//turn the floor into a linked list
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
		//runs the pathFinding algorithm
		ArrayList<Block> startList = new ArrayList<Block>();
		startList.add(start);
		while(improved)
		{
			improved=false;
			for(int o=0;o<4;o++)
			{
				findPath(startList);
			}
			
			
		}
		
			
	}
	public ArrayList<Block> findPath(ArrayList<Block> b)
	{
		//adds current block to the walked path.
		currentPath.addAll(b);
		int totalObstacles=0;
		//checks for obstacle block on top of the current block
		System.out.println("ob"+obstacles.get(0));
		/*
		for(int r=0;r<obstacles.size();r++)
		{
			if(obstacles.get(r).getPosition().x==b.get(0).getPosition().x&&obstacles.get(r).getPosition().z==b.get(0).getPosition().z)
			{
				totalObstacles++;
			}
		
		}
		//decides new distance and saves it
		if(lastVisited!=null)
		{
			System.out.println("tot: "+totalObstacles+" last "+lastVisited.getHeight());
			b.get(0).setDistanceToGoal(b.get(0).getDistanceToGoal()+Math.abs(totalObstacles-lastVisited.getHeight()));
			b.get(0).setHeight(totalObstacles);
		}
		else
		{
			System.out.println("tot: "+totalObstacles);
			b.get(0).setDistanceToGoal(b.get(0).getDistanceToGoal()+totalObstacles);
			b.get(0).setHeight(totalObstacles);
		}
		*/
		ArrayList<Block> path = new ArrayList<Block>();
		System.out.println("x: "+b.get(0).getPosition().x+" z: "+b.get(0).getPosition().z);
		//checks if current block is on target/end block
		if(b.get(0).getPosition().x==end.getPosition().x&&b.get(0).getPosition().z==end.getPosition().z)
		{
			System.out.println("target found");
			return b;
		}
		//if not then will try best option to come closer to the target.
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
			lastVisited=b.get(0);
			ArrayList<Block> tmp = new ArrayList<Block>();
			currentLength=currentLength+b.get(0).getDistanceToGoal();
			System.out.println(currentLength+" bl: "+bestLength);
			tmp.add(priority.pop());
			path.add(findPath(tmp).get(0));
			//checks if path found is better than the one already found, if there is any.
			if(bestLength==-1)
			{
				System.out.println("bl = -1");
				bestLength=currentLength;
				currentLength=0;
				bestPath=currentPath;
				improved=true;
			}
			else
			{
				
				if(currentLength<bestLength&&currentLength!=0)
				{
					System.out.println("improved " + currentLength+" bl: "+bestLength);
					bestLength=currentLength;
					currentLength=0;
					bestPath=currentPath;
					improved=true;
				}
				else
				{
					System.out.println("not improved " + currentLength+" bl: "+bestLength);
					
					currentLength=0;
					
				}
			}
			lastVisited=new Block(null, null);	
			return currentPath;
		}
		
		
	}
	//gets result.
	public ArrayList<Block> getBestPath()
	{
		return bestPath;
	}
}
		
				
	
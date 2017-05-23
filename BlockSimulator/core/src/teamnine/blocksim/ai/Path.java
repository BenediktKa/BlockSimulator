package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockList;

public class Path {
	
	private BlockList blockList;
	private Block target;
	private Block mainTarget;
	private Block intitalPosition;
	private ArrayList<Vector3> bestPath=new ArrayList<Vector3>();
	private int bestDistance=-1;
	private int currentDistance=0;
	private int numberOfRobots;
	private int targetBlocks;
	private ArrayList<Vector3> currentPath=new ArrayList<Vector3>();

	public Path(BlockList blockList, Block initialPosition, Block target, int numRoboBlocks, int numTargetBlocks)
	{
		System.out.println("Path" +target.getPosition());
		numberOfRobots=blockList.getRobotBlockList().size();
		targetBlocks=blockList.getGoalList().size();
		this.blockList=blockList;
		this.intitalPosition=initialPosition;
		this.target=target;		
		mainTarget=target;
		findPath(initialPosition.getPosition());
	}
	public void findPath(Vector3 start)
	{
		
		if(start.x==target.getPosition().x&&start.z==target.getPosition().z)
		{
			return;
		}
		currentPath.add(new Vector3(start.x,start.y,start.z));
		currentDistance=currentDistance+(int) ((Math.abs(start.x - target.getPosition().x) + Math.abs(start.z - target.getPosition().z) + start.y) - 1);
		ArrayList<Vector3> moves = new ArrayList<Vector3>();
		ArrayList<Integer> moveDistances = new ArrayList<Integer>();
		if(start.x!=0)
		{
			moves.add(new Vector3(start.x-1,0,start.z));
			moveDistances.add((int) ((Math.abs(start.x-1 - target.getPosition().x) + Math.abs(start.z - target.getPosition().z) + start.y) - 1));
		}
		if(start.x!=blockList.getGridSize())
		{
			moves.add(new Vector3(start.x+1,0,start.z));
			moveDistances.add((int) ((Math.abs(start.x+1 - target.getPosition().x) + Math.abs(start.z - target.getPosition().z) + start.y) - 1));
		}
		if(start.z!=0)
		{
			moves.add(new Vector3(start.x,0,start.z-1));
			moveDistances.add((int) ((Math.abs(start.x - target.getPosition().x) + Math.abs(start.z - target.getPosition().z-1) + start.y) - 1));
		
		}
		if(start.z!=blockList.getGridSize())
		{
			moves.add(new Vector3(start.x,0,start.z+1));
			moveDistances.add((int) ((Math.abs(start.x-1 - target.getPosition().x) + Math.abs(start.z - target.getPosition().z+1) + start.y) - 1));
		}
		
		System.out.println(moves.size()+" "+moveDistances.size()+" "+moveDistances.get(0));
		int bestMoveD=-1;
		Vector3 bestMove= new Vector3(0,0,0);
		for(int i=0;i<moves.size();i++)
		{
			if(bestMoveD==-1)
			{
				bestMoveD=moveDistances.get(i);
				bestMove=moves.get(i);
			}
			if(moveDistances.get(i)<bestMoveD)
			{
				bestMoveD=moveDistances.get(i);
				bestMove=moves.get(i);
			}
		}/*
		boolean wall=false;
		boolean climbable=true;
		for(int i=0;i<blockList.getObstacleList().size();i++)
		{
			if(bestMove.x==blockList.getObstacleList().get(i).getPosition().x&&bestMove.z==blockList.getObstacleList().get(i).getPosition().z)
			{
				wall=true;
			}
		}
		if(wall)
		{
			int height=0;
			for(int i=0;i<blockList.getObstacleList().size();i++)
			{
				if(bestMove.x==blockList.getObstacleList().get(i).getPosition().x&&bestMove.z==blockList.getObstacleList().get(i).getPosition().z)
				{
					height++;
				}
			}
			int lostBlocks=1;
			for(int i=0;i<height;i++)
			{
				lostBlocks=lostBlocks+i;
			}
			if((numberOfRobots-lostBlocks)<targetBlocks)
				climbable=false;
		}
		if(wall&&!climbable)
		{
			findOpenening(bestMove,null,start);
		}*/
		
		
		currentPath.add(bestMove);
		currentDistance=currentDistance+bestMoveD;
		System.out.println(bestMove+" "+bestMoveD);
		findPath(bestMove);
		
	}/*
	private Vector3 findOpenening(Vector3 current, Vector3 last, Vector3 start) {
		ArrayList<Vector3> walls = new ArrayList<Vector3>();
		for(int i=0;i<blockList.getObstacleList().size();i++)
		{
			if(current.x+1==blockList.getObstacleList().get(i).getPosition().x&&current.z==blockList.getObstacleList().get(i).getPosition().z)
			{
				walls.add(new Vector3(current.x+1,1,current.z));
			}
			else if(current.x-1==blockList.getObstacleList().get(i).getPosition().x&&current.z==blockList.getObstacleList().get(i).getPosition().z)
			{
				walls.add(new Vector3(current.x-1,1,current.z));
			}
			else if(current.x==blockList.getObstacleList().get(i).getPosition().x&&current.z+1==blockList.getObstacleList().get(i).getPosition().z)
			{
				walls.add(new Vector3(current.x,1,current.z+1));
			}
			else if(current.x==blockList.getObstacleList().get(i).getPosition().x&&current.z-1==blockList.getObstacleList().get(i).getPosition().z)
			{
				walls.add(new Vector3(current.x,1,current.z-1));
			}
			else if(current.x+1==blockList.getObstacleList().get(i).getPosition().x&&current.z+1==blockList.getObstacleList().get(i).getPosition().z)
			{
				walls.add(new Vector3(current.x+1,1,current.z+1));
			}
			else if(current.x-1==blockList.getObstacleList().get(i).getPosition().x&&current.z-1==blockList.getObstacleList().get(i).getPosition().z)
			{
				walls.add(new Vector3(current.x-1,1,current.z-1));
			}
			else if(current.x-1==blockList.getObstacleList().get(i).getPosition().x&&current.z+1==blockList.getObstacleList().get(i).getPosition().z)
			{
				walls.add(new Vector3(current.x-1,1,current.z+1));
			}
			else if(current.x+1==blockList.getObstacleList().get(i).getPosition().x&&current.z-1==blockList.getObstacleList().get(i).getPosition().z)
			{
				walls.add(new Vector3(current.x+1,1,current.z-1));
			}
		}
		for(int i=0;i<walls.size();i++)
		{
			if(walls.get(i).equals(last))
			{
				walls.remove(i);
			}
		}
		Vector3 toReturn=null;
		if(walls.size()==0)
		{
			if(current.x>last.x)
				toReturn= new Vector3(current.x+1,1,current.z);
			else if(current.x<last.x)
				toReturn=  new Vector3(current.x-1,1,current.z);
			else if(current.z>last.z)
				toReturn=  new Vector3(current.x,1,current.z+1);
			else if(current.z>last.z)
				toReturn=  new Vector3(current.x,1,current.z-1);
		}
		else
		{
			for(int i=0;i<walls.size();i++)
			{
				if(start.x<walls.get(i).x||start.x>walls.get(i).x)
				{
					
				}
				else
				{
					
				}
			}
			
		}
		
	}*/
	public ArrayList<Vector3> getFinalList()
	{
		return currentPath;
	}
}

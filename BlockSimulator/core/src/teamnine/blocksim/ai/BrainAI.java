package teamnine.blocksim.ai;
import java.util.ArrayList;


import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockType;
import teamnine.blocksim.block.RobotBlock;
import teamnine.blocksim.block.blocklist.BlockListController;
import teamnine.blocksim.hud.LevelEditorHUD.AIMode;
import teamnine.blocksim.ai.astar.*;

public class BrainAI //
{
	private ArrayList<Block> obstacles;
	private ArrayList<RobotBlock> robots = new ArrayList<RobotBlock>();
	private ArrayList<Block> target;
	private ArrayList<Block> floor;
	private AIMode typeAI;
	private BlockListController blockListController = null;

	public BrainAI(AIMode typeAI)
	{
		blockListController = BlockListController.getInstance();
		this.obstacles = blockListController.getBlockList(BlockType.Obstacle);
		ArrayList<Block> tempRobots = blockListController.getBlockList(BlockType.Robot);
		for(Block block : tempRobots)
		{
			robots.add((RobotBlock)block);
		}
		this.target = blockListController.getBlockList(BlockType.Goal);
		this.floor = blockListController.getBlockList(BlockType.Floor);
		this.typeAI = typeAI;
		// Block maxTarget=findFurthestTarget();
		final Block minTarget = findClosestTarget();
		RobotBlock maxRobot = findClosestRobot(minTarget);
		
		if (typeAI == AIMode.Dijkstra)
		// Dijkstra PathFinder
		{
			final PathFinder path = new PathFinder();
			path.startPathFinder(maxRobot, minTarget);
			final ArrayList<Vector3> finalPath = path.getFinalList();
			final ArrayList<Vector3> aPath2=  new ArrayList<Vector3>();
			for(int i=finalPath.size()-1;i>-1;i--)
			{
				aPath2.add(finalPath.get(i));
			}
			for (Vector3 vector : aPath2)
			{
				blockListController.createBlock(vector, BlockType.Path);
			}
			final MoveSnake movement = new MoveSnake(robots, obstacles, floor);
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					movement.startMove3(aPath2);
				}
			}).start();
		}
		else if (typeAI == AIMode.Astar)
		// Astar PathFinder
		{
			AStarPath aStar = new AStarPath();
			final ArrayList<Vector3> aPath=  aStar.initializeAStar();
			for (Vector3 vector : aPath)
			{
				blockListController.createBlock(vector, BlockType.Path);
			}
			
			final MoveSnake aMovement = new MoveSnake(robots, obstacles, floor);
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					aMovement.startMove3(aPath);
				}
			}).start();
		}
		else
		//Greedy PathFinder
		{
		
			//final Path p2 = new Path();
			//p2.findPath(maxRobot.getPosition(), minTarget);
			final ArrayList<Vector3> p3= new ArrayList<Vector3>();
			p3.add(minTarget.getPosition());
			final Moves movement = new Moves(robots, obstacles, floor, target.size());
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					movement.startMove3(p3);
				}
			}).start();
			
		}
	}

	/*
	 * Does not take the y-coordinates into account, since PathFinding fails
	 * with targetblocks that are not on the floor
	 */
	public Block findFurthestTarget()
	{
		RobotBlock first = robots.get(0);
		int maxDistance = 0;
		Block maxTarget = target.get(0);
		for (int i = 0; i < target.size(); i++)
		{
			int distance = (int) (Math.abs(first.getPosition().x - target.get(i).getPosition().x) + Math.abs(first.getPosition().z - target.get(i)
					.getPosition().z)/* + (target.get(i).getPosition().y-1) */);
			if (distance > maxDistance)
			{
				maxDistance = distance;
				maxTarget = target.get(i);
				System.out.println((first.getPosition().x - maxTarget.getPosition().x) + " " + (first.getPosition().z - maxTarget.getPosition().z) + " " + (target.get(i).getPosition().y - 1));
			}
		}
		return maxTarget;
	}
	
	

	public Block findClosestTarget()
	{
		RobotBlock first = robots.get(0);
		int minDistance = -1;
		Block minTarget = target.get(0);
		for (int i = 0; i < target.size(); i++)
		{
			int distance = (int) (Math.abs(first.getPosition().x - target.get(i).getPosition().x) + Math.abs(first.getPosition().z - target.get(i).getPosition().z) + (target.get(i).getPosition().y - 1));
			if (minDistance == -1)
			{
				minDistance = distance;
				minTarget = target.get(i);
			}
			if (distance < minDistance)
			{
				minDistance = distance;
				minTarget = target.get(i);
			}
		}
		
		return new Block(findTargetOrigin(), BlockType.Path);
	}

	public Vector3 findTargetOrigin()
	{
		float minX = Float.MAX_VALUE;
		float maxX = -1;
		float minZ = Float.MAX_VALUE;
		float maxZ = -1;
		float minY = Float.MAX_VALUE;
		
		for (Block target : target)
		{
			if(target.getPosition().x < minX)
			{
				minX = target.getPosition().x;
			}
			if(target.getPosition().x > maxX)
			{
				maxX = target.getPosition().x;
			}
			if(target.getPosition().z < minZ)
			{
				minZ = target.getPosition().z;
			}
			if(target.getPosition().z > maxZ)
			{
				maxZ = target.getPosition().z;
			}
			if(target.getPosition().y < minY)
			{
				minY = target.getPosition().y;
			}
		}
		
		float minRobotX = Float.MAX_VALUE;
		float maxRobotX = -1;
		float minRobotZ = Float.MAX_VALUE;
		float maxRobotZ = -1;
		
		for (Block robot : robots)
		{
			if(robot.getPosition().x < minRobotX)
			{
				minRobotX = robot.getPosition().x;
			}
			if(robot.getPosition().x > maxRobotX)
			{
				maxRobotX = robot.getPosition().x;
			}
			if(robot.getPosition().z < minRobotZ)
			{
				minRobotZ = robot.getPosition().z;
			}
			if(robot.getPosition().z > maxRobotZ)
			{
				maxRobotZ = robot.getPosition().z;
			}
		}
		
		float TargetXOrigin;
		float TargetZOrigin;
		float TargetYOrigin = minY;
		boolean Xgreater = false;
		boolean Xsmaller = false;
		boolean Zgreater = false;
		boolean Zsmaller = false;
		
		if(maxX <= minRobotX)
		{
			TargetXOrigin = maxX;
			Xsmaller = true;
		}
		else if(minX >= maxRobotX)
		{
			TargetXOrigin = minX;
			Xgreater = true;
		}
		else TargetXOrigin = minX;
		
		if(maxZ <= minRobotZ)
		{
			TargetZOrigin = maxZ;
			Zsmaller = true;
		}
		else if(minZ >= maxRobotZ)
		{
			TargetZOrigin = minZ;
			Zgreater = true;
		}
		else TargetZOrigin = minZ;
		
		
		//To prevent that the origin would be positioned on a place where a obstacle is 
		//TODO: CHECK how to deal with both x and z increasing/decreasing... only one, the right one, would be more usefull.
		Vector3 Targetorigin = new Vector3(TargetXOrigin, TargetYOrigin, TargetZOrigin);
		while(null!=blockListController.getBlockAtPointWithType(Targetorigin, BlockType.Obstacle))
		{
			if(Xgreater) TargetXOrigin++;
			else if(Xsmaller) TargetXOrigin--;
			if(Zgreater) TargetZOrigin++;
			else if(Zsmaller) TargetZOrigin--;
			Targetorigin = new Vector3(TargetXOrigin, TargetYOrigin, TargetZOrigin);
		}
		return Targetorigin;
	}
	
	public RobotBlock findClosestRobot(Block mt)
	{

		int minDistance = -1;
		RobotBlock minRobot = robots.get(0);
		for (int i = 0; i < robots.size(); i++)
		{
			int distance = (int) (Math.abs(mt.getPosition().x - robots.get(i).getPosition().x) + Math.abs(mt.getPosition().z - robots.get(i).getPosition().z) + robots.get(i).getPosition().y);

			if (minDistance == -1)
			{
				minDistance = distance;
				minRobot = robots.get(i);
			}
			else if (distance < minDistance)
			{
				minDistance = distance;
				minRobot = robots.get(i);
			}
		}
		return minRobot;
	}
}
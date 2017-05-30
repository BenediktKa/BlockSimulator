package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockList;
import teamnine.blocksim.block.RobotBlock;
import teamnine.blocksim.hud.LevelEditorHUD.AIMode;

public class BrainAI //
{
	private ArrayList<Block> obstacles;
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> target;
	private ArrayList<Block> floor;
	AIMode typeAI;

	public BrainAI(BlockList blockList, AIMode typeAI)
	{
		this.obstacles = blockList.getBlockList(Block.Type.Obstacle);
		this.robots = blockList.getRobotBlockList();
		this.target = blockList.getBlockList(Block.Type.Goal);
		this.floor = blockList.getBlockList(Block.Type.Floor);
		this.typeAI = typeAI;
		// Block maxTarget=findFurthestTarget();
		final Block minTarget = findClosestTarget();
		RobotBlock maxRobot = findClosestRobot(minTarget);
		
		if (typeAI == AIMode.Dijkstra)
		// Dijkstra PathFinder
		{
			final PathFinder path = new PathFinder(blockList, robots.size(), target.size());
			path.startPathFinder(maxRobot, minTarget);
			ArrayList<Vector3> finalPath = path.getFinalList();
			for (Vector3 vector : finalPath)
			{
				blockList.createBlock(vector, Block.Type.Path);
			}
			final Move3 movement = new Move3(robots, obstacles, floor);
		}
		
		else
		//Greedy PathFinder
		{
			final Path p2 = new Path(blockList, robots.size(), target.size());
			p2.findPath(maxRobot.getPosition(), minTarget);
			final ArrayList<Vector3> p3= new ArrayList<Vector3>();
			p3.add(minTarget.getPosition());
			final Move6 movement = new Move6(robots, obstacles, floor);
		
			boolean testingMovement = true;
			if(testingMovement)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						movement.startMove3(p3, minTarget);
					}
				}).start();
			}
		}
		//TODO: START RECONFIGURATION WHEN MOVEMENT IS DONE
		
		final SmartMovement smartMovement = new SmartMovement(blockList); //name is not to offend anyone, it isn't smart at all
		final BlockList thisblocklist = blockList;
		
		final Move6 movement = new Move6(robots, obstacles, floor);
		boolean testingReconfiguration = false;
		if(testingReconfiguration)
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					new Reconfiguration(thisblocklist,minTarget,movement,smartMovement);			
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
		return minTarget;
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
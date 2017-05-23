package teamnine.blocksim.ai;

import java.util.ArrayList;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockList;
import teamnine.blocksim.block.RobotBlock;

public class BrainAI //
{
	private ArrayList<Block> obstacles;
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> target;
	private ArrayList<Block> floor;

	public BrainAI(BlockList blockList)
	{
		this.obstacles = blockList.getBlockList(Block.Type.Obstacle);
		this.robots = blockList.getRobotBlockList();
		this.target = blockList.getBlockList(Block.Type.Goal);
		this.floor = blockList.getBlockList(Block.Type.Floor);
		// Block maxTarget=findFurthestTarget();
		final Block minTarget = findClosestTarget();
		RobotBlock maxRobot = findClosestRobot(minTarget);
		final PathFinder path = new PathFinder(blockList, robots.size(), target.size());
		final Path p2 = new Path(blockList, maxRobot, minTarget, robots.size(), target.size());

		for (int i = 0; i < p2.getFinalList().size(); i++)
		{
			blockList.createBlock(p2.getFinalList().get(i), Block.Type.Path);
		}

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new Move3(p2.getFinalList(), robots, obstacles, floor, minTarget);
			}
		}).start();
		// new Reconfiguration(robots,target,minTarget);

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
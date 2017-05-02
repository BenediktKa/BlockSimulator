package teamnine.blocksim.ai;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.RobotBlock;

public class BrainAI {
	private ArrayList<Block> obstacles;
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> target;
	int gridSize;
	public BrainAI(ArrayList<Block> obstacles, final ArrayList<RobotBlock> robots, ArrayList<Block> target, int gridSize)
	{
		this.obstacles=obstacles;
		this.robots=robots;
		this.target=target;
		this.gridSize=gridSize;
		Block maxTarget=findFurthestTarget();
		RobotBlock maxRobot=findClosestRobot(maxTarget);
		final PathFinder path = new PathFinder(obstacles,maxRobot ,maxTarget,gridSize,gridSize);
		
		new Thread(new Runnable() {
			   @Override
			   public void run() {
				  new Move(path.getFinalList(), robots);
			   }
			}).start();
	}
	public Block findFurthestTarget()
	{
		RobotBlock first = robots.get(0);
		int maxDistance=0;
		Block maxTarget=target.get(0);
		for(int i=0; i<target.size();i++)
		{
			int distance = (int)(Math.abs(first.getPosition().x-target.get(i).getPosition().x)+Math.abs(first.getPosition().z-target.get(i).getPosition().z)+target.get(i).getPosition().y);
			if(distance>maxDistance)
			{
				maxDistance=distance;
				maxTarget=target.get(i);
			}
		}
		return maxTarget;
	}
	public RobotBlock findClosestRobot(Block mt)
	{
		
		int minDistance=-1;
		RobotBlock minRobot=robots.get(0);
		for(int i=0; i<robots.size();i++)
		{
			int distance = (int)(Math.abs(mt.getPosition().x-robots.get(i).getPosition().x)+Math.abs(mt.getPosition().z-robots.get(i).getPosition().z)+robots.get(i).getPosition().y);

			if(minDistance==-1)
			{
				minDistance=distance;
				minRobot=robots.get(i);
			}
			else if(distance<minDistance)
			{
				minDistance=distance;
				minRobot=robots.get(i);
			}
		}
		return minRobot;
	}
}
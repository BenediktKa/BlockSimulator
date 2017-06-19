package teamnine.blocksim.ai.astar;

import com.badlogic.gdx.math.Vector3;

public class AStarBlock implements Comparable<AStarBlock>
{
	private Vector3 position;
	private double cost = Integer.MAX_VALUE;
	private double priority = 0;
	private AStarBlock previousBlock;
	
	public AStarBlock(Vector3 position)
	{
		this.position = position;
	}
	
	public Vector3 getPosition()
	{
		return position;
	}
	
	public double getCost()
	{
		return cost;
	}
	
	public void setCost(double cost)
	{
		this.cost = cost;
	}
	
	public double getPriority()
	{
		return priority;
	}
	
	public void setPriority(double priority)
	{
		this.priority = priority;
	}
	
	public AStarBlock getPreviousBlock()
	{
		return previousBlock;
	}
	
	public void setPreviousBlock(AStarBlock previousBlock)
	{
		this.previousBlock = previousBlock;
	}

	public int compareTo(AStarBlock block2)
	{
		if(this.priority > block2.priority)
			return -1;
		if(this.priority < block2.priority)
			return 1;
		return 0;
	}
	
	
}

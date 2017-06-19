package teamnine.blocksim.ai.astar;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.BlockType;
import teamnine.blocksim.block.blocklist.BlockListController;
import teamnine.blocksim.hud.Notification;

public class AStarPath
{
	private BlockListController blockListController = null;
	private int agentCount;
	private int goalCount;
	
	private Vector3 startPos, endPos;

	public AStarPath()
	{
		blockListController = BlockListController.getInstance();
	}

	public ArrayList<Vector3> initializeAStar()
	{
		agentCount = blockListController.getBlockList(BlockType.Robot).size();
		goalCount = blockListController.getBlockList(BlockType.Goal).size();
		
		if (agentCount < 2)
			return null;
		
		startPos = blockListController.getBlockList(BlockType.Robot).get(0).getPosition();
		endPos = blockListController.getBlockList(BlockType.Goal).get(0).getPosition();

		return buildPath();
	}

	public ArrayList<Vector3> buildPath()
	{
		double maxDistance = startPos.dst(endPos);
		
		PriorityQueue<AStarBlock> priorityQueue = new PriorityQueue<AStarBlock>();
		AStarBlock rootBlock = new AStarBlock(startPos);
		rootBlock.setCost(0);
		AStarBlock currentBlock = rootBlock;
		
		priorityQueue.add(rootBlock);
		while(!priorityQueue.isEmpty())
		{
			currentBlock = priorityQueue.poll();
			
			if(currentBlock.getPosition().equals(endPos))
				return createPathList(currentBlock);
			
			for(Vector3 nextPos : getNeighbors(currentBlock.getPosition()))
			{
				double newCost = currentBlock.getCost() + 1;
				AStarBlock nextBlock = posExists(currentBlock, nextPos);
				
				if(newCost < nextBlock.getCost())
				{
					nextBlock.setCost(newCost);
					nextBlock.setPreviousBlock(currentBlock);
					nextBlock.setPriority(newCost + (maxDistance - nextPos.dst(endPos)));
					priorityQueue.add(nextBlock);
				}
			}
		}
		return null;
	}
	
	public ArrayList<Vector3> getNeighbors(Vector3 currentPos)
	{
		ArrayList<Vector3> neighbors = new ArrayList<Vector3>();
		
		Vector3 x1 = new Vector3(currentPos.x + 1, currentPos.y, currentPos.z);
		Vector3 x1y1 = new Vector3(currentPos.x + 1, currentPos.y + 1, currentPos.z);
		Vector3 x2 = new Vector3(currentPos.x - 1, currentPos.y, currentPos.z);
		Vector3 x2y2 = new Vector3(currentPos.x - 1, currentPos.y + 1, currentPos.z);
		Vector3 z1 = new Vector3(currentPos.x, currentPos.y, currentPos.z + 1);
		Vector3 z1y1 = new Vector3(currentPos.x, currentPos.y + 1, currentPos.z + 1);
		Vector3 z2 = new Vector3(currentPos.x, currentPos.y, currentPos.z - 1);
		Vector3 z2y2 = new Vector3(currentPos.x, currentPos.y + 1, currentPos.z - 1);
		
		if(inGrid(x1.x) && validBlock(x1)&& validBlock(x1y1))
			neighbors.add(x1);
		if(inGrid(x2.x) && validBlock(x2)&& validBlock(x2y2))
			neighbors.add(x2);
		if(inGrid(z1.z) && validBlock(z1) && validBlock(z1y1))
			neighbors.add(z1);
		if(inGrid(z2.z) && validBlock(z2) && validBlock(z2y2))
			neighbors.add(z2);
		
		return neighbors;
	}
	
	private AStarBlock posExists(AStarBlock aStarBlock, Vector3 pos)
	{
		while(aStarBlock.getPreviousBlock() != null)
		{
			aStarBlock = aStarBlock.getPreviousBlock();
			if(aStarBlock.getPosition().equals(pos))
				return aStarBlock;
		}
		return new AStarBlock(pos);
	}
	
	public ArrayList<Vector3> createPathList(AStarBlock block)
	{
		ArrayList<Vector3> reversePath = new ArrayList<Vector3>();
		ArrayList<Vector3> path = new ArrayList<Vector3>();
		
		while(block.getPreviousBlock() != null)
		{
			block = block.getPreviousBlock();
			reversePath.add(block.getPosition());
		}
		
		for(int i = reversePath.size() - 1; i >= 0; i--)
		{
			path.add(reversePath.get(i));
		}
		
		return path;
	}
	
	private boolean validBlock(Vector3 pos)
	{
		return blockListController.getBlockTypeAtPoint(pos) != BlockType.Obstacle;
	}
	
	public boolean inGrid(float value)
	{
		if (value >= blockListController.getFloorGridSize() || value < 0)
			return false;
		return true;
	}
}

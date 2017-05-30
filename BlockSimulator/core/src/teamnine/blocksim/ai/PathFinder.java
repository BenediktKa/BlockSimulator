package teamnine.blocksim.ai;

import static java.lang.Float.MAX_VALUE;

import java.util.AbstractList;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockList;

public class PathFinder
{
	BlockList blockList;
	final int numRoboBlocks;
	final int numTargetBlocks;
	final int maxX;
	final int maxZ;
	ArrayList<DistanceBlock> initialList = new ArrayList<DistanceBlock>();
	ArrayList<DistanceBlock> list = new ArrayList<DistanceBlock>();
	AbstractList<Block> obstacles;
	ArrayList<DistanceBlock> disObstacles;
	ArrayList<Vector3> vectorList = new ArrayList<Vector3>();

	public PathFinder(BlockList blockList, int numRoboBlocks, int numTargetBlocks){
		this.blockList = blockList;
		this.numRoboBlocks = numRoboBlocks;
		this.numTargetBlocks = numTargetBlocks;
		obstacles = blockList.getBlockList(Block.Type.Obstacle);
		maxX = blockList.getGridSize();
		maxZ = maxX;
		
		// create a List with the obstacles
		disObstacles = new ArrayList<DistanceBlock>();
		for (int i = 0; i < obstacles.size(); i++)
		{
			float x = obstacles.get(i).getPosition().x;
			float z = obstacles.get(i).getPosition().z;
			float y = obstacles.get(i).getPosition().y;
			boolean add = true;
			
			for (int j = 0; j < disObstacles.size(); j++)
			{
				if ((x == disObstacles.get(j).getData().x) && (z == disObstacles.get(j).getData().z))
				{
					if (disObstacles.get(j).getHigh() < y)
					{
						System.out.println("arrives");
						add = false;
						j = disObstacles.size();
					}
				}
			}
			
			if(add)
			{
				disObstacles.add(new DistanceBlock(MAX_VALUE, obstacles.get(i).getPosition(), (int) y));
			}
		} 
	}
	
	public void startPathFinder(Block initialP, Block target)
	{
		DistanceBlock initialPosition = new DistanceBlock(0, initialP.getPosition(), 0);
		int initalX = (int) initialPosition.getData().x;
		int initalZ = (int) initialPosition.getData().z;
		
		if (!vectorList.isEmpty()) vectorList.clear();
		
		// create a list with all the possible positions
		for (int i = 0; i < maxX; i++)
		{
			for (int j = 0; j < maxZ; j++)
			{
				if ((i == initalX) && (j == initalZ))
				{
					initialList.add(initialPosition);
				}
				else
				{
					boolean added = false;
					int mY = 0;
					for (int m = 0; m < disObstacles.size(); m++)
					{
						if ((disObstacles.get(m).getData().x == i) && (disObstacles.get(m).getData().z == j) && (disObstacles.get(m).getData().y > mY))
						{
							initialList.add(disObstacles.get(m));
							added = true;
							m = disObstacles.size();
						}
					}
					if (!(added))
					{
						initialList.add(new DistanceBlock(MAX_VALUE, new Vector3(i, 1, j), 0));
					}
				}
			}
		}

		// set the neighbours
		// set corners
		initialList.get(0).setNeighboursAndWeights(new DistanceBlock[]
		{ initialList.get(1), initialList.get(maxX) }); //
		initialList.get(maxX - 1).setNeighboursAndWeights(new DistanceBlock[]
		{ initialList.get(maxX - 2), initialList.get((maxX * 2) - 1) }); //
		initialList.get(maxX * (maxZ - 1)).setNeighboursAndWeights(new DistanceBlock[]
		{ initialList.get(maxX * (maxZ - 2)), initialList.get(maxX * (maxZ - 1) + 1) }); //
		initialList.get(maxX * maxZ - 1).setNeighboursAndWeights(new DistanceBlock[]
		{ initialList.get(maxX * (maxZ - 1) - 1), initialList.get(maxX * maxZ - 2) }); //

		// set sides
		for (int i = 1; i < maxZ - 1; i++)
		{
			initialList.get(i).setNeighboursAndWeights(new DistanceBlock[]
			{ initialList.get(i - 1), initialList.get(i + 1), initialList.get(i + maxZ) }); //
			initialList.get(i + (maxX * (maxZ - 1))).setNeighboursAndWeights(new DistanceBlock[]
			{ initialList.get(i + (maxX * (maxZ - 1)) - 1), initialList.get(i + (maxX * (maxZ - 1)) + 1), initialList.get((i + (maxX * (maxZ - 1))) - maxZ) }); //
		}
		for (int i = 1; i < maxX - 1; i++)
		{
			initialList.get(i * maxX).setNeighboursAndWeights(new DistanceBlock[]
			{ initialList.get(i * maxX + maxX), initialList.get(i * maxX - maxX), initialList.get(i * maxX + 1) }); //
			initialList.get(((i + 1) * maxZ) - 1).setNeighboursAndWeights(new DistanceBlock[]
			{ initialList.get((i * maxZ) - 1), initialList.get(((i + 2) * maxZ) - 1), initialList.get(((i + 1) * maxZ) - 2) });//
		}

		// set middle
		for (int i = 1; i < maxX - 1; i++)
		{
			for (int j = 1; j < maxZ - 1; j++)
			{
				if (initialList.get((maxZ * i) + j).getNeighbours() == null)
				{
					initialList.get((maxZ * i) + j).setNeighboursAndWeights(new DistanceBlock[]
					{ initialList.get((maxX * i) + j - maxX), initialList.get((maxX * i) + j - 1), initialList.get((maxX * i) + j + maxX), initialList.get((maxX * i) + j + 1) }); //
				}
			}
		}

		// implement Dijkstra's algorithm
		DistanceBlock tar = new DistanceBlock(MAX_VALUE, target.getPosition(), 0);
		Dijkstra dijkstra = new Dijkstra(initialList, tar,numRoboBlocks,numTargetBlocks);
		list = dijkstra.getFinalList();
		setFinalList(list.get(list.size() - 1));
	}

	public void setFinalList(DistanceBlock current)
	{
		vectorList.add(current.getData());
		if (current.getPrevious() != null)
		{
			setFinalList(current.getPrevious());
		}
	}

	public ArrayList<Vector3> getFinalList()
	{
		return vectorList;
	}
}
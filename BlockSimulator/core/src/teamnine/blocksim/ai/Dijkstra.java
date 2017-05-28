package teamnine.blocksim.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import teamnine.blocksim.block.BlockComparator;

public class Dijkstra
{
	PriorityQueue originalList;
	ArrayList finalList = new ArrayList();
	Comparator blockComparator = new BlockComparator();
	DistanceBlock target;
	int numTargetBlocks;
	int numRoboBlocks;

	public Dijkstra(ArrayList<DistanceBlock> list, DistanceBlock target, int numRoboBlocks,int numTargetBlocks)
	{
		this.numRoboBlocks=numRoboBlocks;
		this.numTargetBlocks=numTargetBlocks;
		this.target = target;
		originalList = new PriorityQueue<DistanceBlock>(list.size(), blockComparator);

		for (int i = 0; i < list.size(); i++)
		{
			originalList.add(list.get(i));
		}

		completeFinalList(originalList);
	}

	public void completeFinalList(PriorityQueue<DistanceBlock> listToReduce)
    {
        DistanceBlock position = listToReduce.poll();
        finalList.add(position);
        DistanceBlock[] neighbours = position.getNeighbours();
        int[] weights = position.getWeights();
        if((position.getX() == target.getX()) && (position.getZ() == target.getZ()))
        {
        	return;
        }
        else if (neighbours != null)
        {
            for (int i = 0; i < neighbours.length; i++)
            {
            	DistanceBlock neig = neighbours[i];
            	DistanceBlock neighbour = null;
            	boolean containsNeig = false;
            	
            	ArrayList<DistanceBlock> array = new ArrayList<DistanceBlock>();
        		for (int m = 0; m < listToReduce.size(); m++)
        		{
        			DistanceBlock object = listToReduce.poll();
        			if((neig.getX() == object.getX()) && (neig.getZ() == object.getZ()))
        			{
        				for(int j= 0; j < array.size(); j++)
        				{
        					listToReduce.add(array.get(j));
        				}
        				neighbour = object;
        				containsNeig = true;
        				m = listToReduce.size();
        			}
        			else
        			{
        				array.add(object);
        			}
        		}
        		if (!(containsNeig))
        		{
        			
        			for(int j= 0; j < array.size(); j++)
    				{
    					listToReduce.add(array.get(j));
    				}
        		}	
        		else
        		{
        			
	            	int neededRobo = 1;
	            	for (int j = weights[i]; j>0; j--)
	            	{
	            		neededRobo = neededRobo + j;
	            	}
		            if((numTargetBlocks <= (numRoboBlocks- neededRobo)))
		            {
		            	numRoboBlocks = numRoboBlocks- neededRobo;
			            if (neighbour.getDistance() > (position.getDistance() + weights[i]))
			            {
			            	
			                 neighbour.setPrevious(position);
			                 neighbour.setDistance(position.getDistance() + weights[i]);
			            }
		            }
		            listToReduce.add(neighbour);
        		}
            }
            
            if (listToReduce.size() != 0)
            {
            	completeFinalList(listToReduce);
            }
        }
    }

	public ArrayList<DistanceBlock> getFinalList()
	{
		return finalList;
	}
}
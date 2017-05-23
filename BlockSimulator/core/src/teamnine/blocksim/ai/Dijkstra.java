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
		System.out.println(target.getData() + "dijk");
	}

	public void completeFinalList(PriorityQueue<DistanceBlock> listToReduce)
    {
        DistanceBlock position = listToReduce.poll();
        finalList.add(position);
        DistanceBlock[] neighbours = position.getNeighbours();
        int[] weights = position.getWeights();
        
        if(position.getData().equals(target.getData()))
        {
        	return;
        }
        else if (neighbours != null)
        {
            for (int i = 0; i < neighbours.length; i++)
            {
            	int neededRobo = 2;
            	for (int j = weights[i]; j>0; j--)
            	{
            		neededRobo = neededRobo + j -1;
            	}
            	System.out.println(neededRobo +  " needed");
	            if((numTargetBlocks < (numRoboBlocks- neededRobo)))
	            {
	            	numRoboBlocks = numRoboBlocks- neededRobo;
	            	DistanceBlock neighbour = neighbours[i];
	                if (listToReduce.contains(neighbour))
	                {
		                if (neighbour.getDistance() >= (position.getDistance() + weights[i]))
		                {
		                  	listToReduce.remove(neighbour);
		                   	neighbour.setPrevious(position);
		                   	neighbour.setDistance(position.getDistance() + weights[i]);
		                   	listToReduce.add(neighbour);
		                }
		            } 
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
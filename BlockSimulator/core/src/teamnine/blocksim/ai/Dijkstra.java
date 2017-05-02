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

    public Dijkstra(ArrayList<DistanceBlock> list, DistanceBlock target)
    {
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
        
        if(position.getData() == target.getData())
        {
        	System.out.println("called");
        	return;
        }
        else if (neighbours != null)
        {
            for (int i = 0; i < neighbours.length; i++)
            {
            	DistanceBlock neighbour = neighbours[i];
                if (listToReduce.contains(neighbour))
                {
	                if (neighbour.getDistance() > (position.getDistance() + weights[i]))
	                {
	                  	listToReduce.remove(neighbour);
	                   	neighbour.setPrevious(position);
	                   	neighbour.setDistance(position.getDistance() + weights[i]);
	                   	listToReduce.add(neighbour);
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
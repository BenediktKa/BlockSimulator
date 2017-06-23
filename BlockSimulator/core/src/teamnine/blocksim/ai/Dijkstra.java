package teamnine.blocksim.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import teamnine.blocksim.block.BlockComparator;

public class Dijkstra
{
	PriorityQueue<DistanceBlock> originalList;
	ArrayList<DistanceBlock> finalList = new ArrayList<DistanceBlock>();
	Comparator<DistanceBlock> blockComparator = new BlockComparator();
	DistanceBlock target;
	int numTargetBlocks;
	int numRoboBlocks;

	public Dijkstra(ArrayList<DistanceBlock> list, DistanceBlock target, int numRoboBlocks,int numTargetBlocks)
	{
		this.numRoboBlocks=numRoboBlocks;
		this.numTargetBlocks=numTargetBlocks;
		this.target = target;
		originalList = new PriorityQueue<DistanceBlock>(list.size(), blockComparator);

		//add all elements from ArrayList to the Priority Queue that will be used
		for (int i = 0; i < list.size(); i++)
		{
			originalList.add(list.get(i));
		}
		
		completeFinalList(originalList);
	}

	//The originalList with all the position is reduced, and the positions "visited" are added in the finalList.
	public void completeFinalList(PriorityQueue<DistanceBlock> listToReduce)
    {
        DistanceBlock position = listToReduce.poll();
        if (((((position.getHigh() * (position.getHigh()+1))/2)) < numTargetBlocks) &&
         	((((position.getHigh() * (position.getHigh()+1))/2)) < numRoboBlocks))
        {
	        finalList.add(position);
	        DistanceBlock[] neighbours = position.getNeighbours();
	        int[] weights = position.getWeights();
	        
	        //checks if the position is the target
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
		            	int size = listToReduce.size();
		        		for (int m = 0; m < size; m++)
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
		        			if (neighbour.getDistance() > (position.getDistance() + weights[i]))
			            {
			                 neighbour.setPrevious(position);
			                 neighbour.setDistance(position.getDistance() + weights[i]);
			            }
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

	public ArrayList<DistanceBlock> getFinalList()
	{
		return finalList;
	}
}
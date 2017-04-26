package teamnine.blocksim;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra
{
    PriorityQueue<DistanceBlock> originalList;
    ArrayList<DistanceBlock> finalList = new ArrayList<DistanceBlock>();
    Comparator<DistanceBlock> blockComparator = new BlockComparator();
    private DistanceBlock target;

    public Dijkstra(ArrayList<DistanceBlock> list, DistanceBlock target)
    {
    	if (target == null)
    	{
    		System.out.println("fuck");
    	}
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
        if (position != target)
        {
	        DistanceBlock[] neighbours = position.getNeighbours();
	        int[] weights = position.getWeights();
	
	        if (position != null)
	        {
	            finalList.add(position);
	
	            if (neighbours != null)
	            {
	                for (int i = 0; i < neighbours.length; i++)
	                {
	                    DistanceBlock neighbour = neighbours[i];
	                    if (listToReduce.contains(neighbour))
	                    {
	                    	listToReduce.remove(neighbour);
	                    	if ((position.getDistance() + weights[i]) < neighbour.getDistance())
	                    	{
	                    		neighbour.setDistance(neighbour.getDistance() + weights[i]);
	                    		neighbour.setPrevious(position);
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
    }

    public ArrayList<DistanceBlock> getFinalList()
    {
        return finalList;
    }
}
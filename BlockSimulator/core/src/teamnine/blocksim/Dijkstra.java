package teamnine.blocksim;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra
{
    PriorityQueue originalList;
    ArrayList finalList = new ArrayList();
    Comparator blockComparator = new BlockComparator();

    public Dijkstra(ArrayList<DistanceBlock> list)
    {
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
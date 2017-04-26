package teamnine.blocksim;
import java.util.AbstractList;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import static java.lang.Float.MAX_VALUE;

public class PathFinder
{
    ArrayList<DistanceBlock> initialList;
    ArrayList<DistanceBlock> list;
    ArrayList<Vector3> finalList = new ArrayList<Vector3>();

    public PathFinder(AbstractList<Block> obstacles, Block initialPosition, Block target, int maxX, int maxZ)
    {
        //create a List with the obstacles and the initial position
        ArrayList<DistanceBlock> disObstacles = new ArrayList<DistanceBlock>();
        initialList = new ArrayList<DistanceBlock>();
        for (int i = 0; i<obstacles.size(); i++)
        {
            float x = obstacles.get(i).getPosition().x;
            float z = obstacles.get(i).getPosition().z;
            disObstacles.add(new DistanceBlock(MAX_VALUE, obstacles.get(i).getPosition(), (int)obstacles.get(i).getPosition().y));
            for (int j = 0; j<disObstacles.size(); j++)
            {
                if ((x == disObstacles.get(j).getData().x) && (z == disObstacles.get(j).getData().z) && (disObstacles.get(j).getHigh()>obstacles.get(i).getPosition().y))
                {
                    disObstacles.remove(disObstacles.size()-1);
                }
            }
        }
        disObstacles.add(new DistanceBlock(0, initialPosition.getPosition(), 0));
        DistanceBlock targetDB= new DistanceBlock(MAX_VALUE, target.getPosition(), 0);
        disObstacles.add(targetDB);
        float one=1;
        //create a list with all the possible positions
        for(float i = 0; i<maxZ; i++)
        {
            for(float j=0; j<maxX; j++)
            {
                boolean added = false;
                for(int m= 0; m<disObstacles.size(); m++)
                {
                    if((disObstacles.get(m).getData().x == i) && (disObstacles.get(m).getData().z == j))
                    {
                        initialList.add(disObstacles.get(m));
                        added = true;
                    }
                }
                if(!(added))
                {
                    initialList.add(new DistanceBlock(MAX_VALUE, new Vector3(one, one, one), 0));
                }
            }
        }

      //set the neighbours
        //set corners
        initialList.get(0).setNeighboursAndWeights(new DistanceBlock[]{initialList.get(2), initialList.get(maxX)}); //
        initialList.get(maxX -1).setNeighboursAndWeights(new DistanceBlock[]{initialList.get(maxX-1 -1), initialList.get((maxX*2) -1)}); //
        initialList.get(maxX*(maxZ-1)).setNeighboursAndWeights(new DistanceBlock[]{initialList.get(maxX*(maxZ-2)), initialList.get(maxX*(maxZ-1) +1)}); //
        initialList.get(maxX*maxZ -1).setNeighboursAndWeights(new DistanceBlock[]{initialList.get(maxX*(maxZ-1) -1), initialList.get((maxX*maxZ -1) -1)}); //

        //set sides
        for(int i=1; i<maxX-2; i++)
        {
            initialList.get(i).setNeighboursAndWeights(new DistanceBlock[]{initialList.get(i-1),initialList.get(i+1),initialList.get(i + maxX)});
            initialList.get(i + (maxX*(maxZ-1))).setNeighboursAndWeights(new DistanceBlock[]{initialList.get(i + (maxX*(maxZ-1)) -1),initialList.get(i + (maxX*(maxZ-1)) +1),
                    initialList.get((i + (maxX*(maxZ-1) +1))-maxX)});
        }
        for(int i=1; i<maxZ-1; i++)
        {
            initialList.get(i*maxX).setNeighboursAndWeights(new DistanceBlock[]{initialList.get(i*maxX + maxX),initialList.get(i*maxX - maxX), initialList.get(i*maxX +1)});
            initialList.get(((i+1) * maxX)-1).setNeighboursAndWeights(new DistanceBlock[]{initialList.get((((i+1) * maxX)-1) + maxX),initialList.get((((i+1) * maxX)-1) - maxX), initialList.get(((i+1) * maxX)-1 -1)}); //
        }

        //set middle
        for(int i=1; i<maxX-1; i++)
        {
            for(int j=1; i<maxZ-1; i++)
            {
                initialList.get((maxX*i) + 1 + j).setNeighboursAndWeights(new DistanceBlock[]{initialList.get((maxX*i) + 1 + j - maxX),initialList.get((maxX*i) + 1 + j -1),initialList.get((maxX*i) + 1 + j + maxX), initialList.get((maxX*i) + 1 + j +1)});
            }
        }

        //implement DTR/ijkstra's algorithm
        Dijkstra dijkstra = new Dijkstra(initialList, targetDB);
        list = dijkstra.getFinalList();
        
        	System.out.println(list.size());
        
        finalList.add(list.get(list.size()-1).getData());
        setFinalList(list.get(list.size()-1).getPrevious());
    }

    public void setFinalList(DistanceBlock current)
    {
        finalList.add(current.getData());
        if (current.getPrevious() != null)
        {
            setFinalList(current.getPrevious());
        }
    }

    public ArrayList<Vector3> getFinalList() {
        return finalList;
    }
}

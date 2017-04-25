package teamnine.blocksim;
public class DistanceBlock
{
    private float distance;
    private DistanceBlock previous;
    private DistanceBlock[] neighbours;
    private int[] weights;
    private Vector3 data;
    private int high;

    public DistanceBlock(float distance, Vector3 data, int high)
    {
        this.distance = distance;
        this.data = data;
        this.high = high;
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }

    public void setNeighboursAndWeights(DistanceBlock[] neighbours)
    {
        this.neighbours = neighbours;
        for (int i = 0; i<neighbours.length; i++)
        {
            weights[i] = (neighbours[i].getHigh()) + 1;
        }
    }

    public int getHigh()
    {
        return high;
    }

    public void setHigh(int high)
    {
        this.high = high;
    }

    public void setPrevious(DistanceBlock previous)
    {
        this.previous = previous;
    }

    public float getDistance()
    {
        return distance;
    }

    public DistanceBlock getPrevious()
    {
        return previous;
    }

    public DistanceBlock[] getNeighbours()
    {
        return neighbours;
    }

    public int[] getWeights()
    {
        return weights;
    }

    public Vector3 getData()
    {
        return data;
    }
}

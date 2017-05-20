package teamnine.blocksim.block;

import java.util.Comparator;

import teamnine.blocksim.ai.DistanceBlock;

public class BlockComparator implements Comparator<DistanceBlock>
{
	public int compare(DistanceBlock first, DistanceBlock second)
	{
		float firstValue = first.getDistance();
		float secondValue = second.getDistance();

		if (firstValue < secondValue)
		{
			return -1;
		}
		else if (firstValue == secondValue)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
}

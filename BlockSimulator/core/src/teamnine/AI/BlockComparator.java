package teamnine.AI;

import java.util.Comparator;

import teamnine.blocksim.Block;

public class BlockComparator implements Comparator<Block> {

	@Override
	public int compare(Block arg0, Block arg1) {
		// TODO Check 1/-1/0
		if(arg0.getPosition().y < arg1.getPosition().y) return 1;
		if(arg0.getPosition().y > arg1.getPosition().y) return -1;
		return 0;
	}
}

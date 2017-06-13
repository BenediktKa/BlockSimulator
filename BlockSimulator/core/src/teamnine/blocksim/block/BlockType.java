package teamnine.blocksim.block;

public enum BlockType
{
	Robot, Obstacle, Goal, Selector, Floor, Path, RobotMoving;

	public BlockType next()
	{
		BlockType types[] = BlockType.values();
		int ordinal = this.ordinal();
		ordinal = ++ordinal % types.length;
		if (types[ordinal] == Selector)
			return types[0];

		return types[ordinal];
	}
}

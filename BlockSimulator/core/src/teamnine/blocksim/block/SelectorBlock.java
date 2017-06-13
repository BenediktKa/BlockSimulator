package teamnine.blocksim.block;

import com.badlogic.gdx.math.Vector3;

public class SelectorBlock extends Block
{
	private static SelectorBlock selectorBlock;
	private BlockType selectedBlock = BlockType.Obstacle;
	
	private SelectorBlock()
	{
		super(new Vector3(0, 0, 0), BlockType.Selector);
	}
	
	public static SelectorBlock getInstance()
	{
		if (selectorBlock == null)
			return (selectorBlock = new SelectorBlock());
		else
			return selectorBlock;
	}
	
	public BlockType getSelectedBlock()
	{
		return selectedBlock;
	}
	
	public void setSelectedBlock(BlockType selectedBlock)
	{
		this.selectedBlock = selectedBlock;
	}
	
	public void setNextBlock()
	{
		selectedBlock = selectedBlock.next();
	}
}

package teamnine.blocksim.block;

import com.badlogic.gdx.math.Vector3;

public class SelectorBlock extends Block
{
	private static SelectorBlock selectorBlock;
	private Block.Type selectedBlock = Block.Type.Obstacle;
	
	private SelectorBlock()
	{
		super(new Vector3(0, 0, 0), Block.Type.Selector);
	}
	
	public static SelectorBlock getInstance()
	{
		if (selectorBlock == null)
			return (selectorBlock = new SelectorBlock());
		else
			return selectorBlock;
	}
	
	public Block.Type getSelectedBlock()
	{
		return selectedBlock;
	}
	
	public void setSelectedBlock(Block.Type selectedBlock)
	{
		this.selectedBlock = selectedBlock;
	}
	
	public void setNextBlock()
	{
		selectedBlock = selectedBlock.next();
	}
}

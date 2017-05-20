package teamnine.blocksim;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;

/**
 * Class that saves information of removed/added block for the undo/redo stacks
 */
public class Action
{

	/** Block Position. */
	private Vector3 position;

	/** Block Type. */
	private Block.Type type;

	/** Block ID. */
	private double ID;

	/** Was Block removed? */
	private boolean removed;

	/**
	 * Instantiates a new action.
	 *
	 * @param position | The Block's Position
	 * @param type | The Block's Type
	 * @param ID | The Block's ID
	 * @param removed | Was Block removed?
	 */
	public Action(Vector3 position, Block.Type type, double ID, boolean removed)
	{
		this.position = position;
		this.type = type;
		this.ID = ID;
		this.removed = removed;
	}

	/**
	 * Gets the saved position.
	 *
	 * @return The saved position
	 */
	public Vector3 getPosition()
	{
		return position;
	}

	/**
	 * Gets the saved block type.
	 *
	 * @return The saved block type
	 */
	public Block.Type getBlockType()
	{
		return type;
	}

	/**
	 * Gets the saved block id.
	 *
	 * @return The saved block id
	 */
	public double getID()
	{
		return ID;
	}

	/**
	 * Was Block removed?
	 *
	 * @return true, if Block was removed
	 */
	public boolean wasRemoved()
	{
		return removed;
	}
}
package teamnine.blocksim.block;

import java.util.Stack;

public class UndoRedoStack<E> extends Stack<E>
{
	private Stack undoStack;
	private Stack redoStack;

	public UndoRedoStack()
	{
		undoStack = new Stack();
		redoStack = new Stack();
	}

	public E push(E value)
	{
		super.push(value);
		undoStack.push("push");
		redoStack.clear();
		return value;
	}

	public E pop()
	{
		E value = super.pop();
		undoStack.push(value);
		undoStack.push("pop");
		redoStack.clear();
		return value;
	}

	public boolean canUndo()
	{
		return !undoStack.isEmpty();
	}

	public void undo()
	{
		if (!canUndo())
		{
			throw new IllegalStateException();
		}
		Object action = undoStack.pop();
		if (action.equals("push"))
		{
			E value = super.pop();
			redoStack.push(value);
			redoStack.push("push");
		}
		else
		{
			E value = (E) undoStack.pop();
			super.push(value);
			redoStack.push("pop");
		}
	}

	public boolean canRedo()
	{
		return !redoStack.isEmpty();
	}

	public void redo()
	{
		if (!canRedo())
		{
			throw new IllegalStateException();
		}
		Object action = redoStack.pop();
		if (action.equals("push"))
		{
			E value = (E) redoStack.pop();
			super.push(value);
			undoStack.push("push");
		}
		else
		{
			E value = super.pop();
			undoStack.push(value);
			undoStack.push("pop");
		}
	}
}
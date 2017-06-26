
package teamnine.blocksim.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.RobotBlock;

public class MoveSnake
{
	private ArrayList<Vector3> path;
	private  ArrayList<Integer> occupied = new ArrayList<Integer>();
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> obstacles;
	private ArrayList<Block> floor;
	private int timestep = 0;
	private boolean targetFound=false;
	private int next=0;

	public MoveSnake(ArrayList<RobotBlock> robots, ArrayList<Block> obstacles, ArrayList<Block> floor)	
	{
		this.obstacles = obstacles;
		this.robots = new ArrayList<RobotBlock>(robots); 
		this.floor = floor;
		/*
		 * for (int i = path.size() - 1; i > 0; i--)
		 * decideMove(this.path.get(i));
		 */	
	}
	
	
	public void startMove3 (final ArrayList<Vector3> path)
	{
		this.path = path;
		for(int i=0;i<path.size();i++)
		{
			occupied.add(0);
		}
		System.out.println("poep " + path.size());
		for (int i = 0; i < path.size(); i++)
			System.out.println(this.path.get(i));
		while(!targetFound)
		{			
			System.out.println("while loop + next "+next+" "+path.get(next));
			decideMove(path.get(next));			
		}
			

		//decideMove(new Vector3(mt.getPosition().x, mt.getPosition().y, mt.getPosition().z));
		System.out.println("Timestep: " + timestep);
		Thread.currentThread().interrupt();
		
		if(targetFound)
		{

			final SmartMovement smartMovement = new SmartMovement(timestep); //name is not to offend anyone, it isn't smart at all
			
			boolean testingReconfiguration = true;
			if(testingReconfiguration)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						new Reconfiguration(path.get(path.size()-1),smartMovement);			
					}
				}).start();
			}
		}		
	}

	public void decideMove(Vector3 v)
	{

		ArrayList<RobotBlock> orderToMove = new ArrayList<RobotBlock>();
		
		 float targetX=v.x; float targetZ=v.z; //finds distance from the block
		// to the target assigns the value to the block
		 
		 for(int i=0;i<robots.size();i++)
		 {
			 float distanceToPath=Math.abs(robots.get(i).getPosition().x-targetX)+Math.abs(robots.get(i).getPosition().z-targetZ)+robots.get(i).getPosition().y;
			 robots.get(i).setDistanceToPath(distanceToPath);
			 orderToMove.add(robots.get(i)); 
		 }
		 
/*
		for (int i = 0; i < robots.size(); i++)
		{
			orderToMove.add(robots.get(i));
		}
		RobotBlock end = new RobotBlock(v, null);
		robots.add(end);
		for (int i = 0; i < robots.size(); i++)
		{
			for (int j = 0; j < robots.size(); j++)
			{
				if (i != j)
				{
					if (robots.get(i).getPosition().x == robots.get(j).getPosition().x && robots.get(i).getPosition().z == robots.get(j).getPosition().z && robots.get(i).getPosition().y - 1 == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x == robots.get(j).getPosition().x && robots.get(i).getPosition().z == robots.get(j).getPosition().z && robots.get(i).getPosition().y + 1 == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x + 1 == robots.get(j).getPosition().x && robots.get(i).getPosition().z == robots.get(j).getPosition().z && robots.get(i).getPosition().y == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x - 1 == robots.get(j).getPosition().x && robots.get(i).getPosition().z == robots.get(j).getPosition().z && robots.get(i).getPosition().y == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x == robots.get(j).getPosition().x && robots.get(i).getPosition().z + 1 == robots.get(j).getPosition().z && robots.get(i).getPosition().y == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
					else if (robots.get(i).getPosition().x == robots.get(j).getPosition().x && robots.get(i).getPosition().z - 1 == robots.get(j).getPosition().z && robots.get(i).getPosition().y == robots.get(j).getPosition().y)
					{
						robots.get(i).addConnection(robots.get(j));
						robots.get(j).addConnection(robots.get(i));
					}
				}
			}
		}

		for (int i = 0; i < robots.size(); i++)
		{//
			setDistances(robots.get(i), end);
			for (int j = 0; j < robots.size(); j++)
			{
				robots.get(j).setVisited(false);
				robots.get(j).setCounter(0);
			}
		}

		for (int i = 0; i < robots.size(); i++)
		{
			robots.get(i).setVisited(false);
			robots.get(i).setCounter(0);
		}
		robots.remove(robots.size() - 1);
		//////////////////////////*/

		ArrayList<RobotBlock> newOrderToMove = order(orderToMove);
		int lastNext = next;
		int move =-1;
		while(lastNext==next)
		{
			if(move<newOrderToMove.size()-1)
			{
				move++;
				moving(newOrderToMove.get(move), v);
			}
			else
			{
				if(next<newOrderToMove.size()-1)
					next++;
				
			}
			
		}
			
		
	}

	public void setDistances(RobotBlock b, RobotBlock end)
	{
		if (b.getPosition().x == end.getPosition().x && b.getPosition().y == end.getPosition().y)
		{
			b.setDistanceToPath(0);
		}
		else
		{
			Queue<RobotBlock> q = new LinkedList<RobotBlock>();
			q.add(b);
			while (q.peek() != null)
			{
				RobotBlock w = q.poll();

				if (w.getVisited() == false)
				{
					w.setVisited(true);
					ArrayList<RobotBlock> connections = w.getConnections();

					for (int i = 0; i < connections.size(); i++)
					{
						if (connections.get(i).getVisited() == false)
						{

							q.add(connections.get(i));
							if (connections.get(i).getCounter() > (w.getCounter() + 1) || connections.get(i).getCounter() == 0)
								connections.get(i).setCounter(w.getCounter() + 1);
						}
					}
				}
			}
			if (end.getCounter() != 0 && end.getCounter() > (int) ((Math.abs(b.getPosition().x - end.getPosition().x) + Math.abs(b.getPosition().z - end.getPosition().z) + b.getPosition().y) - 1))
				b.setDistanceToPath(end.getCounter());
			else
				b.setDistanceToPath((int) ((Math.abs(b.getPosition().x - end.getPosition().x) + Math.abs(b.getPosition().z - end.getPosition().z) + b.getPosition().y) - 1));
		}
	}

	public ArrayList<RobotBlock> order(ArrayList<RobotBlock> otm)
	{
		ArrayList<RobotBlock> sorted = new ArrayList<RobotBlock>();
		float maxDistance = 0;
		for (int i = 0; i < otm.size(); i++)
		{
			if (otm.get(i).getDistanceToPath() > maxDistance)
			{
				maxDistance = otm.get(i).getDistanceToPath();
			}
		}
		ArrayList<Bucket> buckets = new ArrayList<Bucket>();
		buckets.add(new Bucket());
		for (int i = 0; i < maxDistance; i++)
		{
			buckets.add(new Bucket());
		}

		for (int i = 0; i < otm.size(); i++)
		{
			int d = (int) otm.get(i).getDistanceToPath();
			buckets.get(d).addBlock(otm.get(i));
		}

		for (int i = buckets.size() - 1; i > 0; i--)
		{
			sorted.addAll(buckets.get(i).getBloks());
		}

		return sorted;
	}

	public void moving(RobotBlock b, Vector3 v)
	{
		boolean targetReached = false;
		while (!targetReached)
		{
			checkIfCanMove(b);

			ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
			for (int i = 0; i < robots.size(); i++)
			{
				if (robots.get(i).getPosition().x == b.getPosition().x + 1 && robots.get(i).getPosition().z == b.getPosition().z)
				{
					if (robots.get(i).getPosition().y <= b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x, robots.get(i).getPosition().y + 1, robots.get(i).getPosition().z));
					}
				}
				if (robots.get(i).getPosition().x == b.getPosition().x && robots.get(i).getPosition().z == b.getPosition().z + 1)
				{
					if (robots.get(i).getPosition().y <= b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x, robots.get(i).getPosition().y + 1, robots.get(i).getPosition().z));
					}
				}
				if (robots.get(i).getPosition().x == b.getPosition().x - 1 && robots.get(i).getPosition().z == b.getPosition().z)
				{
					if (robots.get(i).getPosition().y <= b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x, robots.get(i).getPosition().y + 1, robots.get(i).getPosition().z));
					}
				}
				if (robots.get(i).getPosition().x == b.getPosition().x && robots.get(i).getPosition().z == b.getPosition().z - 1)
				{
					if (robots.get(i).getPosition().y <= b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x, robots.get(i).getPosition().y + 1, robots.get(i).getPosition().z));
					}
				}
			}
			for (int i = 0; i < floor.size(); i++)
			{
				if (floor.get(i).getPosition().x == b.getPosition().x + 1 && floor.get(i).getPosition().z == b.getPosition().z)
				{
					possibleMovements.add(new Vector3(floor.get(i).getPosition().x, b.getPosition().y, floor.get(i).getPosition().z));
				}
				if (floor.get(i).getPosition().x == b.getPosition().x && floor.get(i).getPosition().z == b.getPosition().z + 1)
				{
					possibleMovements.add(new Vector3(floor.get(i).getPosition().x, b.getPosition().y, floor.get(i).getPosition().z));
				}
				if (floor.get(i).getPosition().x == b.getPosition().x - 1 && floor.get(i).getPosition().z == b.getPosition().z)
				{
					possibleMovements.add(new Vector3(floor.get(i).getPosition().x, b.getPosition().y, floor.get(i).getPosition().z));
				}
				if (floor.get(i).getPosition().x == b.getPosition().x && floor.get(i).getPosition().z == b.getPosition().z - 1)
				{
					possibleMovements.add(new Vector3(floor.get(i).getPosition().x, b.getPosition().y, floor.get(i).getPosition().z));
				}
			}
			removeRobots(possibleMovements);
			removeObstacles(possibleMovements);
			removeImpossibleMovements(possibleMovements, b);
			removeOrPos(possibleMovements, b, v);

			if (b.getPosition().x == v.x && b.getPosition().z == v.z)
			{
				if(b.getPosition().x == path.get(path.size()-1).x && b.getPosition().z == path.get(path.size()-1).z)
				{
					System.out.println("targetReached");
					targetFound=true;
				}
				next++;
				break;
			}
			if (possibleMovements.size() == 0)
			{
				break;
			}
			else
			{
				Vector3 bestMovement = null;
				int bestDistance = -1;
				if (possibleMovements.size() != 0)
				{
					if (possibleMovements.size() == 1)
					{
						bestMovement = possibleMovements.get(0);
					}
					for (int i = 0; i < possibleMovements.size(); i++)
					{
						if (bestDistance == -1)
						{
							bestMovement = possibleMovements.get(i);
							bestDistance = (int) (Math.abs(possibleMovements.get(i).x - v.x) + Math.abs(possibleMovements.get(i).z - v.z));
						}
						else
						{
							if (bestDistance > ((int) (Math.abs(possibleMovements.get(i).x - v.x) + Math.abs(possibleMovements.get(i).z - v.z))))
							{
								bestMovement = possibleMovements.get(i);
								bestDistance = (int) (Math.abs(possibleMovements.get(i).x - v.x) + Math.abs(possibleMovements.get(i).z - v.z));
							}
						}
					}
				}
				if (bestMovement.y > b.getPosition().y)
				{

					boolean possible = true;
					for (int i = 0; i < robots.size(); i++)
						if (b.getPosition().x == robots.get(i).getPosition().x && b.getPosition().z == robots.get(i).getPosition().z && b.getPosition().y + 1 == robots.get(i).getPosition().y)
						{
							possible = false;
						}
					if (possible)
					{
						System.out.println("climb");
						timestep++;
						b.climb();
						checkIfCanMove(b);
					}
					else
					{
						break;
					}

				}
				if (bestMovement.x < b.getPosition().x)
				{
					System.out.println("move left");
					timestep++;
					b.moveLeft();
				}
				else if (bestMovement.x > b.getPosition().x)
				{
					System.out.println("move right");
					timestep++;
					b.moveRight();
				}
				else if (bestMovement.z < b.getPosition().z)
				{
					System.out.println("move back");
					timestep++;
					b.moveBackwards();
				}
				else
				{
					System.out.println("move forward");
					timestep++;
					b.moveForward();
				}

			}
		}
		checkIfCanMove(b);
	}

	public void removeOrPos(ArrayList<Vector3> possibleMovements, RobotBlock b, Vector3 v)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for (int i = 0; i < possibleMovements.size(); i++)
		{
			if (possibleMovements.get(i).equals(b.getOriginalPos()))
			{
				toRemove.add(possibleMovements.get(i));
			}
			else if (possibleMovements.size() > 1 && Math.abs(b.getPosition().x - v.x) + Math.abs(b.getPosition().z - v.z) < Math.abs(possibleMovements.get(i).x - v.x) + Math.abs(possibleMovements.get(i).z - v.z))
			{
				toRemove.add(possibleMovements.get(i));
			}
		}
		possibleMovements.removeAll(toRemove);
	}

	public void removeRobots(ArrayList<Vector3> pm)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for (int i = 0; i < pm.size(); i++)
		{
			for (int j = 0; j < robots.size(); j++)
			{
				if (pm.get(i).equals(robots.get(j).getPosition()))
				{
					toRemove.add(pm.get(i));
				}
			}
		}
		pm.removeAll(toRemove);
	}

	public void removeObstacles(ArrayList<Vector3> pm)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for (int i = 0; i < pm.size(); i++)
		{
			for (int j = 0; j < obstacles.size(); j++)
			{
				if (pm.get(i).equals(obstacles.get(j).getPosition()))
				{
					toRemove.add(pm.get(i));
				}
			}
		}
		pm.removeAll(toRemove);
	}

	public void removeImpossibleMovements(ArrayList<Vector3> pm, RobotBlock b)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		boolean locked = false;
		for (int k = 0; k < robots.size(); k++)
		{
			if (b.getPosition().x == robots.get(k).getPosition().x && b.getPosition().z == robots.get(k).getPosition().z && b.getPosition().y + 1 == robots.get(k).getPosition().y)
			{
				pm.clear();
				locked = true;
			}
		}
		if (!locked)
		{
			for (int i = 0; i < pm.size(); i++)
			{

				if (pm.get(i).x < b.getPosition().x)
				{
					boolean remove = true;
					for (int j = 0; j < robots.size(); j++)
					{
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
						{
							remove = false;
							break;
						}
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}

						}
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}

						}
						if ((robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z) && (robots.get(j).getPosition().y != b.getPosition().y))
						{
							remove = false;
							break;
						}
					}

					if (remove)
						toRemove.add(pm.get(i));
				}
				else if (pm.get(i).x > b.getPosition().x)
				{
					boolean remove = true;
					for (int j = 0; j < robots.size(); j++)
					{
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
						{
							remove = false;
							break;
						}
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}
						}
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}
						}
						if ((robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z) && (robots.get(j).getPosition().y != b.getPosition().y))
						{
							remove = false;
							break;
						}
					}

					if (remove)
						toRemove.add(pm.get(i));
				}
				else if (pm.get(i).z < b.getPosition().z)
				{
					boolean remove = true;
					for (int j = 0; j < robots.size(); j++)
					{
						if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							remove = false;
							break;
						}
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}
						}
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z - 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							for (int k = 0; k < robots.size(); k++)
							{
								if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
								{
									remove = false;
									break;
								}
							}
						}
						if ((robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z) && (robots.get(j).getPosition().y != b.getPosition().y))
						{
							remove = false;
							break;
						}
					}

					if (remove)
						toRemove.add(pm.get(i));
				}
				else if (pm.get(i).z > b.getPosition().z)
				{
					boolean remove = true;
					for (int j = 0; j < robots.size(); j++)
					{
						if (robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							remove = false;
							break;
						}
						if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							if (robots.get(j).getPosition().x == b.getPosition().x + 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
							{
								remove = false;
								break;
							}
						}
						if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z + 1 && robots.get(j).getPosition().y == b.getPosition().y)
						{
							if (robots.get(j).getPosition().x == b.getPosition().x - 1 && robots.get(j).getPosition().z == b.getPosition().z && robots.get(j).getPosition().y == b.getPosition().y)
							{
								remove = false;
								break;
							}
						}
						if ((robots.get(j).getPosition().x == b.getPosition().x && robots.get(j).getPosition().z == b.getPosition().z) && (robots.get(j).getPosition().y != b.getPosition().y))
						{
							remove = false;
							break;
						}
					}
					if (remove)
						toRemove.add(pm.get(i));
				}
			}
			pm.removeAll(toRemove);
		}
	}

	public int getTimestep()
	{
		return timestep;
	}

	public void checkIfCanMove(RobotBlock b)
	{
		if (StateManager.state == SimulationState.MENU || StateManager.state == SimulationState.BUILD)
		{
			try
			{
				Thread.currentThread().join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		while (b.getMoving() || StateManager.state == SimulationState.PAUSE)
		{
			try
			{
				Thread.sleep(250);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
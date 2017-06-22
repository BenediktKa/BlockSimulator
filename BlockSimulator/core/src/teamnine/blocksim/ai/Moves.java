//TO DO

//check for closest exit
//shit shit shit
//shit shit shit
//shit shit shit
package teamnine.blocksim.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.StateManager;
import teamnine.blocksim.StateManager.SimulationState;
import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.RobotBlock;
import teamnine.blocksim.block.blocklist.BlockListController;

public class Moves
{
	private ArrayList<Vector3> path;
	private ArrayList<Vector3> passages = new ArrayList<Vector3>();
	private Vector3 intermediatePath=null;
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> obstacles;
	private ArrayList<Block> floor;
	private int timestep = 0;
	private boolean found =false;
	private boolean intermediateFound =false;
	private int executedMoves=1;
	private	int amountOfTargets;
	private boolean firstReached=true;

	public Moves(ArrayList<RobotBlock> robots, ArrayList<Block> obstacles, ArrayList<Block> floor,int amountOfTargets)	
	{
		this.obstacles = obstacles;
		this.robots = new ArrayList<RobotBlock>(robots); 
		this.floor = floor;
		this.amountOfTargets=amountOfTargets;
		/*
		 * for (int i = path.size() - 1; i > 0; i--)
		 * decideMove(this.path.get(i));
		 */	
	}
	
	public void startMove3 (final ArrayList<Vector3> path)
	{
		this.path = path;
		System.out.println("poep " + path.size());
		for (int i = 0; i < path.size(); i++)
			System.out.println(path.get(i));
		while(!found)
		{
			if(executedMoves==0)
			{
				
				RobotBlock closest = null;
				for (int z = 0; z < robots.size(); z++)
				{
					
					if (closest == null)
					{
						closest = robots.get(z);
					}
					else
					{
						if (closest.getDistanceToPath() > robots.get(z).getDistanceToPath())
						{
							closest = robots.get(z);
						}
					}
				}
				intermediatePath=findOpening(closest.getPosition());
			
				for(int z=0;z<robots.size();z++)
				{
					robots.get(z).setOriginalPos(new Vector3(-1,-1,-1));
				}
				while(!intermediateFound)
				{
					decideMove(intermediatePath);
					if(executedMoves==0)
					{
						break;
					}
				}
				
				passages.add(new Vector3(intermediatePath.x,intermediatePath.y,intermediatePath.z));
				intermediatePath=null;
				intermediateFound=false;		
				firstReached=true;
				
			}
			else
			{
				executedMoves=0;
				decideMove(this.path.get(0));
			}
			
		}

		System.out.println("Timestep: " + timestep);
		Thread.currentThread().interrupt();
		
		if(found)
		{

			final SmartMovement smartMovement = new SmartMovement(); //name is not to offend anyone, it isn't smart at all
			
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

	public Vector3 findOpening(Vector3 start)
	{
		Vector3 lastVector = null;
		Vector3 currentVector = start;
		ArrayList<Vector3> passedWalls = new ArrayList<Vector3>();
		passedWalls.add(start);

		if (lastVector == null)
		{
			for (int i = 0; i < obstacles.size(); i++)
			{
				if (start.x + 1 == obstacles.get(i).getPosition().x && start.z == obstacles.get(i).getPosition().z)
				{
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					passedWalls.add(obstacles.get(i).getPosition().cpy());
					break;
				}
				else if (start.x - 1 == obstacles.get(i).getPosition().x && start.z == obstacles.get(i).getPosition().z)
				{
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					passedWalls.add(obstacles.get(i).getPosition().cpy());
					break;
				}
				else if (start.x == obstacles.get(i).getPosition().x && start.z + 1 == obstacles.get(i).getPosition().z)
				{
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					passedWalls.add(obstacles.get(i).getPosition().cpy());
					break;
				}
				else if (start.x == obstacles.get(i).getPosition().x && start.z - 1 == obstacles.get(i).getPosition().z)
				{
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					passedWalls.add(obstacles.get(i).getPosition().cpy());
					break;
				}
			}
		}
		
		System.out.println("current " + currentVector);
		boolean found2 = false;
		
		while (!found2)
		{
			found2 = true;
			for (int i = 0; i < obstacles.size(); i++)
			{
				//for loop checking all checked walls...
				boolean alreadyChecked=false;
				for(int p=0; p<passedWalls.size();p++)
				{
					if (passedWalls.get(p).x == obstacles.get(i).getPosition().x && passedWalls.get(p).z == obstacles.get(i).getPosition().z)
					{
						alreadyChecked=true;
						break;						
					}
				}
				if(alreadyChecked)
				{
					System.out.println("slready checked "+obstacles.get(i).getPosition());
					continue;
				}			
				if (currentVector.x + 1 == obstacles.get(i).getPosition().x && currentVector.z == obstacles.get(i).getPosition().z)
				{
					System.out.println("foun next1 " + obstacles.get(i).getPosition());
					found2 = false;
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					passedWalls.add(obstacles.get(i).getPosition().cpy());
					break;

				}
				else if (currentVector.x - 1 == obstacles.get(i).getPosition().x && currentVector.z == obstacles.get(i).getPosition().z)
				{
					System.out.println("foun next2 " + obstacles.get(i).getPosition());
					found2 = false;
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					passedWalls.add(obstacles.get(i).getPosition().cpy());
					break;

				}
				else if (currentVector.x == obstacles.get(i).getPosition().x && currentVector.z + 1 == obstacles.get(i).getPosition().z)
				{
					System.out.println("foun next3 " + obstacles.get(i).getPosition());
					found2 = false;
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					passedWalls.add(obstacles.get(i).getPosition().cpy());
					break;

				}
				else if (currentVector.x == obstacles.get(i).getPosition().x && currentVector.z - 1 == obstacles.get(i).getPosition().z)
				{
					System.out.println("foun next4 " + obstacles.get(i).getPosition());
					found2 = false;
					lastVector = currentVector.cpy();
					currentVector = obstacles.get(i).getPosition().cpy();
					passedWalls.add(obstacles.get(i).getPosition().cpy());
					break;

				}				
			}
			
			
			boolean check=false;
			int openingSize=1;
			int height=1;
			System.out.println("check for holes");
			while(!check)
			{
				check=true;
				
				for(int y=0;y<obstacles.size();y++)
				{
					if(currentVector.x==obstacles.get(y).getPosition().x&&height==obstacles.get(y).getPosition().y&&currentVector.z==obstacles.get(y).getPosition().z)
					{
						System.out.println("found one higher"+height+" os "+openingSize );
						check=false;
						height++;
						
						break;
					
					}
				}
			}
			for(int y=0;y<obstacles.size();y++)
			{
				if(currentVector.x==obstacles.get(y).getPosition().x&&height+1==obstacles.get(y).getPosition().y&&currentVector.z==obstacles.get(y).getPosition().z)
				{
					System.out.println("nope");
					openingSize=0;
					break;				
				}
			}
				
			System.out.println("found potential hole"+height+" os "+openingSize );
			openingSize++;
			if(openingSize>=2)
			{
				System.out.println("found hole "+height+" os "+openingSize );
				int totalNeeded=3;
				for(int q=height-1;q>0;q--)
				{
					totalNeeded=totalNeeded+q;
				}
				if(robots.size()>=totalNeeded+(amountOfTargets-2))
				{
					System.out.println("found hole, use it "+height+" os "+openingSize );
					found2=true;
					return currentVector;
				}
			}
				
					
			
			System.out.println("current2 " + currentVector);
		}
		if(passedWalls.size()>2)
		{
			if (lastVector.x < currentVector.x)
			{
				System.out.println("x<x");
				return new Vector3(currentVector.x + 1, 1, currentVector.z);
			}
			else if (lastVector.x > currentVector.x)
			{
				System.out.println("x>x");
				return new Vector3(currentVector.x - 1, 1, currentVector.z);
			}
			else if (lastVector.z < currentVector.z)
			{
				System.out.println("z<z");
				return new Vector3(currentVector.x, 1, currentVector.z + 1);
			}
			else
			{
				System.out.println("z>z");
				return new Vector3(currentVector.x, 1, currentVector.z - 1);
			}
		}
		else
		{
			if (lastVector.x < currentVector.x)
			{
				System.out.println("x<x");
				return new Vector3(currentVector.x , 1, currentVector.z-1);
			}
			else if (lastVector.x > currentVector.x)
			{
				System.out.println("x>x");
				return new Vector3(currentVector.x , 1, currentVector.z-1);
			}
			else if (lastVector.z < currentVector.z)
			{
				System.out.println("z<z");
				return new Vector3(currentVector.x-1, 1, currentVector.z );
			}
			else
			{
				System.out.println("z>z");
				return new Vector3(currentVector.x-1, 1, currentVector.z );
			}
		}
		
		
	}
	public void decideMove(Vector3 v)
	{
		System.out.println("decide "+v);
		ArrayList<RobotBlock> orderToMove = new ArrayList<RobotBlock>();
		for (int i = 0; i < robots.size(); i++)
		{
			orderToMove.add(robots.get(i));
		}
		float targetX=v.x; 
		float targetZ=v.z;
		for(int i=0;i<robots.size();i++) 
		{
			float distanceToPath=Math.abs(robots.get(i).getPosition().x-targetX)+Math.abs(robots.get(i).getPosition().z-targetZ)+robots.get(i).getPosition().y; robots.get(i).setDistanceToPath(distanceToPath);
	    }
		System.out.println("distances are set ");
		System.out.println("blocks are going to be ordered "+orderToMove.size() );
		ArrayList<RobotBlock> newOrderToMove = order(orderToMove);
		System.out.println("blocks are ordered "+newOrderToMove.size() );
		for (int i = 0; i < newOrderToMove.size(); i++)
		{
			moving(newOrderToMove.get(i), v);
		}
	}

	public void setDistances(RobotBlock b, RobotBlock end)
	{
		System.out.println("distances "+end.getPosition());
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
			System.out.println("end while ");
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
		System.out.println("start moving " );
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
			System.out.println("possible movements "+possibleMovements.size());
			removeRobots(possibleMovements);
			removeObstacles(possibleMovements);
			removeImpossibleMovements(possibleMovements, b);
			removeOrPos(possibleMovements, b, v);
			removeBadClimbs(possibleMovements);
			removePassages(possibleMovements);
			System.out.println("possible movements after check "+possibleMovements.size());
			if (b.getPosition().x == path.get(0).x && b.getPosition().z == path.get(0).z)
			{
				found=true;
				break;
			}
			if(intermediatePath!=null)
			{
				if (b.getPosition().x == intermediatePath.x && b.getPosition().z == intermediatePath.z)
				{
					System.out.println("reached intermediate Target");
					if(firstReached)
					{
						System.out.println("reached intermediate Target, now change it");
						firstReached=false;
						
						boolean bigger = true;
						int attempt = 1;
						int result = 0;
						int currentBest = 0;
						while (bigger)
						{
							System.out.println("statrt");
							int total = 0;
							for (int j = attempt; j > 0; j--) {
								total = total + j;
								System.out.println("total "+total+" att "+attempt+" cur "+currentBest);
								
							}
							if (total > robots.size()) {
								System.out.println("exit");
								result = currentBest;
								bigger = false;
							} else {
								System.out.println("total set currentBest");
								currentBest = attempt;
								attempt++;
							}
						}
						
						Vector3 ip= null;
						if(b.getOriginalPos().x<intermediatePath.x)
						{
							ip=new Vector3(intermediatePath.x + result, 1, intermediatePath.z);
						}
						else if(b.getOriginalPos().x>intermediatePath.x)
						{
							ip=new Vector3(intermediatePath.x - result, 1, intermediatePath.z);
						}
						else if(b.getOriginalPos().z<intermediatePath.z)
						{
							ip=new Vector3(intermediatePath.x , 1, intermediatePath.z + result);
						}
						else 
						{
							ip=new Vector3(intermediatePath.x , 1, intermediatePath.z - result);
						}
						System.out.println("reached intermediate Target, now change it to "+ip);
						intermediatePath=ip;
						
						decideMove(intermediatePath);
					}
					else
					{
						
						for(int z=0;z<robots.size();z++)
						{
							robots.get(z).setOriginalPos(new Vector3(-1,-1,-1));
						}
						
						
						
						
						intermediateFound=true;
						break;
					}
					
				}
			}
			
			if (possibleMovements.size() == 0)
			{
				break;
			}
			else
			{
				executedMoves++;
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

	public void removePassages(ArrayList<Vector3> possibleMovements)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
	
		for(int i=0;i<possibleMovements.size();i++)
		{
			for(int j=0;j<passages.size();j++)
			{
				if(possibleMovements.get(i).x==passages.get(j).x&&possibleMovements.get(i).x==passages.get(j).x)
				{
					toRemove.add(possibleMovements.get(i));
				}
			}
		}
		System.out.println("remove pass"+ toRemove.size());
		possibleMovements.removeAll(toRemove);
	}
	public void removeBadClimbs(ArrayList<Vector3> possibleMovements)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for(int i=0;i<possibleMovements.size();i++)
		{
			for(int j=0; j< obstacles.size();j++)
			{
				if(possibleMovements.get(i).x==obstacles.get(j).getPosition().x &&possibleMovements.get(i).z==obstacles.get(j).getPosition().z)
				{
					int totalNeeded=3;
					for(int q=(int)obstacles.get(j).getPosition().y;q>0;q--)
					{
						totalNeeded=totalNeeded+q;
					}
					if(robots.size()<totalNeeded+(amountOfTargets-2))
					{
						toRemove.add(possibleMovements.get(i));
					}
				}
			}
		}
		System.out.println("remove bad climb "+ toRemove.size());
		possibleMovements.removeAll(toRemove);
		
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
		System.out.println("removed orPos or bad move "+ toRemove.size());
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
		System.out.println("removed other robots "+ toRemove.size());
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
		System.out.println("removed obstacles "+ toRemove.size());
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
			System.out.println("removed impossible moves "+ toRemove.size());
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

package teamnine.blocksim.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.RobotBlock;

public class Move3 
{
	private ArrayList<Vector3> path;
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> obstacles;
	private ArrayList<Block> floor;

	
	public Move3(ArrayList<Vector3> path, ArrayList<RobotBlock> robots, ArrayList<Block> obstacles,ArrayList<Block> floor, Block mt)
	{
		this.obstacles=obstacles;
		this.path=path;
		this.robots= new ArrayList<RobotBlock>(robots);
		this.floor=floor;
	
		for(int i=path.size()-1;i>0;i--)
			decideMove(this.path.get(i));
		
		decideMove(new Vector3(mt.getPosition().x,mt.getPosition().y,mt.getPosition().z));
		
		Thread.currentThread().interrupt();
	}
	public void decideMove(Vector3 v)
	{	
		
		ArrayList<RobotBlock> orderToMove=new ArrayList<RobotBlock>();
		float targetX=v.x;
		
		float targetZ=v.z;
		//finds distance from the block to the target assigns the value to the block 
		
		for(int i=0;i<robots.size();i++)
		{
			float distanceToPath=Math.abs(robots.get(i).getPosition().x-targetX)+Math.abs(robots.get(i).getPosition().z-targetZ)+robots.get(i).getPosition().y;
			robots.get(i).setDistanceToPath(distanceToPath);
			orderToMove.add(robots.get(i));
		}
		ArrayList<RobotBlock> newOrderToMove=order(orderToMove);
		for(int i=0;i<newOrderToMove.size();i++)
		{
			System.out.println("block: "+newOrderToMove.get(i)+"x; "+newOrderToMove.get(i).getPosition().x+" z: "+newOrderToMove.get(i).getPosition().z+" height: "+newOrderToMove.get(i).getPosition().y+" dis "+newOrderToMove.get(i).getDistanceToPath()+" target: "+v);
		}
			System.out.println();
		for(int i=0;i<newOrderToMove.size();i++)
		{
			moving(newOrderToMove.get(i),v);
		}
		
		
	}
	public void setDistances(RobotBlock b, RobotBlock end)
	{
		System.out.println("counter at start"+b.getCounter());
		if(b.getPosition().x==end.getPosition().x&&b.getPosition().y==end.getPosition().y)
			b.setDistanceToPath(0);
		else
		{
		Queue<RobotBlock> q=new LinkedList<RobotBlock>();
		q.add(b);
		while(q.peek()!=null)
		{
			RobotBlock w= q.poll();

			if(w.getVisited()==false)
			{
				w.setVisited(true);
				ArrayList<RobotBlock> connections= w.getConnections();
				
				for(int i=0;i<connections.size();i++)
				{
					if(connections.get(i).getVisited()==false)
					{	
					
						q.add(connections.get(i));
						if(connections.get(i).getCounter()>(w.getCounter()+1)||connections.get(i).getCounter()==0)
							connections.get(i).setCounter(w.getCounter()+1);
					} 
				}
			}
		}
		System.out.println("counter at end "+end.getCounter());
		if(end.getCounter()!=0)
			b.setDistanceToPath(end.getCounter());
		else
			b.setDistanceToPath((int)Math.abs(b.getPosition().x-end.getPosition().x)+Math.abs(b.getPosition().z-end.getPosition().z)+b.getPosition().y);
	}	
	}
	
	public ArrayList<RobotBlock> order(ArrayList<RobotBlock> otm)
	{	
		ArrayList<RobotBlock> sorted = new ArrayList<RobotBlock>();
		float maxDistance=0;
		for(int i=0; i<otm.size();i++)
		{
			if(otm.get(i).getDistanceToPath()>maxDistance)
			{
				maxDistance=otm.get(i).getDistanceToPath();
			}
		}
		ArrayList<Bucket> buckets = new ArrayList<Bucket>();
		buckets.add(new Bucket());
		for(int i=0;i<maxDistance;i++)
		{
			buckets.add(new Bucket());
		}
		
		for(int i=0; i<otm.size();i++)
		{
			int d =(int)otm.get(i).getDistanceToPath();
			buckets.get(d).addBlock(otm.get(i));
		}
		
		for(int i=buckets.size()-1;i>0;i--)
		{
			sorted.addAll(buckets.get(i).getBloks());
		}
		
		return sorted;
	}
	public void moving(RobotBlock b, Vector3 v)
	{
		System.out.println("Moving");
		boolean targetReached=false;
	
		while(!targetReached)
		{
			while(b.getMoving())
			{
				try 
				{
					Thread.sleep(250);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			ArrayList<Vector3> possibleMovements = new ArrayList<Vector3>();
			
			for(int i = 0; i < robots.size(); i++)
			{
				if(robots.get(i).getPosition().x==b.getPosition().x+1&&robots.get(i).getPosition().z==b.getPosition().z)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}					
				}
				if(robots.get(i).getPosition().x==b.getPosition().x&&robots.get(i).getPosition().z==b.getPosition().z+1)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
				if(robots.get(i).getPosition().x==b.getPosition().x-1&&robots.get(i).getPosition().z==b.getPosition().z)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
				if(robots.get(i).getPosition().x==b.getPosition().x&&robots.get(i).getPosition().z==b.getPosition().z-1)
				{
					if(robots.get(i).getPosition().y<=b.getPosition().y)
					{
						possibleMovements.add(new Vector3(robots.get(i).getPosition().x,robots.get(i).getPosition().y+1,robots.get(i).getPosition().z));
					}
				}
			}
			System.out.println("size "+possibleMovements.size());
			for(int i = 0; i < floor.size(); i++)
			{
				if(floor.get(i).getPosition().x==b.getPosition().x+1&&floor.get(i).getPosition().z==b.getPosition().z)
				{
						possibleMovements.add(new Vector3(floor.get(i).getPosition().x,b.getPosition().y,floor.get(i).getPosition().z));			
				}
				if(floor.get(i).getPosition().x==b.getPosition().x&&floor.get(i).getPosition().z==b.getPosition().z+1)
				{
						possibleMovements.add(new Vector3(floor.get(i).getPosition().x,b.getPosition().y,floor.get(i).getPosition().z));
				}
				if(floor.get(i).getPosition().x==b.getPosition().x-1&&floor.get(i).getPosition().z==b.getPosition().z)
				{
						possibleMovements.add(new Vector3(floor.get(i).getPosition().x,b.getPosition().y,floor.get(i).getPosition().z));
				}
				if(floor.get(i).getPosition().x==b.getPosition().x&&floor.get(i).getPosition().z==b.getPosition().z-1)
				{
						possibleMovements.add(new Vector3(floor.get(i).getPosition().x,b.getPosition().y,floor.get(i).getPosition().z));
				}
			}
			System.out.println("size2 "+possibleMovements.size());
			for(int i=0;i<possibleMovements.size();i++)
			{
				System.out.print(possibleMovements.get(i)+" ");
			}
			System.out.print(b.getPosition()+" ");
			System.out.println();
			removeRobots(possibleMovements);
			removeObstacles(possibleMovements);
			removeImpossibleMovements(possibleMovements,b);
			removeOrPos(possibleMovements,b,v);
			System.out.println("size checked "+possibleMovements.size());
			for(int i=0; i<possibleMovements.size();i++)
			System.out.print("pms "+possibleMovements.get(i)+"  ");
			System.out.println();
				
			if(b.getPosition().x==v.x&&b.getPosition().z==v.z)
			{
				break;
			}
			if(possibleMovements.size()==0)
			{
				break;
			}
			else
			{
				Vector3 bestMovement = null;
				int bestDistance=-1;
				if(possibleMovements.size()!=0)
				{
					if(possibleMovements.size()==1)
					{
						bestMovement= possibleMovements.get(0);
					}
					for(int i=0;i<possibleMovements.size();i++)
					{
						if(bestDistance==-1)
						{
							bestMovement=possibleMovements.get(i);
							bestDistance=(int) (Math.abs(possibleMovements.get(i).x-v.x)+Math.abs(possibleMovements.get(i).z-v.z)+possibleMovements.get(i).y);
						}
						else
						{
							if(bestDistance>((int) (Math.abs(possibleMovements.get(i).x-v.x)+Math.abs(possibleMovements.get(i).z-v.z)+possibleMovements.get(i).y)))
							{
								bestMovement=possibleMovements.get(i);
								bestDistance=(int) (Math.abs(possibleMovements.get(i).x-v.x)+Math.abs(possibleMovements.get(i).z-v.z)+possibleMovements.get(i).y);
							}
						}
					}
				}
				System.out.println("bm "+bestMovement+" current "+b.getPosition()+" size "+possibleMovements.size() );
				
				System.out.println("bm "+bestMovement);
				if(bestMovement.y>b.getPosition().y)
				{
					
					boolean possible=true;
					for(int i=0;i<robots.size();i++)
						if(b.getPosition().x==robots.get(i).getPosition().x&&b.getPosition().z==robots.get(i).getPosition().z&&b.getPosition().y+1==robots.get(i).getPosition().y)
						{
							possible=false;
						}
					if(possible)
					{
					
					System.out.println("climb"+" BLOCK: "+b);
					//b.setPosition(b.getPosition().x,b.getPosition().y+1,b.getPosition().z);
					b.climb();
					while(b.getMoving())
					{
						try 
						{
							Thread.sleep(250);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					}
					else
					{
						break;
					}
					
				}
				if(bestMovement.x<b.getPosition().x)
				{
					
					System.out.println("move left"+" BLOCK: "+b);
					//b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
					b.moveLeft();
				}
				else if(bestMovement.x>b.getPosition().x)
				{
					
					System.out.println("move right"+" BLOCK: "+b);
					//b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
					b.moveRight();
				}
				else if(bestMovement.z<b.getPosition().z)
				{
					
					System.out.println("move back"+" BLOCK: "+b);
					//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z-1);
					b.moveBackwards();
				}
				else
				{
					
					System.out.println("move forward"+" BLOCK: "+b);
					b.moveForward();
					//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z+1);					
				}
			
			}
			}
		
			
			
		
		while(b.getMoving())
		{
			try 
			{
				Thread.sleep(250);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("next block");
	}
	public void removeOrPos(ArrayList<Vector3> possibleMovements, RobotBlock b, Vector3 v)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for(int i = 0; i < possibleMovements.size(); i++)
		{
			if(possibleMovements.get(i).equals(b.getOriginalPos()))
			{
				toRemove.add(possibleMovements.get(i));
				System.out.println("fail1");
			}
			else if(/*possibleMovements.size()>1&&*/b.getPosition().dst(v) < possibleMovements.get(i).dst(v))
			{
				//System.out.println(possibleMovements.get(i));
				System.out.println("fail2");
				toRemove.add(possibleMovements.get(i));
			}
		}
		System.out.println("teoremove1 "+toRemove.size());
		for(int i=0;i<toRemove.size();i++)
		{
			System.out.print(toRemove.get(i)+" ");
		}
		System.out.println();
		possibleMovements.removeAll(toRemove);
	}
	public void removeRobots(ArrayList<Vector3> pm)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for(int i = 0; i < pm.size(); i++)
		{
			for(int j = 0; j < robots.size(); j++)
			{
				if(pm.get(i).equals(robots.get(j).getPosition()))
				{
					toRemove.add(pm.get(i));
				}
				
			}
		}
		System.out.println("teoremove2 "+toRemove.size());
		for(int i=0;i<toRemove.size();i++)
		{
			System.out.print(toRemove.get(i)+" ");
		}
		System.out.println();
		pm.removeAll(toRemove);
	}
	
	public void removeObstacles(ArrayList<Vector3> pm)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for(int i = 0; i < pm.size(); i++)
		{
			for(int j = 0; j < obstacles.size(); j++)
			{
				if(pm.get(i).equals(obstacles.get(j).getPosition()))
				{
					toRemove.add(pm.get(i));
				}
			}
		}
		System.out.println("teoremove3 "+toRemove.size());
		for(int i=0;i<toRemove.size();i++)
		{
			System.out.print(toRemove.get(i)+" ");
		}
		System.out.println();
		pm.removeAll(toRemove);
	}
	public void removeImpossibleMovements(ArrayList<Vector3> pm,RobotBlock b)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		boolean locked=false;
		for(int k=0;k<robots.size();k++)
		{
			if(b.getPosition().x==robots.get(k).getPosition().x&&b.getPosition().z==robots.get(k).getPosition().z&&b.getPosition().y+1==robots.get(k).getPosition().y)
			{
				locked=true;
			}
		}
		if(!locked)
		{
		for(int i=0;i<pm.size();i++)
		{
			
				
			if(pm.get(i).x<b.getPosition().x)
			{
				boolean remove=true;
				for(int j=0;j<robots.size();j++)
				{
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						System.out.println("ok1");
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						for(int k=0;k<robots.size();k++)
						{
							if(robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
							{System.out.println("ok2");
								remove=false;
								break;
							}	
						}
						
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						for(int k=0;k<robots.size();k++)
						{
							if(robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
							{System.out.println("ok3");
								remove=false;
								break;
							}
						}
						
					}
					if((robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z)&&(robots.get(j).getPosition().y!=b.getPosition().y))
					{System.out.println("ok4");
						remove=false;
						break;
					}
				}
				
				if(remove)
					toRemove.add(pm.get(i));
			}
			else if(pm.get(i).x>b.getPosition().x)
			{
				boolean remove=true;
				for(int j=0;j<robots.size();j++)
				{
					if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						for(int k=0;k<robots.size();k++)
						{
							if(robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
							{System.out.println("ok2");
								remove=false;
								break;
							}	
						}
					}
					if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						for(int k=0;k<robots.size();k++)
						{
							if(robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
							{System.out.println("ok2");
								remove=false;
								break;
							}	
						}
					}
					if((robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z)&&(robots.get(j).getPosition().y!=b.getPosition().y))
					{
						remove=false;
						break;
					}
				}
				
				if(remove)
					toRemove.add(pm.get(i));
			}
			else if(pm.get(i).z<b.getPosition().z)
			{
				boolean remove=true;
				for(int j=0;j<robots.size();j++)
				{
					if(robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						for(int k=0;k<robots.size();k++)
						{
							if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z&&robots.get(j).getPosition().y==b.getPosition().y)
							{System.out.println("ok2");
								remove=false;
								break;
							}	
						}
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						for(int k=0;k<robots.size();k++)
						{
							if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z&&robots.get(j).getPosition().y==b.getPosition().y)
							{System.out.println("ok2");
								remove=false;
								break;
							}	
						}
					}
					if((robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z)&&(robots.get(j).getPosition().y!=b.getPosition().y))
					{
						remove=false;
						break;
					}
				}
			
				if(remove)
					toRemove.add(pm.get(i));
			}
			else if(pm.get(i).z>b.getPosition().z)
			{
				boolean remove=true;
				for(int j=0;j<robots.size();j++)
				{
					if(robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z&&robots.get(j).getPosition().y==b.getPosition().y)
						{System.out.println("ok2");
							remove=false;
							break;
						}
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z&&robots.get(j).getPosition().y==b.getPosition().y)
						{System.out.println("ok2");
							remove=false;
							break;
						}
					}
					if((robots.get(j).getPosition().x==b.getPosition().x&&robots.get(j).getPosition().z==b.getPosition().z)&&(robots.get(j).getPosition().y!=b.getPosition().y))
					{
						remove=false;
						break;
					}
				}
				
				if(remove)
					toRemove.add(pm.get(i));
			}
			
		}
		System.out.println("teoremove5 "+toRemove.size());
		for(int i=0;i<toRemove.size();i++)
		{
			System.out.print(toRemove.get(i)+" ");
		}
		System.out.println();
		pm.removeAll(toRemove);
		}
	}
}

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
		/*
		RobotBlock end= new RobotBlock(v,null,0,null);
		robots.add(end);
		for(int i=0;i<robots.size();i++)
		{
			for(int j=i;j<robots.size();j++)
			{
				if(i!=j){
				if(robots.get(i).getPosition().x==robots.get(j).getPosition().x&&robots.get(i).getPosition().z==robots.get(j).getPosition().z&&robots.get(i).getPosition().y-1==robots.get(j).getPosition().y)
				{
					robots.get(i).addConnection(robots.get(j));
					robots.get(j).addConnection(robots.get(i));
				}
				else if(robots.get(i).getPosition().x==robots.get(j).getPosition().x&&robots.get(i).getPosition().z==robots.get(j).getPosition().z&&robots.get(i).getPosition().y+1==robots.get(j).getPosition().y)
				{
					robots.get(i).addConnection(robots.get(j));
					robots.get(j).addConnection(robots.get(i));
				}
				else if(robots.get(i).getPosition().x+1==robots.get(j).getPosition().x&&robots.get(i).getPosition().z==robots.get(j).getPosition().z&&robots.get(i).getPosition().y==robots.get(j).getPosition().y)
				{
					robots.get(i).addConnection(robots.get(j));
					robots.get(j).addConnection(robots.get(i));
				}
				else if(robots.get(i).getPosition().x-1==robots.get(j).getPosition().x&&robots.get(i).getPosition().z==robots.get(j).getPosition().z&&robots.get(i).getPosition().y==robots.get(j).getPosition().y)
				{
					robots.get(i).addConnection(robots.get(j));
					robots.get(j).addConnection(robots.get(i));
				}
				else if(robots.get(i).getPosition().x==robots.get(j).getPosition().x&&robots.get(i).getPosition().z+1==robots.get(j).getPosition().z&&robots.get(i).getPosition().y==robots.get(j).getPosition().y)
				{
					robots.get(i).addConnection(robots.get(j));
					robots.get(j).addConnection(robots.get(i));
				}
				else if(robots.get(i).getPosition().x==robots.get(j).getPosition().x&&robots.get(i).getPosition().z-1==robots.get(j).getPosition().z&&robots.get(i).getPosition().y==robots.get(j).getPosition().y)
				{
					robots.get(i).addConnection(robots.get(j));
					robots.get(j).addConnection(robots.get(i));
				}
				}
			}
		}
		for(int i=0;i<robots.size();i++)
		{	
			computeDistance(robots.get(i),end);
			for(int j=0;j<robots.size();j++)
			{	
				robots.get(j).setVisited(false);
				robots.get(j).setCounter(0);
			}
		}
		for(int i=0;i<robots.size();i++)
		{
			System.out.println("dis: "+robots.get(i).getDistanceToPath()+" pos "+robots.get(i).getPosition()+" tpos "+robots.get(robots.size()-1).getPosition());
			for(int j=0;j<robots.get(i).getConnections().size();j++)
			{
				System.out.print(robots.get(i).getConnections().get(j).getPosition()+"  ");
			}
			System.out.println();
		}
		
		robots.remove(robots.size()-1);
		for(int i=0;i<robots.size();i++)
			robots.get(i).clearConnections();
		*/
		ArrayList<RobotBlock> newOrderToMove=order(orderToMove);
		for(int i=0;i<newOrderToMove.size();i++)
		{
			System.out.println("block: "+newOrderToMove.get(i)+"x; "+newOrderToMove.get(i).getPosition().x+" z: "+newOrderToMove.get(i).getPosition().z+" height: "+newOrderToMove.get(i).getPosition().y+" dis "+newOrderToMove.get(i).getDistanceToPath()+" target: "+v);
		}
			System.out.println();
		for(int k=0;k<newOrderToMove.size();k++){
			moving(newOrderToMove.get(k),v);
		}
			//newOrderToMove.remove(0);
			//orderToMove=newOrderToMove;
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
		
		//System.out.println("moving startblock: "+b+" b.vector: "+b.getPosition()+" "+" t.vector: "+v);
		
		boolean targetReached=false;
		//runs until target has been reached or no further movements are possible
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
			
			/////////////////////////////////////////////////////////////
		//ArrayList<Vector3> possibleMovements=new ArrayList<Vector3>();
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
			removeRobots(possibleMovements);
			removeObstacles(possibleMovements);
			removeImpossibleMovements(possibleMovements,b);
			removeOrPos(possibleMovements,b,v);
			//removeOrPos(possibleMovements,b,v);
			for(int i=0; i<possibleMovements.size();i++)
			System.out.println("pms "+possibleMovements.get(i));
				
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
				/*boolean changed=false;
				while(!changed)
				{
					changed=true;
					System.out.println(possibleMovements.size());
					for(int j=0;j<possibleMovements.size();j++)
					{
						if(bestDistance==-1)
						{
							bestMovement=possibleMovements.get(0);
							bestDistance=(int)(Math.abs(possibleMovements.get(j).x-v.x)+Math.abs(possibleMovements.get(j).z-v.z)+possibleMovements.get(j).y);
							changed=false;
						}
						else
						{
							if(possibleMovements.size()>1&&j<possibleMovements.size()-1){
							if(possibleMovements.get(j).dst(v)< possibleMovements.get(j+1).dst(v))
							{
								bestMovement=possibleMovements.get(j);
								bestDistance=(int)(Math.abs(possibleMovements.get(j).x-v.x)+Math.abs(possibleMovements.get(j).z-v.z)+possibleMovements.get(j).y);
								changed=false;
							}
							}
						}
					}
				}*/
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
			}
			else if(/*possibleMovements.size()>1&&*/b.getPosition().dst(v) <= possibleMovements.get(i).dst(v))
			{
				System.out.println(possibleMovements.get(i));
				toRemove.add(possibleMovements.get(i));
			}
		}
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
		pm.removeAll(toRemove);
	}
	public void removeImpossibleMovements(ArrayList<Vector3> pm,RobotBlock b)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
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
			if(pm.get(i).x>b.getPosition().x)
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
			if(pm.get(i).z<b.getPosition().z)
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
							if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
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
							if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
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
			if(pm.get(i).z>b.getPosition().z)
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
						if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
						{System.out.println("ok2");
							remove=false;
							break;
						}
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
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
		pm.removeAll(toRemove);
	}
}

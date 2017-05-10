//to do:
//prioritize blocks who have the highest height
//add safe to everything
package teamnine.blocksim.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.RobotBlock;

public class Move {
	private ArrayList<Vector3> path;
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> obstacles;
	private int pauseTime;
	
	public Move(ArrayList<Vector3> path, ArrayList<RobotBlock> robots, ArrayList<Block> obstacles, Block mt)
	{
		this.obstacles=obstacles;
		this.path=path;
		this.robots= new ArrayList<RobotBlock>(robots);
		pauseTime=350*robots.size();
		for(int i=path.size()-1;i>0;i--)
			decideMove(this.path.get(i));
		
		decideMove(new Vector3(mt.getPosition().x,mt.getPosition().y,mt.getPosition().z));
		
		Thread.currentThread().interrupt();
	}
	//does all the prep work with finding and assigning distances
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
		/*for(int i=0;i<robots.size();i++)
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
	
	//orders the block based on their distance to the target using bucket sort. largest distance first.
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
	//once the order has been made, the movements will go to try and place the block as close to the first step of 
	//the path as possible all the way to the target
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
			boolean safe =false;
			boolean none=true;

			//checks which movements are possible
			
			for(int k=0;k<robots.size();k++)
			{
				
				if(b.getPosition().z==robots.get(k).getPosition().z&&robots.get(k).getPosition().y==b.getPosition().y+1&&b.getPosition().x==robots.get(k).getPosition().x)
				{
					safe=false;
					break;
				}
		
				if(b.getPosition().x+1==robots.get(k).getPosition().x&&robots.get(k).getPosition().y==b.getPosition().y&&robots.get(k).getPosition().z==b.getPosition().z||b.getPosition().x-1==robots.get(k).getPosition().x&&robots.get(k).getPosition().y==b.getPosition().y&&robots.get(k).getPosition().z==b.getPosition().z
				  ||b.getPosition().x==robots.get(k).getPosition().x&&robots.get(k).getPosition().y==b.getPosition().y&&robots.get(k).getPosition().z==b.getPosition().z+1||b.getPosition().x==robots.get(k).getPosition().x&&robots.get(k).getPosition().y==b.getPosition().y&&robots.get(k).getPosition().z==b.getPosition().z-1
				  ||b.getPosition().z==robots.get(k).getPosition().z&&robots.get(k).getPosition().y==b.getPosition().y-1&&b.getPosition().x==robots.get(k).getPosition().x)
				{
					safe= true;
				}
				
				
			}
			System.out.println("lol"+" safe "+safe);
			if(safe)
			{
				if(v.x==b.getPosition().x+1&&v.z==b.getPosition().z)
				{
					boolean right=true;
					for(int l =0;l<robots.size();l++)
					{
						//System.out.println(robots.get(l).getPosition()+" ,  "+b.getPosition());
						if(b.getPosition().x+1==robots.get(l).getPosition().x&&b.getPosition().y==robots.get(l).getPosition().y&&b.getPosition().z==robots.get(l).getPosition().z)
						{	System.out.println("failed");
							right=false;
						}
					}
					if(right)
					{
						for(int l =0;l<obstacles.size();l++)
						{
							if(b.getPosition().x+1==obstacles.get(l).getPosition().x&&b.getPosition().y==obstacles.get(l).getPosition().y&&b.getPosition().z==obstacles.get(l).getPosition().z)
							{System.out.println("failed");
								right=false;
							}
						}
					}
					if(right)
					{
						System.out.println("right"+" BLOCK: "+b);
						
						b.moveRight();
						none=false;
						//b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
						break;
					}
					
					
				}
				if(v.x==b.getPosition().x-1&&v.z==b.getPosition().z)
				{	
					boolean left=true;
					for(int l =0;l<robots.size();l++)
					{
						if(b.getPosition().x-1==robots.get(l).getPosition().x&&b.getPosition().y==robots.get(l).getPosition().y&&b.getPosition().z==robots.get(l).getPosition().z)
						{System.out.println("failed");
							left=false;
						}
					}
					if(left)
					{
						for(int l =0;l<obstacles.size();l++)
						{
							if(b.getPosition().x-1==obstacles.get(l).getPosition().x&&b.getPosition().y==obstacles.get(l).getPosition().y&&b.getPosition().z==obstacles.get(l).getPosition().z)
							{System.out.println("failed");
								left=false;
							}
						}
					}
					if(left)
					{
						System.out.println("left"+" BLOCK: "+b);
						
						b.moveLeft();
						none=false;
						//b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
						break;
					}
					
				}
				if(v.z==b.getPosition().z+1&&v.x==b.getPosition().x)
				{
					boolean forw=true;
					for(int l =0;l<robots.size();l++)
					{
						if(b.getPosition().x==robots.get(l).getPosition().x&&b.getPosition().y==robots.get(l).getPosition().y&&b.getPosition().z+1==robots.get(l).getPosition().z)
						{System.out.println("failed");
							forw=false;
						}
					}
					if(forw)
					{
						for(int l =0;l<obstacles.size();l++)
						{
							if(b.getPosition().x==obstacles.get(l).getPosition().x&&b.getPosition().y==obstacles.get(l).getPosition().y&&b.getPosition().z+1==obstacles.get(l).getPosition().z)
							{System.out.println("failed");
								forw=false;
							}
						}
					}
					if(forw)
					{
						System.out.println("forward"+" BLOCK: "+b);
						
						b.moveForward();
						none=false;
						//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z+1);
						break;
					}
				}
				if(v.z==b.getPosition().z-1&&v.x==b.getPosition().x)
				{
					boolean back=true;
					for(int l =0;l<robots.size();l++)
					{
						if(b.getPosition().x==robots.get(l).getPosition().x&&b.getPosition().y==robots.get(l).getPosition().y&&b.getPosition().z-1==robots.get(l).getPosition().z)
						{System.out.println("failed");
							back=false;
						}
					}
					if(back)
					{
						for(int l =0;l<obstacles.size();l++)
						{
							if(b.getPosition().x==obstacles.get(l).getPosition().x&&b.getPosition().y==obstacles.get(l).getPosition().y&&b.getPosition().z-1==obstacles.get(l).getPosition().z)
							{System.out.println("failed");
								back=false;
							}
						}
					}
					if(back)
					{
						System.out.println("back"+" BLOCK: "+b);
						
						b.moveBackwards();
						none=false;
						//b.setPosition(b.getPosition().x,b.getPosition().y,b.getPosition().z-1);
						break;
					}
					
				}
			}
			boolean floating =true;
			if(!safe)
			{
				for(int p=0;p<obstacles.size();p++)
				{
					if(b.getPosition().x==obstacles.get(p).getPosition().x&&b.getPosition().z==obstacles.get(p).getPosition().z&&b.getPosition().y-1==obstacles.get(p).getPosition().y)
					{
						floating =false;
						break;
					}
				}
			}
			/////////////////////////////////////////////////////////////
			if(none && floating){
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
			removeRobots(possibleMovements);
			removeObstacles(possibleMovements);
			removeOrPos(possibleMovements,b,v);
			for(int i=0; i<possibleMovements.size();i++)
			System.out.println("pms "+possibleMovements.get(i));
				
			if(b.getPosition().x==v.x&&b.getPosition().z==v.z)
			{
			//	System.out.println("yaaaay");
				break;
			}
			if(possibleMovements.size()==0)
			{
				break;
			}
			
			else
			{
				Vector3 bestMovement=null;
				float bestDistance=-1;
				boolean checkAgain=true;
				while(checkAgain)
				{
					checkAgain=false;
					for(int i=0;i<possibleMovements.size();i++)
					{
						if(bestDistance==-1)
						{
							bestMovement=possibleMovements.get(i);
							bestDistance=(Math.abs(possibleMovements.get(i).x-v.x)+Math.abs(possibleMovements.get(i).z-v.z)+possibleMovements.get(i).y);
							checkAgain=true;
						}
						else
						{
							if(possibleMovements.size()>1&&i<possibleMovements.size()-1)
							{
								if(possibleMovements.get(i).dst(v)<bestDistance)
								{
									bestMovement=possibleMovements.get(i);
									bestDistance=Math.abs(possibleMovements.get(i).x-v.x)+Math.abs(possibleMovements.get(i).z-v.z)+possibleMovements.get(i).y;
									checkAgain=false;
								}
							}
						}
					}
				}
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
					}
					else
					{
						break;
					}
					
				}
				else if(bestMovement.x<b.getPosition().x)
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
			
			none=true;
			
			
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
	
	public void removeOrPos(ArrayList<Vector3> possibleMovements, RobotBlock b, Vector3 v)
	{
		ArrayList<Vector3> toRemove = new ArrayList<Vector3>();
		for(int i = 0; i < possibleMovements.size(); i++)
		{
			if(possibleMovements.get(i).equals(b.getOriginalPos()))
			{
				toRemove.add(possibleMovements.get(i));
			}
			else if(possibleMovements.size()>1&&b.getPosition().dst(v) <= possibleMovements.get(i).dst(v))
			{
				System.out.println(possibleMovements.get(i));
				toRemove.add(possibleMovements.get(i));
			}
		}
		possibleMovements.removeAll(toRemove);
	}
	public void computeDistance(RobotBlock b, RobotBlock end)
	{
		if(b.getPosition().equals(end.getPosition()))
		{
			b.setDistanceToPath(0);
		}
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
			if(end.getCounter()>0)
				b.setDistanceToPath(end.getCounter());
			else
			{
				for(int i=0;i<robots.size();i++)
				{
					float distanceToPath=Math.abs(robots.get(i).getPosition().x-end.getPosition().x)+Math.abs(robots.get(i).getPosition().z-end.getPosition().z)+robots.get(i).getPosition().y;
					robots.get(i).setDistanceToPath(distanceToPath);
					
				}
			}
		}	
		
	}
	}

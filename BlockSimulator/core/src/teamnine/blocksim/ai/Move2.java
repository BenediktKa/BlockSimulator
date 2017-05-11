package teamnine.blocksim.ai;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.*;

public class Move2 
{
	private ArrayList<RobotBlock> robots;
	private ArrayList<Block> obstacles;
	private ArrayList<Block> floor;
	private ArrayList<Vector3> path;
	public Move2(ArrayList<Vector3> path, ArrayList<RobotBlock> robots, ArrayList<Block> obstacles, ArrayList<Block> floor)
	{
		System.out.println("start");
		this.robots=robots;
		this.obstacles=obstacles;
		this.floor=floor;
		this.path=path;
		for(int i=path.size()-1;i>0;i--)
		{
			ArrayList<RobotBlock> rb =decideBlockToMove(path.get(i));
			System.out.println("decideBlockToMove and size "+rb.size());
			for(int j=0;j<rb.size();j++)
			{
				Vector3 move=decideMove(rb.get(j), path.get(i));
				if(move!=null)
					moving(move, rb.get(j), path.get(i));
				
				
			}
		}	
	}
	public ArrayList<RobotBlock> decideBlockToMove(Vector3 t)
	{
		ArrayList<RobotBlock> order = new ArrayList<RobotBlock>();
		for(int i=0;i<robots.size();i++)
		{
			order.add(robots.get(i));
		}
		RobotBlock end = new RobotBlock(t,null,0,null);
		robots.add(end);
		for(int i=0;i<robots.size();i++)
		{
			for(int j=0;j<robots.size();j++)
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
		System.out.println("linkedList is created");
		for(int i=0;i<robots.size();i++)
		{
			setDistances(robots.get(i),end);
			for(int j=0;j<robots.size();j++)
			{
				robots.get(j).setVisited(false);
				robots.get(j).setCounter(0);
			}
		}
		
		for(int i=0;i<robots.size();i++)
		{
			robots.get(i).setVisited(false);
			robots.get(i).setCounter(0);
		}
		robots.remove(robots.size()-1);
		ArrayList<RobotBlock> newOrder=order(order);
		return newOrder;
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
	public void setDistances(RobotBlock b, RobotBlock end)
	{
		System.out.println("start distances "+b.getPosition());
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
						if(connections.get(i).getCounter()>(w.getCounter()+1)||connections.get(i).getCounter()==0)
							connections.get(i).setCounter(w.getCounter()+1);
						
						if(connections.get(i).getVisited()==false)
						{	
							q.add(connections.get(i));
						} 
					}
				}
			}
			if(end.getCounter()>0)
			{
				b.setDistanceToPath(end.getCounter());
				System.out.println(end.getCounter());
			}
				
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
	public Vector3 decideMove(RobotBlock b, Vector3 v)
	{
		ArrayList<Vector3> possibleMovements=new ArrayList<Vector3>();
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
					possibleMovements.add(new Vector3(floor.get(i).getPosition().x,floor.get(i).getPosition().y+1,floor.get(i).getPosition().z));			
			}
			if(floor.get(i).getPosition().x==b.getPosition().x&&floor.get(i).getPosition().z==b.getPosition().z+1)
			{
					possibleMovements.add(new Vector3(floor.get(i).getPosition().x,floor.get(i).getPosition().y+1,floor.get(i).getPosition().z));
			}
			if(floor.get(i).getPosition().x==b.getPosition().x-1&&floor.get(i).getPosition().z==b.getPosition().z)
			{
					possibleMovements.add(new Vector3(floor.get(i).getPosition().x,floor.get(i).getPosition().y+1,floor.get(i).getPosition().z));
			}
			if(floor.get(i).getPosition().x==b.getPosition().x&&floor.get(i).getPosition().z==b.getPosition().z-1)
			{
					possibleMovements.add(new Vector3(floor.get(i).getPosition().x,floor.get(i).getPosition().y+1,floor.get(i).getPosition().z));
			}
		}
		collision(possibleMovements);
		removeImpossibleMovements(possibleMovements,b);
		
		Vector3 bestMovement = null;
		int bestDistance=-1;
		if(possibleMovements.size()!=0)
		{
			if(possibleMovements.size()==1)
			{
				return possibleMovements.get(0);
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
		return bestMovement;
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
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
					{
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
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x+1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
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
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z-1&&robots.get(j).getPosition().y==b.getPosition().y)
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
						remove=false;
						break;
					}
					if(robots.get(j).getPosition().x==b.getPosition().x-1&&robots.get(j).getPosition().z==b.getPosition().z+1&&robots.get(j).getPosition().y==b.getPosition().y)
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
	public void collision(ArrayList<Vector3> pm)
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
	public boolean moving(Vector3 m, RobotBlock b,Vector3 t)
	{
		boolean go=true;
		if(m.y>b.getPosition().y)
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
				go=false;
			
		}
		if(go){
		if(m.x<b.getPosition().x)
		{
			
			System.out.println("move left"+" BLOCK: "+b);
			//b.setPosition(b.getPosition().x-1,b.getPosition().y,b.getPosition().z);
			b.moveLeft();
		}
		else if(m.x>b.getPosition().x)
		{
			
			System.out.println("move right"+" BLOCK: "+b);
			//b.setPosition(b.getPosition().x+1,b.getPosition().y,b.getPosition().z);
			b.moveRight();
		}
		else if(m.z<b.getPosition().z)
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
		if(go){
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
		if(m.x==t.x&&m.z==t.z)
		{
			return true;
		}
		}
		return false;
		
	}
	
}

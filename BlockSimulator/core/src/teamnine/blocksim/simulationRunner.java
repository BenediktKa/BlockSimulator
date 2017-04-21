package teamnine.blocksim;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class simulationRunner {
	private ArrayList<ArrayList<float[]>> simulation;
	private boolean wait = true;
	
	public simulationRunner(final String[][] simulationData, final BlockSimulator blockSimulator){
		
		for( int i=0; i<simulationData.length; i++){
			//System.out.println("read line");
			
			
			
			int TS = Integer.parseInt(simulationData[i][0]);
			
			for(int j=0; j<blockSimulator.blockList.size(); j++){
				
				if(blockSimulator.blockList.getBlock(j).getID() == Double.parseDouble(simulationData[i][1])){
					
					//long startTime = System.currentTimeMillis();
					//System.out.println(startTime);
					
					//while (System.currentTimeMillis() <= startTime+1000){
						//System.out.println("...");
					//}
					
					//System.out.println(simulationData[i][5]+" "+simulationData[i][7]+1+" "+simulationData[i][6]);
					
					blockSimulator.blockList.getBlock(j).setPosition(
							Float.parseFloat(simulationData[i][5]),
							Float.parseFloat(simulationData[i][7])+1,
							Float.parseFloat(simulationData[i][6]));
					//System.out.println("waited");
					
				}
			}
		}
	}
	
}

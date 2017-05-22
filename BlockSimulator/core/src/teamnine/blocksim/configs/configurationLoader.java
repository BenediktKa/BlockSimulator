package teamnine.blocksim.configs;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.math.Vector3;

import teamnine.blocksim.block.Block;
import teamnine.blocksim.block.BlockList;

public class configurationLoader {
	private BlockList blockList;
	private File robotConfig;
	private File targetConfig;
	private File obstacleConfig;
	private String[][] robotData;
	private String[][] targetData;
	private String[][] obstacleData;
	
	
	public configurationLoader(BlockList blockList)
	{
		this.blockList=blockList;
		chooseFiles();
		blockList.disposeExceptFloor();
		Reader reader = new Reader();
		
		try
		{
			robotData = reader.readFiles(robotConfig);
			addToBlockList(robotData, 0);
		}
		catch(NullPointerException e)
		{
			System.out.println("No Robot Configuration File selected or found");
		}
		try
		{
			targetData = reader.readFiles(targetConfig);
			addToBlockList(targetData, 1);
		}
		catch(NullPointerException e)
		{
			System.out.println("No Obstacle Configuration File selected or found");
		}
		try
		{
			obstacleData = reader.readFiles(obstacleConfig);
			addToBlockList(obstacleData, 2);
		}
		catch(NullPointerException e)
		{
			System.out.println("No Target Configuration File selected or found");
		}
		
	}
	
	private void chooseFiles()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
		
		fileChooser.setDialogTitle("Open Start Robot Configuration");
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			robotConfig = fileChooser.getSelectedFile();
		}
		
		fileChooser.setDialogTitle("Open Target Configuration");
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		
		{
			targetConfig = fileChooser.getSelectedFile();
		}
		
		fileChooser.setDialogTitle("Open Obstacle Configuration");
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			obstacleConfig = fileChooser.getSelectedFile();
		}
	}
	
	/**
	 * 
	 * @param data [i][x-pos,z-pos,y-pos] or [i][ID,x-pos,z-pos,y-pos] 
	 * @param type 0: robot, 1: target, 2: obstacle
	 */
	private void addToBlockList(String[][] data, int type)
	{
		Block.Type thisType;
		if(type==0) thisType = Block.Type.Robot;
		else if(type==1) thisType = Block.Type.Goal;
		else thisType = Block.Type.Obstacle;
		
		for(int i=0; i<data.length; i++)
		{
			System.out.println(data[i].length);
			if(type==2) //Does not have an ID
			{
				blockList.createBlock(new Vector3(Float.parseFloat(data[i][0]), (Float.parseFloat(data[i][2]) + 1), Float.parseFloat(data[i][1])), thisType);
				System.out.println("block created");
			}
			else //There is an ID, either an double value or 'x', therefore the try-catch
			{
				try
				{
					double newID = Double.parseDouble(data[i][0]);
					blockList.createBlock(new Vector3(Float.parseFloat(data[i][1]), (Float.parseFloat(data[i][3]) + 1), 
							Float.parseFloat(data[i][2])), thisType, newID);
					System.out.println("block created");
				} 
				catch (NumberFormatException e)
				{
					System.out.println("Exception");
					blockList.createBlock(new Vector3(Float.parseFloat(data[i][1]), (Float.parseFloat(data[i][3]) + 1), 
							Float.parseFloat(data[i][2])), Block.Type.Robot);
					System.out.println("block created");
				}
			}
		}
	}
}

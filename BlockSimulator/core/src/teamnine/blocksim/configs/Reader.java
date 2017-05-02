package teamnine.blocksim.configs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader
{
    private ArrayList<String> text;
    private String[][] blockData;
    private String[][] simulationData;
    private BufferedReader br;

    public String[] read(Scanner in) throws IOException
    {
        text = new ArrayList<String>();
        while (in.hasNextLine())
        {
            text.add(in.nextLine());
        }
        String[] nom = new String[text.size()];
        nom = text.toArray(nom);
        return nom;
    }
    
    public Reader (File file)
    {
    	try{
    	FileReader reader = new FileReader(file);
        br = new BufferedReader(reader);
        System.out.println(file);
        
        text = new ArrayList<String>();
        text.add(br.readLine());
        int cntr = 0;

        while(text.get(cntr)!=null){
            text.add(br.readLine());
            cntr++;
        }
        text.remove(cntr);
        
        blockData = new String[text.size()][];
        simulationData = new String[text.size()][];
        
        for(int i=0; i<text.size(); i++){
        	
        	String[] test = text.get(i).split(",\\s+");
        	
        	if(test.length<=4){ //For reading start configurations
        		blockData[i] = test;
        	} else { //For reading start
        		simulationData[i] = test;
        	}
        }
        
    }
    catch (IOException e) {
        System.out.println("IO Exception");
    }
    }

    public String[][] getBlockData()
    {
        return blockData;
    }
    
    public String[][] getSimulationData()
    {
    	return simulationData;
    }
}

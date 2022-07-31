import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;



public class MockDraftDriver {

	public static void main(String[] args ) {
		System.out.println("---------Reading Starting File In Now----------");
		File playerStatFile = new File("test.txt");
		TreeMap<String,ArrayList<Object>> allPlayers = new TreeMap<String,ArrayList<Object>>();
		
		try {
			
			Scanner fileReader = new Scanner(playerStatFile);
			
			while(fileReader.hasNextLine()) {
				String currPlayerStatsString = fileReader.nextLine();
				String[] currPlayerStatsArray = currPlayerStatsString.split(" ");
				ArrayList<Object> otherPlayStats = new ArrayList<Object>(0);
				
				for(String nextVal : Arrays.copyOfRange(currPlayerStatsArray, 1, currPlayerStatsArray.length)) {
					try {
						otherPlayStats.add(Double.parseDouble(nextVal));
					}
					catch (NumberFormatException error) {
						otherPlayStats.add("" + nextVal);
					}
				}
				
				allPlayers.put(currPlayerStatsArray[0],otherPlayStats);
			}
			fileReader.close();
		}
		catch (FileNotFoundException error) {
			System.out.println("File Not Found - Check File Name");
		}

		catch (Exception error) {
			System.out.println("Other Error Found - See Below /n ------------------------------------------");
			error.printStackTrace();
		}
		
		System.out.println("Done\n---------Creating Player Models Now----------");
		ArrayList<PlayerModel> allPlayerModels = new ArrayList<PlayerModel>();
		
		for(String nextPlayer : allPlayers.keySet()) {
			ArrayList<Object> nextPlayerStats = allPlayers.get(nextPlayer);
			String nextPlayerPos = nextPlayerStats.get(0).toString();
			
			if(nextPlayerPos.equals("RB")) {
				allPlayerModels.add(new RunningBackPlayerModel(nextPlayer, "", Double.valueOf(nextPlayerStats.get(1).toString()), 
																	Double.valueOf(nextPlayerStats.get(2).toString())));
			}
			else if(nextPlayerPos.equals("WR")) {
				allPlayerModels.add(new WideReceiverPlayerModel(nextPlayer, "", Double.valueOf(nextPlayerStats.get(1).toString()), 
						Double.valueOf(nextPlayerStats.get(2).toString())));
			}	
		}
		allPlayerModels.sort(null);
		DraftHandler draftHandler = new DraftHandler(allPlayerModels);
		draftHandler.toString();
		System.out.println("Done");
		System.out.println("---------Exiting----------");
	}
	
}

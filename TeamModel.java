import java.util.ArrayList;
import java.util.TreeMap;

public class TeamModel {
	private String teamName;
	
	private TreeMap<String,ArrayList<PlayerModel>> thisTeamPlayers;
	
	
	
	public TeamModel(String teamName){
		this.teamName = teamName;
		this.thisTeamPlayers = new TreeMap<String,ArrayList<PlayerModel>>();
		
		ArrayList<PlayerModel> quarterBackList = new ArrayList<PlayerModel>();
		ArrayList<PlayerModel> runningBackList = new ArrayList<PlayerModel>();
		ArrayList<PlayerModel> tightEndList = new ArrayList<PlayerModel>();
		ArrayList<PlayerModel> wideReceiverList = new ArrayList<PlayerModel>();
		
		this.thisTeamPlayers.put(QuarterBackPlayerModel.POSITIONSHORTHANDLE, quarterBackList);
		this.thisTeamPlayers.put(RunningBackPlayerModel.POSITIONSHORTHANDLE, runningBackList);
		this.thisTeamPlayers.put(TightEndPlayerModel.POSITIONSHORTHANDLE, tightEndList);
		this.thisTeamPlayers.put(WideReceiverPlayerModel.POSITIONSHORTHANDLE, wideReceiverList);
	}
	
	public void addPlayer(String playerClassString, PlayerModel playerToAdd) {
		this.thisTeamPlayers.get(playerClassString).add(playerToAdd);
		this.thisTeamPlayers.get(playerClassString).sort(null);
	}
	
	
	/** Working on sorting Starters from bench8*/
	public String toString() {
		TreeMap<String,ArrayList<PlayerModel>> copyOfThisTeamPlayers = 
			new TreeMap<String,ArrayList<PlayerModel>>(this.thisTeamPlayers);
		
		PlayerModel flexPlayer = null;	
		
		String returnString = "\n       " + this.teamName + "      " + "\n---------------------------------------\n";
		
		for(String currPosition : copyOfThisTeamPlayers.keySet()) {

			int amountOfStarters = 1;
			
			ArrayList<PlayerModel> playersAtThisPosition = copyOfThisTeamPlayers.get(currPosition);
			
			if(currPosition.equals(RunningBackPlayerModel.POSITIONSHORTHANDLE) ||
			currPosition.equals(WideReceiverPlayerModel.POSITIONSHORTHANDLE)) {
				amountOfStarters = 2;
			}

			for (int currStarters = 1; currStarters <= amountOfStarters; currStarters++) {

				PlayerModel currStarer = this.getStartersForPosition(playersAtThisPosition);
				if(currStarer != null) { 
					returnString += currPosition + " - " + currStarer + "\n";
					playersAtThisPosition.remove(currStarer);
				}
				else returnString += currPosition + " - None\n";
			}

			if(currPosition.equals(RunningBackPlayerModel.POSITIONSHORTHANDLE) ||
			currPosition.equals(WideReceiverPlayerModel.POSITIONSHORTHANDLE) ||
			currPosition.equals(TightEndPlayerModel.POSITIONSHORTHANDLE)) {
					
				PlayerModel firstPlayerOnBench = this.getStartersForPosition(playersAtThisPosition);
				if(firstPlayerOnBench != null) {
					if(flexPlayer == null) {
						flexPlayer = firstPlayerOnBench;
					}
					else if(flexPlayer.getPredictedScore() / 16 < firstPlayerOnBench.getPredictedScore() /16) {
						playersAtThisPosition.remove(firstPlayerOnBench);
						copyOfThisTeamPlayers.get(flexPlayer.getPosition()).add(flexPlayer);
						flexPlayer = firstPlayerOnBench;
					}
				} 
			}				
		}

		returnString += "Flex - " + flexPlayer + "\n\n";

		for(String currPosition : copyOfThisTeamPlayers.keySet()) {
			for(PlayerModel nextPositionBenchPlayer : copyOfThisTeamPlayers.get(currPosition)) {
				returnString += "Bench/" + currPosition + " - " + nextPositionBenchPlayer + "\n";
			}
		}
		return returnString;
	}
	
	private PlayerModel getStartersForPosition(ArrayList<PlayerModel> playersAtThisPosition) {
		if(playersAtThisPosition.isEmpty()) {
			return null;
		}
		else {
			PlayerModel starter = playersAtThisPosition.get(0);
			for(PlayerModel nextPlayer : playersAtThisPosition) {
				if(starter.getPredictedScore() / 17.0 < nextPlayer.getPredictedScore() /17) {
					starter = nextPlayer;
				}
			}
			return starter;
		}
	}
}

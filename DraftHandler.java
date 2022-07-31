import java.util.ArrayList;

public class DraftHandler {
	private ArrayList<PlayerModel> playersLeft;
	private VaribleOddsPicker randomNumGen;
	
	public DraftHandler(ArrayList<PlayerModel> startingPlayers) {
		this.playersLeft = new ArrayList<PlayerModel>(startingPlayers);
		this.randomNumGen = new VaribleOddsPicker();
		TeamModel testTeam = new TeamModel("Uber Meyer's Lap Dance");
		while(this.playersLeft.size() > 0) {
			int nextPick = this.randomNumGen.newOdds(this.playersLeft.size());
			System.out.println(this.playersLeft.get(nextPick-1));
			testTeam.addPlayer(this.playersLeft.get(nextPick-1).getPosition(), this.playersLeft.remove(nextPick-1));
			this.playersLeft.sort(null);
		}
		System.out.println(testTeam);
	}
}

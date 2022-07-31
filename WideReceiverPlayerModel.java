

public class WideReceiverPlayerModel extends PlayerModel {
	private static final double WIDERECEIVERALLOWEDREACH = 0.2;
	public static final String POSITIONSHORTHANDLE = "WR";
	
	public WideReceiverPlayerModel(String firstName, String lastName, Double predictedScore, Double avgADP) {
		super(firstName,lastName,POSITIONSHORTHANDLE, predictedScore,avgADP,WIDERECEIVERALLOWEDREACH);
	}
	

}

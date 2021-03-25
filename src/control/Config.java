package control;

public class Config {
	
	//MainApp
	public static String FILEPATH = "./resources/json/games.json";
	public static String ROOTLAYOUT = "view/RootLayout.fxml";
	public static String COREOVERVIEW = "view/CoreOverview.fxml";
	public static String EDITGAMEDIALOG = "view/EditGameDialog.fxml";
	public static String APPTITLE = "openSplitter v1.0.0";
	public static String SOUND = "resources/sound/alert_sound.mp3";
	public static String EDTITLE = "Edit current game splits";
	
	//CoreOverviewController
	public static String EMPTY = "";
	public static String PAUSED = "PAUSED";
	public static String TOTAL = "Total";
	public static String ZERODELTA = "+00.000s";
	public static String PLUS = "+";
	public static String PBHEADER = "You beat your PB ! Do you want to valid your time";
	public static String PBCONTENT = "Click OK to confirm, else click CANCEL.";
	
	//RootLayoutController
	public static String FIRST = "__First__";
	public static String FTITLE = "Choose split's logo";
	public static String FDIR = "./resources/logo";
	public static String GITHUB = "https://github.com/Arthur095/openSplitter";
	public static String ADDTITLE = "Adding a game";
	public static String ADDCONTENT = "Enter a game name:";
	public static String DELHEADER = "The selected game will be deleted. Are you sure to proceed";
	public static String DELTITLE = "Deleting selected game";
	public static String DELCONTENT = "Click OK to confirm, else click CANCEL.";
	public static String ICON = "file:./resources/logo/default.png";
	
	//JSonReadWrite
	public static String SOB = "sob";
	public static String PB = "pb";
	public static String NULL = "null";
	public static String NAME = "name";
	public static String LOGO = "logo";
	
	//Split
	public static String FLAG = "./resources/logo/flag.png";
	public static String DEFAULT= "./resources/logo/default.png";
	public static String DMARK = "-";
	
	//Chrono
	public static String DOT = ".";
	public static String GMT = "GMT";
	public static String FPATTERN = "HH:mm:ss.SSS";
	public static String SSPATTERN = "ss.SSS's'";
	public static String SMPATTERN = "mm'm'ss.SSS's'";
	public static String SHPATTERN = "HH'h'mm'm'ss.SSS's'";
	
}

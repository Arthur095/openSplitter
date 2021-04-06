package control;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * Const var definition class.
 *
 */
public class Config {
	
	//MainApp
	public static String FILEPATH = "./resources/json/games.json";
	public static String ROOTLAYOUT = "view/RootLayout.fxml";
	public static String COREOVERVIEW = "view/CoreOverview.fxml";
	public static String EDITGAMEDIALOG = "view/EditGameDialog.fxml";
	public static String EDITKEYBINDSDIALOG = "view/EditKeybindsDialog.fxml";
	public static String APPTITLE = "openSplitter";
	public static String SOUND = "resources/sound/alert_sound.mp3";
	public static String EDTITLE = "Edit current game splits";
	public static String DEFAULTURI = "file:./resources/logo/default.png";
	public static String STYLESHEET = "view/Style.css";
	public static String STYLE = "myDialog";
	public static String FAILTITLE = "ERROR";
	public static String FAILCONTENT = "Important files are missing, you should download the application again.";
	
	
	//CoreOverviewController
	public static String EMPTY = "";
	public static String PAUSED = "PAUSED";
	public static String TOTAL = "Total";
	public static String ZERODELTA = "+00.000s";
	public static String PLUS = "+";
	public static String PBHEADER = "You beat your PB ! Do you want to valid your time ?";
	public static String PBCONTENT = "Click OK to confirm, else click CANCEL.";
	
	//RootLayoutController
	public static String FIRST = "__First__";
	public static String FTITLE = "Choose split's logo";
	public static String FDIR = "./resources/logo";
	public static String GITHUB = "https://github.com/Arthur095/openSplitter";
	public static String ADDTITLE = "Adding a game";
	public static String ADDCONTENT = "Enter a game name:";
	public static String DELHEADER = "The selected game will be deleted. Are you sure to proceed ?";
	public static String DELTITLE = "Deleting selected game";
	public static String DELCONTENT = "Click OK to confirm, else click CANCEL.";
	public static String ICON = "file:./resources/logo/default.png";
	public static String PSTYLESHEET = "Style.css";
	public static String KEYTITLE = "Changing keybinds";
	public static String DELPBHEADER = "The selected game PB will be deleted. Are you sure to proceed ?";
	public static String DELPBTITLE = "Deleting selected game";
	public static String DELPBCONTENT = "Click OK to confirm, else click CANCEL.";
	public static String DELSOBHEADER = "The selected game SOB will be deleted. Are you sure to proceed ?";
	public static String DELSOBTITLE = "Deleting selected game";
	public static String DELSOBCONTENT = "Click OK to confirm, else click CANCEL.";
	
	//JSonReadWrite
	public static String SOB = "sob";
	public static String PB = "pb";
	public static String NAME = "name";
	public static String LOGO = "logo";
	public static String CONFNF = "config.json file not found. It will be created instead.";
	public static String GAMESNF = "games.json file not found. It will be created instead.";
	
	
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
	
	//Keybinds
	public static String START = "start";
	public static String PAUSE = "pause";
	public static String HIDE = "hide";
	public static String RESET = "reset";
	public static String SAVE = "save";
	public static String WAIT = "...";
	public static String UNBIND = "Set";
	private static String[] illegalKeys = {"Ctrl", "Alt", "Shift", "ù", "Undefined"};
	public static List<String> ILLEGALKEYS = Arrays.asList(illegalKeys);
	public static String CONFIGPATH = "./resources/json/config.json";
}

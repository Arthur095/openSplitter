package control.view;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.github.cliftonlabs.json_simple.JsonException;

import control.Config;
import control.MainApp;
import control.model.Split;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditKeybindsController {

	/*Attributes*/
	private Stage dialogStage;
	
	@FXML
	private AnchorPane mainPane;
	
	private HashMap<String, Runnable> Keybinds = new HashMap<String, Runnable>();
	private HashMap<KeyCombination, Runnable> Accelerators = new HashMap<KeyCombination, Runnable>();	
	
    /*Buttons*/
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button hideButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button saveButton;
    
    /*CheckBoxes*/
    @FXML
    private CheckBox ctrlBox;
    @FXML
    private CheckBox altBox;
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public EditKeybindsController() {
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    //Prevents use of spacebar and enter.
	mainPane.addEventFilter(KeyEvent.KEY_PRESSED, k -> {
	        if ( k.getCode() == KeyCode.SPACE || k.getCode() == KeyCode.ENTER){
	            k.consume();
	        }
	    });
	
	//Hande checkboxes
    ctrlBox.selectedProperty().addListener((o, oldValue, newValue) -> {
        if (newValue == true) {
        	altBox.setSelected(false);
        }
    });
    altBox.selectedProperty().addListener((o, oldValue, newValue) -> {
        if (newValue == true) {
        	ctrlBox.setSelected(false);
        }
    });
    }
    
    //attention à la scene des accelerateurs...
    @FXML
    private void handleSet(Event event) {
    	EventHandler<MouseEvent> mouseHandler = MouseEvent::consume;
    	dialogStage.addEventFilter(MouseEvent.ANY, mouseHandler);
    	
    	Button button = (Button) event.getSource();
    	String hashKey;
    	
    	switch(button.getId()) {
    		case "startButton":
    			hashKey = Config.START;
    			break;
    		case "pauseButton":
    			hashKey = Config.PAUSE;
    			break;
    		case "hideButton":
    			hashKey = Config.HIDE;
    			break;
    		case "resetButton":
    			hashKey = Config.RESET;
    			break;
    		case "saveButton":
    			hashKey = Config.SAVE;
    			break;
    		default:
    			hashKey = Config.START;
    			break;
    	}
    	String oldKey = button.getText();
    	button.setText(Config.WAIT);
 
        dialogStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	dialogStage.removeEventFilter(MouseEvent.ANY, mouseHandler);
            	String key = event.getCode().getName();
            	if(Config.ILLEGALKEYS.contains(key)) {
            		return;
            	}
                button.setText(key);
                
                dialogStage.getScene().setOnKeyPressed(null);
                Accelerators.values().removeIf(value -> value.equals(Keybinds.get(hashKey)));
                KeyCodeCombination bind = null;
                if(ctrlBox.isSelected()) {
                	KeyCodeCombination keybind = new KeyCodeCombination(event.getCode(), KeyCombination.CONTROL_DOWN);
                	bind = keybind;
                }
                else if(altBox.isSelected()) {
                	KeyCodeCombination keybind = new KeyCodeCombination(event.getCode(), KeyCombination.ALT_DOWN);
                	bind = keybind;
                }
                else {
                	KeyCodeCombination keybind = new KeyCodeCombination(event.getCode());
                	bind = keybind;
                }
                if(Accelerators.containsKey(bind)) {
                	button.setText(oldKey);
                	return;
                }
                Accelerators.put(bind, Keybinds.get(hashKey));
                System.out.println(Accelerators.toString());
            }
        });
    }
    

    /**
     * When finish is pressed, close the dialog and keeps state of Accelerators map
     */
    @FXML
    public void handleFinish(){
    	dialogStage.close();
    }
    
    /**
     * When cancel is pressed, close the dialog and nullify Accelerators map
     */
    @FXML
    public void handleCancel(){
    	Accelerators = null;
    	dialogStage.close();
    }
    
    /*Getters & Setters*/
 
    public HashMap<KeyCombination, Runnable> getAccelerators(){
    	return Accelerators;
    }
    
    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
		dialogStage.setOnCloseRequest(event -> {
			Accelerators = null;
		});
    }
    
    public void setKeybinds(HashMap<String, Runnable> keybinds) {
    	this.Keybinds = keybinds;
    }

}

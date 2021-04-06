package control.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import control.Config;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * Dialog UI controller that handles keybinds editing.
 *
 */
public class EditKeybindsController {

	/*Attributes*/
	private Stage dialogStage;
	
	private HashMap<String, Runnable> Keybinds = new HashMap<String, Runnable>();
	private HashMap<KeyCombination, Runnable> Accelerators = new HashMap<KeyCombination, Runnable>();	
	
	private EventHandler<MouseEvent> mouseHandler = MouseEvent::consume;
	
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
    @FXML
    private CheckBox unbindBox;
    
    /*Scene*/
	@FXML
	private AnchorPane mainPane;
    
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
    //Prevents use of spacebar and enter to avoid editing two keybinds at the same time.
	mainPane.addEventFilter(KeyEvent.KEY_PRESSED, k -> {
	        if ( k.getCode() == KeyCode.SPACE || k.getCode() == KeyCode.ENTER){
	            k.consume();
	        }
	    });
    }//initialize
    
    /**
     * Listen the next input after pressing button and adding to accelerators map corresponding keycombination (depending on checkboxes) and runnable matching string.
     * @param event
     */
    @FXML
    private void handleSet(Event event) {
    
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
    	
    	if(unbindBox.isSelected()) {
    		Accelerators.values().removeIf(value -> value.equals(Keybinds.get(hashKey)));
    		button.setText(Config.UNBIND);
    		return;
    	}
    	
    	String oldKey = button.getText();
    	button.setText(Config.WAIT);
    	dialogStage.addEventFilter(MouseEvent.ANY, mouseHandler);
    	
        dialogStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	dialogStage.removeEventFilter(MouseEvent.ANY, mouseHandler);
            	String key = event.getCode().getName();
            	if(Config.ILLEGALKEYS.contains(key)) {
            		return;
            	}
                
                dialogStage.getScene().setOnKeyPressed(null);
                System.out.println(Accelerators.toString());
                Accelerators.values().removeIf(value -> value.equals(Keybinds.get(hashKey)));
                KeyCodeCombination bind = null;
                if(ctrlBox.isSelected()) {
                	KeyCodeCombination keybind = new KeyCodeCombination(event.getCode(), KeyCombination.CONTROL_DOWN);
                	bind = keybind;
                	button.setText(bind.getName());
                }
                else if(altBox.isSelected()) {
                	KeyCodeCombination keybind = new KeyCodeCombination(event.getCode(), KeyCombination.ALT_DOWN);
                	bind = keybind;
                	button.setText(bind.getName());
                }
                else {
                	KeyCodeCombination keybind = new KeyCodeCombination(event.getCode());
                	bind = keybind;
                	button.setText(bind.getName());
                }
                if(Accelerators.containsKey(bind)) {
                	button.setText(oldKey);
                	return;
                }
                Accelerators.put(bind, Keybinds.get(hashKey));
            }
        });
    }//HandleSet
    

    /**
     * When finish is pressed, closes the dialog and keeps state of Accelerators map.
     */
    @FXML
    public void handleFinish(){
    	dialogStage.close();
    }//handleFinish
    
    /**
     * When cancel is pressed, close the dialog and nullify Accelerators map.
     */
    @FXML
    public void handleCancel(){
    	Accelerators = null;
    	dialogStage.close();
    }//handleCancel
    
    /**
     * Uncheck other boxes if checked.
     */
    @FXML
    public void handleUnbindBox() {
    	if(unbindBox.isSelected()) {
    		ctrlBox.setSelected(false);
    		altBox.setSelected(false);
    	}
    }//handleUnbindBox
    
    /**
     * Uncheck other boxes if checked.
     */
    @FXML
    public void handleCtrlBox() {
    	if(ctrlBox.isSelected()) {
    		unbindBox.setSelected(false);
    		altBox.setSelected(false);
    	}
    }//handleCtrlBox
    
    /**
     * Uncheck other boxes if checked.
     */
    @FXML
    public void handleAltBox() {
    	if(altBox.isSelected()) {
    		unbindBox.setSelected(false);
    		ctrlBox.setSelected(false);
    	}
    }//handleAltBox
    
    /**
     * Set buttons text depending on accelerators map keycombination name.
     */
    public void setupButton() {
    	HashMap<String, KeyCombination> acc = new HashMap<String, KeyCombination>();
       	for(Map.Entry<KeyCombination, Runnable> value : Accelerators.entrySet()) {
    		int indexVal = Arrays.asList(Keybinds.values().toArray()).indexOf(value.getValue());
    		acc.put((String) Arrays.asList(Keybinds.keySet().toArray()).get(indexVal), value.getKey());
    	}
       	for(String key : acc.keySet()) {
       		switch (key){
	       		case "start":
	       			startButton.setText(acc.get(key).getName());
	       			break;
	       		case "pause":
	       			pauseButton.setText(acc.get(key).getName());
	       			break;
	       		case "hide":
	       			hideButton.setText(acc.get(key).getName());
	       			break;
	       		case "save":
	       			saveButton.setText(acc.get(key).getName());
	       			break;
	       		case "reset":
	       			resetButton.setText(acc.get(key).getName());
	       			break;
	       		default:
	       			break;
       		}
       	}
    }//setupButton
    
    
    /*Getters & Setters*/
 
    public HashMap<KeyCombination, Runnable> getAccelerators(){
    	return Accelerators;
    }
    
    /**
     * Sets the stage of this dialog and add a listener in case it get closed without the cancel button.
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
	public void setAccelerators(HashMap<KeyCombination, Runnable> accelerators) {
		Accelerators = accelerators;
	}
}

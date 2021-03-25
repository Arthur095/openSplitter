package control.view;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import control.Config;
import control.model.Split;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditGameController {
	
	/*Attributes*/
	private Stage dialogStage;
	private ArrayList<Split> gameSplits;
	
    /*ComboBox*/
    @FXML 
    private ComboBox<String> addBox;
    @FXML 
    private ComboBox<String> deleteBox; 
    
    /*TextField*/
    @FXML
    private TextField nameField;
    @FXML
    private TextField logoField;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public EditGameController() {
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
	
    /**
     * When the add button is pressed, create a new split matching the values of text fields and put it in gameSplits array in the index of the selected combobox item, then update comboboxes
     */
    @FXML
    public void handleAdd(){
    	if(addBox.getSelectionModel().getSelectedItem() == null){
    		return;
    	}
    	ArrayList<String> splitNameList = gameSplits.stream().map(Split::splitName).collect(Collectors.toCollection(ArrayList::new));
    	if(!(splitNameList.contains(nameField.getText()))) {
    		if(!(addBox.getSelectionModel().getSelectedItem().equals(Config.FIRST))) {
    			gameSplits.add(splitNameList.indexOf(addBox.getSelectionModel().getSelectedItem())+1,new Split(nameField.getText(), logoField.getText()));
    		}
    		else {
    			gameSplits.add(0, new Split(nameField.getText(), logoField.getText()));
    		}
	    	setCombobox();
	    	nameField.setText(Config.EMPTY);
	    	logoField.setText(Config.EMPTY);
    	}
    }
    
    /**
     * When delete is pressed, remove from gameSplits array the selected object in combobox, then update comboboxes
     */
    @FXML
    public void handleDelete(){
    	if(deleteBox.getSelectionModel().getSelectedItem() == null){
    		return;
    	}
    	ArrayList<String> splitNameList = gameSplits.stream().map(Split::splitName).collect(Collectors.toCollection(ArrayList::new));
    	gameSplits.remove(splitNameList.indexOf(deleteBox.getSelectionModel().getSelectedItem()));
    	setCombobox();
    }
    
    /**
     * Open a file explorer to fill the logo path text field with selected file absolute path
     */
    @FXML
    public void handleBrowse(){
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle(Config.FTITLE);
    	fileChooser.setInitialDirectory(new File(Config.FDIR));
    	File file = fileChooser.showOpenDialog(dialogStage);
    	if(file != null) {
    		logoField.setText(file.getAbsolutePath().toString());
    	}
    }
    
    /**
     * When finish is pressed, close the dialog and keeps state of gameSplits array
     */
    @FXML
    public void handleFinish(){
    	dialogStage.close();
    }
    
    /**
     * When cancel is pressed, close the dialog and nullify gameSplits array
     */
    @FXML
    public void handleCancel(){
    	gameSplits = null;
    	dialogStage.close();
    }
    
    /**
     * Fill the dialog comboboxes with current data from gameSplits
     */
    public void setCombobox() {
    	ArrayList<String> splitNameList = gameSplits.stream().map(Split::splitName).collect(Collectors.toCollection(ArrayList::new));
    	addBox.getItems().clear();
    	deleteBox.getItems().clear();
    	addBox.getItems().add(Config.FIRST);
    	addBox.getItems().addAll(splitNameList);
    	deleteBox.getItems().addAll(splitNameList);
    }
    
    /*Getters & Setters*/
    
    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
	public ArrayList<Split> getGameSplits() {
		return gameSplits;
	}
    public void setGameSplits(ArrayList<Split> gameSplits) {
    	this.gameSplits = gameSplits;
    }
	
}

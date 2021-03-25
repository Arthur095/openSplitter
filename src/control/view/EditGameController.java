package control.view;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import control.MainApp;
import control.model.Split;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditGameController {
	
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
	
    // Reference to the main application
    private MainApp mainApp;
	
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
    
    
    public void setCombobox() {
    	ArrayList<String> splitNameList = gameSplits.stream().map(Split::splitName).collect(Collectors.toCollection(ArrayList::new));
    	addBox.getItems().clear();
    	deleteBox.getItems().clear();
    	addBox.getItems().add("__First__");
    	addBox.getItems().addAll(splitNameList);
    	deleteBox.getItems().addAll(splitNameList);
    }
	
    @FXML
    public void handleAdd(){
    	if(addBox.getSelectionModel().getSelectedItem() == null){
    		return;
    	}
    	ArrayList<String> splitNameList = gameSplits.stream().map(Split::splitName).collect(Collectors.toCollection(ArrayList::new));
    	if(!(splitNameList.contains(nameField.getText()))) {
    		if(!(addBox.getSelectionModel().getSelectedItem().equals("First"))) {
    			gameSplits.add(splitNameList.indexOf(addBox.getSelectionModel().getSelectedItem())+1,new Split(nameField.getText(), logoField.getText()));
    		}
    		else {
    			gameSplits.add(0, new Split(nameField.getText(), logoField.getText()));
    		}
	    	setCombobox();
	    	nameField.setText("");
	    	logoField.setText("");
    	}
    }
    
    @FXML
    public void handleDelete(){
    	if(deleteBox.getSelectionModel().getSelectedItem() == null){
    		return;
    	}
    	ArrayList<String> splitNameList = gameSplits.stream().map(Split::splitName).collect(Collectors.toCollection(ArrayList::new));
    	gameSplits.remove(splitNameList.indexOf(deleteBox.getSelectionModel().getSelectedItem()));
    	setCombobox();
    }
    
    @FXML
    public void handleBrowse(){
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Choose split's logo");
    	File file = fileChooser.showOpenDialog(dialogStage);
    	if(file != null) {
    		logoField.setText(file.getAbsolutePath().toString());
    	}
    }
    
    @FXML
    public void handleFinish(){
    	
    }
    
    @FXML
    public void handleCancel(){
    	dialogStage.close();
    }
	
    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setGame(ArrayList<Split> gameSplits) {
    	this.gameSplits = gameSplits;
    }
	
	
}

package control.view;

import java.io.File;
import java.io.IOException;
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
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditKeybindsController {

	/*Attributes*/
	private Stage dialogStage;

    /*TextField*/
    @FXML
    private TextField nameField;
    @FXML
    private TextField logoField;

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
    	dialogStage.close();
    }
    
    /*Getters & Setters*/
    
    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


}

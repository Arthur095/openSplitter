package control.model;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Split {

	/*Attributes*/
	private StringProperty splitName = new SimpleStringProperty();
	private ObjectProperty<ImageView> logo = new SimpleObjectProperty<>();
	private String logoPath;
	private StringProperty time = new SimpleStringProperty();
	private StringProperty personalBest = new SimpleStringProperty();
	private StringProperty sumOfBest = new SimpleStringProperty();
	private StringProperty delta = new SimpleStringProperty();
	
	/**
	 * Default constructor
	 */
	public Split() {}
	
	/**
	 * Only Split name constructor.
	 * @param split
	 */
	public Split(String split) {
		this.splitName.setValue(split);
		Image img = new Image(new File("./resources/logo/flag.png").toURI().toString(), 30.0, 30.0, false, false);
		ImageView imgV = new ImageView(img);
		this.logo.setValue(imgV);
	}
	
	/**
	 * Full constructor
	 * @param split
	 * @param logoPath
	 * @param personalBest
	 * @param sumOfBest
	 */
	public Split(String split, String logoPath, String personalBest, String sumOfBest) {
		
		this.splitName.setValue(split);
		this.personalBest.setValue(personalBest);
		this.sumOfBest.setValue(sumOfBest);
		this.logoPath = logoPath;
		if(logoPath.equals("-")) {
			logoPath = "./resources/logo/default.png";
		}
		Image img = new Image(new File(logoPath).toURI().toString(), 30.0, 30.0, false, false);
		ImageView imgV = new ImageView(img);
		this.logo.setValue(imgV);
	}
	
	
	/*Getters & Setters*/
	public StringProperty splitNameProperty() {
		return splitName;
	}
	public ObjectProperty<ImageView> logoProperty() {
		return logo;
	}
	public StringProperty timeProperty() {
		return time;
	}
	public StringProperty personalBestProperty() {
		return personalBest;
	}
	public StringProperty sumOfBestProperty() {
		return sumOfBest;
	}
	public StringProperty deltaProperty() {
		return delta;
	}
	public String getSumOfBest() {
		return sumOfBest.toString();
	}
	public String getLogoPath() {
		return logoPath;
	}
	
}
	
	
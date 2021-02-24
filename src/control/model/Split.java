package control.model;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class Split {

	private StringProperty splitName = new SimpleStringProperty();
	private ObjectProperty<ImageView> logo = new SimpleObjectProperty<>();
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
		ImageView img = new ImageView(new File("./resources/logo/flag.png").toURI().toString());
		img.setFitHeight(30);
		img.setFitWidth(30);
		this.logo.setValue(img);
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
		if(logoPath.equals("-")) {
			logoPath = "./resources/logo/default.png";
		}
		ImageView img = new ImageView(new File(logoPath).toURI().toString());
		img.setFitHeight(30);
		img.setFitWidth(30);
		this.logo.setValue(img);
	}
	
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

	
}
	
	
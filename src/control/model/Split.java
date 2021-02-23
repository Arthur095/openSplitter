package control.model;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Split {

	private StringProperty split = new SimpleStringProperty();
	private ObjectProperty<ImageView> logo = new SimpleObjectProperty<>();
	private StringProperty time = new SimpleStringProperty();
	private StringProperty bestTime = new SimpleStringProperty();
	
	
	public Split(String split, String logo, String time, String bestTime) {
		
		this.split.setValue(split);
		this.time.setValue(time);
		this.bestTime.setValue(bestTime);
		if(logo == null) {
			logo = "default.png";
		}
		ImageView img = new ImageView(new File(logo).toURI().toString());
		this.logo.setValue(img);
	}
	
	public StringProperty splitProperty() {
		return split;
	}
	
	public ObjectProperty<ImageView> logoProperty() {
		return logo;
	}
	
	public StringProperty timeProperty() {
		return time;
	}
	
	public StringProperty bestTimeProperty() {
		return bestTime;
	}
	
	public void setTime(StringProperty time) {
		this.time = time;
		
	}

	public void setBestTime(String bestTime) {
		this.bestTime.setValue(bestTime);
	}
}

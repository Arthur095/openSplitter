package control.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Split {

	private StringProperty split = new SimpleStringProperty();
	private StringProperty logo = new SimpleStringProperty();
	private StringProperty time = new SimpleStringProperty();
	private DoubleProperty bestTime = new SimpleDoubleProperty();
	
	
	public Split(String split, String logo, String time, double bestTime) {
		this.split.setValue(split);
		this.logo.setValue(logo);
		this.time.setValue(time);
		this.bestTime.setValue(bestTime);
	}
	
	public StringProperty splitProperty() {
		return split;
	}
	
	public StringProperty logoProperty() {
		return logo;
	}
	
	public StringProperty timeProperty() {
		return time;
	}
	
	public DoubleProperty bestTimeProperty() {
		return bestTime;
	}
	
	public void setTime(StringProperty time) {
		this.time = time;
		
	}

	public void setBestTime(double bestTime) {
		this.bestTime.setValue(bestTime);
	}
}

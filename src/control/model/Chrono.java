package control.model;
import java.io.Serializable;
import java.sql.Date;

import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


public class Chrono implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -246687182791063554L;
	
	/*Attributes*/
    private Timeline timeline;
    private DoubleProperty timeSeconds = new SimpleDoubleProperty();
    private StringProperty fullTimer = new SimpleStringProperty();
    private Duration time = Duration.ZERO;
    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
    static {
    	format.setTimeZone(TimeZone.getTimeZone("GMT"));
    };
    
    /**
     * Constructor
     */
	public Chrono(){

	}

	/**
	 * Format time from seconds to hours, min and seconds.
	 * @return
	 */
	public String formatTime(double timer) {
		String date = format.format(new Date((long) (timer*1000)));
		return date;
	}
	
	/*Getters & Setters*/
	
	public Timeline getTimeline() {
		return timeline;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}

	public DoubleProperty getTimeSeconds() {
		return timeSeconds;
	}

	public void setTimeSeconds(DoubleProperty timeSeconds) {
		this.timeSeconds = timeSeconds;
	}

	public Duration getTime() {
		return time;
	}

	public void setTime(Duration time) {
		this.time = time;
	}

	public StringProperty getFullTimer() {
		return fullTimer;
	}

	public void setFullTimer(StringProperty fullTimer) {
		this.fullTimer = fullTimer;
	}

	
}

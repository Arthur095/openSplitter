package control.model;
import java.io.Serializable;
import java.sql.Date;

import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
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
    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS"); //Alternatively "HH'h'mm'm'ss.SSS's'"
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
	public static String formatTime(double timer) {
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

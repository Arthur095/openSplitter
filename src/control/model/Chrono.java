package control.model;
import java.sql.Date;

import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;


public class Chrono{
	
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
	 * @return String
	 */
	public static String formatTime(double timer) {
		String date = format.format(new Date((long) (timer*1000)));
		return date;
	}//formatTime
	
	/**
	 * Format time from seconds to the correct pattern depending if minuts or hours are negative or not.
	 * @param timer
	 * @return
	 */
	public static String formatTime(double timer, double personalBest) {
		SimpleDateFormat pattern;
		String date;
		double delta = timer - personalBest;
		date = "+";
		if(delta < 0.0) {
			delta = personalBest - timer;
			date = "-";
		}
		if(delta < 60) {
			pattern = new SimpleDateFormat("ss.SSS's'");	
		}
		else if(delta < 3600) {
			pattern = new SimpleDateFormat("mm'm'ss.SSS's'");
		}
		else {
			pattern = new SimpleDateFormat("HH'h'mm'm'ss.SSS's'");
		}
		pattern.setTimeZone(TimeZone.getTimeZone("GMT"));
		date += pattern.format(new Date((long) (delta*1000)));
		return date;
	}//formatTimeDelta
	
	/**
	 * Converts hours:minutes:seconds.milliseconds to seconds.milliseconds .
	 * @param timeString
	 * @return
	 */
	public static String reverseFormatTime(String timeString) {
		LocalTime localTime = LocalTime.parse(timeString);
		return String.valueOf(localTime.toSecondOfDay()) + "." + String.valueOf(localTime.getNano()).substring(0,3);
	}//reverseFormatTime
	
    /**
     * Sums time in an array of double.
     * @param column
     * @return
     */
    public static double sumTime(ArrayList<Double> column) {
    	Double total = 0.0;
    	Double previous = 0.0;
    	for(Double time: column) {
    		if(time != null) {
    			total += time - previous;
    			previous = time;
    		}
    	}
    	return total;
    }//sumTime
    
    /**
     * Sums independent segments of time in an array of double.
     * @param column
     * @return
     */
    public static double sumTimeSob(ArrayList<Double> column) {
    	Double total = 0.0;
    	for(Double time: column) {
    		if(time != null) {
    			total += time;
    		}
    	}
    	return total;
    }//sumTimeSob
	
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

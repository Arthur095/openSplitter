package control.model;
import javafx.scene.control.Label;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import org.junit.jupiter.api.Test;
class ChronoTest {

	@Test
	void testFormat() {
		//GIVEN
		double time = 60.0;
		//WHEN
		String timer = Chrono.formatTime(time);
		//THEN
		assertTrue(timer.equals("00:01:00.000"));
	}
	
	@Test
	void testFormatDelta() {
		//GIVEN
		double time1 = 12.0;
		double time2 = 72.0;
		//WHEN
		String time = Chrono.formatTime(time1, time2);
		//THEN
		assertTrue(time.equals("-01m00.000s"));
		
		//GIVEN
		time1 = 1.0;
		time2 = 3602;
		//WHEN
		time = Chrono.formatTime(time1, time2);
		//THEN
		assertTrue(time.equals("-01h00m01.000s"));
		
		//GIVEN
		time1 = 3602.0;
		time2 = 1.0;
		//WHEN
		time = Chrono.formatTime(time1, time2);
		//THEN
		assertTrue(time.equals("+01h00m01.000s"));
	}

	@Test
	void testReverseFormatTime() {
		String time = "00:01:12.233";
		String result = Chrono.reverseFormatTime(time);
		assertTrue(result.equals("72.233"));
		
		time = "02:01:23.933";
		result = Chrono.reverseFormatTime(time);
		assertTrue(result.equals("7283.933"));
	}
}

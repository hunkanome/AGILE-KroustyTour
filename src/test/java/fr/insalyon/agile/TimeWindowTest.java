package fr.insalyon.agile;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.insalyon.model.TimeWindow;

class TimeWindowTest {

	@Test
	void testGetTimeWindowList() {
		List<TimeWindow> windows = TimeWindow.getTimeWindows();
		int expectedSize = TimeWindow.MAX_TIME_WINDOW - TimeWindow.MIN_TIME_WINDOW + 1;
		int actualSize = windows.size();
		assertEquals(expectedSize, actualSize);
		for (int i = TimeWindow.MIN_TIME_WINDOW; i <= TimeWindow.MAX_TIME_WINDOW; i++) {
			TimeWindow expected = TimeWindow.getTimeWindow(i);
			TimeWindow actual = windows.get(i - 8);
			assertEquals(expected, actual);
		}
	}

	@Test
	void testGetTimeWindow() {
		TimeWindow window = TimeWindow.getTimeWindow(8);
		int actual = window.getStartHour();
		assertEquals(8, actual);
	}

	@Test
	void testGetInvalidTimeWindow() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> TimeWindow.getTimeWindow(7));
		String exceptionMessage = "Invalid hour for time window";
		String actualMessage = exception.getMessage();
		assertEquals(exceptionMessage, actualMessage);
	}

	@Test
	void testToString() {
		TimeWindow window = TimeWindow.getTimeWindow(8);
		String expected = "From 8h to 9h";
		String actual = window.toString();
		assertEquals(expected, actual);
	}

	@Test
	void testIsRightBefore() {
		TimeWindow window = TimeWindow.getTimeWindow(8);
		TimeWindow next = TimeWindow.getTimeWindow(9);
		boolean result = window.isRightBefore(next);
		assertTrue(result);
	}

	@Test
	void testIsRightBeforeFail() {
		TimeWindow window = TimeWindow.getTimeWindow(8);
		TimeWindow next = TimeWindow.getTimeWindow(10);
		boolean result = window.isRightBefore(next);
		assertFalse(result);
	}

	@Test
	void testIsBefore() {
		TimeWindow window = TimeWindow.getTimeWindow(8);
		TimeWindow next = TimeWindow.getTimeWindow(9);
		boolean result = window.isBefore(next);
		assertTrue(result);
	}

	@Test
	void testIsBeforeFail() {
		TimeWindow window = TimeWindow.getTimeWindow(10);
		TimeWindow next = TimeWindow.getTimeWindow(9);
		boolean result = window.isBefore(next);
		assertFalse(result);
	}

	@Test
	void testIsAfter() {
		TimeWindow window = TimeWindow.getTimeWindow(10);
		TimeWindow previous = TimeWindow.getTimeWindow(9);
		boolean result = window.isAfter(previous);
		assertTrue(result);
	}

	@Test
	void testIsAfterFail() {
		TimeWindow window = TimeWindow.getTimeWindow(9);
		TimeWindow previous = TimeWindow.getTimeWindow(10);
		boolean result = window.isAfter(previous);
		assertFalse(result);
	}

}

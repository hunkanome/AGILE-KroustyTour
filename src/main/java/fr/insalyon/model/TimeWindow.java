package fr.insalyon.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A one-hour time interval during which a courier can make a delivery
 * 
 * @see Delivery
 * @see Courier
 */
public class TimeWindow {

	private static final Duration DURATION = Duration.ofHours(1);
	
	private static ObservableList<TimeWindow> timeWindows;

	private static final int[] possibleTimeWindows = { 8, 9, 10, 11 };

	/**
	 * @return the list of the available time windows
	 */
	public static ObservableList<TimeWindow> getTimeWindows() {
		if (timeWindows == null) {
			System.out.println("création time window");
			timeWindows = FXCollections.observableArrayList();
			for (int startHour : possibleTimeWindows) {
				TimeWindow window = new TimeWindow(startHour);
				timeWindows.add(window);
			}
		}
		return timeWindows;
	}
	
	public static TimeWindow getTimeWindow(int startHour) {
		for (TimeWindow window : getTimeWindows()) {
			if (window.getStartHour() == startHour) {
				return window;
			}
		}
		throw new IllegalArgumentException("Invalid hour for time window");
	}

	private final int startHour;

	private TimeWindow(int hour) {
		for (int tw : possibleTimeWindows) {
			if (tw == hour) {
				this.startHour = hour;
				return;
			}
		}
		throw new IllegalArgumentException("Invalid hour for time window");
	}

	public int getStartHour() {
		return startHour;
	}

	public boolean isRightBefore(TimeWindow other) {
		return this.startHour + 1 == other.startHour;
	}

	public boolean isBefore(TimeWindow other) {
		return this.startHour < other.startHour;
	}

	public boolean isAfter(TimeWindow other) {
		return this.startHour > other.startHour;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.startHour);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeWindow other = (TimeWindow) obj;
		return this.startHour == other.startHour;
	}

	@Override
	public String toString() {
		int endHour = this.startHour + (int) DURATION.toHours();
		return "From " + this.startHour + "h to " + endHour + "h";
	}
	
	
}
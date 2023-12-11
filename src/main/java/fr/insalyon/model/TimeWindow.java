package fr.insalyon.model;

import java.time.Duration;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A one-hour time interval during which a courier can make a delivery.<br/>
 * It uses a multiton pattern to create all the instances and deliver it
 * 
 * @see Delivery
 * @see Courier
 */
public class TimeWindow {

	private static final Duration DURATION = Duration.ofHours(1);

	private static ObservableList<TimeWindow> timeWindows;

	/**
	 * The start hour of the first TimeWindow
	 */
	public static final int MIN_TIME_WINDOW = 8;
	/**
	 * The start hour of the last TimeWindow
	 */
	public static final int MAX_TIME_WINDOW = 11;

	/**
	 * @return the list of the available time windows
	 */
	public static ObservableList<TimeWindow> getTimeWindows() {
		if (timeWindows == null) {
			timeWindows = FXCollections.observableArrayList();
			for (int i = MIN_TIME_WINDOW; i <= MAX_TIME_WINDOW; i++) {
				TimeWindow window = new TimeWindow(i);
				timeWindows.add(window);
			}
		}
		return timeWindows;
	}

	/**
	 * @param startHour - the hour the TimeWindow starts
	 * @return the TimeWindow instance with the corresponding start hour
	 * @throws IllegalArgumentException if the start hour is invalid
	 */
	public static TimeWindow getTimeWindow(int startHour) throws IllegalArgumentException {
		for (TimeWindow window : getTimeWindows()) {
			if (window.getStartHour() == startHour) {
				return window;
			}
		}
		throw new IllegalArgumentException("Invalid hour for time window");
	}

	private final int startHour;

	private TimeWindow(int hour) {
		this.startHour = hour;
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
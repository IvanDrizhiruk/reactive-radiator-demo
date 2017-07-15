package ua.dp.radiator.utils;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONDAY;

public class DataTimeUtils {

	private static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat longDataTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String currentLongTime() {
		return longDataTimeFormat.format(Calendar.getInstance().getTime());
	}

	public static String calculateMondayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_WEEK, MONDAY);

		return dataFormat.format(calendar.getTime());
	}

	public static ZonedDateTime nowZonedDateTime() {
		return ZonedDateTime.now(ZoneId.of("UTC")).withNano(0);
	}
}

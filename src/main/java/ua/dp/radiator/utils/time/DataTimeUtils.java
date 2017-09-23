package ua.dp.radiator.utils.time;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONDAY;

@Component
public class DataTimeUtils {

	public ZonedDateTime nowZonedDateTime() {
		return ZonedDateTime.now(ZoneId.of("UTC")).withNano(0);
	}
}

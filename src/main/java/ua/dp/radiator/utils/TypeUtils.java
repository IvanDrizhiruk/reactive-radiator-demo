package ua.dp.radiator.utils;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;

public class TypeUtils {
	private static Logger LOG = Logger.getLogger(TypeUtils.class.getName());
	
	public static Integer toIntegerOrNull(String loadedString) {
		if(Strings.isNullOrEmpty(loadedString)) {
			return null;
		}

		try {
			return Integer.valueOf(loadedString);
		}catch (NumberFormatException e) {
			LOG.error(String.format("Can not convert '%s' to integer.", loadedString), e);
			return null;
		}
	}

	public static Long toLongOrNull(String loadedString) {
		if(Strings.isNullOrEmpty(loadedString)) {
			return null;
		}

		try {
			return Long.valueOf(loadedString);
		}catch (NumberFormatException e) {
			LOG.error(String.format("Can not convert '%s' to long.", loadedString), e);
			return null;
		}
	}
}

package ua.dp.radiator.utils;

import com.google.common.base.Stopwatch;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Timer {
	Stopwatch timer;

	public Timer() {
		timer = Stopwatch.createStarted();
	}

	public long elapsedTimeInMilliseconds() {
		timer.stop();
		return timer.elapsed(MILLISECONDS);
	}
}

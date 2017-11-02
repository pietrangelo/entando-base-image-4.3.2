/*
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando Enterprise Edition software.
 * You can redistribute it and/or modify it
 * under the terms of the Entando's EULA
 * 
 * See the file License for the specific language governing permissions   
 * and limitations under the License
 * 
 * 
 * 
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package org.entando.entando.plugins.jpemailmarketing.aps.system.utils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class TimeUtils {

	public static boolean validate(final String time) {
		Pattern pattern = Pattern.compile(TIME24HOURS_PATTERN);
		Matcher matcher = pattern.matcher(time);
		return matcher.matches();
	}

	private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
	
	/**
	 * Extract a specific value from a cron expression
	 * @param cron the string to parse
	 * @param key the type to extract
	 * @return the requested value
	 */
	public static String getCronValue(String cron, int key) {
		String[] values = cron.split(" ");
		return values[key];
	}

	/**
	 * Update a cron value 
	 * @param cron the expression to update
	 * @param key the type to update
	 * @param newValue the new value
	 * @return
	 */
	public static String updateCronValue(String cron, int key, String newValue) {
		String[] values = cron.split(" ");
		values[key] = newValue;
		return StringUtils.join(values, " ");
	}
	
	public static final int CRON_SECONDS = 0;
	public static final int CRON_MINUTES = 1;
	public static final int CRON_HOURS = 2;
	public static final int CRON_DAY_OF_MONTH = 3;
	public static final int CRON_MONTH = 4;
	public static final int CRON_DAY_OF_WEEK = 5;
	public static final int CRON_YEAR = 6;

	public static final String CRON_EVERY_DAY_MIDNIGHT = "0 0 0 * * *";
	
	///seconds | minutes | hours | day_of_month | month | day_of_week 
	//0 29 17 * * FRI,TUE
	
	
	
	public static String prettyPrintSeconds(int millis) {
		return TimeUtils.prettyPrintSeconds(new Long(millis));
	}
	
	public static String prettyPrintSeconds(long millis) {
		return String.format("%02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}

	public static String prettyPrintMinutes(int millis) {
		return TimeUtils.prettyPrintMinutes(new Long(millis));
	}

	public static String prettyPrintMinutes(long millis) {
		String formatted = String.format("%02d:%02d", 
				TimeUnit.MILLISECONDS.toHours(millis),
				Math.abs(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));
		if (!formatted.startsWith("-")) formatted = "+" + formatted;
		return formatted;
	}
}

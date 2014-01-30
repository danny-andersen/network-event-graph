package com.dsa.pcapneo.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jersey.api.ParamException;

public class Resource {
	private static final Log log = LogFactory.getLog(Resource.class);
	protected static final String DATE_FORMAT_LONG = "YYYY-MM-dd'T'HH:mm:ss";
	protected static final String DATE_FORMAT_SHORT = "YYYY-MM-dd";

	protected long parseDateString(String startDate, long def) {
		long start = def;
		//See if is a long date
		if (startDate != null && !startDate.isEmpty()) {
			try {
				start = Long.parseLong(startDate);
			} catch (NumberFormatException ne) {
				//Not a number - must be a date string
				SimpleDateFormat format = null;
				if (startDate.length() == DATE_FORMAT_LONG.length()) {
					format = new SimpleDateFormat(DATE_FORMAT_LONG);
				} else if (startDate.length() == DATE_FORMAT_SHORT.length()) {
					format = new SimpleDateFormat(DATE_FORMAT_SHORT);
				}
				if (format != null) {
					try {
						Date date = format.parse(startDate);
						start = date.getTime();
					} catch (ParseException pe) {
						log.error("Invalid date format: " + startDate);
						throw new ParamException.QueryParamException(pe, startDate,
								"Invalid date format");
					}
				}
			}
		}
		return start;
	}

}

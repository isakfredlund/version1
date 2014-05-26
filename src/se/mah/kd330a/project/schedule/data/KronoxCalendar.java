package se.mah.kd330a.project.schedule.data;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

import android.util.Log;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.filter.Rule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
public class KronoxCalendar {
	
	private static final String 	TAG = KronoxCalendar.class.getName();
	private static Calendar 		calendar;
	
	/**
	 * This creates the calendar object from an iCal file. Must be called before everything else.
	 * This is an imported package from iCal4j.
	 * 
	 * @param fin
	 *        KronoxReader.getFile()
	 * @throws IOException
	 *         Local file could be missing or corrupt
	 * @throws ParserException
	 *         Internal errors in the iCalendar file
	 */

	public static void createCalendar(FileInputStream fin) throws IOException, ParserException {
		CalendarBuilder builder = new CalendarBuilder();
		KronoxCalendar.calendar = builder.build(fin);
		Log.i(TAG, "CalendarBuilder called");	
	}
	
	public static Collection<?> todaysEvents() {
		Collection<?> retval = null;
		try {
			java.util.Calendar today = java.util.Calendar.getInstance();
			today.set(java.util.Calendar.HOUR_OF_DAY, 0);
			today.clear(java.util.Calendar.MINUTE);
			today.clear(java.util.Calendar.SECOND);
			Dur oneDay = new Dur(7, 0, 0, 0);
			Rule[] rules = new Rule[1];
			Period period = new Period(new DateTime(today.getTime()), oneDay);
			rules[0] = new PeriodRule(period);
			Filter filter = new Filter(rules, Filter.MATCH_ANY);
			retval = filter.filter(calendar.getComponents(Component.VEVENT));
		} catch (Exception e) {
			Log.e("KronoxCalendar", e.toString());	
		}
	
		return retval;					
	}
	
	public static Collection<?> getWeeksEventsFromThisWeek(int weekFromThisWeek) {
		// Find date of this Monday.
		java.util.Calendar thisMonday = java.util.Calendar.getInstance();
		final int currentDayOfWeek = (thisMonday.get(java.util.Calendar.DAY_OF_WEEK) + 7 - thisMonday.getFirstDayOfWeek()) % 7;
		thisMonday.add(java.util.Calendar.DAY_OF_YEAR, -currentDayOfWeek +(7*weekFromThisWeek));
		thisMonday.set(java.util.Calendar.HOUR_OF_DAY, 0);
		thisMonday.clear(java.util.Calendar.MINUTE);
		thisMonday.clear(java.util.Calendar.SECOND);
		//Ok continue
		Dur sevenDays = new Dur(7, 0, 0, 0);
		Rule[] rules = new Rule[1];
		Period period = new Period(new DateTime(thisMonday.getTime()), sevenDays);
		rules[0] = new PeriodRule(period);
		
		// MATCH_ALL - all rules must be matched
		// MATCH_ANY - any rule may be matched
		Filter filter = new Filter(rules, Filter.MATCH_ANY);
		if (calendar != null){
			return filter.filter(calendar.getComponents(Component.VEVENT));
		} else {
			return null;
		}
	}
}


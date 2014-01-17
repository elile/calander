package calendar.Dal;



import java.util.LinkedList;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

public class getListOfCalendarsIds {
	
	public static LinkedList<String> getCalendarIds(Activity a)
	{
		LinkedList<String> ret = new LinkedList<String>();
		Uri uri = CalendarContract.Calendars.CONTENT_URI;
		String[] projection = new String[] {
				CalendarContract.Calendars._ID,
				CalendarContract.Calendars.ACCOUNT_NAME,
				CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
				CalendarContract.Calendars.NAME,
				CalendarContract.Calendars.CALENDAR_COLOR
		};

		Cursor calendarCursor = a.managedQuery(uri, projection, null, null, null);
		while (calendarCursor.moveToNext()) {
			ret.add(calendarCursor.getString(0)+";"+calendarCursor.getString(4));
		}
		return ret;
	}
	
	public static LinkedList<String> getCalendarNamess(Activity a)
	{
		LinkedList<String> ret = new LinkedList<String>();
		Uri uri = CalendarContract.Calendars.CONTENT_URI;
		String[] projection = new String[] {
				CalendarContract.Calendars._ID,
				CalendarContract.Calendars.ACCOUNT_NAME,
				CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
				CalendarContract.Calendars.NAME,
				CalendarContract.Calendars.CALENDAR_COLOR
		};

		Cursor calendarCursor = a.managedQuery(uri, projection, null, null, null);
		while (calendarCursor.moveToNext()) {
			ret.add(calendarCursor.getString(2)+" "+calendarCursor.getString(3));
		}
		return ret;
	}

}

package calendar.Dal;



import java.util.LinkedList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;

public class getListOfCalendarsIds 
{
	
	public static LinkedList<String> getCalendarIds(Activity a)
	{
		LinkedList<String> ret = new LinkedList<String>();
		Uri uri = CalendarContract.Calendars.CONTENT_URI;
		String[] projection = new String[] 
				{
				CalendarContract.Calendars._ID,
				CalendarContract.Calendars.ACCOUNT_NAME,
				CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
				CalendarContract.Calendars.NAME,
				CalendarContract.Calendars.CALENDAR_COLOR
				};

		Cursor calendarCursor = a.managedQuery(uri, projection, null, null, null);
		while (calendarCursor.moveToNext())
		{
			ret.add(calendarCursor.getString(0)+";"+calendarCursor.getString(4));
		}
		return ret;
	}
	
	public static LinkedList<String> getCalendarNamess(Activity a)
	{
		LinkedList<String> ret = new LinkedList<String>();
		Uri uri = CalendarContract.Calendars.CONTENT_URI;
		String[] projection = new String[] 
				{
				CalendarContract.Calendars._ID,
				CalendarContract.Calendars.ACCOUNT_NAME,
				CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
				CalendarContract.Calendars.NAME,
				CalendarContract.Calendars.CALENDAR_COLOR
				};
		Cursor calendarCursor = a.managedQuery(uri, projection, null, null, null);
		while (calendarCursor.moveToNext())
		{
			ret.add(calendarCursor.getString(2)+" "+calendarCursor.getString(3));
		}
		return ret;
	}
	
	public static LinkedList<String> getCalendarIdsTry2(Activity a)
	{
		LinkedList<String> ret = new LinkedList<String>();
		// Projection array. Creating indices for this array instead of doing
		// dynamic lookups improves performance.
		final String[] EVENT_PROJECTION = new String[] {
		    Calendars._ID,                           
		    Calendars.ACCOUNT_NAME,                  
		    Calendars.CALENDAR_DISPLAY_NAME,         
		    Calendars.OWNER_ACCOUNT                  
		};
		
		final int PROJECTION_ID_INDEX = 0;
		final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
		final int PROJECTION_DISPLAY_NAME_INDEX = 2;
		final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
		
		// Run query
		Cursor cur = null;
		ContentResolver cr = a.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;   		
		// Submit the query and get a Cursor object back. 
		cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
		while (cur.moveToNext()) {
			ret.add(cur.getLong(PROJECTION_ID_INDEX)+";"+Color.BLUE);
		}
		return ret;
	}

}

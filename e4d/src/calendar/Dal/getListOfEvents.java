package calendar.Dal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Instances;
import dateAndTime.utils.MyEvent;
import dateAndTime.utils.dayDate;


public class getListOfEvents {

	static Cursor cursor;
	
	// get only event of one day - this is for faster ui
	public static LinkedList<MyEvent> readCalendar(Context context, String id, ContentResolver contentResolver, dayDate DayDate) 
	{
		LinkedList<MyEvent> ret = new LinkedList<MyEvent>();
		// For each calendar, display all the events from the previous week to the end of next week.        
		// uri for events
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();

		// events Between: day in past <->  day in futere
		ContentUris.appendId(builder, getMillisToStartOfDay(DayDate.getDay(),DayDate.getMonth(),DayDate.getYear()));
		ContentUris.appendId(builder, getMillisToEndOfDay(DayDate.getDay(),DayDate.getMonth(),DayDate.getYear()));

		Cursor eventCursor = contentResolver.query(builder.build(),new String[]  { "title","description", "begin", "end", "allDay", "eventLocation" }, "calendar_id=" + id.split(";")[0], null, "startDay ASC, startMinute ASC");
		if(eventCursor.getCount()>0)
		{
			if(eventCursor.moveToFirst())
			{
				do
				{
					final String title = eventCursor.getString(0);
					final long begin = eventCursor.getLong(2);
					final long end = eventCursor.getLong(3);
					final Boolean allDay = !eventCursor.getString(4).equals("0");
					final String location = eventCursor.getString(5);
					final String details = eventCursor.getString(1);
					MyEvent e = new MyEvent(title, begin, end, allDay, location, details, Integer.parseInt(id.split(";")[1]));
					ret.add(e);
				}
				while(eventCursor.moveToNext());
			}
		}
		return ret;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static long getMillisToStartOfDay(String day, String mon, String year) 
	{
		// this method to know from when to start the query for day list
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
		formatter.setLenient(false);

		String oldTime = day+"-"+mon+"-"+year+", 00:00";
		Date oldDate = null;
		try {
			oldDate = formatter.parse(oldTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long oldMillis = oldDate.getTime();
		return oldMillis;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static long getMillisToEndOfDay(String day, String mon, String year)
	{
		// this method to know from when to end the query for day list
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
		formatter.setLenient(false);

		String oldTime = day+"-"+mon+"-"+year+", 23:59";
		Date oldDate = null;
		try {
			oldDate = formatter.parse(oldTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long oldMillis = oldDate.getTime();
		return oldMillis;
	}


}

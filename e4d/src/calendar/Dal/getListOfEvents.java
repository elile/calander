package calendar.Dal;

import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Instances;
import dateAndTime.utils.MyEvent;
import dateAndTime.utils.dayDate;
import dateAndTime.utils.getTimeThings;


public class getListOfEvents 
{
	static Cursor cursor;
	private static long startDayInMillis;
	private static long endDayInMillis;

	public static long getStartDayInMillis() 
	{
		return startDayInMillis;
	}

	public static void setStartDayInMillis(long startDayInMillis) 
	{
		getListOfEvents.startDayInMillis = startDayInMillis;
	}

	public static long getEndDayInMillis() 
	{
		return endDayInMillis;
	}

	public static void setEndDayInMillis(long endDayInMillis) 
	{
		getListOfEvents.endDayInMillis = endDayInMillis;
	}

	// get only event of one day - this is for faster ui
	public static LinkedList<MyEvent> readCalendar(Context context, String id, ContentResolver contentResolver, dayDate DayDate) 
	{
		startDayInMillis = getTimeThings.getMillisToStartOfDay(DayDate.getDay(), DayDate.getMonth(), DayDate.getYear())/*+60000*/;
		endDayInMillis = getTimeThings.getMillisToEndOfDay(DayDate.getDay(), DayDate.getMonth(), DayDate.getYear());
		// get app events
		LinkedList<MyEvent> ret = new MyDbDal(context).getEventsByDate(DayDate);

		// For each calendar, display all the events from the previous week to the end of next week.        
		// uri for events
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();

		// events Between: day in past <->  day in futere
		ContentUris.appendId(builder, startDayInMillis );
		ContentUris.appendId(builder, endDayInMillis );

		Cursor eventCursor = contentResolver.query(builder.build(),new String[]  { "title","description", "begin", "end", "allDay", "eventLocation" }, "calendar_id="+ id.split(";")[0], null, "startDay ASC, startMinute ASC");
		if(eventCursor.getCount()>0)
		{
			if(eventCursor.moveToFirst())
			{
				do
				{
					String title = eventCursor.getString(0);
					long begin = eventCursor.getLong(2);
					long end = eventCursor.getLong(3);
					Boolean allDay = !eventCursor.getString(4).equals("0");
					String location = eventCursor.getString(5);
					String details = eventCursor.getString(1);
					if (begin <= startDayInMillis && !allDay)
					{
						begin = startDayInMillis;
					}
					if (end > endDayInMillis  && !allDay) 
					{
						end = endDayInMillis;
					}
//					if (begin == end) {
//						end = begin + 86340000;
//					}
					MyEvent e = new MyEvent(title, begin, end, allDay, location, details, Integer.parseInt(id.split(";")[1]));
					e.setId(-1);
					if (isEventOk(e, ret)) 
					{	
						ret.add(e); 
					}
				}
				while(eventCursor.moveToNext());
			}
		}
		return ret;
	}

	private static boolean isEventOk(MyEvent e, LinkedList<MyEvent> ret) 
	{
		if (e.isAllDay()) {
			return true;
		}
		long s1 = e.getBegin();
		long e1 = e.getEnd();
		for (MyEvent myEvent : ret) 
		{
			long s2 = myEvent.getBegin();
			long e2 = myEvent.getEnd();
			if (((s2>s1 && e2<e1))||((s2<s1)&&(e2>s1))||((s2<e1)&&(e2>e1))||((s2<s1)&&(e2>e1))) 
			{
				return false;
			}
		}
		return true;
	}

}

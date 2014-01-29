package calendar.Dal;

import java.util.LinkedList;

import dateAndTime.utils.MyEvent;
import dateAndTime.utils.dayDate;
import dateAndTime.utils.getTimeThings;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDbDal 
{

	Context c;
	MyDbHelper myDbHelper;

	public MyDbDal(Context c)
	{
		this.c=c;
		myDbHelper = new MyDbHelper(c);
	}
	
	public void deleteEventById(long id)
	{
		SQLiteDatabase db = myDbHelper.getWritableDatabase();
		db.delete(Constants.TABLE_NAME, Constants._ID+"="+id, null);
		db.close();
	}
	
	public void updateEventById(long id, MyEvent e)
	{
		SQLiteDatabase db = myDbHelper.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.put(Constants.COL_TITLE, e.getTitle());
		val.put(Constants.COL_BEGIN, e.getBegin());
		val.put(Constants.COL_END, e.getEnd());
		val.put(Constants.COL_ALLDAY, e.isAllDay() ? "1" : "0");
		val.put(Constants.COL_LOCATION, e.getLocation());
		val.put(Constants.COL_DETAILS, e.getDetails());
		val.put(Constants.COL_COLOR, e.getColor());
		db.update(Constants.TABLE_NAME, val, "_id "+"="+id, null);		
		db.close();
	}

	public void insertEvent(MyEvent e)
	{
		SQLiteDatabase db = myDbHelper.getWritableDatabase();
		ContentValues val = new ContentValues();

		val.put(Constants.COL_TITLE, e.getTitle());
		val.put(Constants.COL_BEGIN, e.getBegin());
		val.put(Constants.COL_END, e.getEnd());
		val.put(Constants.COL_ALLDAY, e.isAllDay() ? "1" : "0");
		val.put(Constants.COL_LOCATION, e.getLocation());
		val.put(Constants.COL_DETAILS, e.getDetails());
		val.put(Constants.COL_COLOR, e.getColor());

		e.setId(db.insertOrThrow(Constants.TABLE_NAME, null, val));
		db.close();
	}
	
	public void insertListEvents(LinkedList< MyEvent> l)
	{
		for (MyEvent myEvent : l)
		{
			insertEvent(myEvent);
		}
	}
	
	public LinkedList<MyEvent> getEventsByDate(dayDate d)
	{
		LinkedList<MyEvent> temp = getEvents();
		LinkedList<MyEvent> ret = new LinkedList<MyEvent>();
		long startDayInMillis = getTimeThings.getMillisToStartOfDay(d.getDay(), d.getMonth(), d.getYear())+60000;
		long endDayInMillis = getTimeThings.getMillisToEndOfDay(d.getDay(), d.getMonth(), d.getYear());		
		if (temp.size() > 0)
			for (MyEvent ev : temp) 
				if (ev.getBegin()>=startDayInMillis && ev.getEnd()<=endDayInMillis) 
					ret.add(ev);
		return ret;		
	}
	
	public LinkedList<MyEvent> getEvents()
	{
		SQLiteDatabase db = myDbHelper.getWritableDatabase();
		LinkedList<MyEvent> events = new LinkedList<MyEvent>();
		String[] cols = {
				Constants._ID,
				Constants.COL_TITLE,
				Constants.COL_BEGIN,
				Constants.COL_END,
				Constants.COL_ALLDAY,
				Constants.COL_LOCATION,
				Constants.COL_DETAILS,
				Constants.COL_COLOR
				};
		Cursor c = db.query(
				false, 
				Constants.TABLE_NAME, 
				cols,
				null, 
				null,
				null, 
				null, 
				null, 
				null
				);
		if (c.getCount()>0) 
		{
			while(c.moveToNext()){
				MyEvent event = new MyEvent(
					c.getString(1), 
					Long.parseLong(c.getString(2)),	
					Long.parseLong(c.getString(3)),
					(c.getString(4).compareTo("1") == 0) ? true : false,	
					c.getString(5),
					c.getString(6),	
					Integer.parseInt(c.getString(7))
					);
				event.setId(Integer.parseInt(c.getString(0)));
				events.add(event);
			}
		}
		db.close();
		return events;
		
	}

	public MyEvent getEventById(long id)
	{
		SQLiteDatabase db = myDbHelper.getWritableDatabase();
		MyEvent event = null;
		String[] cols = {
				Constants.COL_TITLE,
				Constants.COL_BEGIN,
				Constants.COL_END,
				Constants.COL_ALLDAY,
				Constants.COL_LOCATION,
				Constants.COL_DETAILS,
				Constants.COL_COLOR
				};
		Cursor c = db.query(
				false, 
				Constants.TABLE_NAME, 
				cols,
				Constants._ID+"="+id, 
				null,
				null, 
				null, 
				null, 
				null
				);
		if (c.getCount()>0) 
		{
			c.moveToNext();
			event = new MyEvent(
					c.getString(0), 
					Long.parseLong(c.getString(1)),	
					Long.parseLong(c.getString(2)),
					(c.getString(3).compareTo("1") == 0) ? true : false,	
					c.getString(4),
					c.getString(5),	
					Integer.parseInt(c.getString(6))
					);
		}
		db.close();
		return event;
	}
}

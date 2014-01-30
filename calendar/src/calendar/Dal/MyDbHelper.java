package calendar.Dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDbHelper extends SQLiteOpenHelper
{
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "events.db";

	private static final String DATABASE_CREATE = "create table "+ Constants.TABLE_NAME + "(" 
			+ Constants._ID+ " integer primary key autoincrement, "
			+ Constants.COL_TITLE + " TEXT,"  
			+ Constants.COL_BEGIN + " TEXT,"  
			+ Constants.COL_END + " TEXT,"  
			+ Constants.COL_ALLDAY + " TEXT,"  
			+ Constants.COL_LOCATION + " TEXT,"  
			+ Constants.COL_DETAILS + " TEXT,"  
			+ Constants.COL_COLOR + " TEXT);"	;

	public MyDbHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) 
	{
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		
	}


}
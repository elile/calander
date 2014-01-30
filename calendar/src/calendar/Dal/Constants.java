package calendar.Dal;

import android.provider.BaseColumns;

public class Constants implements BaseColumns
{
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "events";
	
	public static final String COL_TITLE = "title";
	public static final String COL_BEGIN = "begin";
	public static final String COL_END = "end";
	public static final String COL_ALLDAY = "allday";
	public static final String COL_LOCATION = "location";
	public static final String COL_DETAILS = "details";
	public static final String COL_COLOR = "color";
	
}

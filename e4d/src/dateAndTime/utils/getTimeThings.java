package dateAndTime.utils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

public class getTimeThings 
{

	public static String getMonth()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat mondf = new SimpleDateFormat("MM");
		String mon = mondf.format(c.getTime());
		return mon;		
	}

	public static String getMonth(String month)
	{
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("MMM").parse(month));
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		SimpleDateFormat mondf = new SimpleDateFormat("MM");
		String mon = mondf.format(cal.getTime());
		return mon;		
	}

	public static String getYear() 
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat yeardf = new SimpleDateFormat("yyyy");
		String year = yeardf.format(c.getTime());
		return year;
	}

	public static int getDaysInMonth(String smonth, String syear)
	{
		int monthNumber = Integer.parseInt(smonth);
		int year = Integer.parseInt(syear);
		Calendar mycal = new GregorianCalendar(year, monthNumber-1, 1);
		return mycal.getActualMaximum(Calendar.DAY_OF_MONTH); 
	}


	public static String getDayNameByDate(String day, String month, String year)
	{
		String input = day+"-"+month+"-"+year;
		SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = inFormat.parse(input);
		} catch (ParseException e) {
			Log.e("error", e.toString()); 
		}
		SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
		if (date != null) 
		{
			String goal = outFormat.format(date);
			return goal;
		}else {
			return "-1";
		}
	}

	public static String getDayNameByNum(int dayNum) 
	{
		return new DateFormatSymbols().getWeekdays()[dayNum];
	}

	public static int getDay() 
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat daydf = new SimpleDateFormat("dd");
		String day = daydf.format(c.getTime());
		return  Integer.parseInt(day);
	}

	public static String getDayNumber(long milliSeconds)
	{
		return getDate(milliSeconds, "dd-MM-yyyy").split("-")[0];
	}
	public static String getMonNumber(long milliSeconds)
	{
		return getDate(milliSeconds, "dd-MM-yyyy").split("-")[1];	
	}
	public static String getYearNumber(long milliSeconds)
	{
		return getDate(milliSeconds, "dd-MM-yyyy").split("-")[2];	
	}


	public static String getDate(long milliSeconds, String dateFormat)
	{
		// Create a DateFormatter object for displaying date in specified format.
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		// Create a calendar object that will convert the date and time value in milliseconds to date. 
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

}

package frames.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import builder.views.CustomSpinner;
import builder.views.DayTimeLineBuilder;
import calendar.Dal.getListOfEvents;

import com.example.e4d.MainActivity;
import com.example.e4d.SingleTouchEventView;
import com.example.e4d6.R;

import dateAndTime.utils.MyEvent;
import dateAndTime.utils.dayDate;
import dateAndTime.utils.getTimeThings;

public class CalendarFrame extends Fragment implements OnClickListener
{

	private static int DAYS = 28;
	private ImageButton[] dayButton = new ImageButton[DAYS];
	private Spinner extraDay;
	private View cal;
	private final String PACKEGENAME = "com.example.e4d6";
	private boolean firstFired;
	private LinearLayout hours2;
	private String id;
	private String month ;
	private String year ;
	private String day ;
	private int daysInMonth;
	private MainActivity mainAc;

	
	@Override
    public void onAttach(Activity activity) 
	{
        super.onAttach(activity);
		mainAc = (MainActivity)activity;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		cal = inflater.inflate(R.layout.calendar,container ,false);

		month = getArguments().getString("month");
		year = getArguments().getString("year");
		day = getArguments().getString("day");
		
		mainAc.setTextDay(day);

		daysInMonth = getTimeThings.getDaysInMonth(month,year);		

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		id = prefs.getString("cal_id", "");
		firstFired = true;
		int dayExtra =  daysInMonth - DAYS;

		// init the 29 30 31 days extra in month
		if (dayExtra > 0) 
			initSpinner(dayExtra);

		// init all the 28 days buttons
		initButtonsDay();

		// write the name of day in the correct date
		writeDays(Integer.parseInt(day));

		// write the middele time line of the events
		writeEventsField(new dayDate(day, month, year));


		return  cal;
	}


	private void writeEventsField(dayDate dayDateP)
	{
		LinearLayout hour = (LinearLayout)cal.findViewById(R.id.hours);
		// the holder of day time line
		hours2 = (LinearLayout)cal.findViewById(R.id.hours2);
		// repaint
		((ViewGroup) cal.findViewById(R.id.all_day_layout)).removeAllViews();
		// make the view of all day events
		TextView allDayTitle = new TextView(getActivity());
		allDayTitle.setText("All day events ");
		allDayTitle.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		allDayTitle.setGravity(Gravity.CENTER);
		((ViewGroup) cal.findViewById(R.id.all_day_layout)).addView(allDayTitle);
		// repaint
		hour.removeAllViews();
		hours2.removeAllViews();
		// 24 hours
		hour.setWeightSum(24);
		// loop to write the hours in layout
		for (int i = 0; i < 24; i++) 
		{
			TextView t = new TextView(getActivity());
			if (i<10) {
				t.setText("  0" + i + " ");
			}else {
				t.setText("  " + i + " ");
			}
			// the textview Weight is 1 = one hour
			t.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 1));
			t.setBackgroundResource(R.drawable.table_row_hour_border);
			// paint in red only if its now
			if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == i)
			{
				String nowDday = getTimeThings.getDayNumber(Calendar.getInstance().getTimeInMillis()); 
				String nowDmon = getTimeThings.getMonNumber(Calendar.getInstance().getTimeInMillis()); 
				String nowDyear = getTimeThings.getYearNumber(Calendar.getInstance().getTimeInMillis()); 
				if ( dayDateP.getDay().equals(nowDday) 	&& dayDateP.getMonth().equals(nowDmon) 	&& dayDateP.getYear().equals(nowDyear)) 
				{
					t.setBackgroundColor(Color.RED);
					t.setTextColor(Color.WHITE);
				}
			}
			hour.addView(t);
		}

		ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
		LinkedList<MyEvent> events = getListOfEvents.readCalendar(getActivity(), id , contentResolver, dayDateP);

		if (events.size()>0) {
			// dinamic gui add time line
			DayTimeLineBuilder.buildViewDayTimeLine(getActivity(), events, hours2, dayDateP, cal);
		}else {
			TextView t2 = new TextView(getActivity());
			t2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,0, 1440));
			t2.setBackgroundColor(Color.LTGRAY);
			hours2.addView(t2);
		}



	}



	private void initSpinner(int dayExtra) 
	{
		LinearLayout extra_frame = (LinearLayout)cal.findViewById(R.id.dayb_29_30_31);
		// add my spinner that it can handle with same selected day
		extraDay = new CustomSpinner(getActivity());
		extraDay.setVisibility(View.VISIBLE);
		List<String> list = new ArrayList<String>();

		switch (dayExtra) {
		case 1:
			list.add("29 "+getTimeThings.getDayNameByDate("29", month, year));
			break;
		case 2:
			list.add("29 "+getTimeThings.getDayNameByDate("29", month, year));
			list.add("30 "+getTimeThings.getDayNameByDate("30", month, year));
			break;
		case 3:
			list.add("29 "+getTimeThings.getDayNameByDate("29", month, year));
			list.add("30 "+getTimeThings.getDayNameByDate("30", month, year));
			list.add("31 "+getTimeThings.getDayNameByDate("31", month, year));
			break;
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		extraDay.setAdapter(dataAdapter);
		extraDay.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,	int pos, long arg3) 
			{
				if (!firstFired) {
					Toast.makeText(getActivity(),arg0.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
					writeDays( pos+29) ;
					writeEventsField(new dayDate((pos+29)+"", month, year));
				}else 
					firstFired = false;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		} );
		extra_frame.addView(extraDay);
	}

	private void writeDays(int yellow) 
	{
		// the yellow day is today
		for (int i = 1; i < daysInMonth+1; i++) 
		{
			String dayName = getTimeThings.getDayNameByDate(i + "", month, year);
			if (i <= DAYS)
			{
				String res = "day_b" + i;
				((TextView) cal.findViewById(getResByIdText(res))).setText(dayName);
				paintWarperOfDayBtn(dayName, i, yellow);
			}
		}
	}


	private void paintWarperOfDayBtn(String dayName, int dayNum, int yellow)
	{
		// resurse id
		String res = "warper_b" + dayNum;
		// find the wraper layout for do backround
		LinearLayout warper = (LinearLayout) cal.findViewById(getResByIdText(res));
		GradientDrawable bgShape = (GradientDrawable) warper.getBackground();
		// if today is the case paint yellow
		if (dayNum == yellow) 
		{
			bgShape.setColor(Color.YELLOW);
		} 
		else 
		{
			if (dayName.equals(getTimeThings.getDayNameByNum(Calendar.FRIDAY))) 
			{
				bgShape.setColor(Color.rgb(255, 165, 0));//orange
			}
			else if (dayName.equals(getTimeThings.getDayNameByNum(Calendar.SATURDAY)))
			{
				bgShape.setColor(Color.RED);
			} 
			else
				bgShape.setColor(Color.GREEN);
		}
	}



	private void initButtonsDay() 
	{
		// setOnClickListener dynamicly 
		for(int i=1; i<DAYS+1; i++) 
		{
			String buttonID = "b" + i ;
			int resID = getResByIdText(buttonID);
			dayButton[i-1] = ((ImageButton) cal.findViewById(resID));
			dayButton[i-1].setOnClickListener(this);
		}
	}

	// on day button click
	@Override
	public void onClick(View v) 
	{
		int numOfDayInMon;
		int i;
		for (i = 1; i < dayButton.length; i++) 
			if (v == dayButton[i-1])
			{
				break;
			}
		numOfDayInMon=i;
		mainAc.setTextDay(numOfDayInMon+"");
		writeDays(numOfDayInMon);
		writeEventsField(new dayDate(numOfDayInMon+"", month, year));

	}


	private int getResByIdText(String id)
	{
		return cal.getResources().getIdentifier(id, "id", PACKEGENAME );
	}

	//add this to the RelativeLayout for painting on it if needed
	public RelativeLayout bAddView(RelativeLayout mLayout2)
	{
		SingleTouchEventView s = new SingleTouchEventView(getActivity(), null);            
		s.setBackgroundColor(Color.TRANSPARENT);            
		mLayout2.addView(s); 
		return mLayout2;
	}





}

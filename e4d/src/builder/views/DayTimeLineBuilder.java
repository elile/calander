package builder.views;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import calendar.Dal.getListOfEvents;

import com.example.e4d6.R;

import dateAndTime.utils.MyEvent;
import dateAndTime.utils.dayDate;
import dateAndTime.utils.getTimeThings;

public class DayTimeLineBuilder 
{

	private static long startDayInMillis;
	private static long endDayInMillis;

	public static void buildViewDayTimeLine(Activity a, LinkedList<MyEvent> events, LinearLayout hours2, dayDate day, View cal) 
	{
		hours2.setWeightSum(1440);

		startDayInMillis = getListOfEvents.getMillisToStartOfDay(day.getDay(), day.getMonth(), day.getYear())+60000;
		endDayInMillis = getListOfEvents.getMillisToEndOfDay(day.getDay(), day.getMonth(), day.getYear());

		// for each event i calc min in day;
		for (MyEvent e : events)
		{
			e.setStartNormal(convertMillisToMinInDay(e.getBegin())+1);
			e.setEndNormal(convertMillisToMinInDay(e.getEnd()));
			//Log.e("eli", "s:"+e.getBegin()+" e:"+e.getEnd());
		}

		LinkedList<MyEvent> events_new = new LinkedList<MyEvent>();
		for (MyEvent e : events) {
			if (e.isAllDay())
			{
				if (  getTimeThings.getDate(e.getBegin(),"dd-MM-yyyy").equals(getTimeThings.getDate(startDayInMillis,"dd-MM-yyyy"))  ) 
				{
					LinearLayout allDayEvent = (LinearLayout) cal
							.findViewById(R.id.all_day_layout);
					TextView t6 = new TextView(a);
					t6.setTextAppearance(a, android.R.style.TextAppearance_DeviceDefault_Medium);
					//t6.setMaxTextSize(100);
					t6.setBackgroundColor(e.getColor() - 50);
					t6.setText(e.toString());
					//t6.setEllipsize(TruncateAt.END);
					//t6.setSingleLine(false);
					allDayEvent.addView(t6);
				}
			}else {
				events_new.add(e);
			}

		}

		if (events_new.size() == 0) {
			TextView t2 = new TextView(a);
			t2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,0, 1440));
			t2.setBackgroundColor(Color.LTGRAY);
			hours2.addView(t2);
			return;
		}

		Collections.sort(events_new, new Comparator<MyEvent>() {
			@Override
			public int compare(MyEvent e1, MyEvent e2) {
				//negative number when e1 < e2
				if (e1.getStartNormal()<e2.getStartNormal() && e1.getEndNormal()<e2.getEndNormal()) 
					return -1;
				//positive number when e1 > e2
				else if (e1.getStartNormal()>e2.getStartNormal() && e1.getEndNormal()>e2.getEndNormal()) 
					return 1;
				//0 when e1 == e2
				else 
					return 0;
			}
		});


		TextView t2 = new TextView(a);
		t2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,0, events_new.getFirst().getStartNormal()));
		t2.setBackgroundColor(Color.LTGRAY);
		hours2.addView(t2);

		// paint function
		for (MyEvent e : events_new) 
		{

			TextView t4 = new TextView(a);
			t4.setTextAppearance(a, android.R.style.TextAppearance_DeviceDefault_Medium);
			
			if ((e.getEndNormal() - e.getStartNormal()) > 10)
			{

				t4.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, 0, e.getEndNormal()
						- e.getStartNormal()));
			} else {
				t4.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 10));
			}
			t4.setBackgroundColor(e.getColor());
			t4.setText(e.toString());
			hours2.addView(t4);
			if (e != events_new.getLast())
			{
				TextView t5 = new TextView(a);
				t5.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, 0, events_new.get(
								events_new.indexOf(e) + 1).getStartNormal()
								- e.getEndNormal()));
				t5.setBackgroundColor(Color.LTGRAY);
				hours2.addView(t5);
			} else {
				TextView t5 = new TextView(a);
				t5.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 1440 - e
						.getEndNormal()));
				t5.setBackgroundColor(Color.LTGRAY);
				hours2.addView(t5);
			}
		}



	}

	private static int convertMillisToMinInDay(long millis) 
	{

		if (millis == startDayInMillis) {
			return 0;
		}else if (millis == endDayInMillis) {
			return 1440;
		}
		else {
			long diffInMillis = millis - startDayInMillis ;
			return millisToMin(diffInMillis);
		}
	}

	private static int millisToMin(long diffInMillis) {
		return (int)TimeUnit.MILLISECONDS.toMinutes(diffInMillis) ;
	}

}

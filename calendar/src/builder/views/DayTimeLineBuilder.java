package builder.views;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.Color;
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
	static Activity a;

	private static long startDayInMillis;
	private static long endDayInMillis;
	private static long hour_in_millis = 3600000;
	private static long min_in_millis = 60000;

	public static void buildViewDayTimeLine(Activity ac, LinkedList<MyEvent> events, LinearLayout hours2, dayDate day, View cal) 
	{
		a = ac ;
		final dayDate d = day ;
		// if all day field pressed make popup to insert event
		LinearLayout allDayEvent = (LinearLayout) cal.findViewById(R.id.all_day_layout);
		allDayEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// from where the quick action will pop up
				initPopUp.setView(v);
				// reload the event that we see in the dialog
				initPopUp.setEventToUpdate(new MyEvent("", getTimeThings.getMillisToStartOfDay(d.getDay(), d.getMonth(), d.getYear()), getTimeThings.getMillisToEndOfDay(d.getDay(), d.getMonth(), d.getYear()), true, "", "", -7090966));
				// change backround for see where you insert event 
				v.setBackgroundColor(Color.rgb(135,206,250));
				// this will show the popup
				initPopUp.getQuickActionForEmpty().show(v);							
			}
		});

		// for every min in day - this give me most Accuracy 
		hours2.setWeightSum(1440);
		startDayInMillis = getListOfEvents.getStartDayInMillis();
		endDayInMillis = getListOfEvents.getEndDayInMillis();

		// for each event i calc normalized minute in day 
		for (MyEvent e : events)
		{
			e.setStartNormal(convertMillisToMinInDay(e.getBegin())+1);
			e.setEndNormal(convertMillisToMinInDay(e.getEnd()));
		}
		LinkedList<MyEvent> events_new = new LinkedList<MyEvent>();
		// all day events are draw in another place and need to remove
		for (final MyEvent e : events)
		{
			if (e.isAllDay())
			{
				if (getTimeThings.getDate(e.getBegin(),"dd-MM-yyyy").equals(getTimeThings.getDate(startDayInMillis,"dd-MM-yyyy")) ) 
				{
					// all day event
					TextView t6 = new TextView(a);
					t6.setTextAppearance(a, android.R.style.TextAppearance_DeviceDefault_Medium);
					t6.setBackgroundColor(e.getColor());
					t6.setText(e.toString());
					// add lisitner is here
					// for event
					t6.setOnClickListener(new View.OnClickListener() 
					{
						@Override
						public void onClick(View v) {
							eventOnClick(e, v);							
						}
					});
					allDayEvent.addView(t6);
				}
			}else {
				events_new.add(e);
			}

		}

		if (events_new.size() == 0) {
			for (int i = 0; i < 24; i++) {
				final int j = i;
				TextView t2 = new TextView(a);
				t2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,0, 60));
				t2.setBackgroundColor(Color.LTGRAY);
				t2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mainAreaOnClick(new MyEvent("", startDayInMillis+hour_in_millis*j, startDayInMillis+hour_in_millis*j+hour_in_millis, true, "", "", -7090966), v);
					}
				});
				hours2.addView(t2);
			}
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

		int firstTime = events_new.getFirst().getStartNormal();
		final long emptyEndInMill = events_new.getFirst().getBegin();
		final long hourBeforeEmptyEnd = startDayInMillis + (((int) (firstTime / 60) - 1)*hour_in_millis) ;
		final long endOfEmptyBeforeHour = startDayInMillis + (events_new.getFirst().getStartNormal()%60+60) * min_in_millis;
		// until the first event
		if (firstTime > 60) 
		{
			for (int i = 0; i < (int) (firstTime / 60) - 1; i++) 
			{
				final int j = i;
				TextView t2 = new TextView(a);
				t2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,0, 60));
				t2.setBackgroundColor(Color.LTGRAY);
				t2.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						mainAreaOnClick(new MyEvent("", startDayInMillis+hour_in_millis*j, startDayInMillis+hour_in_millis*j+hour_in_millis, false, "", "", -7090966), v);					
					}
				});
				hours2.addView(t2);
			}
			TextView t2 = new TextView(a);
			t2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,0, (events_new.getFirst().getStartNormal())%60+60));
			t2.setBackgroundColor(Color.LTGRAY);
			t2.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					mainAreaOnClick(new MyEvent("", hourBeforeEmptyEnd, emptyEndInMill, false, "", "", -7090966), v);						
				}
			});
			hours2.addView(t2);
		}else {
			TextView t2 = new TextView(a);
			t2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,0, events_new.getFirst().getStartNormal()));
			t2.setBackgroundColor(Color.LTGRAY);
			t2.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					mainAreaOnClick(new MyEvent("", startDayInMillis, endOfEmptyBeforeHour, false, "", "", -7090966), v);						
				}
			});
			hours2.addView(t2);
		}

		// the paint function
		for (final MyEvent e : events_new) 
		{
			int eventInterval = e.getEndNormal()- e.getStartNormal() ;
			TextView t4 = new TextView(a);
			t4.setTextAppearance(a, android.R.style.TextAppearance_DeviceDefault_Medium);
			if ((e.getEndNormal() - e.getStartNormal()) > 10)
			{
				t4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, eventInterval));
			} else {
				// 10 is the minimum weight
				t4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 10));
			}
			t4.setBackgroundColor(e.getColor());
			t4.setText(e.toString());
			// add lisitner to t4 - here
			// for event
			t4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					eventOnClick(e, v);
				}
			});
			hours2.addView(t4);
			if (e != events_new.getLast())
			{
				int interval = events_new.get(events_new.indexOf(e) + 1).getStartNormal()- e.getEndNormal();
				final long startEmptyAdd = e.getEnd();
				final long endEmptyAdd = events_new.get(events_new.indexOf(e) + 1).getBegin();
				final long endOfEmptyBtween = endEmptyAdd-((interval%60+60)*min_in_millis) ;

				if (interval < 60)
				{
					TextView t5 = new TextView(a);
					t5.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, interval));
					t5.setBackgroundColor(Color.LTGRAY);
					t5.setOnClickListener(new View.OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							mainAreaOnClick(new MyEvent("", startEmptyAdd, endEmptyAdd, false, "", "", -7090966), v);
						}
					});
					hours2.addView(t5);
				}else {
					final long from = e.getEnd();
					for (int i = 0; i < (int) (interval / 60) - 1; i++) 
					{
						final long j = from + i*hour_in_millis;
						TextView t5 = new TextView(a);
						t5.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 60));
						t5.setBackgroundColor(Color.LTGRAY);
						t5.setOnClickListener(new View.OnClickListener() 
						{
							@Override
							public void onClick(View v) 
							{
								mainAreaOnClick(new MyEvent("", j, j+hour_in_millis, false, "", "", -7090966), v);
							}
						});
						hours2.addView(t5);
					}
					TextView t5 = new TextView(a);
					t5.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, interval%60+60));
					t5.setBackgroundColor(Color.LTGRAY);
					t5.setOnClickListener(new View.OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							mainAreaOnClick(new MyEvent("",endOfEmptyBtween , endEmptyAdd, false, "", "", -7090966), v);
						}
					});
					hours2.addView(t5);
				}
			} else {
				// event e was the last
				int lastInterval = 1440 - e.getEndNormal() ;
				if (lastInterval < 60)
				{
					TextView t5 = new TextView(a);
					t5.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, lastInterval));
					t5.setBackgroundColor(Color.LTGRAY);
					t5.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							mainAreaOnClick(new MyEvent("",e.getEnd() , endDayInMillis, false, "", "", -7090966), v);
						}
					});
					hours2.addView(t5);
				}else {
					final long startEmptyFinalEvent = e.getEnd();
					final long lastArea = lastInterval;
					for (int i = 0; i < (int)(lastInterval/60)-1; i++) 
					{
						final long j = startEmptyFinalEvent+i*hour_in_millis;
						TextView t5 = new TextView(a);
						t5.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 60));
						t5.setBackgroundColor(Color.LTGRAY);
						t5.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								mainAreaOnClick(new MyEvent("",j , j+hour_in_millis, false, "", "", -7090966), v);
							}
						});
						hours2.addView(t5);
					}
					TextView t5 = new TextView(a);
					t5.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, lastInterval%60+60));
					t5.setBackgroundColor(Color.LTGRAY);
					t5.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							mainAreaOnClick(new MyEvent("",endDayInMillis-((lastArea%60+60)*min_in_millis), endDayInMillis, false, "", "", -7090966), v);
						}
					});
					hours2.addView(t5);
				}
			}
		}
	}

	protected static void eventOnClick(MyEvent e, View v) 
	{
		v.setBackgroundColor(e.getColor() - 50);
		initPopUp.setView(v);
		initPopUp.setEventToUpdate(e);
		initPopUp.getQuickActionForEvent().show(v);			
	}

	protected static void mainAreaOnClick(MyEvent myEvent, View v) 
	{
		initPopUp.setView(v);
		v.setBackgroundColor(Color.rgb(135,206,250));
		// the event is only for init -> no id
		initPopUp.setEventToUpdate(myEvent);
		initPopUp.getQuickActionForEmpty().show(v);		
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

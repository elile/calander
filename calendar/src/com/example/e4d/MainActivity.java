package com.example.e4d;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Events;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import builder.views.initPopUp;
import builder.views.menuBuilder;

import calendar.Dal.getListOfCalendarsIds;

import com.example.e4d6.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import dateAndTime.utils.getTimeThings;
import frames.fragments.CalendarFrame;
import frames.fragments.HowToUseFrame;
import frames.fragments.changeDayCallBack;

public class MainActivity extends Activity implements OnClickListener,changeDayCallBack
{
	private SlidingMenu menu;
	private boolean Toshow=false;
	private Dialog dialog ;
	private TextView monthTextview;
	private TextView yearTextview;
	private TextView dayTextview;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// build the top action bar in color and button
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2CA9DE")));
		// first create
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//		Dialog d = new DialogForInsertEvet(this).buildDialog(new MyEvent("", getTimeThings.getMillisToEndOfDay("29", "01", "2014"), getTimeThings.getMillisToEndOfDay("29", "01", "2014")+3600000, false, "", "", Color.BLUE), new dayDate("29", "01", "2014"));
		//		d.show();

		initPopUp.initForEmpty(this);
		initPopUp.initForEvent(this);
		dialog = new Dialog(this);

		// id = id_number;id_color
		LinkedList<String> ids = getListOfCalendarsIds.getCalendarIds(this);
		initDialog(ids);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String sherdPref = prefs.getString("cal_id", "");
		if (sherdPref.compareTo("")==0 && ids.size() != 0) 
		{
			Editor editor = prefs.edit() ;
			editor.putString("cal_id", ids.get(0)) ;
			editor.commit() ;
		}
		// build slide menu
		menu = menuBuilder.buildMenu(getApplicationContext(), this, R.layout.menu_frame);



		monthTextview = (TextView) menu.getMenu().findViewById(R.id.mounthView);
		yearTextview = (TextView) menu.getMenu().findViewById(R.id.yearView);
		dayTextview = (TextView) menu.getMenu().findViewById(R.id.dayView);
		// init onClick in the menu
		initOnClick();
		// current date to the slide menu
		setLeftUpdateDate();
		inflateToday();

		menu.showMenu();

	}

	private void initDialog(final LinkedList<String> ids) 
	{
		// custom dialog
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(R.layout.custom_dialog_layout);
		dialog.setTitle("Select calendar");
		// set the custom dialog components - text, image and button
		final Spinner s = (Spinner) dialog.findViewById(R.id.spinner_ids);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getListOfCalendarsIds.getCalendarNamess(this));
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(dataAdapter);					
		Button dialogButton = (Button) dialog.findViewById(R.id.ok_event_dialog);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				Editor editor = prefs.edit() ;
				editor.putString("cal_id", ids.get(s.getSelectedItemPosition()) ) ;
				editor.commit() ;
				inflateToday();
				dialog.dismiss();
			}
		});
	}

	private void initOnClick()
	{
		View vm= menu.getMenu();

		Button calendar = (Button) vm.findViewById(R.id.calendar_btn);
		Button useing = (Button) vm.findViewById(R.id.help_btn);
		Button setting = (Button) vm.findViewById(R.id.setting_btn);
		Button sync = (Button) vm.findViewById(R.id.sync_btn);
		Button add_event = (Button) vm.findViewById(R.id.add_event_btn);
		Button select_calendar = (Button) vm.findViewById(R.id.select_calendar_btn);

		ImageButton add_mon = (ImageButton) vm.findViewById(R.id.add_month);
		ImageButton sub_mon = (ImageButton) vm.findViewById(R.id.sub_month);
		ImageButton add_year = (ImageButton) vm.findViewById(R.id.add_year);
		ImageButton sub_year = (ImageButton) vm.findViewById(R.id.sub_year);

		add_mon.setOnClickListener(this) ;
		sub_mon.setOnClickListener(this) ;
		add_year.setOnClickListener(this) ;
		sub_year.setOnClickListener(this) ;

		calendar.setOnClickListener(this) ;
		useing.setOnClickListener(this) ;
		setting.setOnClickListener(this) ;
		sync.setOnClickListener(this) ;
		add_event.setOnClickListener(this);
		select_calendar.setOnClickListener(this);


	}


	@SuppressLint("SimpleDateFormat")
	private void setLeftUpdateDate() 
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat daydf = new SimpleDateFormat("dd");
		String day = daydf.format(c.getTime());
		SimpleDateFormat mondf = new SimpleDateFormat("MMM");
		String mon = mondf.format(c.getTime());
		SimpleDateFormat yeardf = new SimpleDateFormat("yyyy");
		String year = yeardf.format(c.getTime());

		dayTextview.setText(day);
		monthTextview.setText(mon);
		yearTextview.setText(year);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle presses on the action bar items
		switch (item.getItemId())
		{
		case R.id.action_menu:
			if (Toshow) 
			{
				menu.showMenu();
				Toshow = false;
			}else {
				menu.toggle();  
				Toshow = true;
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// if we want the coustem action bar
		@Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main, menu);
			return super.onCreateOptionsMenu(menu);
		}

	// to menu button
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ( keyCode == KeyEvent.KEYCODE_MENU ) 
		{
			if (Toshow) {
				menu.showMenu();
				Toshow = false;
			}else {
				menu.toggle(true);  
				Toshow = true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.calendar_btn :
			inflateToday();
			break;
		case R.id.help_btn :
			getFragmentManager().beginTransaction().replace(R.id.elementContainer, new HowToUseFrame()).commit();
			break;
		case R.id.setting_btn :
			break;
		case R.id.sync_btn :
			inflateToday();
			break;
		case R.id.add_event_btn:
			open_intent_insert_event() ;
			break;
		case R.id.select_calendar_btn:
			dialog.show();
			break;
		case R.id.add_month:
			dayTextview.setText("01");
			add_mon();
			break;
		case R.id.sub_month:
			dayTextview.setText("01");
			sub_mon();
			break;
		case R.id.add_year:
			dayTextview.setText("01");
			add_year();
			break;
		case R.id.sub_year:
			dayTextview.setText("01");
			sub_year();
			break;

		}
	}

	private void sub_year() 
	{
		int y = Integer.parseInt(yearTextview.getText().toString());
		if (y > 1999)
		{
			y--;
			yearTextview.setText(y+"");
			update_main_frame();
		}
	}

	private void add_year() 
	{
		int y = Integer.parseInt(yearTextview.getText().toString());
		if (y < 2035)
		{
			y++;
			yearTextview.setText(y+"");
			update_main_frame();
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void sub_mon() 
	{
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("MMM").parse(monthTextview.getText().toString()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int m = cal.get(Calendar.MONTH) + 1;
		if (m > 1)
		{
			m--;
			monthTextview.setText(new DateFormatSymbols().getMonths()[m-1]);
			update_main_frame();
		}
		else if (m == 1) 
		{
			monthTextview.setText(new DateFormatSymbols().getMonths()[11]);
			update_main_frame();
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void add_mon() 
	{
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("MMM").parse(monthTextview.getText().toString()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int m = cal.get(Calendar.MONTH) + 1;
		if (m < 12)
		{
			m++;
			monthTextview.setText(new DateFormatSymbols().getMonths()[m-1]);
			update_main_frame();
		}
		else if (m == 12) 
		{
			monthTextview.setText(new DateFormatSymbols().getMonths()[0]);
			update_main_frame();
		}
	}

	private void update_main_frame() 
	{
		inflateDay(	getTimeThings.getMonth(monthTextview.getText().toString()),
				yearTextview.getText().toString(), dayTextview.getText().toString());
	}

	private void inflateToday() 
	{
		setLeftUpdateDate();

		// inflate fragment with now date
		Bundle bundle = new Bundle();
		bundle.putString("month", getTimeThings.getMonth());
		bundle.putString("year", getTimeThings.getYear());
		bundle.putString("day", getTimeThings.getDay()+"");

		// set Fragmentclass Arguments
		CalendarFrame calendarFrame = new CalendarFrame();
		calendarFrame.setArguments(bundle);
		getFragmentManager().beginTransaction().replace(R.id.elementContainer, calendarFrame).commit();
	}

	private void inflateDay(String mon,String year, String d) 
	{
		// inflate fragment with now date
		Bundle bundle = new Bundle();
		bundle.putString("month", mon);
		bundle.putString("year", year);
		bundle.putString("day", Integer.parseInt(d)+"");

		// set Fragmentclass Arguments
		CalendarFrame calendarFrame = new CalendarFrame();
		calendarFrame.setArguments(bundle);
		getFragmentManager().beginTransaction().replace(R.id.elementContainer, calendarFrame).commit();
	}

	private void open_intent_insert_event() 
	{
		Intent calIntent = new Intent(Intent.ACTION_INSERT);
		calIntent.setType("vnd.android.cursor.item/event");
		calIntent.putExtra(Events.TITLE, "");
		calIntent.putExtra(Events.EVENT_LOCATION, "");
		calIntent.putExtra(Events.DESCRIPTION, "");
		startActivityForResult(calIntent, 0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		inflateToday();
	}

	public void setTextDay(String day)
	{
		int d = Integer.parseInt(day);
		if(d<10){
			dayTextview.setText("0"+day);
		}else{
			dayTextview.setText(day);
		}
	}



}

package com.example.e4d;

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
import android.widget.Spinner;
import android.widget.TextView;
import builder.views.menuBuilder;
import calendar.Dal.getListOfCalendarsIds;

import com.example.e4d6.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import dateAndTime.utils.getTimeThings;

import frames.fragments.CalendarFrame;
import frames.fragments.HowToUseFrame;

public class MainActivity extends Activity implements OnClickListener
{
	private SlidingMenu menu;
	private boolean Toshow=false;
	private Dialog dialog ;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// build the top action bar in color and button
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2CA9DE")));

		// first create
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dialog = new Dialog(this);

		// id = id_number;id_color
		LinkedList<String> ids = getListOfCalendarsIds.getCalendarIds(this);
		initDialog(ids);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String sherdPref = prefs.getString("cal_id", "");
		if (sherdPref == "") 
		{
			Editor editor = prefs.edit() ;
			editor.putString("cal_id", ids.get(0)) ;
			editor.commit() ;
		}

		inflateToday();

		// build slide menu
		menu = menuBuilder.buildMenu(getApplicationContext(), this, R.layout.menu_frame);

		// init onClick in the menu
		initOnClick();

		// paint to slide menu
		setLeftUpdateDate();

		menu.showMenu();

	}

	private void inflateToday() {
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
		Button dialogButton = (Button) dialog.findViewById(R.id.button1);
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

		View v= menu.getMenu();
		((TextView) v.findViewById(R.id.dayView)).setText(day);
		((TextView) v.findViewById(R.id.mounthView)).setText(mon);
		((TextView) v.findViewById(R.id.yearView)).setText(year);
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

		}
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





}

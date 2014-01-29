package builder.views;

import calendar.Dal.MyDbDal;

import com.example.e4d6.R;

import dateAndTime.utils.MyEvent;
import dateAndTime.utils.dayDate;
import dateAndTime.utils.getTimeThings;
import frames.fragments.CalendarFrame;
import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class initPopUp 
{
	private static Activity a;
	private static final int ID_ADD = 1;
	private static final int ID_Delete_Event = 2;
	private static final int ID_Show_details = 3;
	private static final int ID_Edit_event_details = 4;

	private static QuickAction quickActionForEmpty;
	private static QuickAction quickActionForEvent;
	private static dayDate day;
	private static MyEvent eventToUpdate;
	private static View view;

	public static void initForEmpty(Activity ac) 
	{
		a=ac;
		quickActionForEmpty = new QuickAction(a);
		ActionItem addItem = new ActionItem(ID_ADD, "Add an event", a.getResources().getDrawable(android.R.drawable.ic_input_add));
		quickActionForEmpty.addActionItem(addItem);

		//setup the action item click listener
		quickActionForEmpty.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) 
			{
				ActionItem actionItem = quickAction.getActionItem(pos);
				if (actionId == ID_ADD)
				{
					final Dialog dialog = new DialogForInsertEvet(a).buildDialog(eventToUpdate, day);
					Button dialogButton = (Button) dialog.findViewById(R.id.ok_event_dialog);
					dialogButton.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							MyEvent eventToInsert = new MyEvent(
											getTitleFromDialog(dialog),
											getBeginFromDialog(dialog),
											getEndFromDialog(dialog), 
											allDayFromDialog(dialog),
											locationFromDialog(dialog), 
											detailsFromDialog(dialog), 
											eventToUpdate.getColor()	);
							MyDbDal sql = new MyDbDal(a);
							sql.insertEvent(eventToInsert);
							// inflate fragment with now date
							Bundle bundle = new Bundle();
							bundle.putString("month", day.getMonth());
							bundle.putString("year", day.getYear());
							bundle.putString("day", Integer.parseInt(day.getDay())+"");

							// set Fragmentclass Arguments
							CalendarFrame calendarFrame = new CalendarFrame();
							calendarFrame.setArguments(bundle);
							a.getFragmentManager().beginTransaction().replace(R.id.elementContainer, calendarFrame).commit();
						
							dialog.dismiss();
						}

						private String detailsFromDialog(Dialog dialog) 
						{
							EditText details = (EditText)dialog.findViewById(R.id.details);
							return details.getText().toString();
						}

						private String locationFromDialog(Dialog dialog)
						{
							EditText location = (EditText)dialog.findViewById(R.id.location);
							return location.getText().toString();
						}

						private boolean allDayFromDialog(Dialog dialog) 
						{
							CheckBox isAllDay = (CheckBox)dialog.findViewById(R.id.all_day_event);				
							return isAllDay.isChecked();
						}
						private long getBeginFromDialog(Dialog dialog)
						{
							myTimePicker begin = (myTimePicker)dialog.findViewById(R.id.timePicker_begin);
							return getTimeThings.getMillisFromDate(day, begin.getHour_display().getText().toString(),begin.getMin_display().getText().toString());
						}
						private long getEndFromDialog(Dialog dialog) 
						{
							myTimePicker end = (myTimePicker)dialog.findViewById(R.id.timePicker_end);
							return getTimeThings.getMillisFromDate(day, end.getHour_display().getText().toString(),end.getMin_display().getText().toString());
						}



						private String getTitleFromDialog(Dialog dialog)
						{
							EditText title = (EditText)dialog.findViewById(R.id.title);
							return title.getText().toString();
						}
					});
					dialog.show();
					view.setBackgroundColor(Color.LTGRAY);
					Toast.makeText(a.getApplicationContext(), "Event added", Toast.LENGTH_SHORT).show();
				} 
			}
		});

		quickActionForEmpty.setOnDismissListener(new QuickAction.OnDismissListener() 
		{
			@Override
			public void onDismiss() 
			{
				view.setBackgroundColor(Color.LTGRAY);
				//				Toast.makeText(a.getApplicationContext(), "dismissed", Toast.LENGTH_SHORT).show();
			}
		});


	}

	public static void initForEvent(Activity ac) 
	{
		a=ac;
		quickActionForEvent = new QuickAction(a);
		ActionItem acceptItem = new ActionItem(ID_Delete_Event, "Delete Event", a.getResources().getDrawable(android.R.drawable.ic_delete));
		ActionItem uploadItem = new ActionItem(ID_Show_details, "Show details", a.getResources().getDrawable(android.R.drawable.btn_default));
		ActionItem EditItem   = new ActionItem(ID_Edit_event_details, "Edit event details", a.getResources().getDrawable(android.R.drawable.btn_default));
		uploadItem.setSticky(true);

		quickActionForEvent.addActionItem(EditItem);
		quickActionForEvent.addActionItem(acceptItem);
		quickActionForEvent.addActionItem(uploadItem);

		//setup the action item click listener
		quickActionForEvent.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) 
			{
				ActionItem actionItem = quickAction.getActionItem(pos);
				if (actionId == ID_ADD) {
					Toast.makeText(a.getApplicationContext(), "Add item selected", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(a.getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
				}
			}
		});

		quickActionForEvent.setOnDismissListener(new QuickAction.OnDismissListener() 
		{
			@Override
			public void onDismiss() 
			{
				view.setBackgroundColor(((ColorDrawable)view.getBackground()).getColor()+50);
				Toast.makeText(a.getApplicationContext(), "Ups..dismissed", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static dayDate getDay() {
		return day;
	}

	public static void setDay(dayDate day) {
		initPopUp.day = day;
	}

	public static MyEvent getEventToUpdate() {
		return eventToUpdate;
	}

	public static void setEventToUpdate(MyEvent eventToUpdate) {
		initPopUp.eventToUpdate = eventToUpdate;
	}

	public static QuickAction getQuickActionForEmpty() 
	{
		return quickActionForEmpty;
	}


	public static QuickAction getQuickActionForEvent() 
	{
		return quickActionForEvent;
	}


	public static View getView() {
		return view;
	}

	public static void setView(View view) {
		initPopUp.view = view;
	}


}

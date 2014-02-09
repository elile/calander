package builder.views;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import calendar.Dal.MyDbDal;

import com.example.e4d6.R;

import dateAndTime.utils.MyEvent;
import dateAndTime.utils.dayDate;
import dateAndTime.utils.getTimeThings;
import frames.fragments.CalendarFrame;

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
					lockDrectlyWriteing(dialog);
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

					});
					dialog.setOnDismissListener(new OnDismissListener() 
					{
						@Override
						public void onDismiss(DialogInterface arg0) 
						{
							view.setBackgroundColor(Color.LTGRAY);
						}

					});
					if (allDayFromDialog(dialog)) {
						lockEditInAllDayEvent(dialog);
					}
					dialog.show();
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
			}
		});
	}

	

	public static void initForEvent(Activity ac) 
	{
		a=ac;
		quickActionForEvent = new QuickAction(a);
		ActionItem Delete_Event = new ActionItem(ID_Delete_Event, "Delete Event", a.getResources().getDrawable(android.R.drawable.ic_delete));
		ActionItem Show_details = new ActionItem(ID_Show_details, "Show details", a.getResources().getDrawable(android.R.drawable.btn_default));
		ActionItem Edit_event   = new ActionItem(ID_Edit_event_details, "Edit event details", a.getResources().getDrawable(android.R.drawable.ic_menu_edit));

		quickActionForEvent.addActionItem(Delete_Event);
		quickActionForEvent.addActionItem(Show_details);
		quickActionForEvent.addActionItem(Edit_event);

		//setup the action item click listener
		quickActionForEvent.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() 
		{
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) 
			{
				ActionItem actionItem = quickAction.getActionItem(pos);
				if (actionId == ID_Edit_event_details)
				{
					if (eventToUpdate.getId() == -1) 
					{
						// the event is from the android calander
						view.setBackgroundColor(eventToUpdate.getColor());
						Toast.makeText(a, "Currently you cannot edit external events", Toast.LENGTH_LONG).show();
						return;
					}
					final Dialog dialog = new DialogForInsertEvet(a).buildDialog(eventToUpdate, day);
					lockDrectlyWriteing(dialog);
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

							//Log.e("sql", "id of edit=" + eventToUpdate)

							sql.updateEventById(eventToUpdate.getId(), eventToInsert);
							// inflate fragment with date
							Bundle bundle = new Bundle();
							bundle.putString("month", day.getMonth());
							bundle.putString("year", day.getYear());
							bundle.putString("day", Integer.parseInt(day.getDay())+"");

							// set Fragmentclass Arguments
							CalendarFrame calendarFrame = new CalendarFrame();
							calendarFrame.setArguments(bundle);
							a.getFragmentManager().beginTransaction().replace(R.id.elementContainer, calendarFrame).commit();
							Toast.makeText(a.getApplicationContext(), "Event edited", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}

					});
					dialog.setOnDismissListener(new OnDismissListener() 
					{
						@Override
						public void onDismiss(DialogInterface arg0) 
						{
							view.setBackgroundColor(eventToUpdate.getColor());
						}

					});
					dialog.show();
				}
				else if (actionId == ID_Show_details)
				{
					final Dialog dialog = new DialogForInsertEvet(a).buildDialog(eventToUpdate, day);
					lockAll(dialog);
					Button dialogButton = (Button) dialog.findViewById(R.id.ok_event_dialog);
					dialogButton.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							dialog.dismiss();
						}

					});
					dialog.setOnDismissListener(new OnDismissListener() 
					{
						@Override
						public void onDismiss(DialogInterface arg0) 
						{
							view.setBackgroundColor(eventToUpdate.getColor());
						}

					});
					dialog.show();
				}else {
					if (eventToUpdate.getId() == -1) 
					{
						// the event is from the android calander
						view.setBackgroundColor(eventToUpdate.getColor());
						Toast.makeText(a, "Currently you cannot edit external events", Toast.LENGTH_LONG).show();
						return;
					}
					MyDbDal sql = new MyDbDal(a);
					sql.deleteEventById(eventToUpdate.getId());
					// inflate fragment with date
					Bundle bundle = new Bundle();
					bundle.putString("month", day.getMonth());
					bundle.putString("year", day.getYear());
					bundle.putString("day", Integer.parseInt(day.getDay())+"");

					// set Fragmentclass Arguments
					CalendarFrame calendarFrame = new CalendarFrame();
					calendarFrame.setArguments(bundle);
					a.getFragmentManager().beginTransaction().replace(R.id.elementContainer, calendarFrame).commit();

					view.setBackgroundColor(eventToUpdate.getColor());
					Toast.makeText(a.getApplicationContext(), "Event deleted", Toast.LENGTH_SHORT).show();
				
				}
			}
		});

		quickActionForEvent.setOnDismissListener(new QuickAction.OnDismissListener() 
		{
			@Override
			public void onDismiss() 
			{
				view.setBackgroundColor(eventToUpdate.getColor());
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


	private static String detailsFromDialog(Dialog dialog) 
	{
		EditText details = (EditText)dialog.findViewById(R.id.details);
		return details.getText().toString();
	}
	private static String locationFromDialog(Dialog dialog)
	{
		EditText location = (EditText)dialog.findViewById(R.id.location);
		return location.getText().toString();
	}
	private static boolean allDayFromDialog(Dialog dialog) 
	{
		CheckBox isAllDay = (CheckBox)dialog.findViewById(R.id.all_day_event);				
		return isAllDay.isChecked();
	}
	private static long getBeginFromDialog(Dialog dialog)
	{
		myTimePicker begin = (myTimePicker)dialog.findViewById(R.id.timePicker_begin);
		String h = begin.getHour_display().getText().toString();
		String m = begin.getMin_display().getText().toString();
		if (h.equals("0") && m.equals("0"))
		{
			m="01";
		}
		long ret = getTimeThings.getMillisFromDate(day, h, m);
		//		if (begin.getHour_display().getText().toString().compareTo("0")==0 && begin.getMin_display().getText().toString().compareTo("0")==0) {
		//			ret+=60000;
		//		}
		return ret;
	}
	private static long getEndFromDialog(Dialog dialog) 
	{
		myTimePicker end = (myTimePicker)dialog.findViewById(R.id.timePicker_end);
		return getTimeThings.getMillisFromDate(day, end.getHour_display().getText().toString(),end.getMin_display().getText().toString());
	}
	private static String getTitleFromDialog(Dialog dialog)
	{
		EditText title = (EditText)dialog.findViewById(R.id.title);
		return title.getText().toString();
	}

	protected static void lockAll(Dialog dialog) 
	{
		((myTimePicker)dialog.findViewById(R.id.timePicker_begin)).getHour_plus().setEnabled(false);
		((myTimePicker)dialog.findViewById(R.id.timePicker_begin)).getHour_minus().setEnabled(false);

		((myTimePicker)dialog.findViewById(R.id.timePicker_begin)).getMin_plus().setEnabled(false);
		((myTimePicker)dialog.findViewById(R.id.timePicker_begin)).getMin_minus().setEnabled(false);
		
		((myTimePicker)dialog.findViewById(R.id.timePicker_end)).getHour_plus().setEnabled(false);
		((myTimePicker)dialog.findViewById(R.id.timePicker_end)).getHour_minus().setEnabled(false);
		
		((myTimePicker)dialog.findViewById(R.id.timePicker_end)).getMin_plus().setEnabled(false);
		((myTimePicker)dialog.findViewById(R.id.timePicker_end)).getMin_minus().setEnabled(false);
		
		lockEditInAllDayEvent(dialog);
		EditText title = (EditText)dialog.findViewById(R.id.title);
		EditText details = (EditText)dialog.findViewById(R.id.details);
		EditText location = (EditText)dialog.findViewById(R.id.location);
		title.setEnabled(false);
		details.setEnabled(false);
		location.setEnabled(false);
	}

	public static void lockEditInAllDayEvent(final Dialog dialog) 
	{
		((CheckBox)dialog.findViewById(R.id.all_day_event)).setEnabled(false);
		lockDrectlyWriteing(dialog);		
	}
	
	protected static void lockDrectlyWriteing(Dialog dialog) 
	{
		((myTimePicker)dialog.findViewById(R.id.timePicker_begin)).getHour_display().setEnabled(false);
		((myTimePicker)dialog.findViewById(R.id.timePicker_begin)).getMin_display().setEnabled(false);
		((myTimePicker)dialog.findViewById(R.id.timePicker_end)).getHour_display().setEnabled(false);
		((myTimePicker)dialog.findViewById(R.id.timePicker_end)).getMin_display().setEnabled(false);	
		
	}

}

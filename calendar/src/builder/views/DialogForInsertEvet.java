package builder.views;

import android.app.Dialog;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.e4d6.R;

import dateAndTime.utils.MyEvent;
import dateAndTime.utils.dayDate;
import dateAndTime.utils.getTimeThings;

public class DialogForInsertEvet 
{
	Context c;
	private Dialog dialog ;

	public DialogForInsertEvet(Context c)
	{
		this.c = c;
		dialog = new Dialog(c);
	}
	
	public Dialog buildDialog(MyEvent e, dayDate d)
	{
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(R.layout.add_event_dialog_layout);
		dialog.setTitle("Add Event");
		TextView date_for_day = (TextView) dialog.findViewById(R.id.date_for_day);
		EditText title = (EditText)dialog.findViewById(R.id.title);
		myTimePicker begin = (myTimePicker)dialog.findViewById(R.id.timePicker_begin);
		myTimePicker end = (myTimePicker)dialog.findViewById(R.id.timePicker_end);
		EditText details = (EditText)dialog.findViewById(R.id.details);
		EditText location = (EditText)dialog.findViewById(R.id.location);
		CheckBox isAllDay = (CheckBox)dialog.findViewById(R.id.all_day_event);
		begin.setCurrentTimeFormate(myTimePicker.HOUR_24);
		begin.setAMPMVisible(false);
		end.setCurrentTimeFormate(myTimePicker.HOUR_24);
		end.setAMPMVisible(false);
		date_for_day.setText(d.getDay()+"-"+d.getMonth()+"-"+d.getYear());
		title.setText(e.getTitle());
		begin.getHour_display().setText(Integer.parseInt(getTimeThings.getDate(e.getBegin(),"dd-MM-yyyy HH:mm").split(" ")[1].split(":")[0]) +"" );
		begin.getMin_display().setText(Integer.parseInt(getTimeThings.getDate(e.getBegin(),"dd-MM-yyyy HH:mm").split(" ")[1].split(":")[1]) +"" );
		end.getHour_display().setText(Integer.parseInt(getTimeThings.getDate(e.getEnd(),"dd-MM-yyyy HH:mm").split(" ")[1].split(":")[0]) +"" );
		end.getMin_display().setText(Integer.parseInt(getTimeThings.getDate(e.getEnd(),"dd-MM-yyyy HH:mm").split(" ")[1].split(":")[1]) +"" );
		details.setText(e.getDetails());
		location.setText(e.getLocation());
		isAllDay.setChecked(e.isAllDay());
		
		return dialog;
	}

}

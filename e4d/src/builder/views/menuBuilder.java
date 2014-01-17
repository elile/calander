package builder.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class menuBuilder 
{

	public static SlidingMenu buildMenu(Context context, Activity activity, int layoutToAttach) 
	{
		
		SlidingMenu menu = new SlidingMenu(context);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidth(100);
		menu.setFadeDegree(1f);
		menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
		menu.setBehindWidth(300);
		menu.setBackgroundColor(Color.parseColor("#C7E1FF"));
		menu.setMenu(layoutToAttach);
		
		
		return menu;
	}

}

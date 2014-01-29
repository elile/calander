package dateAndTime.utils;


public class MyEvent 
{
	private String title;
	private long begin;
	private long end;
	private boolean allDay;
	private String location;
	private String details;
	private int color ;
	private long id;

	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}

	private int startNormal=0;
	private int endNormal=0;

	public int getStartNormal() {
		return startNormal;
	}

	public void setStartNormal(int startNormal) {
		this.startNormal = startNormal;
	}

	public int getEndNormal() {
		return endNormal;
	}

	public void setEndNormal(int endNormal) {
		this.endNormal = endNormal;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getBegin() {
		return begin;
	}

	public void setBegin(long begin) {
		this.begin = begin;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDetails() {
		if (!myIsEmpty(details)) {
			return details.replace("\n", " ").replace("\r", " ");
		}else {
			return details;
		}
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public MyEvent(String title, long begin2, long end2, boolean allDay,
			String location, String details, int color) {
		this.title = title;
		this.begin = begin2;
		this.end = end2;
		this.allDay = allDay;
		this.location = location;
		this.details = details;
		this.color = color;
	}

	@Override
	public String toString() 
	{
		// TODO need to delete the \n from getDetails()
		if (isAllDay())
		{
			if (!myIsEmpty(getTitle()) && !myIsEmpty(getDetails()))
				return "Title: "+getTitle()+", Description: "+getDetails()+", Day: "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy");
			else if (  myIsEmpty(getTitle()) && !myIsEmpty(getDetails())   ) 
				return "Description: "+getDetails()+", Day: "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy");
			else if(   !myIsEmpty(getTitle()) && myIsEmpty(getDetails())   )
				return "Title: "+getTitle()+", Day: "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy");
			else 
				return "No Title or Description, Day: "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy");
		}
		else if ((getEndNormal()-getStartNormal())>=180)
		{
			if (!myIsEmpty(getTitle()) && !myIsEmpty(getDetails()))
				return "Title: " + getTitle() + ", Description: "+ getDetails() + "\n"
				+ "Begin: "	+ getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")
				+ "\n" + "End: "+ getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm");
			else if (  myIsEmpty(getTitle()) && !myIsEmpty(getDetails())   ) 
				return "Description: "+ getDetails() + "\n"
				+ "Begin: "	+ getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")
				+ "\n" + "End: "+ getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm");
			else if(   !myIsEmpty(getTitle()) && myIsEmpty(getDetails())   )
				return "Title: " + getTitle()+ "\n" 
				+ "Begin: "	+ getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")
				+ "\n" + "End: "+ getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm");
			else 
				return "Title: No Title or Description"+ "\n" 
				+ "Begin: "	+ getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")
				+ "\n" + "End: "+ getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm");
		}
		else 
		{
			if (!myIsEmpty(getTitle()) && !myIsEmpty(getDetails()))
				//				Log.e("eli", "*** "+getTitle()+"  "+ getDetails()+"  "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")+"  "+getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm"));
				return "Title: "+getTitle()+", Description: "+getDetails()+", Begin: "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")+ ", End: "+ getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm");
			else if (  myIsEmpty(getTitle()) && !myIsEmpty(getDetails())   ) 
				return "Description: "+getDetails()+", Begin: "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")+ ", End: "+ getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm");
			else if(   !myIsEmpty(getTitle()) && myIsEmpty(getDetails())   )
				return "Title: "+getTitle()+", Begin: "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")+ ", End: "+ getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm");
			else 
				return "No Title or Description, Begin: "+getTimeThings.getDate(getBegin(), "dd-MM-yyyy HH:mm")+ ", End: "+ getTimeThings.getDate(getEnd(), "dd-MM-yyyy HH:mm");
		}
	}

	private boolean myIsEmpty(String s)
	{
//		if (s.length()==0) 
//			return true;
		 if(s != null && !s.isEmpty())
			return false;
		else return true;		
	}

}

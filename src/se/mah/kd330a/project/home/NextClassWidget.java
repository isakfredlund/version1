package se.mah.kd330a.project.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.util.Log;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.model.ScheduleItem;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;

public class NextClassWidget {
	ArrayList<ScheduleItem> items;

	private String courseName;
	private String courseId;
	private String location;
	private String date;
	private String startTime;
	private String endTime;
	private String courseName2;
	private String courseId2;
	private String location2;
	private String date2;
	private String startTime2;
	private String endTime2;
	private int nbrOfItems=0;
	private static String TAG = "NextClassWidget";

	public NextClassWidget() {

	}


	public boolean anyClassesToday() {
		if (!Me.getInstance().getUserID().isEmpty()) {
			listEvents();
			if (setData()) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	private void listEvents() {
		items = new ArrayList<ScheduleItem>();
		Collection<?> kronox_events = KronoxCalendar.todaysEvents();
		Log.i(TAG,"Nbr of items in list: "+kronox_events.size());
		if (kronox_events != null) {
			items.clear();
			for (Iterator<?> i = kronox_events.iterator(); i.hasNext();) {
				Component c = (Component) i.next();
				if (c instanceof VEvent) {
					items.add(new ScheduleItem((VEvent) c));
					
				}
			}
		}
	}

	private boolean setData() {
		if (items.size()==1) {
			nbrOfItems=1;
			setLocation(items.get(0).getRoomCode());
			setStartTime(items.get(0).getStartTime());
			setEndTime(items.get(0).getEndTime());
			setDate(items.get(0).getShortWeekDay() + ", " + items.get(0).getDateAndTime2());
			setCourseId(items.get(0).getCourseId());
			setCourseName(Me.getInstance().getCourse(getCourseId()).getDisplaynameEn()); 
			return true;
		} else if (items.size()>1){
			nbrOfItems=2;
			setLocation(items.get(0).getRoomCode());
			setStartTime(items.get(0).getStartTime());
			setEndTime(items.get(0).getEndTime());
			setDate(items.get(0).getShortWeekDay() + ", " + items.get(0).getDateAndTime2());
			setCourseId(items.get(0).getCourseId());
			setCourseName(Me.getInstance().getCourse(getCourseId()).getDisplaynameEn());
			setLocation2(items.get(1).getRoomCode());
			setStartTime2(items.get(1).getStartTime());
			setEndTime2(items.get(1).getEndTime());
			setDate2(items.get(1).getShortWeekDay() + ", " + items.get(1).getDateAndTime2());
			setCourseId2(items.get(1).getCourseId());
			setCourseName2(Me.getInstance().getCourse(getCourseId2()).getDisplaynameEn()); 
			return true;
		}else{	
			Log.e(getClass().toString(), "item list is empty");
			return false;
		}
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
 //Item2
	public String getCourseName2() {
		return courseName2;
	}

	public String getCourseId2() {
		return courseId2;
	}

	public String getLocation2() {
		return location2;
	}

	public String getDate2() {
		return date2;
	}

	public String getStartTime2() {
		return startTime2;
	}

	public String getEndTime2() {
		return endTime2;
	}

	public int getNbrOfItems() {
		return nbrOfItems;
	}
	public void setCourseName2(String courseName2) {
		this.courseName2 = courseName2;
	}

	public void setCourseId2(String courseId2) {
		this.courseId2 = courseId2;
	}

	public void setLocation2(String location2) {
		this.location2 = location2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public void setStartTime2(String startTime2) {
		this.startTime2 = startTime2;
	}

	public void setEndTime2(String endTime2) {
		this.endTime2 = endTime2;
	}

}

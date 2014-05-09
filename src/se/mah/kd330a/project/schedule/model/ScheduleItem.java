package se.mah.kd330a.project.schedule.model;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;

//import se.mah.kd330a.project.schedule.data.KronoxCourse;
//import se.mah.kd330a.project.schedule.data.KronoxJSON;

import android.os.AsyncTask;
import android.util.Log;
import net.fortuna.ical4j.model.component.VEvent;

/*
 *  TODO: 
 *  Kronox gives us a very oddly formatted SUMMARY field. We do not want to show this as is.
 *  So we need to figure out what information we can extract here and categorize it.
 *  You get the summary by the command v.getSummary().getValue() in the constructor below
 *  A typical string can look like:
 *
 *  Coursegrp: KD330A-20132-62311- Sign: K3LARA Description: Project room Activity type: Ok�nd
 *  Programme: VGSJU13h VGSJU13h1 VGSJU13h2 Coursegrp: OM113A-20132-OM113-D16 Sign: HSANMOS Description: �vning Injektionsgivning VP. 16:1 Activity type: Ok�nd
 *  
 *  Can we make it presentable?
 */
public class ScheduleItem implements Serializable {
	/**
	 * C110
	 */
	private static final long serialVersionUID = 2L;
	
	SimpleDateFormat time_format = new SimpleDateFormat("HH:mm", Locale.US);
	SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
	SimpleDateFormat date_format2 = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
	SimpleDateFormat week_day_format = new SimpleDateFormat("EEEE", Locale.US);
	SimpleDateFormat short_week_day_format = new SimpleDateFormat("EE", Locale.US);

	private String startTime;
	private String endTime;
	private String weekDay;
	private String shortWeekDay;
	private String dateAndTime1;
	private String dateAndTime2;
	private String location;
	private String courseName;
	private String teacherID;
	private String description;
	private boolean isDivider = false;
	
	
	//Needed for color coordinating
		private String courseID = "";

	// text += "Summary:" + v.getSummary().getValue() + "\n";
	// text += "Last modified:" +
	// date_format.format(v.getLastModified().getDate()) + "\n";
	public ScheduleItem(VEvent v) {
		startTime = time_format.format(v.getStartDate().getDate());
		weekDay = week_day_format.format(v.getStartDate().getDate());
		shortWeekDay = short_week_day_format.format(v.getStartDate().getDate());
		dateAndTime2 = date_format2.format(v.getStartDate().getDate());

		endTime = time_format.format(v.getEndDate().getDate());
		location = v.getLocation().getValue();
		courseName = "PlaceHolder";
		String summary = v.getSummary().getValue();
		
		if (summary.indexOf("Coursegrp: ") != -1) {
			courseName = summary.substring(summary.indexOf("Coursegrp: ") + 11,
					summary.indexOf("Coursegrp: ") + 29);
			
			
			courseID = summary.substring(summary.indexOf("Coursegrp: ") + 11,
					summary.indexOf("Coursegrp: ") + 17); 
			
		}
		if (summary.indexOf("Sign: ") != -1) {
			teacherID = summary.substring(summary.indexOf("Sign: ") + 6,
					summary.indexOf("Sign: ") + 12);
		}
		if (summary.indexOf("Description: ") != -1){
			description = summary.substring(summary.indexOf("Description: ") + 13,
					summary.indexOf("Activity type: "));
		}
		//Log.i("ScheduleSummary",v.getSummary().getValue());
		//Log.i("Description: ",description);
	}

	public String getDescription() {
		return description;
	}

	public boolean isDividerElement() {
		return isDivider;
	}

	public void setDividerElement() {
		isDivider = true;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getRoomCode() {
		return location;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getCourseName() {
		return courseName;
	}

	public String getTeacherID() {
		return teacherID;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public String getShortWeekDay() {
		return shortWeekDay;
	}
	
	public String getDateAndTime2() {
		return dateAndTime2;
	}
	
	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	
}

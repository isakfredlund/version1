package se.mah.kd330a.project.profile.view;

import java.util.ArrayList;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.adladok.model.Course;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.framework.MainActivity;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

@SuppressLint("ResourceAsColor")
public class FragmentProfile extends android.support.v4.app.Fragment{
	SharedPreferences sharedPref;
	private String profilName;
	private String profilId;
	private ArrayList<Course> myCourses;
	TextView profilNameTextView;
	TextView profilIdTextView;
	LinearLayout coursesContent;
	TextView currentCourse;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater
				.inflate(R.layout.activity_settings, container, false);
		
		if (Me.getInstance().getUserID() != null) {
			profilName = Me.getInstance().getDispayName();
			profilId = Me.getInstance().getUserID();
			myCourses = (ArrayList<Course>) Me.getInstance().getCourses();
			profilNameTextView = (TextView)rootView.findViewById(R.id.profil_name);
			profilIdTextView = (TextView)rootView.findViewById(R.id.profil_id);
			profilNameTextView.setText(profilName);
			profilIdTextView.setText(profilId);
			coursesContent = (LinearLayout)rootView.findViewById(R.id.my_courses_content);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			for (Course course : myCourses) {
				currentCourse = new TextView(getActivity());
				currentCourse.setText(course.getDisplaynameSv());
				currentCourse.setLayoutParams(params);
				currentCourse.setPadding(12, 5, 12, 5);
				currentCourse.setTextColor(R.color.grey_dark);
				currentCourse.setTextSize(14);
				coursesContent.addView(currentCourse);
			}
		} else {
			profilNameTextView = (TextView)rootView.findViewById(R.id.profil_name);
			profilNameTextView.setText("No profile");
		}
		
		MainActivity.mDrawerLayout.closeDrawer(MainActivity.mDrawerList);
		return rootView;
	}
	
}

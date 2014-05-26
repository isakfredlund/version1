package se.mah.kd330a.project.profile.view;

import java.util.ArrayList;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.StartActivity;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.adladok.model.Course;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class SettingsActivity extends Activity {
	SharedPreferences sharedPref;
	private String profilName;
	private String profilId;
	private ArrayList<Course> myCourses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		addProfilInformation();
	}

	@SuppressLint("ResourceAsColor")
	private void addProfilInformation() {
		if (Me.getInstance().getUserID() != null) {
			profilName = Me.getInstance().getDispayName();
			profilId = Me.getInstance().getUserID();
			myCourses = (ArrayList<Course>) Me.getInstance().getCourses();
			TextView profilNameTextView = (TextView) findViewById(R.id.profil_name);
			TextView profilIdTextView = (TextView) findViewById(R.id.profil_id);
			profilNameTextView.setText(profilName);
			profilIdTextView.setText(profilId);
			LinearLayout coursesContent = (LinearLayout) findViewById(R.id.my_courses_content);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			for (Course course : myCourses) {
				TextView currentCourse = new TextView(this);
				currentCourse.setText(course.getDisplaynameSv());
				currentCourse.setLayoutParams(params);
				currentCourse.setPadding(12, 5, 12, 5);
				currentCourse.setTextColor(R.color.grey_dark);
				currentCourse.setTextSize(14);
				coursesContent.addView(currentCourse);
			}
		} else {
			TextView profilNameTextView = (TextView) findViewById(R.id.profil_name);
			profilNameTextView.setText("No profile");
		}
	}
	
	// Actionbar "Profile" icon and menu. Settings main.xml
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate doubble the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	// This button on standby.
	public void toLadokInlogg(View view) {
		//Clear all including backstack
		Me.getInstance().clearAllIncludingSavedData(this);
		Intent intent = new Intent(this, StartActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

}

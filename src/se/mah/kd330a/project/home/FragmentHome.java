package se.mah.kd330a.project.home;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import net.fortuna.ical4j.data.ParserException;
import se.mah.kd330a.project.adladok.model.Constants;
import se.mah.kd330a.project.adladok.model.Course;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.adladok.model.ScheduleFixedDelay.UpdateType;
import se.mah.kd330a.project.framework.MainActivity;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import se.mah.kd330a.project.home.data.RSSFeed;
import se.mah.kd330a.project.itsl.Article;
import se.mah.kd330a.project.itsl.FeedManager;
import se.mah.kd330a.project.itsl.ListPagerAdapter;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import se.mah.kd330a.project.schedule.view.FragmentScheduleWeekPager;
import se.mah.kd330a.project.R;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


// vattenmelon

public class FragmentHome extends Fragment implements FeedManager.FeedManagerDoneListener, Observer
{

	private NextClassWidget nextClass;
	private ViewGroup rootView;
	private RSSFeed newsFeed;
	private ObjectInputStream in = null;
	private FileInputStream fis = null;
	private boolean profileRegistered = false;
	private FeedManager ITSLfeedManager;
	private String TAG ="FragmentHome";
	private String actionBarTitle[];
	
	public FragmentHome()
	{
		//Hej Hej Hem!
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i("FragmentHome", "OnCreate: ");
		super.onCreate(savedInstanceState);
		Me.getInstance().getObservable().addObserver(this);
        try {
			KronoxCalendar.createCalendar(KronoxReader.getFile(getActivity().getApplicationContext()));
		} catch (Exception e) {
			Log.e("FragmentHome", e.toString());
		} 
		try
		{
			nextClass = new NextClassWidget();
			profileRegistered = nextClass.anyClassesToday();
		}
		catch (Exception e)
		{
			Log.e("FragmentHome", "OnCreate: "+e.toString());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.i("FragmentHome", "OnCreateView: ");
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_home, container, false);
		setNextKronoxClass(rootView);
		setNewsFeedMah(rootView);
		ITSLfeedManager = new FeedManager(this, getActivity().getApplicationContext());
		//ITSLfeedManager.getFeedList().size()
		Log.i(TAG,"ITSLfeedManager.getFeedList().size()" + ITSLfeedManager.getFeedList().size());
		if (!ITSLfeedManager.loadCache())
		{
			ITSLfeedManager.reset();
			ITSLfeedManager.processFeeds();
		}
		actionBarTitle = getResources().getStringArray(R.array.menu_texts);
		getActivity().getActionBar().setTitle(actionBarTitle[0]); //Fetches "Home" from the string array
		//Perhaps or all should be done in ScheduledFixedDelay????
		return rootView;
	}

	private void setNewsFeedMah(ViewGroup rootView)
	{
		Log.i(TAG,"setNewsFeedMah: ");
		try
		{
			fis = getActivity().openFileInput(Constants.mahNewsSavedFileName);
			in = new ObjectInputStream(fis);
			newsFeed = (RSSFeed) in.readObject();
			in.close();
			fis.close();
			Log.i(TAG, "Items in MAHNews feed: "+ Integer.toString(newsFeed.getItemCount()));
		}
		catch (Exception ex)
		{
			Log.e(TAG, "Error in get method");
		}

		try
		{
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			LinearLayout newsFeedMahWidget = (LinearLayout) rootView.findViewById(R.id.news_feed_widget);
			for (int i = 0; i < 1; i++)
			{
				TextView title = (TextView) newsFeedMahWidget.findViewById(R.id.text_latest_news_heading);
				title.setText(newsFeed.getItem(i).getTitle());
				TextView description = (TextView) newsFeedMahWidget.findViewById(R.id.text_latest_news_description);
				description.setText(newsFeed.getItem(i).getDescription());

			}
		}
		catch (Exception ex)
		{
			Log.e(TAG, "Error in get method");
		}

	}

	private void setNextKronoxClass(ViewGroup rootView)
	{
		
		LinearLayout nextClassWidget = (LinearLayout) rootView.findViewById(R.id.next_class_widget);
		nextClassWidget.setVisibility(LinearLayout.VISIBLE);
		String courseName = nextClass.getCourseName();
		String courseID = nextClass.getCourseId();
		
		if (profileRegistered)
		{
			TextView textNextClassName = (TextView) nextClassWidget.findViewById(R.id.text_next_class_name);
			textNextClassName.setText(courseName);
			TextView textNextClassDate = (TextView) nextClassWidget.findViewById(R.id.text_next_class_date);
			textNextClassDate.setText(nextClass.getDate());
			TextView textNextClassStartTime = (TextView) nextClassWidget.findViewById(R.id.text_next_class_start_time);
			textNextClassStartTime.setText(nextClass.getStartTime());
			TextView textNextClassEndTime = (TextView) nextClassWidget.findViewById(R.id.text_next_class_end_time);
			textNextClassEndTime.setText(nextClass.getEndTime());
			TextView textNextClassLocation = (TextView) nextClassWidget.findViewById(R.id.text_next_class_location);
			textNextClassLocation.setText(nextClass.getLocation());			
		
			View scheduleColor1 = (View) nextClassWidget.findViewById(R.id.home_schedule1);
			View scheduleColor2 = (View) nextClassWidget.findViewById(R.id.home_schedule2);
			if (Me.getInstance().getCourse(courseID)!= null){
				scheduleColor1.setBackgroundColor(Me.getInstance().getCourse(courseID).getColor());
				scheduleColor2.setBackgroundColor(Me.getInstance().getCourse(courseID).getColor());
			}else{
				scheduleColor1.setBackgroundColor(getResources().getColor(R.color.red_mah));
				scheduleColor2.setBackgroundColor(getResources().getColor(R.color.red_mah));
			}
		}
		else
		{
			//nextClassWidget.setVisibility(LinearLayout.GONE);
			//TextView textNextClassDate = (TextView) nextClassWidget.findViewById(R.id.text_next_class_date);
			//textNextClassDate.setText("No classes found updating....");
		}

	}

//ITSL
	@Override
	public void onFeedManagerDone(FeedManager fm, ArrayList<Article> articles)
	{
		try
		{
			//View widget = (View)rootView.findViewById(R.id.itslearning_widget);
			View widget = rootView;
			Article a = articles.get(0);
			int start = a.getArticleCourseCode().indexOf(" - ");
			String courseName="";
			try{
				courseName = a.getArticleCourseCode().substring(start+3,a.getArticleCourseCode().length()-1);
			}catch  (Exception e){
				Log.i(TAG,e.getMessage());
			}
			Log.i(TAG,"CourseName:"+ courseName+":");
			int color = this.getResources().getColor(R.color.red_mah);
			for (Course c : Me.getInstance().getCourses())
			{
				Log.i(TAG,"course::"+c.getDisplaynameSv()+ "::AcourseNAME::"+courseName);
				if (c.getDisplaynameSv().contains(courseName)||c.getDisplaynameEn().contains(courseName)){
					Log.i(TAG," Color" + c.getColor()+ "course"+c.getDisplaynameSv()+ " Artcode "+a.getArticleCourseCode());
					color=c.getColor();
					break;
				}
			}	
			((TextView)widget.findViewById(R.id.text_itsl_title)).setText(a.getArticleHeader());
			((TextView)widget.findViewById(R.id.text_itsl_date)).setText(a.getArticleDate());
			((TextView)widget.findViewById(R.id.text_itsl_content)).setText(a.getArticleText());
			((View)widget.findViewById(R.id.home_itsl1)).setBackgroundColor(color);
			((View)widget.findViewById(R.id.home_itsl2)).setBackgroundColor(color);
		}
		catch(Exception e)
		{
			Log.e(TAG, "onFeedManagerDone(): " + e.toString());
		}
	}

	@Override
	public void onFeedManagerProgress(FeedManager fm, int progress, int max)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable observable, Object data) {
		UpdateType type= (UpdateType)data;
		Log.i(TAG,"updated with data: "+type);
		switch(type){
		case KRONOX:
			getActivity().runOnUiThread(new Runnable(){
				@Override
				public void run() {
					profileRegistered = nextClass.anyClassesToday();
					if (profileRegistered){
						setNextKronoxClass(rootView);
					}	
				}
				
			});
			
			break;
		case MAHNEWS:
			getActivity().runOnUiThread(new Runnable(){
				@Override
				public void run() {
					setNewsFeedMah(rootView);
				}
				
			});
			break;
		default:
			break;
		}
		
	}
// Sneaky �ndring av Andr� Stenberg.
//Meeeeh! av Sandra P
	//A word for a bird av Oskar
	// Test1 ISak
	//BatBacon!!! 
	//le meow
}

package se.mah.kd330a.project.home;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentHome extends Fragment implements FeedManager.FeedManagerDoneListener, Observer
{
	ImageView InstagramView;
	private NextClassWidget nextClass;
	private ViewGroup rootView;
	private RSSFeed newsFeed;
	private ObjectInputStream in = null;
	private FileInputStream fis = null;
	private boolean profileRegistered = false;
	private FeedManager ITSLfeedManager;
	private String TAG ="FragmentHome";
	private String actionBarTitle[];
	private boolean nbrOfClasses;
	/*private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/"; //Used for Authentication.
	private static final String TOKENURL ="https://api.instagram.com/oauth/access_token=42571979.1fb234f.485cfc1c30fc42ea995f0901967e76e9 HTTP/1.1";//Used for getting token and User details.
	public static final String APIURL = "https://api.instagram.com/v1/tags/MAHStudent/media/recent";//Used to specify the API version which we are going to use.
	public String CALLBACKURL = "ig459cd68d82d642bfb73678ceb77290cd://authorize";//The callback url that we have used while registering the application.
	public String client_id = "459cd68d82d642bfb73678ceb77290cd";
	public String client_secret = "7a0733d8b4f64d4eb0a5e0394cae5074";
	public String token = "42571979.1fb234f.485cfc1c30fc42ea995f0901967e76e9";*/

	
	public FragmentHome()
	{}
		   
  
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
			//Sets profileregistered to true if there are things in the calendar not very logical at all rename??
			//Also creates the nextClass widget with correct info about first event
			profileRegistered = nextClass.anyClassesToday(); 
		}
		catch (Exception e)
		{
			Log.e("FragmentHome", "OnCreate: "+e.toString());
		}
		// Kör denna kod från någon life-cycle metod, t.ex. onCreate eller onActivityCreated
		HTTPWorker test = new HTTPWorker(getActivity(), mHandler, HTTPWorker.GET_LATEST_TAGS, true);
		test.execute("MAHstudent");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.i("FragmentHome", "OnCreateView: ");
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_home, container, false);
		setNextKronoxClass(rootView); //ok here is the stuff 
		//setNewsFeedMah(rootView);
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
		InstagramView = (ImageView) rootView.findViewById(R.id.instagramview);
		return rootView;
	}
	// Deklarera i ert fragment
	Handler mHandler = new Handler(new InstagramHandlerCallback());

	class InstagramHandlerCallback implements Handler.Callback {
		@Override
		public boolean handleMessage(Message msg) {
			ArrayList<InstagramTag> tags = (ArrayList<InstagramTag>) msg
					.getData().getSerializable("data");
			InstagramTag tag = tags.get(0);
			String imgUrl = tag.getStandardResolution();
			new DownloadImageTask(InstagramView).execute(imgUrl);
			return true;
		}
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}


	/**private void setNewsFeedMah(ViewGroup rootView)
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

	}**/

	private void setNextKronoxClass(ViewGroup rootView)
	{
		
		LinearLayout nextClassWidget = (LinearLayout) rootView.findViewById(R.id.next_class_widget);
		nextClassWidget.setVisibility(LinearLayout.VISIBLE); //The visibility can be used here....
		LinearLayout nextClassWidget2 = (LinearLayout) rootView.findViewById(R.id.next_class_widget_2);
		nextClassWidget.setVisibility(LinearLayout.INVISIBLE); //The visibility can be used here....
		//String courseName = nextClass.getCourseName();
		//String courseID = nextClass.getCourseId();
		
		if (profileRegistered)
		{
			if (nextClass.getNbrOfItems()>0){
				nextClassWidget.setVisibility(LinearLayout.VISIBLE);
				TextView textNextClassName = (TextView) nextClassWidget.findViewById(R.id.text_next_class_name);
				textNextClassName.setText(nextClass.getCourseName());
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
				String courseID = nextClass.getCourseId();
				if (Me.getInstance().getCourse(courseID)!= null){
					scheduleColor1.setBackgroundColor(Me.getInstance().getCourse(courseID).getColor());
					scheduleColor2.setBackgroundColor(Me.getInstance().getCourse(courseID).getColor());
				}else{
					scheduleColor1.setBackgroundColor(getResources().getColor(R.color.red_mah));
					scheduleColor2.setBackgroundColor(getResources().getColor(R.color.red_mah));
				}
			}
			if(nextClass.getNbrOfItems()>1){
				nextClassWidget2.setVisibility(LinearLayout.VISIBLE);
				TextView textNextClassName = (TextView) nextClassWidget2.findViewById(R.id.text_next_class_name_2);
				textNextClassName.setText(nextClass.getCourseName2());
				TextView textNextClassDate = (TextView) nextClassWidget2.findViewById(R.id.text_next_class_date_2);
				textNextClassDate.setText(nextClass.getDate2());
				TextView textNextClassStartTime = (TextView) nextClassWidget2.findViewById(R.id.text_next_class_start_time_2);
				textNextClassStartTime.setText(nextClass.getStartTime2());
				TextView textNextClassEndTime = (TextView) nextClassWidget2.findViewById(R.id.text_next_class_end_time_2);
				textNextClassEndTime.setText(nextClass.getEndTime2());
				TextView textNextClassLocation = (TextView) nextClassWidget2.findViewById(R.id.text_next_class_location_2);
				textNextClassLocation.setText(nextClass.getLocation2());			
		
				
				View scheduleColor1 = (View) nextClassWidget2.findViewById(R.id.home_schedule1_2);
				View scheduleColor2 = (View) nextClassWidget2.findViewById(R.id.home_schedule2_2);
				String courseID = nextClass.getCourseId2();
				if (Me.getInstance().getCourse(courseID)!= null){
					scheduleColor1.setBackgroundColor(Me.getInstance().getCourse(courseID).getColor());
					scheduleColor2.setBackgroundColor(Me.getInstance().getCourse(courseID).getColor());
				}else{
					scheduleColor1.setBackgroundColor(getResources().getColor(R.color.red_mah));
					scheduleColor2.setBackgroundColor(getResources().getColor(R.color.red_mah));
				}
			}
		}
		
			
			
		
		if (nbrOfClasses) {
			nextClassWidget.setVisibility(LinearLayout.GONE);
			TextView textNextClassDate = (TextView) nextClassWidget.findViewById(R.id.text_next_class_date);
			textNextClassDate.setText("No classes found updating....");
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
			//((TextView)widget.findViewById(R.id.text_itsl_title)).setText(a.getArticleHeader());
			/*((TextView)widget.findViewById(R.id.text_itsl_date)).setText(a.getArticleDate());*/
			//((TextView)widget.findViewById(R.id.text_itsl_content)).setText(a.getArticleText());
			/*((View)widget.findViewById(R.id.home_itsl1)).setBackgroundColor(color);*/
			//((View)widget.findViewById(R.id.home_itsl2)).setBackgroundColor(color);
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
		/**case MAHNEWS:
			getActivity().runOnUiThread(new Runnable(){
				@Override
				public void run() {
					setNewsFeedMah(rootView);
				}
				
			});
			break;**/
		default:
			break;
		}
		
	}
}

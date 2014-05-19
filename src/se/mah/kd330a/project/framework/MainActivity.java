package se.mah.kd330a.project.framework;

<<<<<<< HEAD
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.fortuna.ical4j.data.ParserException;
import se.mah.kd330a.project.R;
import se.mah.kd330a.project.StartActivity;
import se.mah.kd330a.project.adladok.model.Course;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.adladok.model.ScheduleFixedDelay.UpdateType;
=======


import se.mah.kd330a.project.R;
import se.mah.kd330a.project.adladok.model.Constants;
>>>>>>> c9a5e0dbd40598a724716082ee91b9fd708de2ae
import se.mah.kd330a.project.faq.FragmentFaq;
import se.mah.kd330a.project.find.FragmentFind;
import se.mah.kd330a.project.help.FragmentCredits;
import se.mah.kd330a.project.home.FragmentHome;
import se.mah.kd330a.project.home.data.RSSFeed;
import se.mah.kd330a.project.itsl.FragmentITSL;
<<<<<<< HEAD
import se.mah.kd330a.project.links.FragmentLinks;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
=======
>>>>>>> c9a5e0dbd40598a724716082ee91b9fd708de2ae
import se.mah.kd330a.project.schedule.view.FragmentScheduleWeekPager;
import se.mah.kd330a.project.settings.view.FragmentProfile;
import se.mah.kd330a.project.settings.view.SettingsActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

<<<<<<< HEAD
public class MainActivity extends FragmentActivity implements Observer{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String urlNewsFeed = "http://www.mah.se/english/News/";
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;
    private TypedArray mMenuIcons;
    private TypedArray mMenuColors;
    public RSSFeed newsFeed;
    private final String TAG = "MainActivity";
    private final int HOME = 0;
	private final int SCHEDULE = 1;
	private final int PROFILE = 2;
	private final int ITSL = 3;
	private final int FIND = 4;
	private final int FAQ = 5;
	private final int HELP = 6;
	private final int LOGOUT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Me.getInstance().startUpdate(this);
        Me.getInstance().getObservable().addObserver(this); //register....
        mTitle = mDrawerTitle = getTitle();
        mMenuTitles = getResources().getStringArray(R.array.menu_texts);
        mMenuIcons = getResources().obtainTypedArray(R.array.menu_icons);
        mMenuColors = getResources().obtainTypedArray(R.array.menu_colors);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mDrawerList.setSelector(R.drawable.menu_selector);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new MenuAdapter(this,
                R.layout.drawer_list_item, mMenuTitles, mMenuIcons, mMenuColors));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	Log.i(TAG,"OnPause");
    	//Me.saveMeToLocalStorage(this);
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	Log.i(TAG,"OnResume");
    	 //Me.getInstance().startUpdate(this);
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	Log.i(TAG,"onDestroy Scheduled updater thread stopped");
    	 Me.getInstance().stopUpdate();
    }
    
    public RSSFeed getRssNewsFeed() {
    	return newsFeed;
    } 
    
   /* The overflow menu has been removed bellow: 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    /* Called whenever we call invalidateOptionsMenu() */
 
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar HOME/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_help:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    // Meny till v�nster, �ndra h�r f�r l�gga till logut knappen.
    public void selectItem(int position) {
        // update the main content by replacing fragments
    	android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    	fragmentManager. popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
    	android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
    	Fragment fragment;
    	switch (position) {
=======
public class MainActivity extends FragmentActivity {

	/**
	 * Activity that starts after the StartActivity has finished logging in. 
	 * Holds the "main" app.
	 */

	private DrawerLayout 			mDrawerLayout;
	private ListView 				mDrawerList;
	private ActionBarDrawerToggle 	mDrawerToggle;
	private CharSequence 			mDrawerTitle;
	private CharSequence 			mTitle;
	private String[] 				mMenuTitles;
	private TypedArray 				mMenuIcons;
	private TypedArray 				mMenuColors;
	public RSSFeed 					mNewsFeed;
	private final String 			TAG = MainActivity.class.getName();
	private final int 				HOME = 0;
	private final int 				SCHEDULE = 1;
	private final int 				ITSL = 2;
	private final int 				FIND = 3;
	private final int 				FAQ = 4;
	private final int 				HELP = 5;
	private int 					refreshCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mMenuTitles = getResources().getStringArray(R.array.menu_texts);
		mMenuIcons = getResources().obtainTypedArray(R.array.menu_icons);
		mMenuColors = getResources().obtainTypedArray(R.array.menu_colors);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mDrawerList.setSelector(R.drawable.menu_selector);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new MenuAdapter(this,
				R.layout.drawer_list_item, mMenuTitles, mMenuIcons, mMenuColors));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);


		/** 
		 * The fragment to start in.
		 */
		if (savedInstanceState == null) {
			selectItem(0);
		}
	}


	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG,"OnPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG,"OnResume");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public RSSFeed getRssNewsFeed() {
		return mNewsFeed;
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {
		case R.id.action_help:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	public void selectItem(int position) {
		// update the main content by replacing fragments
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager. popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
		android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment;
		switch (position) {
>>>>>>> c9a5e0dbd40598a724716082ee91b9fd708de2ae
		case HOME:	
			fragment = new FragmentHome();
			break;
		case SCHEDULE:
			fragment = new FragmentScheduleWeekPager();
			transaction.addToBackStack(null);
			break;
		case ITSL:
			fragment = new FragmentITSL();
			transaction.addToBackStack(null);
			break;
		case FIND:
			fragment = new FragmentFind();
			transaction.addToBackStack(null);
			break;
		case PROFILE:
			fragment = new FragmentProfile();
			transaction.addToBackStack(null);
			break;
		case FAQ:
			fragment = new FragmentFaq();
			transaction.addToBackStack(null);
			break;
		case HELP:
			fragment = new FragmentCredits();
			transaction.addToBackStack(null);
			break;
		case LOGOUT:
			fragment = new FragmentLogout();
			transaction.addToBackStack(null);
			loggout();
			break;
		default:	
			fragment = new FragmentHome();
		}
<<<<<<< HEAD
    	transaction.replace(R.id.content_frame, fragment);
    	transaction.commit();
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    public void loggout(){
        Me.getInstance().clearAllIncludingSavedData(this);
        Me.getInstance().stopUpdate();
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    public void toSchedule(View view) {
=======
		transaction.replace(R.id.content_frame, fragment);
		transaction.commit();
		refreshCheck = position;
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	/**
	 * Method for refreshing the currently viewed fragment. 
	 * Use with 
	 * Me.getInstance().startRefresher(FragmentCallback fragmentCallback, Context context) {}
	 * to refresh current fragment.
	 */
	public void refreshCurrent(){
		Fragment fragment = null;
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (refreshCheck) {
		case HOME:	
			fragment = new FragmentHome();
			break;
		case SCHEDULE:
			fragment = new FragmentScheduleWeekPager();
			transaction.addToBackStack(null);
			break;
		case ITSL:
			fragment = new FragmentITSL();
			transaction.addToBackStack(null);
			break;
		case FIND:
			fragment = new FragmentFind();
			transaction.addToBackStack(null);
			break;
		case FAQ:
			fragment = new FragmentFaq();
			transaction.addToBackStack(null);
			break;
		case HELP:
			fragment = new FragmentCredits();
			transaction.addToBackStack(null);
			break;
		}
		transaction.replace(R.id.content_frame, fragment, "FRAGMENT");
		transaction.commit();
		Log.i("Refresh","Refreshed");

	}
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void toSchedule(View view) {
>>>>>>> c9a5e0dbd40598a724716082ee91b9fd708de2ae
		selectItem(this.SCHEDULE);
	}

	public void toITSL(View view) {
		selectItem(this.ITSL);
	}

	public void toFind(View view) {
		selectItem(this.FIND);
	}

<<<<<<< HEAD
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		Log.i(TAG,"Called from updater with data: "+(UpdateType)data);
		switch ((UpdateType)data){
			case KRONOX:
				Log.i(TAG,"Data: KRONOX");
			break;
			case COURSES_and_AD:
				Log.i(TAG,"Data: COURSES");
			break;
			case MAHNEWS:
				Log.i(TAG,"Data: MAHNEWS");
			break;
			case ALL:
				Log.i(TAG,"Data: ALL");
			break;
			default:
			break;
		
		}
=======
	public void toNewsFeedOnWeb(View view) {
		Uri uri = Uri.parse(Constants.URL_NEWS_FEED);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW,
				uri);
		startActivity(launchBrowser);
>>>>>>> c9a5e0dbd40598a724716082ee91b9fd708de2ae
	}
}


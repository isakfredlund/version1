	package se.mah.kd330a.project;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import se.mah.kd330a.project.adladok.model.Constants;
import se.mah.kd330a.project.adladok.model.FragmentCallback;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.adladok.model.Refresh;
import se.mah.kd330a.project.framework.MainActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.EditText;

public class StartActivity extends Activity {
	/**
	 * This is the activity that starts before the Main Activity.
	 * It logs in the user and refreshes the schedule.
	 */
	
	
	// Change this to false if startActivity should refresh
	private boolean debugMode = false;
	
	private final String 	TAG = "StartActivity";
	private EditText 		mEditTextUsername;
	private EditText 		mEditTextPassword;
	boolean 				mRestoredSuccess = false;
	boolean 				mLoginOK = false;
	private String 			mUsername;
	private String 			mPassword;
	private static AsyncTask<String, Void, Integer> asyncLoginTask;
	private enum LOGINMESSAGE {SHOW,NOSHOW};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		((LinearLayout) findViewById(R.id.login_view)).setVisibility(LinearLayout.GONE);
		((LinearLayout) findViewById(R.id.loading_view)).setVisibility(LinearLayout.GONE);
		
		// This checks if the user is already saved on local storage.
		mRestoredSuccess = Me.getInstance().restoreMeFromLocalStorage(this);
		
		//Check if password changed
		Log.i(TAG,"restoredsuccess: "+mRestoredSuccess);
		if(mRestoredSuccess){
			mUsername = Me.getInstance().getUserID();
			mPassword = Me.getInstance().getPassword();
			hideLoginView();
		}else{
			showLoginView(LOGINMESSAGE.NOSHOW);
		}
	}

	// Shows the login view. Toasts if the credentials are wrong.
	public void showLoginView(LOGINMESSAGE loginmessage) {
		switch(loginmessage){
		case SHOW:
			//Toast.makeText(this, "Unable to log you in.", Toast.LENGTH_LONG).show();
			break;
		case NOSHOW:
			break;
		default:
			break;
		}
		((View) findViewById(R.id.loading_view)).setVisibility(View.GONE);
		((View) findViewById(R.id.login_view)).setVisibility(View.VISIBLE);
		mEditTextUsername = (EditText) findViewById(R.id.editText1);
		mEditTextPassword = (EditText) findViewById(R.id.editText2);
		mEditTextUsername.setText(Me.getInstance().getUserID());
		
		// For testing, set default test user.
		mEditTextUsername.setText("testUser1");
		mEditTextPassword.setText("testUser1");


	}

	
	// Hides the login view.
	public void hideLoginView() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow((IBinder) findViewById(R.id.login_view).getWindowToken(), 0);
		((View) findViewById(R.id.login_view)).setVisibility(View.GONE);
		((View) findViewById(R.id.loading_view)).setVisibility(View.VISIBLE);
		asyncLoginTask = new AsyncTaskLoginUser(this).execute(mUsername,mPassword,"hide"); 
	}

	// Get input and show loading screen on click.
	public void loginButtonClicked(View v) {
		mUsername = mEditTextUsername.getText().toString();
		mPassword = mEditTextPassword.getText().toString();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow((IBinder) findViewById(R.id.login_view).getWindowToken(), 0);
		((View) findViewById(R.id.login_view)).setVisibility(View.GONE);
		((View) findViewById(R.id.loading_view)).setVisibility(View.VISIBLE);
		asyncLoginTask = new AsyncTaskLoginUser(this).execute(mUsername,mPassword,"click");
	}
	
	/**
	 * AsyncTask handling login on another thread
	 */
	private class AsyncTaskLoginUser extends AsyncTask<String, Void, Integer> {
	 	private Activity mActivity;

	    public AsyncTaskLoginUser(Activity activity){
	    	this.mActivity = activity; 
	    }
	    

		@Override
	    protected Integer doInBackground(String... params) {
	    	Integer result = 0; //error;
	    	Log.i(TAG,"Starting Login");
	    	
	    	
	        // Get the info from web service
	    	try {
	        	SoapObject loginrequest = new SoapObject(Constants.NAMESPACE, "logInTheUser");
	            loginrequest.addProperty("username", params[0]);
	            loginrequest.addProperty("password", params[1]);
	            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            envelope.dotNet = true;
	            envelope.setOutputSoapObject(loginrequest);
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL);
	            androidHttpTransport.call(Constants.NAMESPACE + "logInTheUser", envelope);
	            String rawResult = ((Object)envelope.getResponse()).toString();
	            
	            // Log.d(TAG,"Loginresult "+rawResult);
	            boolean loggedIn = rawResult.equals("true")?true:false;
	            if (loggedIn&&params[2].equals("hide")){
	            	result = 1;
	            } else if  (loggedIn&&params[2].equals("click")){
	            	result = 2;
	            }
	            
	        	} catch (Exception e) {
	        		Log.e(TAG,"LoginError: not logged in... "+e.getMessage());
	        	}
	    	return result;
	    }
	    
	    @Override
	    protected void onPostExecute(Integer result) {
	        super.onPostExecute(result);
	        String resultCode = "";
	        switch(result){
	        	case 1: //hide
	        		tasksCompleted(mActivity);
	        		resultCode = "From saved data.";
	        		break;
	        	case 2: //click
	        		Me.getInstance().setUserID(mUsername);
	        		Me.getInstance().setPassword(mPassword);
	    			tasksCompleted(mActivity);
	    			resultCode = "From screen.";
	        		break;
	        	default:
	        		resultCode = "Problem.";
	        		showLoginView(LOGINMESSAGE.SHOW); 			
	        		break;
	        }
	        
	        Log.i(TAG,"Logged in with result: " + resultCode);
	    }
	}

	/**
	 * When all tasks have completed we can go on to the MainActivity
	 */
	public void tasksCompleted(final Activity activity) {
		if(!mRestoredSuccess){
		if(!debugMode){
			Me.getInstance().startRefresher(new FragmentCallback() {
				@Override
				public void onRefreshCompleted() {
					Intent intent = new Intent(activity, MainActivity.class);
					startActivity(intent);
					finish();
				}
	        	
	        }, this);
		} else {
			Intent intent = new Intent(activity, MainActivity.class);
			startActivity(intent);
			finish();
		}
		}else{
			Intent intent = new Intent(activity, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.i(TAG, "finish(): destroying StartActivity now");
	}
}

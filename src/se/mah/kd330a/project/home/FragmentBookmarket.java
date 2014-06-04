package se.mah.kd330a.project.home;

import java.util.Set;

import se.mah.kd330a.project.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class FragmentBookmarket extends Fragment //implements OnClickListener
{
   View new_ad;
   View search;
   
   public FragmentBookmarket () {
	}


   
   @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 	
	    ViewGroup rootView = (ViewGroup) inflater
       .inflate(R.layout.bookmarket_fragment, container, false);
	    
	    final Button searchButton = (Button) rootView.findViewById(R.id.Search);
	     searchButton.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View v) {
	        	 Log.i("search","clicked");
	        	 FragmentManager fm = getFragmentManager();
	        	 FragmentTransaction ft = fm.beginTransaction(); //Start adding the fragment by getting the manager for handling this
	        	 FragmentSearchBooks hf = new FragmentSearchBooks(); //Create the fragment 
	        	 ft.replace(R.id.content_frame, hf); //And add it to the manager
	        	 ft.commit(); //OK go ahead do your transaction nothing real
	         }
	     });
	     
	     final Button newAdButton = (Button) rootView.findViewById(R.id.Search);
	     newAdButton.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View v) {
	        	 Log.i("newAd","clicked");
	        	 FragmentManager fm = getFragmentManager();
	        	 FragmentTransaction ft = fm.beginTransaction(); //Start adding the fragment by getting the manager for handling this
	        	 FragmentSearchBooks hf = new FragmentNewAd(); //Create the fragment 
	        	 ft.replace(R.id.content_frame, hf); //And add it to the manager
	        	 ft.commit(); //OK go ahead do your transaction nothing real
	         }
	     });
       return rootView;
       
   }
}

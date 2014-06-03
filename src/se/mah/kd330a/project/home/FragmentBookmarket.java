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
import android.widget.Toast;


public class FragmentBookmarket extends Fragment
implements OnClickListener 
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
	
	    
	    new_ad = (View) rootView.findViewById(R.id.New_ad);
	    new_ad.setOnClickListener((OnClickListener) this);
	    new_ad.setClickable(true);
       return rootView;
       
   }
	


@Override
public void onClick(View v) {
	Log.i("new_ad or search","clicked");
	
	FragmentManager fm = getFragmentManager();
	FragmentTransaction ft = fm.beginTransaction(); //Start adding the fragment by getting the manager for handling this
	FragmentSearchBooks hf = new FragmentSearchBooks(); //Create the fragment 
	ft.add(R.id.bookmarket_search_fragment, hf); //And add it to the manager
	ft.commit(); //OK go ahead do your transaction nothing real
	
	search = (View) View.findViewById(R.id.Search);
    search.setOnClickListener((OnClickListener) this);
    search.setClickable(true);
    
	
	// TODO Auto-generated method stub
	
}
}

/*public void new_ad(View view){
Log.i("new_ad","clicked");
switch (view.getId()){
case R.id.btnSearch:
        //what to put here
       FragmentManager fm = getFragmentManager();
       FragmentTransaction ft = fm.beginTransaction();
       ft.replace(R.id.bookmarket_new_ad_fragment, new TestFragment(), "bookmarket_new_ad_fragment");
        ft.commit();
     break;
    }
} 
BookmarketNew bookmarketNew = (BookmarketNew) MainBookmarket.this.getActivity()
    .getSupportFragmentManager().findFragmentById(R.id.bookmarket_new_ad_fragment);
bookmarketNew.getView().setVisibility(View.VISIBLE);
MainBookmarket.this.getView().setVisibility(View.GONE);

}
return view;
}

public void Search(View view){
Log.i("Search","clicked");
}


public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
// TODO Auto-generated method stub
return null;
}
}*/



package se.mah.kd330a.project.links;

import java.util.ArrayList;
import java.util.List;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.framework.MainActivity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LinksParentFragment extends Fragment {
	
	public static ViewPager viewPager;
	PagerTabStrip pagerTabStrip;
	
	public static List<String[]> titleArrayList = new ArrayList<String[]>();
	public static TypedArray images;
	public static String[] subTitles;
	public static String[] linkOptions;
	public static List<String[]> phoneNumbers = new ArrayList<String[]>();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		phoneNumbers.add(getResources().getStringArray(R.array.student_nr));
	    phoneNumbers.add(getResources().getStringArray(R.array.it_nr));
        phoneNumbers.add(getResources().getStringArray(R.array.library_nr));
		subTitles = getResources().getStringArray(R.array.links_list_sub_options);
		images = getResources().obtainTypedArray(R.array.images_links);
	    linkOptions = getResources().getStringArray(R.array.links_options);
	    titleArrayList.add(getResources().getStringArray(R.array.links_list_options));
		titleArrayList.add(getResources().getStringArray(R.array.student_at_mah));
		titleArrayList.add(getResources().getStringArray(R.array.it));
		titleArrayList.add(getResources().getStringArray(R.array.library));
		titleArrayList.add(getResources().getStringArray(R.array.housing));
		titleArrayList.add(getResources().getStringArray(R.array.career_guide));
		titleArrayList.add(getResources().getStringArray(R.array.social_media));
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    		
    		View v = inflater.inflate(R.layout.links_fragment, container, false);
    		viewPager = (ViewPager) v.findViewById(R.id.pager);
    		viewPager.setAdapter(buildAdapter());
    		pagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_tab_strip);
    		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.red_mah));
            pagerTabStrip.setTextSpacing(1);
    		pagerTabStrip.setDrawFullUnderline(true);
    		
    		MainActivity.mDrawerLayout.closeDrawer(MainActivity.mDrawerList);
    	return v;
	}
    
    private PagerAdapter buildAdapter() {
        return(new CollectionPagerAdapter(getChildFragmentManager()));
      } 
}
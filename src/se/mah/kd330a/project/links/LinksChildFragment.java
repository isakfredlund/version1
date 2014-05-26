package se.mah.kd330a.project.links;



import se.mah.kd330a.project.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LinksChildFragment extends Fragment {
    
    Listener listener = new Listener();
	ArrayAdapter<String> categoriesAdapter;
	ArrayAdapter<String> linksAdapter;
	public static int position;
    String[] linkTitle;

    public static final String POSITION = "se.mah.kd330a.project.links.POSITION";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        position = args.getInt("POSITION");
        linkTitle = LinksParentFragment.titleArrayList.get(position);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.links_list_fragment, container, false);
        Toast.makeText(getActivity(), "VP-POS: " + String.valueOf(LinksParentFragment.viewPager.getCurrentItem()), Toast.LENGTH_SHORT).show();
		ListView listView = (ListView) v.findViewById(R.id.list_view);
		
		if(position == 0) {
		    categoriesAdapter = new CategoriesAdapter(getActivity(), R.layout.link_category_item, linkTitle);
		    listView.setAdapter(categoriesAdapter);
		    listView.setOnItemClickListener(listener);
		} else {
		    linksAdapter = new LinksAdapter(getActivity(), R.layout.link_list_item, linkTitle);
		    listView.setAdapter(linksAdapter);
		}
		
		return v;
	}
	
	private class Listener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            LinksParentFragment.viewPager.setCurrentItem(arg2+1);
        }
	}
	
	
	
}

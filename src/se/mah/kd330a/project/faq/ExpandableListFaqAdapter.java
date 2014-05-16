package se.mah.kd330a.project.faq;

import java.util.HashMap;
import java.util.List;

import se.mah.kd330a.project.R;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListFaqAdapter extends BaseExpandableListAdapter {
	
	private Context _context;
	
    private List<String> _listDataHeader; // header titles
    private List<String> _listDataHeader2; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
	private List<Integer> _groupImages;
	
 
    public ExpandableListFaqAdapter(Context context,List<Integer> groupImages, List<String> listDataHeader, List<String> listDataHeader2, 
            HashMap<String, List<String>> listChildData) {
    	
        this._context = context;
        this._groupImages = groupImages;
        this._listDataHeader = listDataHeader;
        this._listDataHeader2 = listDataHeader2;
        this._listDataChild = listChildData;
    }

	@Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
	@Override
	  public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

	@Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
 
        txtListChild.setText(childText);
        return convertView;
    }

	@Override
	  public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

	@Override
	 public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

	@Override
	  public int getGroupCount() {
        return this._listDataHeader.size();
    }

	@Override
	 public long getGroupId(int groupPosition) {
        return groupPosition;
    }

	@Override
	 public View getGroupView(int groupPosition, boolean isExpanded,
	            View convertView, ViewGroup parent) {
		
		if (groupPosition==0) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.faq_list_group2, null);
		}
		else {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.faq_list_group, null);
		}
			
	        String headerTitle = (String) getGroup(groupPosition);
	        String headerTitleSub = _listDataHeader2.get(groupPosition);
	        int imageId = _groupImages.get(groupPosition);
	      
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.faq_list_group, null);
	        }
	        ImageView imgPointer = (ImageView) convertView.findViewById(R.id.icPointer);
	        ImageView imgPointer2 = (ImageView) convertView.findViewById(R.id.icPointer2);
	        TextView lblListHeader = (TextView) convertView
	                .findViewById(R.id.lblListHeader);
	        lblListHeader.setTypeface(null, Typeface.NORMAL);
	        lblListHeader.setText(headerTitle);
	        
	        TextView lblListHeader2 = (TextView) convertView
	                .findViewById(R.id.lblListHeader2);
	        lblListHeader2.setTypeface(null, Typeface.NORMAL);
	        lblListHeader2.setText(headerTitleSub);
	        
	        ImageView imageHeader = (ImageView) convertView.
	        		findViewById(R.id.imageHeader);
            imageHeader.setImageResource(imageId);
            
            
            if(isExpanded){
            	imgPointer.setVisibility(View.GONE);
            	imgPointer2.setVisibility(View.VISIBLE);
            	
            }
            else if(!isExpanded){
            	imgPointer.setVisibility(View.VISIBLE);
            	imgPointer2.setVisibility(View.GONE);
            	
            }
	        return convertView;
	    }

	@Override
	public boolean hasStableIds() {
        return false;
    }

	@Override
	 public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

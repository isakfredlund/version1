package se.mah.kd330a.project.schedule.view;

import java.util.ArrayList;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import se.mah.kd330a.project.R;
import se.mah.kd330a.project.adladok.model.Course;
import se.mah.kd330a.project.adladok.model.Me;
import se.mah.kd330a.project.find.data.RoomDbHandler;
import se.mah.kd330a.project.find.view.FragmentFloorMap;
import se.mah.kd330a.project.find.view.FragmentResult;
import se.mah.kd330a.project.schedule.model.ScheduleItem;
import se.mah.kd330a.project.schedule.model.ScheduleWeek;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentScheduleWeek extends Fragment implements OnChildClickListener {

	// private static ArrayList<ScheduleItem> _scheduleItemsThisWeek;
	private final String TAG = "FragmentScheduleWeek";
	
	public static FragmentScheduleWeek newInstance(ScheduleWeek scheduleWeek,int position) {
		FragmentScheduleWeek f = new FragmentScheduleWeek();
		Bundle b = new Bundle();
		b.putSerializable("ScheduleWeek", scheduleWeek);
		f.setArguments(b);
		return f;
	}

	ScheduleWeek _scheduleWeek;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_scheduleWeek = (ScheduleWeek) getArguments().getSerializable("ScheduleWeek");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_schedule_expendable_list_view, container,false);
		TextView v = (TextView) rootView.findViewById(R.id.schudule_week_number);
		v.setText("Week " + (_scheduleWeek.getWeekNumber()));
		ExpandableListView elv = (ExpandableListView) rootView.findViewById(R.id.expandable_list);
		TextView empty = (TextView) rootView.findViewById(R.id.emptytw);
		elv.setEmptyView(empty);
		elv.setAdapter(new ExpandableListViewAdapter(getActivity()));
		elv.setOnChildClickListener(this);
		return rootView;
	}

	public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

		String lastDate = null;
		String location;	
		public ExpandableListViewAdapter(Context context) {	

		}

		@Override
		public int getGroupCount() {
			return _scheduleWeek.getScheduleItems().size();
		}

		@Override
		public int getChildrenCount(int i) {
			return 1;
		}

		@Override
		public Object getGroup(int i) {
			return _scheduleWeek.getScheduleItems().get(i);
		}

		@Override
		public Object getChild(int i, int i1) {
			ArrayList<String> childs = new ArrayList<String>();
			childs.add(_scheduleWeek.getScheduleItems().get(i).getTeacherID());
			return childs;
		}

		@Override
		public long getGroupId(int i) {
			return i;
		}

		@Override
		public long getChildId(int i, int i1) {
			return i1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
			LayoutInflater infalInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ScheduleItem currentSI = (ScheduleItem) getGroup(groupPosition);
			String currentDate = currentSI.getDateAndTime2();
			String previousDate = "dummyDate";
			ScheduleItem previousSI = null ;
			String courseID = currentSI.getCourseID();
			Course course = Me.getInstance().getCourse(courseID);
			
			String courseName ="Missing";
			int color = 0;
			if (course!=null){
				courseName = course.getDisplaynameEn();
				color = Me.getInstance().getCourse(courseID).getColor();
			}else{
				courseName = courseID;
				color = getResources().getColor(R.color.red_mah);
			}
			
			if(groupPosition!=0){
				previousSI=(ScheduleItem) getGroup(groupPosition-1);
				previousDate=previousSI.getDateAndTime2();
			}

			if (groupPosition==0||!currentDate.equals(previousDate)) {  //Separator and first Calendar item
				convertView = infalInflater.inflate(R.layout.schedule_list_seperator, null);
				TextView sepertorText = (TextView) convertView.findViewById(R.id.list_item_section_text);
				sepertorText.setText(currentSI.getWeekDay() + ", " + currentDate);
				lastDate = currentDate;
				TextView courseNameTextView = (TextView) convertView.findViewById(R.id.list_course_name);
				courseNameTextView.setText(courseName);
				TextView startTime = (TextView) convertView.findViewById(R.id.list_course_start_time);
				startTime.setText(currentSI.getStartTime());
				TextView endTime = (TextView) convertView.findViewById(R.id.list_course_end_time);
				endTime.setText(currentSI.getEndTime());
				TextView location = (TextView) convertView.findViewById(R.id.list_course_location);
				location.setText(currentSI.getRoomCode());
				ImageView calendarColorFrame1 = (ImageView) convertView.findViewById(R.id.calendarColorFrame1);
				View calendarColorFrame2 = (View) convertView.findViewById(R.id.calendarColorFrame2);
				calendarColorFrame1.setBackgroundColor(color);
				calendarColorFrame2.setBackgroundColor(color);
			} else {  //The calendar item not first
				convertView = infalInflater.inflate(R.layout.schedule_list_group, null);
				TextView courseNameTextView = (TextView) convertView.findViewById(R.id.list_course_name);
				courseNameTextView.setText(courseName);
				TextView startTime = (TextView) convertView.findViewById(R.id.list_course_start_time);
				startTime.setText(currentSI.getStartTime());
				TextView endTime = (TextView) convertView.findViewById(R.id.list_course_end_time);
				endTime.setText(currentSI.getEndTime());
				TextView location = (TextView) convertView.findViewById(R.id.list_course_location);
				location.setText(currentSI.getRoomCode());
				ImageView calendarColorFrame1 = (ImageView) convertView.findViewById(R.id.calendarColorFrame1);
				View calendarColorFrame2 = (View) convertView.findViewById(R.id.calendarColorFrame2);
				calendarColorFrame1.setBackgroundColor(color);
				calendarColorFrame2.setBackgroundColor(color);
			}

			return convertView;

		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
			ArrayList<String> childTexts = (ArrayList<String>) getChild(groupPosition, childPosition);
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.schedule_list_item, null);
			}
			
			ScheduleItem currentSI = (ScheduleItem) getGroup(groupPosition);
			location = currentSI.getRoomCode();
			TextView lector = (TextView) convertView.findViewById(R.id.list_course_child_lector);
			//HERE enter activity
			TextView activity = (TextView)convertView.findViewById(R.id.list_course_child_activity);
			Button btnFind = (Button) convertView.findViewById(R.id.findButton);
			btnFind.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						String[] locs = location.split(" ");
						RoomDbHandler dbHandler = new RoomDbHandler(getActivity());
						if (dbHandler.isRoomExists(locs[0])) {
							startNavigation(locs[0]);
						}
						else if (dbHandler.isRoomExistsAll(locs[0])) {
							//go to floor maps
							showFloorMap(dbHandler.getMapName());
							//Toast.makeText(getActivity(), "floorMapCode: "+dbHandler.getMapName(), Toast.LENGTH_LONG).show();
						}
						else
							Toast.makeText(getActivity(), getString(R.string.find_db_error), Toast.LENGTH_LONG).show();
					}
					catch (Exception e) {
						
					}
				}
			});
			
			String courseID = currentSI.getCourseID();
			Course course = Me.getInstance().getCourse(courseID);
			String courseName ="Missing";
			int color = 0;
			if (course!=null){
				courseName = course.getDisplaynameEn();
				color = Me.getInstance().getCourse(courseID).getColor();
			}
			lector.setText(childTexts.get(0));
			activity.setText(currentSI.getDescription());
			ImageView calendarColorFrameC1 = (ImageView) convertView.findViewById(R.id.calendarColorFrame1);
			ImageView calendarColorFrameC2= (ImageView) convertView.findViewById(R.id.calendarColorFrame2);
			calendarColorFrameC1.setBackgroundColor(color);
			calendarColorFrameC2.setBackgroundColor(color);
			return convertView;
		}
		
		private void startNavigation(String roomNr) {
			Fragment fragment = new FragmentResult();
			Bundle args = new Bundle();
			args.putString(FragmentResult. ARG_ROOMNR, roomNr);
			//args.putInt(FragmentResult.ARG_BUILDINGPOS, spin_selected);
			fragment.setArguments(args);
			FragmentManager	 fragmentManager = getActivity().getSupportFragmentManager();
			FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();	
			fragmentTrans.replace(R.id.content_frame, fragment);
			fragmentTrans.addToBackStack(null);
			fragmentTrans.commit();
		}
		
		private void showFloorMap(String floorMapCode) {
			Fragment fragment = new FragmentFloorMap();
			Bundle args = new Bundle();
			args.putString(FragmentFloorMap.ARG_FLOORMAP, floorMapCode);
			fragment.setArguments(args);
			FragmentManager	 fragmentManager = getActivity().getSupportFragmentManager();
			FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();	
			fragmentTrans.replace(R.id.content_frame, fragment);
			fragmentTrans.addToBackStack(null);
			fragmentTrans.commit();		
		}
		
		@Override
		public boolean isChildSelectable(int i, int i1) {
			return true;
		}


	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int ChildPosition, long id) {
		// TODO Auto-generated method stub
		return parent.collapseGroup(groupPosition);
	}
}

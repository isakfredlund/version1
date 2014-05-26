
package se.mah.kd330a.project.find;

import java.util.Locale;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.find.data.RoomDbHandler;
import se.mah.kd330a.project.find.view.FragmentBuilding;
import se.mah.kd330a.project.find.view.FragmentFloorMap;
import se.mah.kd330a.project.find.view.FragmentResult;
import se.mah.kd330a.project.framework.MainActivity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class FragmentFind extends Fragment {

	private static final String FIND_SPINNER_STATE = "spinChoice";

	private String selposFind = null;
	private int spin_selected = -1;
	private Spinner spinnerFind;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater
				.inflate(R.layout.fragment_screen_find, container, false);
		
		
		MainActivity.mDrawerLayout.closeDrawer(MainActivity.mDrawerList);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();

		spinnerFind = (Spinner) getView().findViewById(R.id.spinner_find_building);
		ArrayAdapter<CharSequence> spinFindadapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.find_building_array,
						android.R.layout.simple_spinner_item);
		spinFindadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFind.setAdapter(spinFindadapter);

		spinnerFind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			/**
			 * Called when a new item is selected (in the Spinner)
			 */
			public void onItemSelected(AdapterView<?> parent,
					View view, int pos, long id) {

				parent.getItemAtPosition(pos);
				Resources res = getResources();
				String[] findCode = res
						.getStringArray(R.array.find_building_code_array);

				selposFind = findCode[pos];
				spin_selected = pos;

			}

			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing, just another required interface callback
			}
		});

		if (spin_selected > -1) 
			spinnerFind.setSelection(spin_selected, true);

		Button btn_Search = (Button) getView().findViewById(R.id.button_find_navigation);
		btn_Search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				find_button_navigation(view);
			}
		});

		AutoCompleteTextView etRoomNr = (AutoCompleteTextView) getView().findViewById(R.id.editText_find_room);
		etRoomNr.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_GO){
					find_button_navigation(v);
				}
				return false;
			}		
		});
	}

	public void find_button_navigation(View v) {

		// ---get the EditText view---
		AutoCompleteTextView txt_room_code = (AutoCompleteTextView) getView().findViewById(R.id.editText_find_room);
		// ---set the data to pass---
		RoomDbHandler dbHandler;
		String textInput = txt_room_code.getText().toString().toLowerCase(Locale.getDefault());
		String roomNr = selposFind + textInput;
		dbHandler = new RoomDbHandler(getActivity());

		if (spinnerFind.getSelectedItemPosition() < 1){
			if(roomNr.isEmpty()) {
				Toast.makeText(getActivity(), getString(R.string.find_no_building_selected), Toast.LENGTH_LONG).show();
				return;
			}
			else{
				runNavigation(dbHandler, roomNr, R.string.find_no_building_selected);
			}
		}
		else{
			if(textInput.isEmpty()){
				//Log.i("test", "selposFind: "+selposFind);
				showBuilding(selposFind);
			}
			else if(textInput.matches("(or|g8|k2|k8|kl|as).*")){

				if(selposFind.equals(textInput.substring(0, 2))){
					//Log.i("testString", "substring: " + textInput.substring(0, 2));
					runNavigation(dbHandler, textInput, R.string.find_db_error);
				}
				else
					Toast.makeText(getActivity(), getString(R.string.find_building_dont_match), Toast.LENGTH_LONG).show();

			}
			else{
				runNavigation(dbHandler, roomNr, R.string.find_db_error);
			}
		}


		//Hiding the keyboard
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}

	private void runNavigation(RoomDbHandler dbHandler, String roomNr, int errorString) {
		if (dbHandler.isRoomExists(roomNr)) {
			startNavigation(roomNr);
		}
		else if (dbHandler.isRoomExistsAll(roomNr)) {
			//go to floor maps
			showFloorMap(dbHandler.getMapName());
			//Toast.makeText(getActivity(), "floorMapCode: "+dbHandler.getMapName(), Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(getActivity(), getString(errorString), Toast.LENGTH_LONG).show();


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

	private void showBuilding(String buildingCode) {

		Fragment fragment = new FragmentBuilding();
		Bundle args = new Bundle();
		args.putString(FragmentBuilding.ARG_BUILDING, buildingCode);
		fragment.setArguments(args);

		FragmentManager	 fragmentManager = getActivity().getSupportFragmentManager();

		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();	
		fragmentTrans.replace(R.id.content_frame, fragment);
		fragmentTrans.addToBackStack(null);
		fragmentTrans.commit();
	}

	private void startNavigation(String roomNr) {

		Fragment fragment = new FragmentResult();
		Bundle args = new Bundle();
		args.putString(FragmentResult. ARG_ROOMNR, roomNr);
		args.putInt(FragmentResult.ARG_BUILDINGPOS, spin_selected);
		fragment.setArguments(args);

		FragmentManager	 fragmentManager = getActivity().getSupportFragmentManager();

		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();	
		fragmentTrans.replace(R.id.content_frame, fragment);
		fragmentTrans.addToBackStack(null);
		fragmentTrans.commit();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(FIND_SPINNER_STATE, spin_selected);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		//Log.i("project", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			//Log.i("project", "onActivityCreated save1 " + savedInstanceState.getInt(FIND_SPINNER_STATE));
			spin_selected = savedInstanceState.getInt(FIND_SPINNER_STATE);
		}

	}
}
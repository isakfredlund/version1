package se.mah.kd330a.project.find.view;

import java.util.Arrays;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.find.data.GetImage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
//import android.widget.Toast;

public class FragmentFloorMap extends Fragment{
	public static final String ARG_BUILDING = "building";
	public static final String ARG_FLOORMAP = "floorMap";

	private String buildingCode;
	private String floorMapCode;
	private String floorCode = "";
	int spPositionBuilding = -1;
	int spPositionFloor = -1;
	private WebView webview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater
				.inflate(R.layout.fragment_screen_find_floor_maps, container, false);

		return rootView;
	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	public void onStart() {
		super.onStart();

		//getting args
		Bundle args = getArguments();
		if(args.containsKey(ARG_BUILDING)){
			buildingCode = args.getString(ARG_BUILDING);
		}
		else if(args.containsKey(ARG_FLOORMAP)){
			floorMapCode = args.getString(ARG_FLOORMAP);
			String[] strCode = floorMapCode.split("_");
			buildingCode = strCode[0];
			floorCode = strCode[1].toUpperCase();
		}	
		//webview settings
		webview = (WebView) getView().findViewById(R.id.webView_find_floor_map);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSupportZoom(true); 
		webview.getSettings().setBuiltInZoomControls(true);
		webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		
		webview.setWebViewClient(new WebViewClient() {
		    @Override
		    public void onPageFinished(WebView view, String url)
		    {
		        /* This call inject JavaScript into the page which just finished loading. */
		    	webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
		    }
		});
		
		//creating spinners
		final Spinner spinnerBuilding = (Spinner) getView().findViewById(R.id.spinner_bilding);
		final Spinner spinnerFloor = (Spinner) getView().findViewById(R.id.spinner_floor);

		//first categoriesAdapter
		ArrayAdapter<CharSequence> buildingAdapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.find_building_array,
						android.R.layout.simple_spinner_item);
		buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerBuilding.setAdapter(buildingAdapter);

		//creating first building spinner selection
		String[] buildings = getResources().getStringArray(R.array.find_building_code_array);
		spPositionBuilding = Arrays.asList(buildings).indexOf(buildingCode);
		spinnerBuilding.setSelection(spPositionBuilding, true);
		int floorArrey = resetFloor(spPositionBuilding);	

		//second categoriesAdapter
		ArrayAdapter<CharSequence> floorAdapter = ArrayAdapter
				.createFromResource(getActivity(), floorArrey,
						android.R.layout.simple_spinner_item);
		floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFloor.setAdapter(floorAdapter);

		//creating first floor spinner selection
		if(!floorCode.isEmpty()){
			String[] floors = getResources().getStringArray(floorArrey);
			String floorName = "Floor "+ floorCode;
			spPositionFloor = Arrays.asList(floors).indexOf(floorName);
			spinnerFloor.setSelection(spPositionFloor, true);
			showFloorMap(floorMapCode);
		}

		// Called when a new item is selected (in the Building Spinner)
		spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//String buildingName = parent.getItemAtPosition(position).toString();
				//Toast.makeText(getActivity(), "position: "+position, Toast.LENGTH_SHORT).show();
				//resetFloor(buildingName);
				spPositionBuilding = position;
				int floorArrey = resetFloor(position);	

				ArrayAdapter<CharSequence> floorAdapter = ArrayAdapter
						.createFromResource(getActivity(), floorArrey,
								android.R.layout.simple_spinner_item);
				floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerFloor.setAdapter(floorAdapter);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing, just another required interface callback
			}
		});

		// Called when a new item is selected (in the Floor Spinner)
		spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String floorName = parent.getItemAtPosition(position).toString();
				String[] strCode = floorName.split(" ");
				floorCode = strCode[1];
				String[] buildings = getResources().getStringArray(R.array.find_building_code_array);
				buildingCode = buildings[spPositionBuilding];
				floorMapCode = buildingCode + "_" + floorCode;

				//here comes the code for showing the map
				showFloorMap(floorMapCode);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing, just another required interface callback
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.find_floormap, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.find_menu_google:
			String[] buildingNames = getResources().getStringArray(R.array.find_building_array);
			String location = buildingNames[spPositionBuilding];

			if(location.equals("Klerken (Kl)"))	
				location = "Carl Gustafs vag 34";
			else if(location.equals("University Hospital (As)"))
				location = "Jan Waldenstroms gata 25";

			//getting the google map
			Intent i = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("geo:0,0?q="+location+"+Malmo+Sweden"));

			startActivity(i);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected void showFloorMap(String floorMapCode) {
		//Toast.makeText(getActivity(), "floorMapCode: "+floorMapCode, Toast.LENGTH_SHORT).show();

		webview = (WebView) getView().findViewById(R.id.webView_find_floor_map);
		//webview.getSettings().setJavaScriptEnabled(true); 
		//String pdf = "https://dl.dropboxusercontent.com/u/11605027/or1.pdf";
		
		//webview.loadUrl("https://dl.dropboxusercontent.com/u/11605027/" + floorMapCode + ".jpg");
			//getResolution(c); ---missing
		String resolution = GetImage.getResolution(getActivity());
		webview.loadUrl("http://195.178.234.7/mahapp/pictlib.aspx?filename="+ floorMapCode +".jpg&resolution="+resolution);
	}

	protected int resetFloor(int position) {

		int floorArrey =  R.array.find_floorEmpy_array;
		switch(position){
		case 0:
			break;
		case 1: //Orkanen
			floorArrey = R.array.find_floorOr_array;
			break;
		case 2: //G�ddan
			floorArrey = R.array.find_floorG8_array;
			break;
		case 3: //Kranen
			floorArrey = R.array.find_floorK2_array;
			break;
		case 4: //Ub�tshallen
			floorArrey = R.array.find_floorK8_array;
			break;
		case 5: //Klerken
			floorArrey = R.array.find_floorKl_array;
			break;
		case 6: //University Hospital
			floorArrey = R.array.find_floorAs_array;
			break;
		default:
			//Toast.makeText(getActivity(), "Read error log", Toast.LENGTH_SHORT).show();
			Log.d("DEBUG", "a different spinner was selected");
			break;
		}

		return floorArrey;

	}

	class MyJavaScriptInterface {
	    @SuppressWarnings("unused")
	    public void processHTML(String html) {
	        // process the html as needed by the app
	    	Log.i("project", html);
	    	//if (html.indexOf("<img") == -1)
	    		//webview.loadUrl("http://195.178.234.7/mahapp/pictlib.aspx?filename=underconstraction.png&resolution=mdpi");
	    }
	}

}

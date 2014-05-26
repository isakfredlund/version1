package se.mah.kd330a.project.adladok.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import se.mah.kd330a.project.adladok.xmlparser.Parser;
import android.content.Context;
import android.util.Log;


public class Me {
	
	/**
	 * Class for holding info about the currently logged in user.
	 */
	
	private static final long 	serialVersionUID = 1L;
	private static final String TAG = "MeClass";
	private static final String ME_FILE_NAME = "MEfile";

	//Static variables because there is only one Me
	private List<Course> 		myCourses = new ArrayList<Course>();
	private List<Teacher> 		myTeachers = new ArrayList<Teacher>();
	private String 				firstName = "";
	private String 				lastName = "";
	private String 				email = "";
	private String 				dispayName = "";
	private boolean 			isStaff = false;
	private boolean 			isStudent = false;
	private String 				userID = "";
	private String 				password = "";
	private static Me 			instanceOfMe;
	private Refresh				mRefresh;

	public static Me getInstance(){
		if (instanceOfMe == null){
			instanceOfMe = new Me();
		}
		return instanceOfMe;
	}

	private Me() {
		/*Singleton*/
	}

	public void startRefresher(FragmentCallback fragmentCallback, Context context) {
		mRefresh = new Refresh(fragmentCallback, context);
		mRefresh.execute();
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUserID() {
		return this.userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDispayName() {
		return this.dispayName;
	}
	public void setDispayName(String dispayName) {
		this.dispayName = dispayName;
	}

	public boolean isStaff() {
		return this.isStaff;
	}
	public void setIsStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}
	public boolean isStudent() {
		return this.isStudent;
	}
	public void setIsStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}

	public String getTeacherName(String teacherID) {
		//search the teachers return name if found if not found call web service but else:
		//Not found call web service
		//Save in arraylist as teacher
		//return the name
		return teacherID;
	}

	public void clearAllIncludingSavedData(Context c) {
		clearAllExcludingSavedData(c);
		String[] files = c.fileList();
		for (String fileName : files) {
			File file = new File(c.getFilesDir(), fileName);
			if (file.exists()){
				file.delete();
				Log.i(TAG,"Deleted: "+fileName);
			}
		}
		Log.i(TAG,"clear all including locally saved data");
	}

	public void clearAllExcludingSavedData(Context c) {
		Log.i(TAG,"clear all excluding saved data");
		clearCourses();
		firstName="";
		lastName="";
		email="";
		dispayName="";
		isStaff = false;
		isStudent = false;
		userID="";
		password="";
	}

	/**Restores Me and my courses from local storage*/
	public boolean restoreMeFromLocalStorage(Context c){
		//Read local storage
		boolean restored = true;
		File file = new File(c.getFilesDir(), ME_FILE_NAME);
		if (file.exists()){
			//Clear content in me:
			String xml = Parser.getXmlFromFile(file);
			Log.i(TAG,"Restored" + xml);
			try {
				Parser.updateMeFromADandLADOK(xml,c);
				if(this.password.isEmpty()||this.userID.isEmpty()){
					restored = false;
				}
			} catch (Exception e) {restored =false;}
		}
		return restored;
	}

	/**Stores Me and my courses on local storage CourseName:
	 * Call this also when changes are made to Me or courses*/
	public void saveMeToLocalStorage(Context c){
		String xml = Parser.writeXml();
		Log.d(TAG,"Saved: " + xml);
		try {
			java.io.FileOutputStream fos = c.openFileOutput(ME_FILE_NAME, Context.MODE_PRIVATE);
			fos.write(xml.getBytes());
			fos.close();
		} catch (Exception e) {

		}
	}



	public void clearCourses(){
		if (myCourses.size()>0){
			Log.i(TAG,"clearing courses in memory");
			myCourses.clear();
		}
	}

	public List<Course> getCourses(){
		return myCourses;
	}

	public Course getCourse(String courseID) {
		for(Course c: myCourses){  //overide equals instead.......
			if (c.getCourseID().equals(courseID)){
				return c;
			}
		}
		return null;
	}

	public void addCourse(Course course) {		
		this.myCourses.add(course);
	}


	/**
	 * Logs in the user.
	 * TODO: Poses a huge security risk, since the id and password is both sent and returned in plaintext.
	 */

	public String getUserInfoAsXML(String loginID, String password){	   
		Object result="";
		try {
			SoapObject loginrequest = new SoapObject(Constants.NAMESPACE, "getUserInfo");
			loginrequest.addProperty("username", loginID);
			loginrequest.addProperty("password", password);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(loginrequest);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL);
			androidHttpTransport.call(Constants.NAMESPACE+"getUserInfo", envelope);
			result = (Object)envelope.getResponse();
		} catch (Exception e) {
			Log.i(TAG,"LoginError: "+e.getMessage());
		}
		return result.toString();
	}
	
}

package se.mah.kd330a.project.adladok.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Observable;

import se.mah.kd330a.project.adladok.xmlparser.Parser;
import se.mah.kd330a.project.home.data.DOMParser;
import se.mah.kd330a.project.home.data.RSSFeed;
import se.mah.kd330a.project.schedule.data.KronoxCalendar;
import se.mah.kd330a.project.schedule.data.KronoxReader;
import android.content.Context;
import android.util.Log;

public class ScheduleFixedDelay extends Observable implements Runnable  {
	
	public final static long initalDelayInSeconds = 0;
	public final static long delayBetweenUpdatesInSeconds = 180; //3  minutes then we take AD every 30 minutes
	//public final static long delayBetweenUpdatesInSeconds = 30; //30sec for testing
	private int ADUpdateCount=10; 
	public enum UpdateType {
		   KRONOX, 
		   COURSES_and_AD,
		   ALL
		}
	private Context c;
    private String TAG ="ScheduleFixedDelay";
    
	public ScheduleFixedDelay(Context c){
		this.c = c;
	}
	
	
	@Override
	public void run() {
			String[] files = c.fileList();
			for (String s : files) {
				Log.d(TAG,"Filename: "+s);
			}
			// updates from AD and LADOK notifies listeners
			if (ADUpdateCount >=10){
				ADUpdateCount=0;
				Log.d(TAG,"Starting AD update");
	        	try{
	        		String userInfoAsXML = Me.getInstance().getUserInfoAsXML(Me.getInstance().getUserID(),Me.getInstance().getPassword());
	        		if(!userInfoAsXML.isEmpty()&&Parser.updateMeFromADandLADOK(userInfoAsXML,c)){
	        			Log.i(TAG,"UserUpdate succesfull saving to local storage");
	        			Me.getInstance().saveMeToLocalStorage(c);
	        			setChanged();
	        			notifyObservers(UpdateType.COURSES_and_AD);
	        		}
	        	}catch(Exception e){
	        		Log.e(TAG,"AD-LADOK Parser update exception");
	        	}
			}else{
				ADUpdateCount++;
			}
			
			//Kronox reads from Kronox with information from registered courses
			try
			{
				KronoxReader.update(c.getApplicationContext());
				KronoxCalendar.createCalendar(KronoxReader.getFile(c.getApplicationContext()));
				setChanged();
				notifyObservers(UpdateType.KRONOX);
				Log.i(TAG, "Updated Calendar from Kronox");
			}catch (Exception f){
					Log.i(TAG, "Kronox: "+f.toString());
			 }
			
			//ITSL
			//????
			
		}
 }


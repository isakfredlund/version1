package se.mah.kd330a.project.adladok.xmlparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import se.mah.kd330a.project.R;
import se.mah.kd330a.project.adladok.model.Course;
import se.mah.kd330a.project.adladok.model.Me;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class Parser{

    //"givenname""lastname""displayname""mahmail""mahisstaff""mahisstudent"
	//"courses""displaynamesv""course""courseid""displaynameen""regcode""program""term"
	
	/**
	 * Update Me and courses from XML if there are any changes
	 * @param xml string
	 * @return boolean if info is updated in  Me
	 * */
	public static boolean updateMeFromADandLADOK(String xmlFromWebservice, Context ctx) throws Exception{
        boolean changedInfo = false;
		XMLParser parser = new XMLParser();
		if (xmlFromWebservice!=null){
			Document doc = parser.getDomElement(xmlFromWebservice);
			//UserID 
			NodeList nl = doc.getElementsByTagName("userid");
			Element e = (Element)nl.item(0);
			if (e!=null&&!Me.getInstance().getUserID().equals(parser.getElementValue(e))){
				Me.getInstance().setUserID(parser.getElementValue(e));
				changedInfo = true;
			}
			nl = doc.getElementsByTagName("password");
			e = (Element)nl.item(0);
			if (e!=null&&!Me.getInstance().getPassword().equals(parser.getElementValue(e))){
				Me.getInstance().setPassword(parser.getElementValue(e));
				changedInfo = true;
			}
			//firstnamevv
			nl = doc.getElementsByTagName("givenname");
			e = (Element)nl.item(0);
			if (e!=null&&!Me.getInstance().getFirstName().equals(parser.getElementValue(e))){
				Me.getInstance().setFirstName(parser.getElementValue(e));
				changedInfo = true;
			}
			//lastname
			nl = doc.getElementsByTagName("lastname");
			e = (Element)nl.item(0);
			if (e!=null&&!Me.getInstance().getLastName().equals(parser.getElementValue(e))){
				Me.getInstance().setLastName(parser.getElementValue(e));
				changedInfo = true;
			}
			//displayname
			nl = doc.getElementsByTagName("displayname");
			e = (Element)nl.item(0);
			if (e!=null&&!Me.getInstance().getDispayName().equals(parser.getElementValue(e))){
				Me.getInstance().setDispayName(parser.getElementValue(e));
				changedInfo = true;
			}
			//email
			nl = doc.getElementsByTagName("mahmail");
			e = (Element)nl.item(0);
			if (e!=null&&!Me.getInstance().getEmail().equals(parser.getElementValue(e))){
				Me.getInstance().setEmail(parser.getElementValue(e));
				changedInfo = true;
			}
			//isstaff
			nl = doc.getElementsByTagName("mahisstaff");
			e = (Element)nl.item(0);
			if (e!=null){
				String s = parser.getElementValue(e);
				if (s.equals("True")||s.equals("true")){
					if (!Me.getInstance().isStaff()){
						Me.getInstance().setIsStaff(true);
						changedInfo = true;
					}
				}else{
					if (Me.getInstance().isStaff()){
						Me.getInstance().setIsStaff(false);
						changedInfo = true;
					}
				}
			}
			// isstudent
			nl = doc.getElementsByTagName("mahisstudent");
			e = (Element)nl.item(0);
			if (e!=null){
				String s = parser.getElementValue(e);
				if (s.equals("True")||s.equals("true")){
					if(!Me.getInstance().isStudent()){
						Me.getInstance().setIsStudent(true);
						changedInfo = true;
					}
				}else{
					if(Me.getInstance().isStudent()){
						Me.getInstance().setIsStudent(false);
						changedInfo = true;
					}
				}
			}
			nl = doc.getElementsByTagName("courses");
			e = (Element) nl.item(0);			
			NodeList courseNode = e.getElementsByTagName("course");
			for (int j =0;j < courseNode.getLength();j++){
				Element e2 = (Element) courseNode.item(j);
				//Remove old courses
				Course c = Me.getInstance().getCourse(parser.getValue(e2, "courseid"));
				if (c==null){  //the course does not exist in Me
					Course course = new Course(parser.getValue(e2, "displaynamesv"), parser.getValue(e2, "courseid"));
					course.setDisplaynameen(parser.getValue(e2,"displaynameen"));
					course.setRegCode(parser.getValue(e2,"regcode"));
					course.setProgram(parser.getValue(e2,"program"));
					course.setAntterm(parser.getValue(e2,"programantterm"));
					course.setTerm(parser.getValue(e2,"term"));
					switch (j) {
					case 0:
						course.setColor(ctx.getResources().getColor(R.color.blue));
						break;
					case 1:
						course.setColor(ctx.getResources().getColor(R.color.orange));								
						break;
					case 2:
						course.setColor(ctx.getResources().getColor(R.color.green));
						break;
					case 3:
						course.setColor(ctx.getResources().getColor(R.color.yellow));
						break;
					case 4:
						course.setColor(ctx.getResources().getColor(R.color.red));
						break;
					case 5:
						course.setColor(ctx.getResources().getColor(R.color.grey_dark));
						break;
					case 6:
						course.setColor(ctx.getResources().getColor(R.color.grey));
						break;
					case 7:
						course.setColor(ctx.getResources().getColor(R.color.grey_middle));
						break;
					default:
						course.setColor(ctx.getResources().getColor(R.color.red_mah));
						break;			
					}
					Me.getInstance().addCourse(course);
					changedInfo = true;
				}
			}
		}else{
			changedInfo=false;
		}
		return changedInfo;
    }
	
	/**
	 * Gett XML from file in localStorage
	 * @param file File 
	 * */
	public static String getXmlFromFile(File file) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try{
			InputStream  inputStream = new FileInputStream(file);
		    int i = inputStream.read();
		    while (i != -1)
		    {
		        byteArrayOutputStream.write(i);
		        i = inputStream.read();
		    }
		    inputStream.close();
		} catch (Exception e) {} 
	    return byteArrayOutputStream.toString();
	}

	/**
	 * Creates a XML file from Me and Courses
	 *
	 * */
	public static String writeXml(){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("","user");
	        serializer.startTag("", "userid");
        	serializer.text(Me.getInstance().getUserID());
        	serializer.endTag("", "userid");
        	serializer.startTag("", "password");
        	serializer.text(Me.getInstance().getPassword());
        	serializer.endTag("", "password");
        	serializer.startTag("", "givenname");
        	serializer.text(Me.getInstance().getFirstName());
        	serializer.endTag("", "givenname");
        	serializer.startTag("", "lastname");
        	serializer.text(Me.getInstance().getLastName());
        	serializer.endTag("", "lastname");
        	serializer.startTag("", "displayname");
        	serializer.text(Me.getInstance().getDispayName());
        	serializer.endTag("", "displayname");
        	serializer.startTag("", "mahmail");
        	serializer.text(Me.getInstance().getEmail());
        	serializer.endTag("", "mahmail");
        	serializer.startTag("", "mahisstudent");
        	serializer.text(String.valueOf(Me.getInstance().isStudent()));
        	serializer.endTag("", "mahisstudent");
        	serializer.startTag("", "mahisstaff");
        	serializer.text(String.valueOf(Me.getInstance().isStaff()));
        	serializer.endTag("", "mahisstaff");
	        serializer.startTag("", "courses");
	        for (Course course: Me.getInstance().getCourses()){
	        	serializer.startTag("", "course");
	        	serializer.startTag("", "courseid");
	        	serializer.text(course.getCourseID());
	        	serializer.endTag("", "courseid");
	        	serializer.startTag("", "displaynamesv");
	        	serializer.text(course.getDisplaynameSv());
	        	serializer.endTag("", "displaynamesv");
	        	serializer.startTag("", "displaynameen");
	        	serializer.text(course.getDisplaynameEn());
	        	serializer.endTag("", "displaynameen");
	        	serializer.startTag("", "regcode");
	        	serializer.text(course.getRegCode());
	        	serializer.endTag("", "regcode");
	        	serializer.startTag("", "program");
	        	serializer.text(course.getProgram());
	        	serializer.endTag("", "program");
	        	serializer.startTag("", "programantterm");
	        	serializer.text(course.getAntterm());
	        	serializer.endTag("", "programantterm");
	        	serializer.startTag("", "term");
	        	serializer.text(course.getTerm());
	        	serializer.endTag("", "term");
	        	serializer.startTag("", "color");
	        	serializer.text(String.valueOf(course.getColor()));
	        	serializer.endTag("", "color");
	            serializer.endTag("", "course");
	        }
	        serializer.endTag("", "courses");
	        serializer.endTag("","user");
	        serializer.endDocument();
	        return writer.toString();
	    } catch (Exception e) {
	        return"<user></user>";
	    } 
	}
}

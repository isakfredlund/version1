package se.mah.kd330a.project.adladok.model;

public class Course {
	
	/**
	 * Class for holding info about a specific course.
	 */
	
	private String 	displaynamesv;
	private String 	displaynameen;
	private String 	courseID;
	private String 	regCode="";
	private String 	program ="";
	private String 	antterm ="";
	private String	term;
	private int	    color = 0;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	public Course(String displayName, String courseID) {
		this.displaynamesv = displayName;
		this.courseID = courseID;
	}
	public String getDisplaynameEn() {
		return displaynameen;
	}
	public void setDisplaynameen(String displaynameEn) {
		this.displaynameen = displaynameEn;
	}

	/**The course ID reused every year format KD330A*/
	public String getCourseID() {
		return courseID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	/**the 6 numbered code used for identify a course a certain year*/ 
	public String getRegCode() {
		return regCode;
	}
	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getAntterm() {
		return antterm;
	}

	public void setAntterm(String antterm) {
		this.antterm = antterm;
	}

	public String getDisplaynameSv() {
		return displaynamesv;
	}
	public void setDisplaynameSv(String displayname) {
		this.displaynamesv = displayname;
	}

	/**Term in the format YYYYT where T is 1 for VT and 2 for HT*/
	public void setTerm(String term) {
		this.term = term;
	}
	public String getTerm() {
		return this.term;
	}

	//There could be duplicates if you have many courses on one program

	public String getKronoxCalendarCode() {
		String s="";
		if(!this.regCode.isEmpty()&&!this.courseID.isEmpty()&&!this.term.isEmpty()){ //course
			s="k."+this.courseID+"-"+this.term+"-"+this.regCode;
		}else if(!this.program.isEmpty()&&!this.antterm.isEmpty()){
			s="p."+this.program+this.antterm.substring(2, 4);
			if(this.antterm.substring(4, 5).equals("2")){
				s= s+"h";
			}
			else{
				s=s+"v";
			}
		}
		return s;
	}


}

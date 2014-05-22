package se.mah.kd330a.project.home;

import java.io.Serializable;

public class InstagramTag implements Serializable {
	// Images for tag
	private String lowResolution = "";
	private String thumbnail = "";
	private String standardResolution = "";

	// Userdetails
	private String username = "";
	private String profilePicture;
	private String fullName = "";
	private String id;

	public InstagramTag(String lowResolution, String thumbnail,
			String standardResolution, String username, String profilePicture,
			String fullName, String id) {
		super();
		this.lowResolution = lowResolution;
		this.thumbnail = thumbnail;
		this.standardResolution = standardResolution;
		this.username = username;
		this.profilePicture = profilePicture;
		this.fullName = fullName;
		this.id = id;
	}

	/**
	 * @return the lowResolution
	 */
	public String getLowResolution() {
		return lowResolution;
	}

	/**
	 * @param lowResolution
	 *            the lowResolution to set
	 */
	public void setLowResolution(String lowResolution) {
		this.lowResolution = lowResolution;
	}

	/**
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param thumbnail
	 *            the thumbnail to set
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * @return the standardResolution
	 */
	public String getStandardResolution() {
		return standardResolution;
	}

	/**
	 * @param standardResolution
	 *            the standardResolution to set
	 */
	public void setStandardResolution(String standardResolution) {
		this.standardResolution = standardResolution;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the profilePicture
	 */
	public String getProfilePicture() {
		return profilePicture;
	}

	/**
	 * @param profilePicture
	 *            the profilePicture to set
	 */
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "TAG from user " + getFullName() + " with url " + getStandardResolution();
	}

}


package se.mah.kd330a.project.home;

/*
 * JSONParser.java
 * 
 * Connects to, and interacts with, the stenslie_webeditor database.
 * 
 * Copyright (C) 2011  1scale1 Handelsbolag
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {


	public static ArrayList<Serializable> getTags(String json_encoded)
			throws JSONException {
		ArrayList<Serializable> output = new ArrayList<Serializable>();
		
		/* Construct the json-root */
		JSONObject json_root = new JSONObject(json_encoded);

		/* Get the list of tags (under "data") */
		JSONArray json_data = json_root.getJSONArray("data");
		
		// Get all tags
		for( int i = 0; i < json_data.length(); i++){
			JSONObject tag = json_data.getJSONObject(i);
			// Get images
			JSONObject images = tag.getJSONObject("images");
			JSONObject low_res = images.getJSONObject("low_resolution");
			String lowRes = low_res.getString("url");
			JSONObject thumb_res = images.getJSONObject("thumbnail");
			String thumb = thumb_res.getString("url");
			JSONObject standard_res = images.getJSONObject("standard_resolution");
			String standard = standard_res.getString("url");
				
			// Get user
			JSONObject user = tag.getJSONObject("user");
			String username = user.getString("username");
			String profilePic = user.getString("profile_picture");
			String fullName = user.getString("full_name");
			String id = user.getString("id");
			
			output.add(new InstagramTag(lowRes, thumb, standard, username, profilePic, fullName, id));
		}

		return output;
	}

}

/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import com.google.common.collect.Lists;

@Controller
public class VideoLikeController {
	/*
	public static final String DATA_PARAMETER = "data";

	public static final String ID_PARAMETER = "id";

	public static final String VIDEO_SVC_PATH = "/video";
	
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";
	*/
	
	//private Map<Long,Video> videos = new HashMap<Long, Video>();
	
	@Autowired
	private VideoRepository videos;
	
	public VideoLikeController() {
		int x = 1;
		int y = x;
	}
	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it
	 * to something other than "AnEmptyController"
	 * 
	 * 
		 ________  ________  ________  ________          ___       ___  ___  ________  ___  __       
		|\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \     
		\ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_   
		 \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \  
		  \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \ 
		   \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
		    \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
                                                                                                                                                                                                                                                                        
	 * 
	 */
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v){		 
		 videos.save(v);
		 return v;
		 //return true;
	}
	
	//@GET(VIDEO_SVC_PATH + "/{id}")
	//public Video getVideoById(@Path("id") long id);
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method=RequestMethod.GET)
	public ResponseEntity<Video> getVideoById(@PathVariable("id") Long id){	
		
		if (!videos.exists(id)) {
			return new ResponseEntity<Video>(HttpStatus.NOT_FOUND);
		}
		
		Video v = videos.findOne(id);
		return new ResponseEntity<Video>(v, HttpStatus.OK);
		 //videos.save(v);
		 //return v;
		 //return true;
	}
	
	//@POST(VIDEO_SVC_PATH + "/{id}/unlike")
	//public Void unlikeVideo(@Path("id") long id);
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method=RequestMethod.POST)
	public ResponseEntity<Void> unlikeVideo(@PathVariable("id") Long id, Principal p){
		if (!videos.exists(id)) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		String username = p.getName(); 
		Video v = videos.findOne(id);
		Set<String> likesUsernames = v.getLikesUsernames();  
		if (!likesUsernames.contains(username)) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} 
		
		likesUsernames.remove(username); //contains(username)
		
		v.setLikesUsernames(likesUsernames);
		v.setLikes(likesUsernames.size());
		videos.save(v);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method=RequestMethod.POST)
	public ResponseEntity<Void> likeVideo(@PathVariable("id") Long id, Principal p){
		
		if (!videos.exists(id)) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		String username = p.getName(); 
		Video v = videos.findOne(id);
		Set<String> likesUsernames = v.getLikesUsernames();  
		if (likesUsernames.contains(username)) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} 
		
		likesUsernames.add(username);
		v.setLikesUsernames(likesUsernames);
		v.setLikes(likesUsernames.size());
		videos.save(v);
		long x = id;
		long y = x;
		//String username = p.getName(); 
		String z = username;
		//p.getClass().
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	//Principal p
	//(@PathVariable(ID_PARAMETER) Long id,
	//@POST(VIDEO_SVC_PATH + "/{id}/like")
	//public Void likeVideo(@Path("id") long id);
	
	/*
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video v){
		Video entity = save(v);
		entity.setDataUrl(getDataUrl(v.getId()));
		//v.setDataUrl(dataUrl);
		return v;
	}
	*/
	
	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method=RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideoList() {
		//videos.put(1, 1000);
		
		Iterable<Video> vids = videos.findAll();
		return Lists.newArrayList(vids);
		//THIS WORKS
		//return Lists.newArrayList(videos.findAll());
		//return videos.findAll();
	}
	
	@RequestMapping(value="/go1",method=RequestMethod.GET)
	public @ResponseBody String goodLuck(){
		return "Good Luck! ";
	}
	
}

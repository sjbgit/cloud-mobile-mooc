package org.magnum.dataup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import retrofit.http.Body;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

@Controller
public final class VideoUploadController {

	public static final String DATA_PARAMETER = "data";

	public static final String ID_PARAMETER = "id";

	public static final String VIDEO_SVC_PATH = "/video";
	
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";
	
	//private Map videos = new HashMap();
	
	//private VideoFileManager videoDataMgr;
	
	public VideoUploadController() {
		// TODO Auto-generated constructor stub
		int x = 1;
		//videoDataMgr = VideoFileManager.get();
	}
	
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.GET)
    public HttpServletResponse getData(@PathVariable(ID_PARAMETER) Long id, HttpServletResponse response) {
		if (videos.containsKey(id) == false) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			
		}
		
		return response;
	}

	
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideoList() {
		//videos.put(1, 1000);
		return videos.values();
	}
	
	
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video v){
		Video entity = save(v);
		entity.setDataUrl(getDataUrl(v.getId()));
		//v.setDataUrl(dataUrl);
		return v;
	}
	
	@RequestMapping(value=VIDEO_DATA_PATH, method=RequestMethod.POST)
	public @ResponseBody VideoStatus setVideoData(@PathVariable(ID_PARAMETER) Long id, @RequestParam(DATA_PARAMETER) MultipartFile videoData,  HttpServletResponse response) throws IOException{
		
		//((ServletRequestAttributes) ResponseContextHolder.
		if (videos.containsKey(id)) {
			//saveSomeVideo()
			saveSomeVideo(videos.get(id), videoData);
			return new VideoStatus(VideoState.READY);

		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return new VideoStatus(VideoState.READY);
		}
	}
	
	public void saveSomeVideo(Video video, MultipartFile videoData) throws IOException {

		VideoFileManager.get().saveVideoData(video, videoData.getInputStream());

   }

	
	private static final AtomicLong currentId = new AtomicLong(0L);

    private Map<Long,Video> videos = new HashMap<Long, Video>();

    public Video save(Video entity) {
        checkAndSetId(entity);
        videos.put(entity.getId(), entity);
        return entity;
    }

    private void checkAndSetId(Video entity) {
        if(entity.getId() == 0){
            entity.setId(currentId.incrementAndGet());
        }
    }
    
    private String getDataUrl(long videoId){
        String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
        return url;
    }

    private String getUrlBaseForLocalServer() {
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    	String base = "http://"+request.getServerName()
    					+ ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
    	return base;
    }

}

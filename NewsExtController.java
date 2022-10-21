package natcash.news.ext.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import natcash.news.NewsApplication;
import natcash.news.ext.dao.params.ConfigFindParams;
import natcash.news.ext.dao.params.NewsFindParams;
import natcash.news.ext.entity.ConfigObject;
import natcash.news.ext.entity.LanguageContent;
import natcash.news.ext.entity.News;
import natcash.news.ext.response.Response;
import natcash.news.ext.service.ConfigExtService;
import natcash.news.ext.service.NewsExtService;
import natcash.news.sql.QueryString;
import natcash.news.common.*;
@RestController
@RequestMapping(path = "/natcash/api")
public class NewsExtController {
	private static final Logger logger = LoggerFactory.getLogger(NewsApplication.class);

	@Autowired
	private NewsExtService newsExtService;
	@Autowired
	private ConfigExtService configExtService;

	@RequestMapping(path = { "/news" }, method = RequestMethod.POST)
	public ResponseEntity<Response<News>> findNews(@RequestBody NewsFindParams newsFindParams) {
		logger.info(Constant.REQUEST_TIME + newsFindParams.toString());
		if (newsFindParams.getQueryLimit() == null) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		} else if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 20) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}
		if (newsFindParams.getQueryOffset() == null) {
			newsFindParams.setQueryOffset(Long.valueOf(1));
		}
		List<News> find = new ArrayList<News>();
		find = newsExtService.findNews(newsFindParams);
		if (find.size() > 0) {
			logger.info(Constant.RESPONSE_TIME,
					new ResponseEntity<Response<News>>(new Response<News>("200", "Success", find), HttpStatus.OK));
			return new ResponseEntity<Response<News>>(new Response<News>("200", "Success", find), HttpStatus.OK);
		}
		return new ResponseEntity<Response<News>>(new Response<News>("404", "News not found"), HttpStatus.NOT_FOUND);

	}
		
	@RequestMapping(path = { "/config" }, method = RequestMethod.POST)
	public ResponseEntity<Response<ConfigObject>> getConfig(@RequestBody ConfigFindParams configFindParams) {
		logger.info(Constant.REQUEST_TIME + configFindParams.toString());
		List<ConfigObject> find = new ArrayList<ConfigObject>();
		find = configExtService.findConfigObject(configFindParams);
		if (find.size() > 0) {
			logger.info(Constant.RESPONSE_TIME,
					new ResponseEntity<Response<ConfigObject>>(new Response<ConfigObject>("200", "Success", find), HttpStatus.OK));
			return new ResponseEntity<Response<ConfigObject>>(new Response<ConfigObject>("200", "Success", find), HttpStatus.OK);
		}
		return new ResponseEntity<Response<ConfigObject>>(new Response<ConfigObject>("404", "Data not found"), HttpStatus.NOT_FOUND);

	}
	
	
	@RequestMapping(path = { "/news/{id}" }, method = RequestMethod.POST)
	public ResponseEntity<Response<News>> findNewsById(@RequestBody NewsFindParams newsFindParams, long id) {
		logger.info(Constant.REQUEST_TIME + newsFindParams.toString());
		if (newsFindParams.getQueryLimit() == null) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		} else if (newsFindParams.getQueryLimit() != null && newsFindParams.getQueryLimit().longValue() > 20) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}
		if (newsFindParams.getQueryOffset() == null) {
			newsFindParams.setQueryOffset(Long.valueOf(1));
		}
		List<News> find = new ArrayList<News>();
		find = (List<News>) newsExtService.findNewsById(newsFindParams, id);
		if (find.size() > 0) {
			logger.info(Constant.RESPONSE_TIME,
					new ResponseEntity<Response<News>>(new Response<News>("200", "Success", find), HttpStatus.OK));
			return new ResponseEntity<Response<News>>(new Response<News>("200", "Success", find), HttpStatus.OK);
		}
		return new ResponseEntity<Response<News>>(new Response<News>("404", "News not found"), HttpStatus.NOT_FOUND);

	}
	
	@RequestMapping(path = { "/news/{language}" }, method = RequestMethod.POST)
	public ResponseEntity<Response<News>> findNews(@RequestBody NewsFindParams newsFindParams, String language, Long positionGroupId){

		logger.info(Constant.REQUEST_TIME + newsFindParams.toString());
		if(newsFindParams.getQueryLimit() == null) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}else if(newsFindParams.getQueryLimit() !=null && newsFindParams.getQueryLimit().longValue() > 20) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}
		if(newsFindParams.getQueryOffset() == null) {
			newsFindParams.setQueryOffset(Long.valueOf(1));
		}
		 List<News> newsL = newsExtService.findNews(newsFindParams, language, positionGroupId);
	       for(News news: newsL){
			if(news.getLaguageContent().equals(language) && news.getCategoryID().equals(positionGroupId)) {
				return new ResponseEntity<Response<News>>(new Response<News>("202","News founded Succesfully", newsL), HttpStatus.OK);
			}
		}	       
		List<News> findSpecificNews = new ArrayList<News>();
		findSpecificNews = newsL;
		if (findSpecificNews.size() > 0) {
			logger.info(Constant.RESPONSE_TIME,
					new ResponseEntity<Response<News>>(new Response<News>("200", "Success", findSpecificNews), HttpStatus.OK));
			return new ResponseEntity<Response<News>>(new Response<News>("200", "Success", findSpecificNews), HttpStatus.OK);
		}
		return new ResponseEntity <Response<News>>(new Response<News>("404","News Not Found"), HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = { "/insert" }, method = RequestMethod.POST)
	public ResponseEntity<Response<News>> addNews(@RequestBody NewsFindParams newsFindParams, News news){
		if(newsFindParams.getQueryLimit() == null) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}else if(newsFindParams.getQueryLimit() !=null && newsFindParams.getQueryLimit().longValue() > 20) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}
		if(newsFindParams.getQueryOffset() == null) {
			newsFindParams.setQueryOffset(Long.valueOf(1));
		}
		
		List<News> listNews = new ArrayList<News>();
		listNews = (List<News>) newsExtService.addNews(newsFindParams, news);
		for(News _news: listNews){
			if(listNews.isEmpty()) {
				return new ResponseEntity<Response<News>>(new Response<News>("202","News Add Succesfully", listNews), HttpStatus.OK);
			}
		}

		List<News> _newsAdd = new ArrayList<News>();
		_newsAdd = listNews;
		if (_newsAdd.size() > 0) {
			logger.info(Constant.RESPONSE_TIME,
					new ResponseEntity<Response<News>>(new Response<News>("200", "Success", _newsAdd), HttpStatus.OK));
			return new ResponseEntity<Response<News>>(new Response<News>("200", "Success", _newsAdd), HttpStatus.OK);
		}
		return new ResponseEntity <Response<News>>(new Response<News>("404","News already exist"), HttpStatus.BAD_REQUEST);
		
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(path = {"/update"}, method = RequestMethod.PUT)
	public ResponseEntity<Response<News>> updateNews(@RequestBody NewsFindParams newsFindParams, News news){
		if(newsFindParams.getQueryLimit() == null) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}else if(newsFindParams.getQueryLimit() !=null && newsFindParams.getQueryLimit().longValue() > 20) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}
		if(newsFindParams.getQueryOffset() == null) {
			newsFindParams.setQueryOffset(Long.valueOf(1));
		}
		
		 List <News> newsUpdate = new ArrayList<News>();
		newsUpdate = newsExtService.findNews(newsFindParams);
		if (newsUpdate.equals(news)){
			newsExtService.updateNews(newsFindParams, news);
				return new ResponseEntity<Response<News>>(new Response<News>("202","News founded Succesfully", newsUpdate), HttpStatus.OK);
			}
	//newsExtService.addNews(newsFindParams, news);

		List<News> _updateNews = new ArrayList<News>();
		_updateNews = newsUpdate;
		if (_updateNews.size() > 0) {
			logger.info(Constant.RESPONSE_TIME,
					new ResponseEntity<Response<News>>(new Response<News>("200", "Success", _updateNews), HttpStatus.OK));
			return new ResponseEntity<Response<News>>(new Response<News>("200", "Success", _updateNews), HttpStatus.OK);
		}
	
		return new ResponseEntity <Response<News>>(new Response<News>("404", "Update News Failed"), HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(path = { "/delete/{id}" }, method = RequestMethod.DELETE)
	public ResponseEntity<Response<News>> deleteNewsById(@RequestBody NewsFindParams newsFindParams, long id){
		if(newsFindParams.getQueryLimit() == null) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}else if(newsFindParams.getQueryLimit() !=null && newsFindParams.getQueryLimit().longValue() > 20) {
			newsFindParams.setQueryLimit(Long.valueOf(20));
		}
		if(newsFindParams.getQueryOffset() == null) {
			newsFindParams.setQueryOffset(Long.valueOf(1));
		}

		News  newsToDelete = new News();
		newsToDelete = newsExtService.findNewsById(newsFindParams, id);
		
			if(newsFindParams.getNewsId()== id || newsToDelete.getId() == id) {
				newsExtService.deleteNewsById(newsFindParams, id);
				return new ResponseEntity<Response<News>>(new Response<News>("202","News founded Succesfully", newsToDelete), HttpStatus.OK);
			}
		
		List<News> _deleteNews = new ArrayList<News>();
		_deleteNews = (List<News>) newsToDelete;
		if (_deleteNews.size() > 0) {
			logger.info(Constant.RESPONSE_TIME,
					new ResponseEntity<Response<News>>(new Response<News>("200", "Success", _deleteNews), HttpStatus.OK));
			return new ResponseEntity<Response<News>>(new Response<News>("200", "Success", _deleteNews), HttpStatus.OK);
		}
		
		return new ResponseEntity <Response<News>>(new Response<News>("404","Delete News Failed"), HttpStatus.BAD_REQUEST);		
	}
}


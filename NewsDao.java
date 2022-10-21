package natcash.news.ext.dao;

import java.util.List;

import natcash.news.ext.dao.params.NewsFindParams;
import natcash.news.ext.entity.LanguageContent;
import natcash.news.ext.entity.News;

public interface NewsDao {
	List<News> findNews(NewsFindParams newsFindParams);
	List<News> findNews(NewsFindParams newsFindParams,String language, Long positionGroudId);
	News addNews(NewsFindParams newsFindParams, News news);
	News updateNews(NewsFindParams newsFindParams, News news);
	News deleteNewsById(NewsFindParams newsFindParams, long id);
	News findById(NewsFindParams newsFindParams, long id);
}

package natcash.news.ext.dao;

import java.util.List;

import natcash.news.ext.entity.CmsImage;


public interface CmsImageDao {
	void insertCmsAnh(CmsImage cmsAnh);
	void insertCmsAnh(List<CmsImage> cmsAnhList);
	void updateCmsAnh(CmsImage cmsAnh);
	void updateCmsAnh(List<CmsImage> cmsAnhList);
	void deleteCmsAnh(Long id);
	void deleteCmsAnh(CmsImage cmsAnh);
	void deleteCmsAnh(List<CmsImage> cmsAnhList);
	CmsImage getCmsAnh(Long id);
}

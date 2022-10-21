package natcash.news.ext.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import natcash.news.ext.entity.*;
import natcash.news.ext.response.Response;
import natcash.news.ext.service.FileService;
import natcash.news.ext.service.NewsExtService;
import natcash.news.common.CommonUtils;
import natcash.news.common.Constant;
 
@Controller
@RequestMapping(path = "/api/file")
public class FileController {
	@Autowired
	private FileService fileService;
	
	@Autowired
	private CommonUtils commonUtils;
	private BufferedImage ensureOpaque(BufferedImage bi) {
		if (bi.getTransparency() == BufferedImage.OPAQUE)
			return bi;
		int w = bi.getWidth();
		int h = bi.getHeight();
		int[] pixels = new int[w * h];
		bi.getRGB(0, 0, w, h, pixels, 0, w);
		BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		bi2.setRGB(0, 0, w, h, pixels, 0, w);
		return bi2;
	}

	@RequestMapping(path = "/cms_anh/{id}", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<Response<CmsImage>> fileUpload(@PathVariable(name = "id") Long id,
			@RequestParam("file") MultipartFile multipartFile) {
		try {
//			String rootFileUpload = commonUtils.getThamso("ROOT_FILE_UPLOAD", "/bidvrun");
			String rootFileUpload = "/Users/ducme/Desktop/Image";
			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(multipartFile.getBytes()));
			String originalFilename = multipartFile.getOriginalFilename();
			String fileEx = commonUtils.getExtensionByGuava(originalFilename);
			if (Arrays.stream(Constant.LIST_IMAGE_TYPE).anyMatch(n -> n.equalsIgnoreCase(fileEx.toLowerCase()))) {
				CmsImage cmsAnh = fileService.getCmsImage(id);
				if (cmsAnh != null) {
					try {
						originalImage = ensureOpaque(originalImage);
						cmsAnh.setFolder("image");
						cmsAnh.setName(originalFilename);
						cmsAnh.setWidth(Long.valueOf(originalImage.getWidth()));
						cmsAnh.setHeight(Long.valueOf(originalImage.getHeight()));
						cmsAnh.setAspectRatio(1.0D * originalImage.getWidth() / originalImage.getHeight());
						cmsAnh.setDateModify(new Date());
						cmsAnh.setFileName(cmsAnh.getId() + "_" + cmsAnh.getDateModify().getTime() + ".jpg");
						cmsAnh.setStatus(1L);
						File file = new File(rootFileUpload + "/" + cmsAnh.getFolder() + "/" + cmsAnh.getFileName());
						if (file.getParentFile().exists() == false) {
							file.getParentFile().mkdirs();
						}
						ImageIO.write(originalImage, "jpg", file);
						fileService.updateCmsImage(cmsAnh);
						return new ResponseEntity<Response<CmsImage>>(new Response<CmsImage>(cmsAnh), HttpStatus.OK);

					} catch (Exception e) {
						e.printStackTrace();
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
					}
				}
			} else {
				return new ResponseEntity<Response<CmsImage>>(
						new Response<CmsImage>("NOT_ACC", "Anh không đúng định dạng."), HttpStatus.NOT_ACCEPTABLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		return ResponseEntity.notFound().build();
	}


}
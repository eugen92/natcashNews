package natcash.news.common;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;

import natcash.news.ext.entity.SysParam;
import natcash.news.ext.service.ConfigExtService;
@Component
public class CommonUtils {
	@Autowired
	private ConfigExtService configExtService;

	public String getThamso(String key, String defaultValue) {
		String value = defaultValue;
		SysParam sysParam = configExtService.getParam(key);
		if (sysParam != null && sysParam.getValue() != null && sysParam.getValue().isEmpty() == false) {
			value = sysParam.getValue();
		}
		return value;
	}
	public BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
		return Scalr.resize(originalImage, targetWidth);
	}

	public int getClosetSize(int size, int[] list) {
		int distance = size - list[0];
		if (distance == 0) {
			return size;
		}
		int idx = 0;

		for (int c = 1; c < list.length; c++) {
			int cdistance = size - list[c];
			if (distance >= 0 && cdistance >= 0) {
				if (cdistance < distance) {
					idx = c;
					distance = cdistance;
				}
			} else if (cdistance > 0) {
				idx = c;
				distance = cdistance;
			}
		}
		if (distance < 0) {
			return 0;
		}
		return list[idx];
	}

	public String getExtensionByGuava(String filename) {
	    return Files.getFileExtension(filename);
	}
}

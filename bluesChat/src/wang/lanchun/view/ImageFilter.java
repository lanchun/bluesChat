package wang.lanchun.view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = f.getName().substring(f.getName().indexOf(".") + 1);
		if (extension != null) {
			if (extension.equals("gif") || extension.equals("jpeg") || extension.equals("jpg")
					|| extension.equals("png")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public String getDescription() {
		return "Í¼Æ¬ÎÄ¼þ(*.jpg, *.jpeg, *.gif, *.png)";
	}
}
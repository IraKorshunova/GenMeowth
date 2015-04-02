package view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MidiFilter  extends FileFilter {

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals("mid")) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		public String getExtension(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 && i < s.length() - 1) {
				ext = s.substring(i + 1).toLowerCase();
			}
			return ext;
		}

		@Override
		public String getDescription() {
			return "*.mid";
		}

}

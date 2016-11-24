package main;

import java.io.*;
import java.util.Scanner;

abstract class BuildingsStorage implements Storage {
	
	public static Storage create(String path, DataType dt) {
		if (dt == DataType.Text) {
			return new TextBuildingsStorage(path);
		} else {
			return new BinaryIntegerStorage(path);
		}
	}
	
	private static class TextBuildingsStorage extends BuildingsStorage {

		public TextBuildingsStorage(String path) {
			super(path);
		}

		@Override
		public int[] load() throws IOException {
			File f = new File(mPath);
			if (f.exists()) {
				long fl = f.length();
				int n = (int) fl >> 1;
				int[] result = new int[n];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				Scanner s = null;
				try {
					fis = new FileInputStream(f);
					bis = new BufferedInputStream(fis);
					s = new Scanner(bis);
					int l = 0;
					while (s.hasNext()) {
						result[l++] = s.nextInt();
					}

					int[] ret = new int[l];
					System.arraycopy(result, 0, ret, 0, l);
					return ret;
				} finally {
					if (s != null) {
						s.close();
					}
				}
			}
			
			return new int[0];
		}

		@Override
		public void save(int[] data) throws IOException {
			File f = new File(mPath);
			f.createNewFile();
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			PrintStream ps = null;
			try {
				fos = new FileOutputStream(f);
				bos = new BufferedOutputStream(fos);
				ps = new PrintStream(bos);
				for (int i = 0, j = data.length - 2; i < j; i += 3) {
					ps.println(data[i] + " " + data[i + 1] + " " + data[i + 2]);
				}
			} finally {
				if (ps != null) {
					ps.close();
				}
			}

		}
		
	}
	
	protected BuildingsStorage(String path) {
		mPath = path;
	}
	
	protected final String mPath;
	
}

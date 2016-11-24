package main;

import java.io.*;

final class BinaryIntegerStorage implements Storage {

	protected BinaryIntegerStorage(String path) {
		mPath = path;
	}

	@Override
	public int[] load() throws IOException {
		File f = new File(mPath);
		if (f.exists()) {
			long fl = f.length();
			int n = (int) fl >> 2;
			int[] result = new int[n];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			DataInputStream dis = null;
			try {
				fis = new FileInputStream(f);
				bis = new BufferedInputStream(fis);
				dis = new DataInputStream(bis);
				for (int i = 0; i != result.length; ++i) {
					result[i] = dis.readInt();
				}
				return result;
			} finally {
				if (dis != null) {
					dis.close();
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
		DataOutputStream dos = null;
		try {
			fos = new FileOutputStream(f);
			bos = new BufferedOutputStream(fos);
			dos = new DataOutputStream(bos);
			for (int d : data) {
				dos.writeInt(d);
			}
		} finally {
			if (dos != null) {
				dos.close();
			}
		}
	}
	
	private final String mPath;

}

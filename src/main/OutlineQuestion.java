package main;

final class OutlineQuestion {	
	public OutlineQuestion(int[] builds) {
		if (builds.length % 3 != 0) {
			throw new IllegalArgumentException();
		}
		
		mBuildings = builds;
	}
	
	public void solve() {
		final int n = mBuildings.length / 3;
		if (n == 0) {
			mOutline = new int[] { 0, 0 };
			mOutline1 = new int[] { 1, 0 };
			return;
		} else if (n == 1) {
			mOutline = new int[4];
			mOutline[0] = mBuildings[0];
			mOutline[1] = mBuildings[1];
			mOutline[2] = mBuildings[2];
			mOutline1 = new int[] { 2, 0, 0, 0 };
			return;
		}
		
		mOutline = new int[n << 2];
		mOutline1 = new int[n << 2];
		
		for (int i = 0, j = 0, k = 0; i != n; ++i) {
			j = i << 2;
			k = (i << 1) + i;
			mOutline[j] = mBuildings[k];
			mOutline[j + 1] = mBuildings[k + 1];
			mOutline[j + 2] = mBuildings[k + 2];
			mOutline1[j] = 2;
		}
		
		int m = n;
		int p = 2;
		while (m > 1) {
			for (int i = 0; i <= m - 2; i += 2) {
				mergeOutlines(i, p);
			}
			
			m = (m + 1) >> 1;
			p <<= 1;
		}		
	}
	
	private void mergeOutlines(int di, int n) {
		final int i1 = di * (n << 1);
		final int i2 = i1 + (n << 1);
		final int n1 = mOutline1[i1];
		final int n2 = mOutline1[i2];
		final int i1e = i1 + (n1 << 1);
		final int i2e = i2 + (n2 << 1);
		
		int i1a = i1;
		int i2a = i2;
		int l1 = -1;
		int l2 = -1;
		
		int di1 = i1;
		while (i1a != i1e && i2a != i2e) {
			if (mOutline[i1a] == mOutline[i2a]) {
				l1 = i1a;
				l2 = i2a;
				mOutline1[di1++] = mOutline[i1a];
				mOutline1[di1++] = Math.max(mOutline[i1a + 1], mOutline[i2a + 1]);
				i1a += 2;
				i2a += 2;
			} else if (mOutline[i1a] < mOutline[i2a]) {
				if (l2 != -1) {
					if (mOutline[i1a + 1] > mOutline[l2 + 1]) {
						mOutline1[di1++] = mOutline[i1a];
						mOutline1[di1++] = mOutline[i1a + 1];
					} else {
						if (mOutline[l2] != mOutline1[di1 - 2] || mOutline[l2 + 1] != mOutline1[di1 - 1]) {
							mOutline1[di1++] = mOutline[i1a];
							mOutline1[di1++] = mOutline[l2 + 1];
						}/* else if (l1 != -1 && mOutline[l2] == mOutline[l1]) {
							mOutline1[di1++] = mOutline[i1a];
							mOutline1[di1++] = mOutline[l2 + 1];
						}*/
					}
				} else {
					mOutline1[di1++] = mOutline[i1a];
					mOutline1[di1++] = mOutline[i1a + 1];
				}
				l1 = i1a;
				i1a += 2;
			} else {
				if (l1 != -1) {
					if (mOutline[i2a + 1] > mOutline[l1 + 1]) {
						mOutline1[di1++] = mOutline[i2a];
						mOutline1[di1++] = mOutline[i2a + 1];
					} else {
						if (mOutline[l1] != mOutline1[di1 - 2] || mOutline[l1 + 1] != mOutline1[di1 - 1]) {
							mOutline1[di1++] = mOutline[i2a];
							mOutline1[di1++] = mOutline[l1 + 1];
						}/* else if (l2 != -1 && mOutline[l1] == mOutline[l2]) {
							mOutline1[di1++] = mOutline[i2a];
							mOutline1[di1++] = mOutline[l1 + 1];
						}*/
					}
				} else {
					mOutline1[di1++] = mOutline[i2a];
					mOutline1[di1++] = mOutline[i2a + 1];
				}
				l2 = i2a;
				i2a += 2;
			}
			
			if (di1 >= i1 + 4) {
				if (mOutline1[di1 - 1] == mOutline1[di1 - 3]) {
					di1 -= 2;
				}
			}
		}
		
		if (i1a != i1e) {
			System.arraycopy(mOutline, i1a, mOutline1, di1, i1e - i1a);
			di1 += i1e - i1a;
		} else {
			System.arraycopy(mOutline, i2a, mOutline1, di1, i2e - i2a);
			di1 += i2e - i2a;
		}
		
		System.arraycopy(mOutline1, i1, mOutline, i1, di1 - i1);
		mOutline1[i1] = (di1 - i1) >> 1;
	}
	
	public int[] result() {
		int[] result = new int[mOutline1[0] << 1];
		System.arraycopy(mOutline, 0, result, 0, result.length);
		
		return result;
	}
	
	private final int[] mBuildings;
	private int[] mOutline;
	private int[] mOutline1;
}


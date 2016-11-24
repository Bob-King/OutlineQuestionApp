package main;

import java.util.Random;

final class BuildingsGenerator {
	
	public BuildingsGenerator(int c, int r, int mid, int mad, int miw, int maw, int mih, int mah) {
		mCount = c;
		mRange = r;
		mMinDistance = mid;
		mMaxDistance = mad;
		mMinBuildingWidth = miw;
		mMaxBuildingWidth = maw;
		mMinBuildingHeight = mih;
		mMaxBuildingHeight = mah;
	}
	
	public int[] generate() {
		if (mCount == 0 || mRange < 1) {
			return new int[0];
		}
		
		int[] builds = new int[mCount * 3];
		
		int l = 0;
		int h = 0;
		int r = 0;
		
		for (int i = 0; i != builds.length; i += 3) {
			l = l + random(mMinDistance, mMaxDistance);
			if (l >= mRange) {
				l = l % mRange;
			}
			r = l + random(mMinBuildingWidth, mMaxBuildingWidth);
			if (r >= mRange) {
				r = mRange;
			}
			
			h = random(mMinBuildingHeight, mMaxBuildingHeight);
			
			builds[i] = l;
			builds[i + 1] = h;
			builds[i + 2] = r;
		}
		
		return builds;
	}
	
	private int random(int min, int max) {
		if (min >= max) {
			return min;
		} else {
			return min + mRandom.nextInt(max - min + 1);
		}
	}	
	
	private final int mCount;
	private final int mRange;
	private final int mMinDistance;
	private final int mMaxDistance;
	private final int mMinBuildingWidth;
	private final int mMaxBuildingWidth;
	private final int mMinBuildingHeight;
	private final int mMaxBuildingHeight;
	
	private Random mRandom = new Random();
}

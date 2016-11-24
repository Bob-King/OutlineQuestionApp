package main;

import java.util.EnumSet;
import java.util.Scanner;
import java.util.Set;

final class Config {

	public Config(String a, String jar, String cp, String in, String out, EnumSet<Option> options, String random) {
		mAction = Action.parse(a);
		mInputPath = in;
		mJarPath = jar;
		mClassPath = cp;
		mOutputPath = out;
		mOptions = options;
		parseRandomConfig(random);
	}
	
	public Action action() {
		return mAction;
	}
	
	public String jarPath() {
		return mJarPath;
	}
	
	public String classPath() {
		return mClassPath;
	}
	
	public String inputPath() {
		return mInputPath;
	}
	
	public String outputPath() {
		return mOutputPath;
	}
	
	public Set<Option> options() {
		return mOptions;
	}
	
	public int count() {
		return mCount;
	}
	
	public int range() {
		return mRange;
	}
	
	public int minDistance() {
		return mMinDistance;
	}
	
	public int maxDistance() {
		return mMaxDistance;
	}
	
	public int minBuildingWidth() {
		return mMinBuildingWidth;
	}
	
	public int maxBuildingWidth() {
		return mMaxBuildingWidth;
	}
	
	public int minBuildingHeight() {
		return mMinBuildingHeight;
	}
	
	public int maxBuildingHeight() {
		return mMaxBuildingHeight;
	}
	
	private void parseRandomConfig(String random) {
		Scanner s = new Scanner(random);
		try {
			s.useDelimiter(",");
			mCount = s.nextInt();
			mRange = s.nextInt();
			mMinDistance = s.nextInt();
			mMaxDistance = s.nextInt();
			mMinBuildingWidth = s.nextInt();
			mMaxBuildingWidth = s.nextInt();
			mMinBuildingHeight = s.nextInt();
			mMaxBuildingHeight = s.nextInt();
		} catch (Exception e) {
			
		} finally {
			s.close();
		}
		
		if (mCount < 10) {
			mCount = 10;
		}
		
		if (mRange < 1) {
			mRange = 1;
		}
		
		if (mMinDistance < 0) {
			mMinDistance = 0;
		} else if (mMinDistance > mRange) {
			mMinDistance = 0;
		}
		
		if (mMaxDistance > mRange) {
			mMaxDistance = mMinDistance;
		}
		
		if (mMaxDistance < mMinDistance) {
			mMaxDistance = mMinDistance;
		}
		
		if (mMinBuildingWidth < 1) {
			mMinBuildingWidth = 1;
		} else if (mMinBuildingWidth > mRange) {
			mMinBuildingWidth = 1;
		}
		
		if (mMaxBuildingWidth > mRange) {
			mMaxBuildingWidth = mMinBuildingWidth;
		}
		
		if (mMaxBuildingWidth < mMinBuildingWidth) {
			mMaxBuildingWidth = mMinBuildingWidth;
		}
		
		if (mMinBuildingHeight < 1) {
			mMinBuildingHeight = 1;
		}
		
		if (mMaxBuildingHeight < mMinBuildingHeight) {
			mMaxBuildingHeight = mMinBuildingHeight;
		}
	}
	
	private final Action mAction;
	private final EnumSet<Option> mOptions;
	private final String mInputPath;
	private final String mOutputPath;
	
	private final String mJarPath;
	private final String mClassPath;
	
	private int mCount = 12;
	
	private int mRange = 200;
	
	private int mMinDistance = 0;	
	private int mMaxDistance = 20;
	
	private int mMinBuildingWidth = 1;
	private int mMaxBuildingWidth = 50;
	
	private int mMinBuildingHeight = 1;
	private int mMaxBuildingHeight = 50;
}

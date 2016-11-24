package main;

final class StopWatch {
	
	public void start() {
		mCurrentTime = System.currentTimeMillis();
	}
	
	public long stop() {
		return System.currentTimeMillis() - mCurrentTime;
	}	
	
	private long mCurrentTime;

}

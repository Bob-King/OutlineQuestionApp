package main;

interface Storage {
	public int[] load() throws Exception;
	public void save(int[] data) throws Exception;
}

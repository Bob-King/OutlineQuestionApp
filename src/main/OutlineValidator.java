package main;

abstract class OutlineValidator {
	
	public static boolean validateOutline(int[] outline, int[] buildings) {
		if (buildings.length < 3 || buildings.length % 3 != 0) {
			throw new IllegalArgumentException();
		}
		
		if (outline.length < 2 || (outline.length & 1) != 0) {
			throw new IllegalArgumentException();
		}
		
		int[] build = new int[3];
		
		for (int i = 0; i != buildings.length; i += 3) {
			build[0] = buildings[i];
			build[1] = buildings[i + 1];
			build[2] = buildings[i + 2];
			
			if (!isBuildInOutline(build, outline)) {
				return false;
			}
		}
		
		int[] slice = new int[2];
		
		for (int i = 0, j = outline[outline.length - 2]; i != j; ++i) {
			slice[0] = i;
			slice[1] = 0;
			selectSlice(buildings, slice);
			if (!isSliceHeightMatched(slice, outline)) {
				return false;
			}			
		}
		
		return true;
	}
	
	private static boolean isBuildInOutline(int[] building, int[] outline) {
		if (building.length != 3) {
			throw new IllegalArgumentException();
		}
		
		if (outline.length < 2 || (outline.length & 1) == 1) {
			throw new IllegalArgumentException();
		}
		
		final int n = outline.length;
		
		if (building[0] < outline[0] || building[2] > outline[n - 2]) {
			return false;
		}
		
		int l = -1;
		int r = -1;
		for (int i = 0; i != n; i += 2) {
			if (l == -1) {
				if (building[0] >= outline[i] && building[0] < outline[i + 2]) {
					l = i;
				}				
			}
			
			if (r == -1) {
				if (building[2] > outline[i] && building[2] <= outline[i + 2]) {
					r = i;
				}
			}
		}
		
		for (int i = l; i <= r; i += 2) {
			if (outline[i + 1] < building[1]) {
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isSliceHeightMatched(int[] slice, int[] outline) {
		if (slice.length != 2) {
			throw new IllegalArgumentException();
		}
		
		if (outline.length < 2 || (outline.length & 1) != 0) {
			throw new IllegalArgumentException();
		}
		
		final int n = outline.length;
		
		if ((slice[0] < outline[0] || slice[0] >= outline[n - 2]) && slice[1] > 0) {
			return false;
		}
		
		for (int i = 0; i != n - 2; i += 2) {
			if (slice[0] >= outline[i] && slice[0] < outline[i + 2]) {
				if (slice[1] == outline[i + 1]) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		if (slice[1] == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void selectSlice(int[] buildings, int[] slice) {
		if (buildings.length < 3 || (buildings.length % 3) != 0) {
			throw new IllegalArgumentException();
		}
		
		if (slice.length != 2) {
			throw new IllegalArgumentException();
		}
		
		for (int i = 0; i != buildings.length; i += 3) {
			if (slice[0] >= buildings[i] && slice[0] < buildings[i + 2]) {
				if (buildings[i + 1] > slice[1]) {
					slice[1] = buildings[i + 1];
				}
			}
		}
	}
}

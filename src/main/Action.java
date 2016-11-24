package main;

enum Action {
	Unknown,
	Help,
	UnitTest,
	Test,
	GenRandomBuildings,
	GenOutline;
	
	public static Action parse(String value) {
		if (value.equalsIgnoreCase("Help")) {
			return Help;
		} else if (value.equalsIgnoreCase("UnitTest")) {
			return UnitTest;
		} else if (value.equalsIgnoreCase("Test")) {
			return Test;
		} else if (value.equalsIgnoreCase("GenRandomBuildings")) {
			return GenRandomBuildings;
		} else if (value.equalsIgnoreCase("GenOutline")) {
			return GenOutline;
		} else {
			return Unknown;
		}
	}
}

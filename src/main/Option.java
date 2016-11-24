package main;

import java.util.EnumSet;

enum Option {
	Verifing,
	TextFormat,
	PrintingData;
	
	public static EnumSet<Option> parse(int value) {
		EnumSet<Option> s = EnumSet.noneOf(Option.class);
		if ((value & 0x1) > 0) {
			s.add(Verifing);
		}
		
		if ((value & 0x2) > 0) {
			s.add(TextFormat);
		}
		
		if ((value & 0x4) > 0) {
			s.add(PrintingData);
		}
		
		return s;
	}
}

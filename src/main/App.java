package main;

import java.io.PrintStream;
import java.util.EnumSet;

public class App {
	
	public static App getApp() {
		return instance;
	}
	
	public PrintStream getLogger() {
		return mLogger;
	}
	
	public Config getConfig() {
		return mConfig;
	}
	
	public String getAppName() {
		return mName;
	}
	
	public boolean initialize() {
		boolean ok = parseCmdlineArgs();
		if (!ok) {
			showUsage();
			return ok;
		}
		
		if (getConfig().action() == Action.Unknown || getConfig().action() == Action.Help) {
			showUsage();
			return ok;
		}
		
		return ok;
	}
	
	public void run() {
		try {
			Command.create(this).exec();
		} catch (Exception e) {
			getLogger().println("An exception occurs and the message is <" + e.getMessage() + ">");
		}
	}
	
	private boolean parseCmdlineArgs() {
		if (mArgs.length == 0) {
			return false;
		}
		
		String command = mArgs[0];
		String input = "";
		String output = "";
		String random = "";
		String jar = "";
		String cp = "";
		
		boolean ok = true;
		String lop;
		EnumSet<Option> eo = EnumSet.noneOf(Option.class);
		char op = '\0';
		
		for (int i = 1; i != mArgs.length; ++i) {
			if (mArgs[i].charAt(0) == '-') {
				if (mArgs[i].length() < 2) {
					ok = false;
					break;
				}
				
				if (mArgs[i].charAt(1) != '-') {
					if (mArgs[i].length() != 2) {
						ok = false;
						break;
					}
					op = mArgs[i].charAt(1);
				} else {
					if (mArgs[i].length() < 4) {
						ok = false;
						break;
					}
					lop = mArgs[i].substring(2);
					if (lop.equals("verify")) {
						op = 'v';
					} else if (lop.equals("text")) {
						op = 't';
					} else if (lop.equals("input")) {
						op = 'i';
					} else if (lop.equals("output")) {
						op = 'o';
					} else if (lop.equals("random")) {
						op = 'r';
					} else if (lop.equals("printdata")) {
						op = 'p';
					} else if (lop.equals("jarfilepath")) {
						op = 'j';
					} else if (lop.equals("classpath")) {
						op = 'c';
					}
				}
				
				switch (op) {
				case 'v':
					eo.add(Option.Verifing);
					break;
					
				case 't':
					eo.add(Option.TextFormat);
					break;
					
				case 'p':
					eo.add(Option.PrintingData);
					break;
				}
			} else {
				switch (op) {
				case 'i':
					input = mArgs[i];
					break;
					
				case 'o':
					output = mArgs[i];
					break;
					
				case 'r':
					random = mArgs[i];
					break;
					
				case 'j':
					jar = mArgs[i];
					break;
					
				case 'c':
					cp = mArgs[i];
					break;
				}
			}
		}
		
		if (ok) {
			mConfig = new Config(command, jar, cp, input, output, eo, random);
		}
		
		return ok;
	}
	
	private void showUsage() {
		final PrintStream log = getLogger();
		log.println("Usage: " + getAppName() + "\tCommand Options");
		log.println("Command:");
		log.println("\tHelp");
		log.println("\tUnitTest");
		log.println("\tTest");
		log.println("\tGenRandomBuildings");
		log.println("\tGenOutline");
		log.println("Options:");
		log.println("\t-r, --random\t\tCOUNT,RANGE,MIN_DISTANCE,MAX_DISTANCE,MIN_WIDTH,MAX_WIDTH,MIN_HEIGHT,MAX_HEIGHT");
		log.println("\t-v, --verify\t\tVerify the built outline");
		log.println("\t-t, --text\t\tGenerate/Parse file in text format");
		log.println("\t-i, --input\t\tInput file");
		log.println("\t-o, --output\t\tOutput file");
		log.println("\t-j,--jarfilepath\t\tThe path of jar file including class OutlineQuestion");
		log.println("\t-c,--classpath\t\tThe class path of class OutlineQuestion in jar file");
	}
	
	private App(String[] args) {
		mArgs = args;
	}
	
	private static App instance;
	
	private final String[] mArgs;
	
	private PrintStream mLogger = System.out;
	private Config mConfig;
	
	private final String mName = "OutlineQuestionApp";
	
	public static void main(String[] args) {
		instance = new App(args);
		
		if (getApp().initialize()) {
			getApp().run();
		}
	}
}

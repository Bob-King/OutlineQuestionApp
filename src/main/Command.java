package main;

import java.io.PrintStream;


abstract class Command {
	
	public static Command create(App app) {
		switch (app.getConfig().action()) {
		case UnitTest:
			return new UnitTestCommand(app);
			
		case Test:
			return new TestCommand(app);
			
		case GenRandomBuildings:
			return new GenRandomBuildingsCommand(app);
			
		case GenOutline:
			return new GenOutlineCommand(app);
			
		default:
			return new NullCommand();
		}
	}
	
	private static class NullCommand extends Command {

		@Override
		public void exec() {			
		}
		
	}

	private static class UnitTestCommand extends Command {
		
		public UnitTestCommand(App app) {
			mApp = app;
			mBuildings = new int[][] {
				{ 3, 5, 11 },
				{ 3, 5, 11, 11, 5, 13 },
				{ 4, 8, 10, 4, 5, 12 },
				{ 4, 8, 10, 4, 5, 7 },
				{ 4, 10, 200, 10, 3, 100 },
				{ 93, 21, 117, 107, 14, 148 },
				{ 84, 17, 93, 93, 21, 117, 100, 47, 107, 107, 14, 148 },
				{ 1, 10, 5, 5, 5, 10, 2, 6, 3, 4, 10, 11 } 
			};
			
			mOutlines = new int[][] {
				{ 3, 5, 11, 0 },
				{ 3, 5, 13, 0 },
				{ 4, 8, 10, 5, 12, 0 },
				{ 4, 8, 10, 0 },
				{ 4, 10, 200, 0 },
				{ 93, 21, 117, 14, 148, 0 },
				{ 84, 17, 93, 21, 100, 47, 107, 21, 117, 14, 148, 0 },
				{ 1, 10, 11, 0 }
			};
			
		}

		@Override
		public void exec() throws Exception {
			PrintStream log = mApp.getLogger();
			for (int i = 0, j = Math.min(mBuildings.length, mOutlines.length); i != j; ++i) {
				log.println("Test case " + (i + 1));
				log.print("Buildings: ");
				printIntArray(mApp.getLogger(), mBuildings[i]);
				log.println();
				log.print("Excepted outline: ");
				printIntArray(mApp.getLogger(), mOutlines[i]);
				log.println();
				mQuestion = new OutlineQuestionWrapper(mBuildings[i]);
				mQuestion.solve();
				int[] result = mQuestion.result();
				log.print("Actual outline: ");
				printIntArray(mApp.getLogger(), result);
				printAlgithmAnalysis(log, mQuestion, mBuildings[i]);
				log.println();
				log.println("Success? " + equalIntArray(mOutlines[i], result));
				log.println();
			}			
		}
		
		private final App mApp;
		private OutlineQuestionWrapper mQuestion;
		private final int[][] mBuildings;
		private final int[][] mOutlines;
		
	}
	
	private static class TestCommand extends Command {
		
		public TestCommand(App app) {
			mApp = app;
		}

		@Override
		public void exec() throws Exception {
			Config cfg = mApp.getConfig();
			PrintStream log = mApp.getLogger();
			long elapse;
			
			log.println("Generating buildings(" + cfg.count() + ")");
			mStopWatch.start();
			mGenerator = new BuildingsGenerator(cfg.count(), cfg.range(), cfg.minDistance(), cfg.maxDistance(), cfg.minBuildingWidth(), cfg.maxBuildingWidth(), cfg.minBuildingHeight(), cfg.maxBuildingHeight());
			mBuildings = mGenerator.generate();
			elapse = mStopWatch.stop();
			log.println("Generated");
			log.println("Elapsed " + formatElapsedTime(elapse));
			
			log.println("Solving");
			mStopWatch.start();
			mQuestion = new OutlineQuestionWrapper(mBuildings);
			mQuestion.solve();
			elapse = mStopWatch.stop();
			log.println("Solved");
			log.println("Elapsed " + formatElapsedTime(elapse));
			printAlgithmAnalysis(log, mQuestion, mBuildings);

			if (cfg.options().contains(Option.Verifing)) {
				log.println("Validating");
				mStopWatch.start();
				boolean ok = OutlineValidator.validateOutline(mQuestion.result(), mBuildings);
				elapse = mStopWatch.stop();
				log.println("Validate result = " + ok);	
				log.println("Elapsed " + formatElapsedTime(elapse));			
			}			
		}
		
		private final App mApp;
		BuildingsGenerator mGenerator;
		int [] mBuildings;
		OutlineQuestionWrapper mQuestion;
		private StopWatch mStopWatch = new StopWatch();
	}
	
	private static class GenRandomBuildingsCommand extends Command {

		public GenRandomBuildingsCommand(App app) {
			mApp = app;
		}

		@Override
		public void exec() throws Exception {
			Config cfg = mApp.getConfig();
			PrintStream log = mApp.getLogger();
			long elapse;
			
			log.println("Generating buildings(" + cfg.count() + ")");
			mStopWatch.start();
			mGenerator = new BuildingsGenerator(cfg.count(), cfg.range(), cfg.minDistance(), cfg.maxDistance(), cfg.minBuildingWidth(), cfg.maxBuildingWidth(), cfg.minBuildingHeight(), cfg.maxBuildingHeight());
			mBuildings = mGenerator.generate();
			elapse = mStopWatch.stop();
			log.println("Generated");
			log.println("Elapsed " + formatElapsedTime(elapse));
			
			log.println("Saving buildings to " + cfg.outputPath());
			mStorage = BuildingsStorage.create(mApp.getConfig().outputPath(), mApp.getConfig().options().contains(Option.TextFormat) ? DataType.Text : DataType.Binary);
			try {
				mStopWatch.start();
				mStorage.save(mBuildings);
				elapse = mStopWatch.stop();
				log.println("Saved");
				log.println("Elapsed " + formatElapsedTime(elapse));
			} catch (Exception e) {
				log.println("Failed to save buildings(" + e.getMessage() + ")");
			}
		}
		
		private final App mApp;
		private BuildingsGenerator mGenerator;
		private int[] mBuildings;
		private Storage mStorage;
		private StopWatch mStopWatch = new StopWatch();
		
	}
	
	private static class GenOutlineCommand extends Command {

		public GenOutlineCommand(App app) {
			mApp = app;
		}

		@Override
		public void exec() throws Exception {
			Config cfg = mApp.getConfig();
			PrintStream log = mApp.getLogger();
			long elapse;
			
			log.println("Loading buildings from " + cfg.inputPath());
			mStopWatch.start();
			mBuildingsStorage = BuildingsStorage.create(cfg.inputPath(), mApp.getConfig().options().contains(Option.TextFormat) ? DataType.Text : DataType.Binary);
			try {
				mBuildings = mBuildingsStorage.load();
				elapse = mStopWatch.stop();
				log.println("Loaded");
				log.println("Elapsed " + formatElapsedTime(elapse));
				if (cfg.options().contains(Option.PrintingData)) {
					printIntArray(log, mBuildings);
					log.println();
				}
			} catch (Exception e) {
				log.println("Failed to load buildings(" + e.getMessage() + ")");
				return;
			}
			
			log.println("Solving");
			mStopWatch.start();
			mQuestion = new OutlineQuestionWrapper(mBuildings);
			mQuestion.solve();
			final int[] result = mQuestion.result();
			elapse = mStopWatch.stop();
			log.println("Solved");
			log.println("Elapsed " + formatElapsedTime(elapse));
			printAlgithmAnalysis(log, mQuestion, mBuildings);
			if (cfg.options().contains(Option.PrintingData)) {
				printIntArray(log, result);
				log.println();
			}

			if (cfg.options().contains(Option.Verifing)) {
				log.println("Validating");
				mStopWatch.start();
				boolean ok = OutlineValidator.validateOutline(mQuestion.result(), mBuildings);
				elapse = mStopWatch.stop();
				log.println("Validate result = " + ok);	
				log.println("Elapsed " + formatElapsedTime(elapse));			
			}			

			
			log.println("Saving outline to " + cfg.outputPath());
			mOutlineStorage = OutlineStorage.create(mApp.getConfig().outputPath(), mApp.getConfig().options().contains(Option.TextFormat) ? DataType.Text : DataType.Binary);
			try {
				mStopWatch.start();
				mOutlineStorage.save(result);
				elapse = mStopWatch.stop();
				log.println("Saved");
				log.println("Elapsed " + formatElapsedTime(elapse));
			} catch (Exception e) {
				log.println("Failed to save buildings(" + e.getMessage() + ")");
			}
			
		}
		
		private final App mApp;
		private Storage mBuildingsStorage;
		private int[] mBuildings;
		private OutlineQuestionWrapper mQuestion;
		private Storage mOutlineStorage;
		private StopWatch mStopWatch = new StopWatch();
		
	}
	
	private static String formatElapsedTime(long elapse) {
		if (elapse < 10000) {
			return elapse + " ms";
		} else {
			return (elapse / 1000) + " s";
		}
	}
	
	private static void printIntArray(PrintStream log, int[] array) {
		log.print("(");
		if (array.length > 0) {
			log.print(array[0]);
			for (int i = 1; i != array.length; ++i) {
				log.print(", " + array[i]);
			}
		}
		log.print(")");
	}
	
	private static boolean equalIntArray(int[] arr1, int[] arr2) {
		if (arr1.length != arr2.length) {
			return false;
		}
		
		for (int i = 0; i != arr1.length; ++i) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	private static double log2(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("n should be grater then 0");
		}
		
		return Math.log(n) / Math.log(2);
	}
	
	private static void printAlgithmAnalysis(PrintStream ps, OutlineQuestionWrapper q, int[] raw) {
		int n = raw.length / 3;
		double base = n * log2(n);
		int c = q.getComparisonCount();
		ps.println("Comparison count = " + c + ", rate = " + ((double)c / base));
		c = q.getCopyCount();
		ps.println("Copy count = " + c + ", rate = " + ((double)c / base));
	}
	
	public abstract void exec() throws Exception;
	
}

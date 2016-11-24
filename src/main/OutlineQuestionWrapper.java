package main;

import java.io.File;
import java.lang.reflect.*;
import java.net.*;

final class OutlineQuestionWrapper {
	
	public OutlineQuestionWrapper(int[] data) throws Exception {
		
		ClassLoader cl = URLClassLoader.newInstance(new URL[] { getJarUrl() });
		mClass = cl.loadClass(getClassPath());
		Constructor<?> ctr = mClass.getConstructor(int[].class);
		mObject = ctr.newInstance(data);
	}
	
	public void solve() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method m = mClass.getMethod("solve", new Class[0]);
		m.invoke(mObject, new Object[0]);
	}
	
	public int[] result() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Method m = mClass.getMethod("result", new Class[0]);
		return (int[])m.invoke(mObject, new Object[0]);		
	}
	
	public int getComparisonCount() {
		try {
			Method m = mClass.getMethod("getComparisonCount", new Class[0]);
			return (int)m.invoke(mObject, new Object[0]);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			App.getApp().getLogger().println("Exception occurs in getComparisonCount <" + e.getMessage() + ">");
			return -1;
		}
	}
	
	public int getCopyCount() {
		try {
			Method m = mClass.getMethod("getCopyCount", new Class[0]);
			return (int)m.invoke(mObject, new Object[0]);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			App.getApp().getLogger().println("Exception occurs in getComparisonCount <" + e.getMessage() + ">");
			return -1;
		}
	}
	
	private URL getJarUrl() throws MalformedURLException {
		return new File(App.getApp().getConfig().jarPath()).toURI().toURL();
	}
	
	private String getClassPath() {
		return App.getApp().getConfig().classPath();
	}
	
	private final Class<?> mClass;
	private final Object mObject;
}

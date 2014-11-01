package engine;

import org.lwjgl.Sys;

public class Time {
	
	private static long lastFrame;
	private static int delta;
	
	public static int getDelta()
	{
		long time = getTime();
		delta = (int)(time - lastFrame);
		lastFrame = time;
		
		return delta;
	}
	
	public static void setDelta(int delta)
	{
		Time.delta = delta;
	}
	
	public static long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

}

package engine;


import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Window {

	private static String defaultTexutre = "default.png";

	private static long LAST_FPS;
	private static int FPS;

	/**
	 * create window size of width & height
	 * 
	 * @param width
	 * @param height
	 */
	public static void createWindow(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		Time.getDelta();
		LAST_FPS = Time.getTime();
		loadDefaultTexture(defaultTexutre);
	}

	/**
	 * create window size of width & height & set title
	 * 
	 * @param width
	 * @param height
	 * @param title
	 */
	public static void createWindow(int width, int height, String title) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);
			Display.create();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		Time.getDelta();
		LAST_FPS = Time.getTime();
		loadDefaultTexture(defaultTexutre);
	}

	/**
	 * create window size of width & height & set version to use
	 * 
	 * @param width
	 * @param height
	 * @param version
	 */
	public static void createWindow(int width, int height, double version) {
		String[] v = String.valueOf(version).split("\\.");
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAtrributes = new ContextAttribs(Integer.valueOf(v[0]), Integer.valueOf(v[1])).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(pixelFormat, contextAtrributes);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		Time.getDelta();
		LAST_FPS = Time.getTime();
		loadDefaultTexture(defaultTexutre);
	}

	/**
	 * create window size of width & height, set title & set version to use
	 * 
	 * @param width
	 * @param height
	 * @param title
	 * @param version
	 */
	public static void createWindow(int width, int height, String title, double version) {
		String[] v = String.valueOf(version).split("\\.");
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAtrributes = new ContextAttribs(Integer.valueOf(v[0]), Integer.valueOf(v[1])).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);
			Display.create(pixelFormat, contextAtrributes);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		Time.getDelta();
		LAST_FPS = Time.getTime();
		loadDefaultTexture(defaultTexutre);
	}

	/**
	 * sets the fps cap
	 * 
	 * @param fps
	 */
	public static void sync(int fps) {
		Display.sync(fps);
	}

	/**
	 * set the title
	 * 
	 * @param title
	 */
	public static void setTitle(String title) {
		Display.setTitle(title);
	}

	/**
	 * enable cull face 1 = front 2 = back
	 * 
	 * @param face
	 */
	public static void enableCullFace(int face) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		if (face == 1) {
			GL11.glCullFace(GL11.GL_FRONT);
		} else if (face == 2) {
			GL11.glCullFace(GL11.GL_BACK);
		} else {
			GL11.glCullFace(GL11.GL_BACK);
		}
	}

	/**
	 * sets whether the window can be resized
	 * 
	 * @param resizable
	 */
	public static void setResizable(boolean resizable) {
		Display.setResizable(resizable);
	}

	/**
	 * returns true if the window was resized
	 * 
	 * @return
	 */
	public static boolean wasResized() {
		return Display.wasResized();
	}

	public static void setViewport() {
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public static void setViewport(int x, int y, int width, int height) {
		GL11.glViewport(x, y, width, height);
	}

	/**
	 * check if the close button is clicked
	 * 
	 * @return
	 */
	public static boolean isCloseRequested() {
		// int delta = Time.getDelta();
		if (Time.getTime() - LAST_FPS > 1000) {
			// TODO Display FPS Methods
			System.out.println("FPS: " + FPS);
			FPS = 0;
			LAST_FPS += 1000;
		}
		FPS++;
		
		return Display.isCloseRequested();
	}

	/**
	 * screenshot
	 * 
	 * @param keyCode
	 */
	//TODO
	public static void screenshot() {
		GL11.glReadBuffer(GL11.GL_FRONT);

		ByteBuffer buffer = BufferUtils.createByteBuffer(getWidth() * getHeight() * 4);
		GL11.glReadPixels(0, 0, getWidth(), getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	
		Calendar c = Calendar.getInstance();
		float time = c.getTimeInMillis();
		int year = Calendar.YEAR;
		int month = Calendar.MONTH;
		int day = Calendar.DAY_OF_MONTH;
		
//		String fileName = "screenshot_" + time + "_" + day + "_" + month + "_" + year + ".png";
		String name = JOptionPane.showInputDialog("Screenshot Name");
		if(name == null)
		{
			name = "blank";
		}
		String fileName = name + ".png";
		
		File file = new File(fileName);
		String format = "png";
		
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < getWidth(); x++)
		{
			for(int y = 0; y < getHeight(); y++)
			{
				int i = (x + (getWidth() * y)) * 4;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				
				image.setRGB(x, getHeight() - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
		
		try{
			ImageIO.write(image, format, file);
		}catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		System.out.println("Saved: " + fileName);
	}

	/**
	 * Display FPS on title bar
	 */
	private static void displayFPS() {
		Display.setTitle("" + FPS);
	}

	/**
	 * Display FPS on:- ; 1 = title bar 2 = console
	 * 
	 * @param loc
	 */
	private static void displayFPS(int loc) {
		if (loc == 1) {
			Display.setTitle("" + FPS);
		}

		if (loc == 2) {
			System.out.println("FPS: " + FPS);
		}
	}

	/**
	 * clear color buffer bit
	 */
	public static void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	public static void enableDepthBuffer() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * clear color buffer bit. if depth = true also clear depth buffer
	 * 
	 * @param depth
	 */
	public static void clear(boolean depth) {
		if (depth) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		} else {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		}
	}

	/**
	 * set default color of display
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public static void clearColor(float red, float green, float blue, float alpha) {
		GL11.glClearColor(red, green, blue, alpha);
	}

	/**
	 * clear color/depth buffer & set default color of display
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public static void clearAll(float red, float green, float blue, float alpha) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(red, green, blue, alpha);
	}

	/**
	 * update the display
	 */
	public static void update() {
		Display.update();
	}

	/**
	 * closes & destroys the display
	 */
	public static void close() {
		Storage.cleanup();
		Display.destroy();
		System.exit(0);
	}

	/**
	 * Loads a default texture so the TexturedMesh has a texture to load if non
	 * is already specified
	 * 
	 * @param fileName
	 */
	private static void loadDefaultTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to find texture: " + fileName);
			// Game.close();
			System.exit(1);
		}

		int textureID = texture.getTextureID();
		Storage.addTexture(textureID);
		System.out.println(textureID);
	}

	public static int getWidth() {
		return Display.getDisplayMode().getWidth();
	}

	public static int getHeight() {
		return Display.getDisplayMode().getHeight();
	}

	public static int getAspectRatio() {
		int ar = getWidth() / getHeight();

		return ar;
	}
}

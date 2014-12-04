package engine;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Storage {
	
	private static ArrayList<Integer> vaos = new ArrayList<Integer>();
	private static ArrayList<Integer> vbos = new ArrayList<Integer>();
	private static ArrayList<Integer> textures = new ArrayList<Integer>();
	
	protected static void addVAO(int vao)
	{
		vaos.add(vao);
	}
	
	protected static void addVBO(int vbo)
	{
		vbos.add(vbo);
	}
	
	protected static int addVAO()
	{
		int vao = GL30.glGenVertexArrays();
		vaos.add(vao);
		
		return vao;
	}
	
	protected static int addVBO()
	{
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		
		return vbo;
	}
	
	protected static void addTexture(int texture)
	{
		textures.add(texture);
	}
	
	public static void cleanup()
	{
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}

		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for(int texture : textures)
		{
			GL11.glDeleteTextures(texture);
		}

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}

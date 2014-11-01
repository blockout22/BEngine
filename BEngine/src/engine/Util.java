package engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Util {
	
	public static IntBuffer createFlippedBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	public static FloatBuffer createFlippedBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	public static FloatBuffer createFlippedBuffer(Vector3f[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 3);

		for (int i = 0; i < data.length; i++) {
			buffer.put(data[i].getX());
			buffer.put(data[i].getY());
			buffer.put(data[i].getZ());
		}

		buffer.flip();

		return buffer;
	}

	public static FloatBuffer createFlippedBuffer(Vector2f[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length * 2);

		for (int i = 0; i < data.length; i++) {
			buffer.put(data[i].getX());
			buffer.put(data[i].getY());
		}

		buffer.flip();

		return buffer;
	}

	public static StringBuilder loadFile(String fileName) {
		StringBuilder sb = new StringBuilder();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			
			String line;
			while((line = br.readLine()) != null)
			{
				sb.append(line).append("\n");
			}
			
			br.close();
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("File not found " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb;
	}
	
}

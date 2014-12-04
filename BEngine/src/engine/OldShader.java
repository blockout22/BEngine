package engine;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class OldShader {
	
	private int programID;
	private int vertexID;
	private int fragmentID;
	
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * 
	 * @param vertexShader
	 * @param fragmentShader
	 */
	public OldShader(String vertexShader, String fragmentShader)
	{
		vertexID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		fragmentID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		
		createProgram();
	}
	
	private void createProgram() {
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexID);
		GL20.glAttachShader(programID, fragmentID);
	}
	
	public void bindAttribLocation(int attrib, String name)
	{
		GL20.glBindAttribLocation(programID, attrib, name);
	}
	
	/**
	 * call this after bindAttribLocation();
	 */
	public void linkAndValidate()
	{
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
//		GL20.glUseProgram(programID);
	}
	
	public int getUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	public void bind()
	{
		GL20.glUseProgram(programID);
	}
	
	public void unbind()
	{
		GL20.glUseProgram(0);
	}
	
	public void getUniform(int value, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		
		matrixBuffer.flip();
		GL20.glUniformMatrix4(value, false, matrixBuffer);
	}

	private int loadShader(String fileName, int type)
	{
		StringBuilder shaderSource = Util.loadFile(fileName);
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.err.println(GL20.glGetShaderInfoLog(shaderID, 1000));
			Storage.cleanup();
			System.exit(-1);
		}
		
		return shaderID;
	}

}

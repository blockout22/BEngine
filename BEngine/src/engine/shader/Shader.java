package engine.shader;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import engine.Storage;
import engine.Util;

public class Shader {
	
	private int program;
	private int vertex;
	private int fragment;
	
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public Shader(String vertexShader, String fragmentShader)
	{
		vertex = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		fragment = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		
		createProgram();
	}
	
	private void createProgram() {
			program = GL20.glCreateProgram();
			GL20.glAttachShader(program, vertex);
			GL20.glAttachShader(program, fragment);
	}
	
	public void bindAttribLocation(int index, String name)
	{
		GL20.glBindAttribLocation(program, index, name);
	}
	
	public void linkAndValidate()
	{
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
	}
	
	public void bind()
	{
		GL20.glUseProgram(program);
	}
	
	public void unbind()
	{
		GL20.glUseProgram(0);
	}
	
	public int getUniformLocation(String uniform)
	{
		return GL20.glGetUniformLocation(program, uniform);
	}
	
	public void loadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
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

	public FloatBuffer getMatrixBuffer() {
		return matrixBuffer;
	}
}

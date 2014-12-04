package engine.archive;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.Storage;
import engine.Util;
import engine.entity.Entity;

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

	public Matrix4f loadProjectionMatrix() {
		Matrix4f projectionMatrix = new Matrix4f();

//		float y_scale = 1f / (float) Math.tan(Math.toRadians(Camera2.getFIELD_OF_VIEW() / 2f));
//		float x_scale = y_scale / Camera2.getASPECT_RATIO();
//		float frustum_length = Camera2.getZ_FAR() - Camera2.getZ_NEAR();
//
//		projectionMatrix.m00 = x_scale;
//		projectionMatrix.m11 = y_scale;
//		projectionMatrix.m22 = -((Camera2.getZ_FAR() + Camera2.getZ_NEAR()) / frustum_length);
//		projectionMatrix.m23 = -1;
//		projectionMatrix.m32 = -((2 * Camera2.getZ_NEAR() * Camera2.getZ_FAR()) / frustum_length);
//		projectionMatrix.m33 = 0;
		
		return projectionMatrix;
	}

	public void loadTransformationMatrix(int location, Entity entity) {
		Matrix4f transformationMatrix = createTransformationMatrix(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
		getUniform(location, transformationMatrix);
	}

	private Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		
		return matrix;
	}

	public void loadProjectionMatrix(int projectionMatrix) {
		Matrix4f matrix = loadProjectionMatrix();
		getUniform(projectionMatrix, matrix);
	}

}

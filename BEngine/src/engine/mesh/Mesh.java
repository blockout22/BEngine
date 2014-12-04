package engine.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import engine.Storage;
import engine.Util;
import engine.camera.Camera;
import engine.entity.Entity;
import engine.math.Maths;
import engine.shader.Shader;

public class Mesh {

	private int vaoID;
	private int vboID;
	private int vboTextureID;
	private int vboiID;

	private int indicesSize;

	private int textureID = 1;
	
	private Shader shader;

	public Mesh(int projectionMatrix_Location, Shader shader, String textureFile, Camera camera) {
		vaoID = Storage.addVAO();
		vboID = Storage.addVBO();
		vboTextureID = Storage.addVBO();
		vboiID = Storage.addVBO();

		textureID = loadTexture(textureFile);

		this.shader = shader;
		shader.bind();
		//Projection Matrix
		shader.loadMatrix(projectionMatrix_Location, camera.getProjectionMatrix());
		shader.unbind();
	}

	public void add(Vector3f[] vertices, Vector2f[] texCoords, int[] indices) {
		indicesSize = indices.length;

		GL30.glBindVertexArray(vaoID);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTextureID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(texCoords), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL30.glBindVertexArray(0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void add(float[] vertices, float[] texCoords, int[] indices) {
		indicesSize = indices.length;

		GL30.glBindVertexArray(vaoID);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTextureID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(texCoords), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL30.glBindVertexArray(0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void draw(int modelMatrix_Location, Entity entity) {
		GL30.glBindVertexArray(vaoID);

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		Matrix4f transformatrionMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
		//Model Matrix
		shader.loadMatrix(modelMatrix_Location, transformatrionMatrix);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesSize, GL11.GL_UNSIGNED_INT, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);

		GL30.glBindVertexArray(0);
	}

	private int loadTexture(String textureFile) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(textureFile));
			System.out.println("Width: " + texture.getImageWidth() + " Height: " + texture.getImageHeight());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Can't find texture: " + textureFile);
			System.out.println("Using Default Texture");
			return 0;
		}

		textureID = texture.getTextureID();
		Storage.addTexture(textureID);
		System.out.println("TexturedMesh TextureID: " + textureID);
		return textureID;
	}

}

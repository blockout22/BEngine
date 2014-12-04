package engine.archive;
//package engine;
//
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL13;
//import org.lwjgl.opengl.GL15;
//import org.lwjgl.opengl.GL20;
//import org.lwjgl.opengl.GL30;
//import org.lwjgl.util.vector.Vector2f;
//import org.lwjgl.util.vector.Vector3f;
//import org.newdawn.slick.opengl.Texture;
//import org.newdawn.slick.opengl.TextureLoader;
//import org.newdawn.slick.util.ResourceLoader;
//
//public class OldTexturedMesh {
//
//	private int vaoID;
//	private int vboID;
//	private int vboTextureID;
//	private int vboiID;
//
//	private int indicesSize;
//
//	private int TextureID = 1;
//	
//	public OldTexturedMesh(Shader shader, String textureFile) {
//		vaoID = Storage.addVAO();
//		vboID = Storage.addVBO();
//		vboTextureID = Storage.addVBO();
//		vboiID = Storage.addVBO();
//
//		TextureID = loadTexture(textureFile);
//		shader.bind();
//		shader.loadProjectionMatrix(1);
//		shader.unbind();
//	}
//
//	public OldTexturedMesh(String textureFile) {
//		vaoID = Storage.addVAO();
//		vboID = Storage.addVBO();
//		vboTextureID = Storage.addVBO();
//		vboiID = Storage.addVBO();
//
//		TextureID = loadTexture(textureFile);
//	}
//
//	public void add(float[] vertices, float[] texCoords, int[] indices) {
//		indicesSize = indices.length;
//
//		GL30.glBindVertexArray(vaoID);
//
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);
//		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTextureID);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(texCoords), GL15.GL_STATIC_DRAW);
//		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//		GL30.glBindVertexArray(0);
//
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
//		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//	}
//
//	public void add(Vector3f[] vertices, Vector2f[] texCoords, int[] indices) {
//		indicesSize = indices.length;
//
//		GL30.glBindVertexArray(vaoID);
//
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);
//		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTextureID);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(texCoords), GL15.GL_STATIC_DRAW);
//		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//
//		GL30.glBindVertexArray(0);
//
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
//		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//	}
//	
//	public void addEntity(Entity entity)
//	{
//		
//	}
//	
//	public void drawEntity(Entity entity, Shader shader, int location)
//	{
//		GL30.glBindVertexArray(vaoID);
//
//		GL20.glEnableVertexAttribArray(0);
//		GL20.glEnableVertexAttribArray(1);
//		
//		shader.getUniform(location, entity.transformEntity(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale()));
//
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureID);
//		
////		shader.loadTransformationMatrix(entity);
//
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
//		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesSize, GL11.GL_UNSIGNED_INT, 0);
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//
//		GL20.glDisableVertexAttribArray(0);
//		GL20.glDisableVertexAttribArray(1);
//
//		GL30.glBindVertexArray(0);
//	}
//
//	public void draw() {
//		GL30.glBindVertexArray(vaoID);
//
//		GL20.glEnableVertexAttribArray(0);
//		GL20.glEnableVertexAttribArray(1);
//
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureID);
//
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
//		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesSize, GL11.GL_UNSIGNED_INT, 0);
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//
//		GL20.glDisableVertexAttribArray(0);
//		GL20.glDisableVertexAttribArray(1);
//
//		GL30.glBindVertexArray(0);
//	}
//
//	private int loadTexture(String textureFile) {
//		Texture texture = null;
//		try {
//			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(textureFile));
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.err.println("Can't find texture: " + textureFile);
//			System.out.println("Using Default Texture");
//			return 0;
//		}
//
//		TextureID = texture.getTextureID();
//		Storage.addTexture(TextureID);
//		System.out.println("TexturedMesh TextureID: " + TextureID);
//		return TextureID;
//	}
//
//}

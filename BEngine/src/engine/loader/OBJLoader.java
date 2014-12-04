package engine.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.camera.Camera;
import engine.mesh.Mesh;
import engine.shader.Shader;

public class OBJLoader {
	
	public static Mesh loadObjModel(String fileName, String textureFile, int projectionMatrix_Location, Shader shader, Camera camera)
	{
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		
		String line;
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		ArrayList<Float> xBox = new ArrayList<Float>(); 
		ArrayList<Float> yBox = new ArrayList<Float>(); 
		ArrayList<Float> zBox = new ArrayList<Float>(); 
		
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		
		try{
			while(true)
			{
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("v "))
				{
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					xBox.add(vertex.x);
					yBox.add(vertex.y);
					zBox.add(vertex.z);
					vertices.add(vertex);
				}else if(line.startsWith("vt "))
				{
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn "))
				{
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f "))
				{
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			
			while(line != null)
			{
				if(!line.startsWith("f "))
				{
					line = reader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				
				line = reader.readLine();
			}
			
			reader.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		
		for(Vector3f vertex : vertices)
		{
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++)
		{
			indicesArray[i] = indices.get(i);
		}
		
		Collections.sort(xBox);
		System.out.println("X Lowest: " + xBox.get(0) + "X Highest: " + xBox.get(xBox.size() - 1));
		
		Collections.sort(yBox);
		System.out.println("Y Lowest: " + yBox.get(0) + "Y Highest: " + yBox.get(yBox.size() - 1));
		
		Collections.sort(zBox);
		System.out.println("Z Lowest: " + zBox.get(0) + "Z Highest: " + zBox.get(zBox.size() - 1));
		
//		if(collisionBox)
//		{
//			float x = xBox.get(0);
//			float y = yBox.get(0);
//			float z = zBox.get(0);
//			
//			ArrayList<Float> tempList = new ArrayList<Float>();
//			tempList.add(xBox.get(0));
//			tempList.add(yBox.get(0));
//			tempList.add(zBox.get(0));
//			tempList.add(xBox.get(xBox.size() - 1));
//			tempList.add(yBox.get(yBox.size() - 1));
//			tempList.add(zBox.get(zBox.size() - 1));
//			Collections.sort(tempList);
//			float low = tempList.get(0);
//			float high = tempList.get(tempList.size() - 1);
//			int size = (int) (high - low);
//			System.out.println("X: " + x + " Y: " + y + " Z: " + z + "Size: " + size);
////			Camera.addCollisionBox1(x, y, z, size);
//		}
		
		Mesh mesh = new Mesh(projectionMatrix_Location, shader, textureFile, camera);
		mesh.add(verticesArray, textureArray, indicesArray);
		
		System.out.println("Done Loading OBJ");
		return mesh;
	}
	
	/**
	 * 
	 * @param fileName = obj file
	 * @param textureFile = texture to go with obj
	 * @param position = xyz offset position of model
	 * @return
	 */
//	public static Mesh loadObjModel(String fileName, String textureFile, Vector3f position)
//	{
//		FileReader fr = null;
//		try {
//			fr = new FileReader(new File(fileName));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		BufferedReader reader = new BufferedReader(fr);
//		
//		String line;
//		List<Vector3f> vertices = new ArrayList<Vector3f>();
//		List<Vector2f> textures = new ArrayList<Vector2f>();
//		List<Vector3f> normals = new ArrayList<Vector3f>();
//		List<Integer> indices = new ArrayList<Integer>();
//		
//		float[] verticesArray = null;
//		float[] normalsArray = null;
//		float[] textureArray = null;
//		int[] indicesArray = null;
//		
//		float x = position.x;
//		float y = position.y;
//		float z = position.z;
//		
//		try{
//			while(true)
//			{
//				line = reader.readLine();
//				String[] currentLine = line.split(" ");
//				
//				if(line.startsWith("v "))
//				{
//					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]) + x, Float.parseFloat(currentLine[2]) + y, Float.parseFloat(currentLine[3]) + z);
//					vertices.add(vertex);
//				}else if(line.startsWith("vt "))
//				{
//					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
//					textures.add(texture);
//				}else if(line.startsWith("vn "))
//				{
//					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
//					normals.add(normal);
//				}else if(line.startsWith("f "))
//				{
//					textureArray = new float[vertices.size() * 2];
//					normalsArray = new float[vertices.size() * 3];
//					break;
//				}
//			}
//			
//			while(line != null)
//			{
//				if(!line.startsWith("f "))
//				{
//					line = reader.readLine();
//					continue;
//				}
//				
//				String[] currentLine = line.split(" ");
//				
//				String[] vertex1 = currentLine[1].split("/");
//				String[] vertex2 = currentLine[2].split("/");
//				String[] vertex3 = currentLine[3].split("/");
//				
//				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
//				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
//				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
//				
//				line = reader.readLine();
//			}
//			
//			reader.close();
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		verticesArray = new float[vertices.size() * 3];
//		indicesArray = new int[indices.size()];
//		
//		int vertexPointer = 0;
//		
//		for(Vector3f vertex : vertices)
//		{
//			verticesArray[vertexPointer++] = vertex.x;
//			verticesArray[vertexPointer++] = vertex.y;
//			verticesArray[vertexPointer++] = vertex.z;
//		}
//		
//		for(int i = 0; i < indices.size(); i++)
//		{
//			indicesArray[i] = indices.get(i);
//		}
//		
//		TexturedMesh mesh = new TexturedMesh(textureFile);
//		mesh.add(verticesArray, textureArray, indicesArray);
//		return mesh;
//	}
//	
//	/**
//	 * 
//	 * @param fileName = obj file
//	 * @param textureFile = texture to go with obj
//	 * xyz offset position of model
//	 * @param x
//	 * @param y
//	 * @param z
//	 * @return
//	 */
//	public static TexturedMesh loadObjModel(String fileName, String textureFile, float x, float y, float z)
//	{
//		FileReader fr = null;
//		try {
//			fr = new FileReader(new File(fileName));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		BufferedReader reader = new BufferedReader(fr);
//		
//		String line;
//		List<Vector3f> vertices = new ArrayList<Vector3f>();
//		List<Vector2f> textures = new ArrayList<Vector2f>();
//		List<Vector3f> normals = new ArrayList<Vector3f>();
//		List<Integer> indices = new ArrayList<Integer>();
//		
//		float[] verticesArray = null;
//		float[] normalsArray = null;
//		float[] textureArray = null;
//		int[] indicesArray = null;
//		
//		try{
//			while(true)
//			{
//				line = reader.readLine();
//				String[] currentLine = line.split(" ");
//				
//				if(line.startsWith("v "))
//				{
//					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]) + x, Float.parseFloat(currentLine[2]) + y, Float.parseFloat(currentLine[3]) + z);
//					vertices.add(vertex);
//				}else if(line.startsWith("vt "))
//				{
//					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
//					textures.add(texture);
//				}else if(line.startsWith("vn "))
//				{
//					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
//					normals.add(normal);
//				}else if(line.startsWith("f "))
//				{
//					textureArray = new float[vertices.size() * 2];
//					normalsArray = new float[vertices.size() * 3];
//					break;
//				}
//			}
//			
//			while(line != null)
//			{
//				if(!line.startsWith("f "))
//				{
//					line = reader.readLine();
//					continue;
//				}
//				
//				String[] currentLine = line.split(" ");
//				
//				String[] vertex1 = currentLine[1].split("/");
//				String[] vertex2 = currentLine[2].split("/");
//				String[] vertex3 = currentLine[3].split("/");
//				
//				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
//				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
//				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
//				
//				line = reader.readLine();
//			}
//			
//			reader.close();
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		verticesArray = new float[vertices.size() * 3];
//		indicesArray = new int[indices.size()];
//		
//		int vertexPointer = 0;
//		
//		for(Vector3f vertex : vertices)
//		{
//			verticesArray[vertexPointer++] = vertex.x;
//			verticesArray[vertexPointer++] = vertex.y;
//			verticesArray[vertexPointer++] = vertex.z;
//		}
//		
//		for(int i = 0; i < indices.size(); i++)
//		{
//			indicesArray[i] = indices.get(i);
//		}
//		
//		TexturedMesh mesh = new TexturedMesh(textureFile);
//		mesh.add(verticesArray, textureArray, indicesArray);
//		return mesh;
//	}
//	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray)
	{
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}

}

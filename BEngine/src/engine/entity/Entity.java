package engine.entity;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.mesh.Mesh;

public class Entity {

	private Mesh mesh;
	private Vector3f position;
	private float rotationX, rotationY, rotationZ;
	private float scale;

	public Entity(Mesh mesh, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
		this.mesh = mesh;
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.scale = scale;
	}

	public Matrix4f transformEntity(Vector3f v3, float rotationX, float rotationY, float rotationZ, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(v3, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotationX), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotationY), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotationZ), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

		return matrix;
	}

	public void Position(float x, float y, float z) {
		this.position.x += x;
		this.position.y += y;
		this.position.z += z;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void setRotation(float x, float y, float z) {
		this.rotationX = x;
		this.rotationY = y;
		this.rotationZ = z;
	}

	public void rotate(float x, float y, float z) {
		this.rotationX += x;
		this.rotationY += y;
		this.rotationZ += z;
	}

	public void Position(Vector3f v3) {
		this.position.x += v3.x;
		this.position.y += v3.y;
		this.position.z += v3.z;
	}

	public void setPosition(Vector3f v3) {
		this.position.x = v3.x;
		this.position.y = v3.y;
		this.position.z = v3.z;
	}

	public void setRotation(Vector3f v3) {
		this.rotationX = v3.x;
		this.rotationY = v3.y;
		this.rotationZ = v3.z;
	}

	public void rotate(Vector3f v3) {
		this.rotationX += v3.x;
		this.rotationY += v3.y;
		this.rotationZ += v3.z;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}

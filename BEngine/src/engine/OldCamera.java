package engine;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class OldCamera {

	private Matrix4f modelMatrix, viewMatrix, projectionMatrix;
	private Vector3f modelPos, modelAngle, modelScale, cameraPos;

//	private boolean isJumping = false;

	// private CollisionCube cc;
	// private Mesh mesh;

	private ArrayList<Vector3f> startPositions = new ArrayList<Vector3f>();
	private ArrayList<Vector3f> endPositions = new ArrayList<Vector3f>();

//	private float scaleDelta = 0.1f;
//	private Vector3f scaleAddResolution = new Vector3f(scaleDelta, scaleDelta, scaleDelta);
//	private Vector3f scaleMinusResolution = new Vector3f(-scaleDelta, -scaleDelta, -scaleDelta);

	private boolean canMoveXPlus, canMoveXMinus, canMoveYPlus, canMoveYMinus, canMoveZPlus, canMoveZMinus;

	private float FIELD_OF_VIEW;
	private float ASPECT_RATIO;
	private float Z_NEAR;
	private float Z_FAR;

	public OldCamera(float fov, float aspectRatio, float z_near, float z_far) {
		this.FIELD_OF_VIEW = fov;
		this.ASPECT_RATIO = aspectRatio;
		this.Z_NEAR = z_near;
		this.Z_FAR = z_far;

		setProjection();

		modelMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();

		modelPos = new Vector3f(0, 0, 0);
		modelAngle = new Vector3f(0, 0, 0);
		modelScale = new Vector3f(1, 1, 1);
		cameraPos = new Vector3f(0, 0, -10);

		// cc = new CollisionCube();

		// floor collision
		addCollisionBox(-1000000, 0, -1000000, 1000000000);
	}

	private void setProjection() {
		projectionMatrix = new Matrix4f();

		float y_scale = 1f / (float) Math.tan(Math.toRadians(FIELD_OF_VIEW / 2f));
		float x_scale = y_scale / ASPECT_RATIO;
		float frustum_length = Z_FAR - Z_NEAR;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((Z_FAR + Z_NEAR) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * Z_NEAR * Z_FAR) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public void setCameraPos(Vector3f pos) {
		cameraPos = pos;
	}

	public void moveForward(float amount) {
		getCollision();
		if (canMoveZPlus) {
			cameraPos.z += amount;
		}
		// modelPos.z += amount;
		// Vector3f.add(modelScale, scaleAddResolution, modelScale);
	}

	public void moveBack(float amount) {
		getCollision();
		if (canMoveZMinus) {
			cameraPos.z -= amount;
		}
		// modelPos.z -= amount;
		// Vector3f.add(modelScale, scaleMinusResolution, modelScale);
	}

	public void moveLeft(float amount) {
		getCollision();
		if (canMoveXPlus) {
			cameraPos.x += amount;
		}
		// modelPos.x += amount;
	}

	public void moveRight(float amount) {
		getCollision();
		if (canMoveXMinus) {
			cameraPos.x -= amount;
		}

		// modelPos.x -= amount;
	}

	public void moveUp(float amount) {
		getCollision();
		if (canMoveYMinus) {
			cameraPos.y -= amount;
		}
	}

	public void moveDown(float amount) {
		getCollision();
		if (canMoveYPlus) {
			cameraPos.y += amount;
		}
	}

	public void rotate(float x, float y) {
		modelAngle.y += x;
		modelAngle.x -= y;
	}

	public void jump() {
		// TODO
	}

	public Matrix4f rotateX() {
		float pitch = modelAngle.x;

		Matrix4f m = new Matrix4f();
		m.m00 = 1;
		m.m10 = 0;
		m.m20 = 0;
		m.m30 = 0;
		m.m01 = 0;
		m.m11 = (float) Math.cos(pitch);
		m.m21 = (float) Math.sin(pitch);
		m.m02 = 0;
		m.m03 = 0;
		m.m12 = (float) -Math.sin(pitch);
		m.m22 = (float) Math.cos(pitch);
		m.m23 = 0;
		m.m03 = 0;
		m.m13 = 0;
		m.m23 = 0;
		m.m33 = 1;
		return m;
	}

	public void useView() {
		modelMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();

		Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);
		Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
		Matrix4f.translate(modelPos, modelMatrix, modelMatrix);

		// TODO;
		Matrix4f.rotate((float) Math.toRadians(modelAngle.x), new Vector3f(1, 0, 0), modelMatrix, modelMatrix);
		Matrix4f.rotate((float) Math.toRadians(modelAngle.y), new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
		Matrix4f.rotate((float) Math.toRadians(modelAngle.z), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
	}

	private void getCollision() {
		// mesh.draw();
		canMoveXPlus = true;
		canMoveXMinus = true;
		canMoveYPlus = true;
		canMoveYMinus = true;
		canMoveZPlus = true;
		canMoveZMinus = true;
		for (Vector3f start : startPositions) {
			for (Vector3f end : endPositions) {

				boolean xStart = cameraPos.getX() > start.getX();
				boolean yStart = cameraPos.getY() > start.getY();
				boolean zStart = cameraPos.getZ() > start.getZ();

				boolean xEnd = cameraPos.getX() < end.getX();
				boolean yEnd = cameraPos.getY() < end.getY();
				boolean zEnd = cameraPos.getZ() < end.getZ();

				if (xStart && xEnd && yStart && yEnd && zStart && zEnd) {
					System.out.println("Collision!");
					// x first pos & x second pos
					float xfpos = Math.abs(cameraPos.getX() - start.getX());
					float xspos = Math.abs(cameraPos.getX() - end.getX());

					// y first pos & y second pos
					float yfpos = Math.abs(cameraPos.getY() - start.getY());
					float yspos = Math.abs(cameraPos.getY() - end.getY());

					// z first pos & z second pos
					float zfpos = Math.abs(cameraPos.getZ() - start.getZ());
					float zspos = Math.abs(cameraPos.getZ() - end.getZ());

					if (xfpos < xspos) {
						// cameraPos.x -= 0.1f;
						canMoveXPlus = false;
					}

					if (xfpos > xspos) {
						// cameraPos.x += 0.1f;
						canMoveXMinus = false;
					}

					if (yfpos < yspos) {
						// cameraPos.y -= 0.1f;
						canMoveYPlus = false;
					}

					if (yfpos > yspos) {
						// cameraPos.y += 0.1f;
						canMoveYMinus = true;
					}

					if (zfpos < zspos) {
						// cameraPos.z -= 0.1f;
						canMoveZPlus = true;
					}

					if (zfpos > zspos) {
						// cameraPos.z += 0.1f;
						canMoveZMinus = true;
					}
				}
			}
		}
	}

	/**
	 * add a collision box starting at xyz coords and ending at xyz + size
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param size
	 */
	public void addCollisionBox(float x, float y, float z, int size) {
		float xStart = x;
		float yStart = y;
		float zStart = z;
		startPositions.add(new Vector3f(xStart, yStart, zStart));

		float xEnd = x + size;
		float yEnd = y + size;
		float zEnd = z + size;
		endPositions.add(new Vector3f(xEnd, yEnd, zEnd));

		// mesh = cc.addCollisionCube(xEnd, yEnd, zEnd);
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public Vector3f getCameraPos()
	{
		return cameraPos;
	}

	public String getCameraPosAsString() {
		return "X: " + cameraPos.getX() + " Y: " + cameraPos.getY() + " Z: " + cameraPos.getZ();
	}
}

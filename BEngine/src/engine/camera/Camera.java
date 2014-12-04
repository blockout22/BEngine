package engine.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float moveSpeed = 0.2f;

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;

	private float FIELD_OF_VIEW;
	private float ASPECT_RATIO;
	private float Z_NEAR;
	private float Z_FAR;
	
	private Matrix4f projectionMatrix;

	public Camera(float fov, float aspectRatio, float z_near, float z_far) {
		this.FIELD_OF_VIEW = fov;
		this.ASPECT_RATIO = aspectRatio;
		this.Z_NEAR = z_near;
		this.Z_FAR = z_far;
		
		createProjectionMatrix();
	}
	
	public void moveDirection(float amount, float direction)
    {
        position.z += amount * Math.sin(Math.toRadians(calculateAngle(yaw) + 90 * direction));
        position.x += amount * Math.cos(Math.toRadians(calculateAngle(yaw) + 90 * direction));
    }
	
	public void move()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.x += Math.sin(yaw * Math.PI / 180) * moveSpeed;
			position.z += -Math.cos(yaw * Math.PI / 180) * moveSpeed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += Math.sin((yaw + 90) * Math.PI / 180) * moveSpeed;
			position.z += -Math.cos((yaw + 90) * Math.PI / 180) * moveSpeed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x += Math.sin((yaw - 90) * Math.PI / 180) * moveSpeed;
			position.z += -Math.cos((yaw - 90) * Math.PI / 180) * moveSpeed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.x -= Math.sin(yaw * Math.PI / 180) * moveSpeed;
			position.z -= -Math.cos(yaw * Math.PI / 180) * moveSpeed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += moveSpeed;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{
			position.y -= moveSpeed;
		}
		
		if(Mouse.isButtonDown(2))
		{
			changePitch(Mouse.getDY() * 0.1f);
		}
		
		if(Mouse.isButtonDown(2))
		{
			changeYaw(Mouse.getDX() * 0.1f);
		}
		
		if(pitch > 89)
		{
			pitch = 89;
		}
		
		if(pitch < -89)
		{
			pitch = -89;
		}
	}
	
	public void changeYaw(float amount)
	{
		this.yaw += amount;
	}
	
	public void changePitch(float amount)
	{
		this.pitch -= amount;
	}
	
	private  float calculateAngle(float yaw)
	{
		float angle = (yaw / 720) * 360;
		
		return angle;
	}
	
	private void createProjectionMatrix()
	{
		float aspectRatio = (float)Display.getWidth() / (float)Display.getHeight();
		float y_scale = 1f / (float) Math.tan(Math.toRadians(FIELD_OF_VIEW / 2f)) * aspectRatio;
		float x_scale = y_scale / aspectRatio;
		float frustum_length = Z_FAR - Z_NEAR;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((Z_FAR + Z_NEAR) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * Z_NEAR * Z_FAR) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getFIELD_OF_VIEW() {
		return FIELD_OF_VIEW;
	}

	public void setFIELD_OF_VIEW(float fIELD_OF_VIEW) {
		FIELD_OF_VIEW = fIELD_OF_VIEW;
	}

	public float getASPECT_RATIO() {
		return ASPECT_RATIO;
	}

	public void setASPECT_RATIO(float aSPECT_RATIO) {
		ASPECT_RATIO = aSPECT_RATIO;
	}

	public float getZ_NEAR() {
		return Z_NEAR;
	}

	public void setZ_NEAR(float z_NEAR) {
		Z_NEAR = z_NEAR;
	}

	public float getZ_FAR() {
		return Z_FAR;
	}

	public void setZ_FAR(float z_FAR) {
		Z_FAR = z_FAR;
	}

}

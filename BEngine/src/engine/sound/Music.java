package engine.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import engine.camera.Camera;

public class Music {

	private boolean isPlaying;
	private Clip clip;

	private String fileName;

	private float volume = 0.0f;

	public Music(String fileName) {
		this.fileName = fileName;
	}

	public void playSound() {
		if (!isPlaying) {
			new Thread(new Runnable() {
				public void run() {
					try {
						clip = AudioSystem.getClip();
						InputStream inputStream = new FileInputStream(new File(fileName));
						InputStream bufferedIn = new BufferedInputStream(inputStream);
						AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
						clip.open(ais);
						FloatControl volControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
						volControl.setValue(volume);
						clip.start();

						isPlaying = true;

						while (isPlaying) {
							try {
								Thread.sleep(1000);
								if (volControl.getValue() != volume) {
									if (volume < -80f) {
										volume = -80f;
									}

									if (volume > 6f) {
										volume = 6f;
									}
									volControl.setValue(volume);
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						clip.stop();
						isPlaying = false;
					} catch (LineUnavailableException e) {
						isPlaying = false;
						e.printStackTrace();
					} catch (UnsupportedAudioFileException e) {
						isPlaying = false;
						e.printStackTrace();
					} catch (IOException e) {
						isPlaying = false;
						e.printStackTrace();
					}
				}
			}).start();
		}else
		{
			new Thread(new Runnable() {
				public void run() {
					System.err.println("Music Already Playing");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	/**
	 * @param cam
	 * @param x
	 * @param y
	 * @param z
	 */
	public synchronized void playAtLocation(Camera cam, float x, float y, float z, float distance)
	{
		float lastX = cam.getPosition().x + distance;
		float lastY = cam.getPosition().y + distance;
		float lastZ = cam.getPosition().z + distance;
		if(!isPlaying)
		{
			if(cam.getPosition().x > x && cam.getPosition().x < lastX && cam.getPosition().y > y && cam.getPosition().y < lastY && cam.getPosition().z > z && cam.getPosition().z < lastZ)
			{
				new Thread(new Runnable() {
					public void run() {
						try {
							isPlaying = true;
							clip = AudioSystem.getClip();
							InputStream inputStream = new FileInputStream(new File(fileName));
							InputStream bufferedIn = new BufferedInputStream(inputStream);
							AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
							clip.open(ais);
							FloatControl volControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
							volControl.setValue(volume);
							clip.start();

							while (isPlaying) {
								try {
									Thread.sleep(1000);
									if (volControl.getValue() != volume) {
										if (volume < -80f) {
											volume = -80f;
										}

										if (volume > 6f) {
											volume = 6f;
										}
										volControl.setValue(volume);
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							clip.stop();
							isPlaying = false;
						} catch (LineUnavailableException e) {
							isPlaying = false;
							e.printStackTrace();
						} catch (UnsupportedAudioFileException e) {
							isPlaying = false;
							e.printStackTrace();
						} catch (IOException e) {
							isPlaying = false;
							e.printStackTrace();
						}
					}
				}).start();
			}else
			{
//				System.out.println(cam.getCameraPos().x);
			}
		}else
		{
			new Thread(new Runnable() {
				public void run() {
//					System.err.println("Music Already Playing At Location");
					//cam.getCameraPos().x > x && cam.getCameraPos().x < lastX && cam.getCameraPos().y > y && cam.getCameraPos().y < lastY && cam.getCameraPos().z > z && cam.getCameraPos().z < lastZ
//					if(cam.getCameraPos().x < x && cam.getCameraPos().x > lastX && cam.getCameraPos().y < y && cam.getCameraPos().y > lastY && cam.getCameraPos().z < z && cam.getCameraPos().z > lastZ)
					if(cam.getPosition().x > x && cam.getPosition().x < lastX && cam.getPosition().y > y && cam.getPosition().y < lastY && cam.getPosition().z > z && cam.getPosition().z < lastZ)
					{
					}else
					{
						isPlaying = false;
					}
					try {
						Thread.sleep(10000);
						return;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public void stop() {
		isPlaying = false;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getVolume() {
		return volume;
	}

}

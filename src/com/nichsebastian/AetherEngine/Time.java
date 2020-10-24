package com.nichsebastian.AetherEngine;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

public class Time {
	
	public static float deltaTime = 0.0f;
	
	private static float gameBegin = (float) glfwGetTime();
	private static float frameBegin = gameBegin;
	private static float now = gameBegin;
	private static float secondTimer = gameBegin;
	
	public static float getTime() { return gameBegin; }
	
	public static void onFrameBegin() {
		now = (float) glfwGetTime();
		
		if (now - secondTimer > 1) {
			glfwSetWindowTitle(
				Application.getWindow(), 
				String.format("%s - %d FPS", Application.getTitle(), Math.round(1.0f / deltaTime)));
			secondTimer = now;
		}
	}
	
	public static void onFrameEnd() {
		deltaTime = now - frameBegin;
		frameBegin = now;
	}
}

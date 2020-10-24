package com.nichsebastian.AetherEngine;

import static org.lwjgl.glfw.GLFW.*;
import com.nichsebastian.AetherEngine.Types.Vector2;

public class Input {
	
	private static KeyboardListener keyboard = new KeyboardListener();
	private static MouseListener mouse = new MouseListener();
	
	public static boolean getKey(int keyCode) {
		if (keyCode >= keyboard.keyPressed.length) return false;
		return keyboard.keyPressed[keyCode];
	}
	
	public static boolean getKeyDown(int keyCode) {
		if (keyCode >= keyboard.keyPressed.length) return false;
		
		if (!keyboard.keyDown[keyCode] && keyboard.keyPressed[keyCode]) {
			keyboard.keyDown[keyCode] = true;
			return true;
		}
		return false;
	}
	
	public static boolean getKeyUp(int keyCode) {
		if (keyCode >= keyboard.keyPressed.length) return false;
		
		if (keyboard.keyUp[keyCode] && !keyboard.keyPressed[keyCode]) {
			keyboard.keyUp[keyCode] = false;
			return true;
		}
		return false;
	}
	
	public static boolean getMouseButton(int button) {
		if (button >= mouse.mouseButtonPressed.length) return false;
		return mouse.mouseButtonPressed[button];
	}
	
	public static boolean getMouseButtonDown(int button) {
		if (button >= mouse.mouseButtonPressed.length) return false;
		
		if (!mouse.mouseButtonDown[button] && mouse.mouseButtonPressed[button]) {
			mouse.mouseButtonDown[button] = true;
			return true;
		}
		return false;
	}
	
	public static boolean getMouseButtonUp(int button) {
		if (button >= mouse.mouseButtonPressed.length) return false;
		
		if (mouse.mouseButtonUp[button] && !mouse.mouseButtonPressed[button]) {
			mouse.mouseButtonUp[button] = false;
			return true;
		}
		return false;
	}
	
	public static Vector2 getMousePosition() {
		return new Vector2((float) mouse.positionX, (float) mouse.positionY);
	}
	
	public static Vector2 getMouseAcceleration() {
		return new Vector2(
			(float) (mouse.lastX - mouse.lastX), 
			(float) (mouse.lastY - mouse.lastY));
	}
	
	public static Vector2 getMouseScroll() {
		return new Vector2((float) mouse.scrollX, (float) mouse.scrollY);
	}
	
	public static boolean isMouseDragging() {
		return mouse.isDragging;
	}
	
	public static void resetMouseAxes() {
		mouse.lastX = mouse.positionX;
		mouse.lastY = mouse.positionY;
		mouse.scrollX = 0;
		mouse.scrollY = 0;
	}
	
	private static class KeyboardListener {

		public boolean keyPressed[] = new boolean[350];
		public boolean keyDown[] = new boolean[350];
		public boolean keyUp[] = new boolean[350];
		
		private KeyboardListener() {
			glfwSetKeyCallback(Application.getWindow(), (window, key, scancode, action, mods) -> {
				if (key >= this.keyPressed.length) return;
				
				if (action == GLFW_PRESS) {
					this.keyPressed[key] = true;
					this.keyUp[key] = true;
				}
				else if (action == GLFW_RELEASE) {
					this.keyPressed[key] = false;
					this.keyDown[key] = false;
					
					if (key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(window, true);
				}
			});
		}
	}
	
	private static class MouseListener {

		public double positionX, positionY, lastX, lastY;
		public double scrollX, scrollY;
		
		public boolean mouseButtonPressed[] = new boolean[3];
		public boolean mouseButtonDown[] = new boolean[3];
		public boolean mouseButtonUp[] = new boolean[3];
		
		public boolean isDragging;
		
		private MouseListener() {
			this.positionX = 0.0f;
			this.positionY = 0.0f;
			this.lastX = 0.0f;
			this.lastY = 0.0f;
			this.scrollX = 0.0f;
			this.scrollY = 0.0f;
			
			glfwSetCursorPosCallback(Application.getWindow(), (window, positionX, positionY) -> {
				this.lastX = this.positionX;
				this.lastY = this.positionY;
				
				this.positionX = positionX;
				this.positionY = positionY;
				
				this.isDragging = this.mouseButtonPressed[0] || this.mouseButtonPressed[1] || this.mouseButtonPressed[2];
			});
			
			glfwSetMouseButtonCallback(Application.getWindow(), (window, button, action, mods) -> {
				if (button >= this.mouseButtonPressed.length) return;
				
				if (action == GLFW_PRESS) {
					this.mouseButtonPressed[button] = true;
					this.mouseButtonUp[button] = true;
				}
				else if (action == GLFW_RELEASE) {
					this.mouseButtonPressed[button] = false;
					this.mouseButtonDown[button] = false;
					this.isDragging = false;
				}
			});
			
			glfwSetScrollCallback(Application.getWindow(), (window, offsetX, offsetY) -> {
				this.scrollX = offsetX;
				this.scrollY = offsetY;
			});
		}
	}
}

package com.nichsebastian.AetherEngine;

import java.nio.IntBuffer;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import com.nichsebastian.AetherEngine.Types.Scene;
import com.nichsebastian.Game.*;

public class Application {
	
	private static Window window;
	
	private static Scene scenes[] = { new GameScene(), new TestScene() };	// TODO: use org.reflections to find all instances of Scene
	private static int currentScene;
	
	public static void main(String[] args) {
		currentScene = 0;
		window = new Window("Aether Engine", 1280, 720, true, false);
		window.run();
	}
	
	public static long getWindow() {
		return window.windowAddress;
	}
	
	public static String getTitle() {
		return window.title;
	}
	
	public static void setTitle(String title) {
		window.title = title;
	}
	
	public static void changeScene(int index) {
		currentScene = index;
		scenes[currentScene].initialize();
	}
	
	private static class Window {
		
		private String title;
		private int width, height;
		private boolean resizable, maximized;
		
		private long windowAddress;	// window*
		
		private Window(String title, int width, int height, boolean resizable, boolean maximized) {
			this.title = title;
			this.width = width;
			this.height = height;
			this.resizable = resizable;
			this.maximized = maximized;
		}
		
		public void run() {
			System.out.println("Running on LWJGL version " + Version.getVersion());
			
			init();
			loop();
			
			glfwFreeCallbacks(windowAddress);
			glfwDestroyWindow(windowAddress);
			
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}
		
		private void init() {
			GLFWErrorCallback.createPrint(System.err).set();
			
			if (!glfwInit()) {
				throw new IllegalStateException("Unable to initialize GLFW");
			}
			
			glfwDefaultWindowHints();
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
			glfwWindowHint(GLFW_RESIZABLE, this.resizable ? GLFW_TRUE : GLFW_FALSE);
			glfwWindowHint(GLFW_MAXIMIZED, this.maximized ? GLFW_TRUE : GLFW_FALSE);
			
			windowAddress = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
			if (windowAddress == NULL) {
				throw new RuntimeException("Failed to create the GLFW window");
			}
			
			// Get the thread stack and push a new frame.
			try (MemoryStack stack = stackPush()) {
				IntBuffer pWidth = stack.mallocInt(1); 	// int*
				IntBuffer pHeight = stack.mallocInt(1); // int*

				glfwGetWindowSize(windowAddress, pWidth, pHeight);
				GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

				glfwSetWindowPos(
					windowAddress,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
				);
			} 	// the stack frame is popped automatically.
			
			glfwMakeContextCurrent(windowAddress);		// set the window as the current OpenGL context.
			GL.createCapabilities();					// loads the OpenGL bindings.
			
			glfwSwapInterval(1);						// v-sync.
			glfwShowWindow(windowAddress);
			
			scenes[currentScene].initialize();
		}
		
		private void loop() {
			glClearColor(0.39f, 0.58f, 0.93f, 0.0f);
			
			while (!glfwWindowShouldClose(windowAddress)) {
				Time.onFrameBegin();
				
				glfwPollEvents();
				scenes[currentScene].update();
				
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				scenes[currentScene].render();
				glfwSwapBuffers(windowAddress);
				
				Time.onFrameEnd();
			}
		}
	}
}

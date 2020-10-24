package com.nichsebastian.Game;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;

import com.nichsebastian.AetherEngine.Application;
import com.nichsebastian.AetherEngine.Input;
import com.nichsebastian.AetherEngine.Types.Scene;
import com.nichsebastian.AetherEngine.Types.Shader;

public class GameScene extends Scene {
	
	private Shader shader;
	private int vaoID, vboID, eboID;
	
	private float vertexArray[] = {
		// position				// color
		0.5f, -0.5f, 0.0f,		1.0f, 0.0f, 0.0f, 1.0f,		// bottom right		0
		-0.5f, 0.5f, 0.0f, 		0.0f, 1.0f, 0.0f, 1.0f,		// top left			1
		0.5f, 0.5f, 0.0f,		0.0f, 0.0f, 1.0f, 1.0f,		// top right		2
		-0.5f, -0.5f, 0.0f,		1.0f, 1.0f, 0.0f, 1.0f		// bottom left		3
	};
	
	private int elementArray[] = {	// REMINDER: must be in counter-clockwise order of vertices.
		2, 1, 0,	// top right triangle
		0, 1, 3		// bottom left triangle
	};

	@Override
	public void initialize() {
		shader = new Shader("assets/shaders/default.glsl");
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();
		
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();
		
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
		
		int positionSize = 3;
		int colorSize = 4;
		int vertexSizeInBytes = (positionSize + colorSize) * Float.BYTES;
		
		glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeInBytes, 0);
		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionSize * Float.BYTES);
	}

	@Override
	public void update() {
		
		if (Input.getKeyUp(GLFW_KEY_SPACE) || Input.getMouseButtonUp(1)) {
			System.out.println("Changing scene");
			Application.changeScene(1);
		}
	}

	@Override
	public void render() {
		shader.use();
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		shader.detach();
	}
}

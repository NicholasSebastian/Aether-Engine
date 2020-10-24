package com.nichsebastian.AetherEngine.Types;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

	private String filepath;
	private String vertexSource, fragmentSource;
	
	private int vertexID, fragmentID;
	private int shaderProgram;
	
	public Shader(String filepath) {
		this.filepath = filepath;
		loadFromFile();
		
		vertexID = compileShader(vertexSource, GL_VERTEX_SHADER);
		fragmentID = compileShader(fragmentSource, GL_FRAGMENT_SHADER);
		
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragmentID);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
	}
	
	public void use() {
		glUseProgram(shaderProgram);
	}
	
	public void detach() {
		glUseProgram(0);
	}
	
	private int compileShader(String shaderSource, int shaderType) {
		int shaderID = glCreateShader(shaderType);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.out.println(String.format("ERROR: '%s'\n\tShader compilation failed.", this.filepath));
			System.out.println(glGetShaderInfoLog(shaderID));
			System.exit(-1);
		}
		return shaderID;
	}
	
	private void loadFromFile() {
		try {
			String source = new String(Files.readAllBytes(Paths.get(this.filepath)));
			String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
			
			int wordStart = source.indexOf("#type") + 6;
			int wordEnd = source.indexOf("\r\n", wordStart);
			String firstPattern = source.substring(wordStart, wordEnd).trim();
			
			wordStart = source.indexOf("#type", wordEnd) + 6;
			wordEnd = source.indexOf("\r\n", wordStart);
			String secondPattern = source.substring(wordStart, wordEnd).trim();
			
			if (firstPattern.equals("vertex") && secondPattern.equals("fragment")) {
				vertexSource = splitString[1];
				fragmentSource = splitString[2];
			}
			else if (firstPattern.equals("fragment") && secondPattern.equals("vertex")) {
				fragmentSource = splitString[1];
				vertexSource = splitString[2];
			}
			else {
				throw new IOException(
					String.format("Unexpected patterns '%s' and '%s' in '%s'", 
						firstPattern, secondPattern, filepath));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

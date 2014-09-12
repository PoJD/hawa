package cz.pojd.rpi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Simple library class for working with JNI (Java Native Interface). Edited the original version to use java.nio package in Java 7
 * 
 * @see http://frommyplayground.com/how-to-load-native-jni-library-from-jar
 *
 * @author Adam Heirnich &lt;adam@adamh.cz&gt;, http://www.adamh.cz
 */
public class NativeUtils {

    /**
     * Private constructor - this class will never be instanced
     */
    private NativeUtils() {
    }

    /**
     * Loads library from current JAR archive
     * 
     * The file from JAR is copied into system temporary directory and then loaded. The temporary file is deleted after exiting. Method uses String as
     * filename because the pathname is "abstract", not system-dependent.
     * 
     * @param filename
     *            The filename inside JAR as an absolute path, e.g. /package/File.ext
     * @throws IOException
     *             If temporary file creation or read/write operation fails
     * @throws IllegalArgumentException
     *             If source file (param path) does not exist
     * @throws IllegalArgumentException
     *             If the path is not absolute or if the filename is shorter than three characters (restriction of {@see
     *             File#createTempFile(java.lang.String, java.lang.String)}).
     */
    public static void loadLibraryFromJar(String path) throws IOException {
	Path inputPath = Paths.get(path);

	if (!inputPath.isAbsolute()) {
	    throw new IllegalArgumentException("The path has to be absolute, but found: " + inputPath);
	}

	String fileNameFull = inputPath.getFileName().toString();
	int dotIndex = fileNameFull.indexOf('.');
	if (dotIndex < 0 || dotIndex >= fileNameFull.length() - 1) {
	    throw new IllegalArgumentException("The path has to end with a file name and extension, but found: " + fileNameFull);
	}

	String fileName = fileNameFull.substring(0, dotIndex);
	String extension = fileNameFull.substring(dotIndex);

	Path target = Files.createTempFile(fileName, extension);
	File targetFile = target.toFile();
	targetFile.deleteOnExit();

	try (InputStream source = NativeUtils.class.getResourceAsStream(inputPath.toString())) {
	    if (source == null) {
		throw new FileNotFoundException("File " + inputPath + " was not found in classpath.");
	    }
	    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
	}
	// Finally, load the library
	System.load(target.toAbsolutePath().toString());
    }
}

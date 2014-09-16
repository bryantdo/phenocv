package src.ddpsc.phenocv.utility;

import org.opencv.core.Core;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author cjmcentee
 */
public class OpenCV {

    public static void Load() {
        String operatingSystem = System.getProperty("os.name");

        if (operatingSystem.startsWith("Windows")) {
            try {
                addLibraryPath(System.getProperty("user.dir") + "\\lib\\windows\\x86");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }

        else {
            System.out.println("Unknown operating system detected, attempting to load OpenCV library."
                + " Look in src.ddpsc.phenocv.utility.OpenCV#Load() to add your operating system to the loading process.");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }

    }

    /**
     * Adds a filepath to the java library path at runtime.
     *
     * Credit:
     * http://fahdshariff.blogspot.com/2011/08/changing-java-library-path-at-runtime.html
     *
     * @param pathToAdd
     * @throws Exception
     */
    private static void addLibraryPath(String pathToAdd) throws Exception{
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        //get array of paths
        final String[] paths = (String[])usrPathsField.get(null);

        //check if the path to add is already present
        for(String path : paths) {
            if(path.equals(pathToAdd)) {
                return;
            }
        }

        //add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length-1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }
}

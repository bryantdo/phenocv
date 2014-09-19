package src.ddpsc.phenocv.utility;

import org.opencv.core.Core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author cjmcentee
 */
public class OpenCV {

    private static String WINDOWS_FILEPATH_FAILURE =
            "For Windows systems, the OpenCV 2.4.9 java bindings .dll must be located at " +
            "\\lib\\windows\\x86 with respect to \"" + System.getProperty("user.dir") + "\".\n" +
            "The appropriate windows .dll is located in the repository for this project at " +
            "github.com/bryantdo/phenocv.";

    private static String UNKNOWN_OS_FAILURE =
            "Cannot load OpenCV native libraries because the operating system is not recognized.";

    public static void load() {
        String operatingSystem = System.getProperty("os.name");

        if (operatingSystem.startsWith("Windows")) {

            File path = new File(System.getProperty("user.dir") + "\\lib\\windows\\x86");

            if ( ! path.exists())
                failWith(WINDOWS_FILEPATH_FAILURE);

            try {
                addLibraryPath(path.getPath());
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            }
            catch (Exception e) {
                failWith(WINDOWS_FILEPATH_FAILURE);
            }
        }

        // Linux, Mac
        else if (operatingSystem.startsWith("Mac") || operatingSystem.startsWith("Linux")) {
            nu.pattern.OpenCV.loadShared();
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }

        else {
            failWith(UNKNOWN_OS_FAILURE + "\nThe detected operating system is: " + operatingSystem);
        }
    }

    private static void failWith(String failureMessage) {
        System.out.println(failureMessage);
        System.exit(-1);
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

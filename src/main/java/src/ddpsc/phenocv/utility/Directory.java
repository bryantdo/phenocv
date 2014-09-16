package src.ddpsc.phenocv.utility;

import java.io.File;

/**
 * @author cjmcentee
 */
public class Directory {

    public static void EnsurePathExistsFor(String filename) {
        File file = new File(filename);

        if (file.isDirectory()) {
            if ( ! file.exists())
                file.mkdir();
        }

        else {
            File parentDirectory = file.getParentFile();

            if ( ! parentDirectory.exists())
                parentDirectory.mkdir();
        }
    }
}

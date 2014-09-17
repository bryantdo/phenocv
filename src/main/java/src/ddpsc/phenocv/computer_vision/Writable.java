package src.ddpsc.phenocv.computer_vision;

/**
 * Indicates an object can be written to the file system.
 *
 * Writes an object to the file system to the provided
 * filename. Filename should not be a directory.
 *
 * @author cjmcentee
 */
public interface Writable {
    void writeTo(String filename);
}

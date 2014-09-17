package src.ddpsc.phenocv.computer_vision;

/**
 * Indicates an object has native JNI hooks tha have to manually
 * be released.
 *
 * OpenCV is written in C++ and therefore java cannot clear the memory
 * of its native objects. Any object that is releasable must be released
 * at the end of its lifetime or the system will suffer memory leaks.
 *
 * Do not initialize any classes that implement this interface without
 * creating explicit references to them and then releasing those references
 * at the end of their lifetime.
 *
 * E.G.:
 *      (new ColorImage("image.png")).writeTo("new destination.png");
 *
 * Will create a reference to a new color image from the file image.png,
 * but because no explicit reference is made, can create a memory leak.
 *
 * The better choice is:
 *      ColorImage image = new ColorImage("image.png");
 *      image.writeTo("new destination.png");
 *      image.release();
 *
 * This properly handles the the JNI memory references.
 *
 * @author cjmcentee
 */
public interface Releasable {
    void release();
}

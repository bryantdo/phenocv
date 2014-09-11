package src.ddpsc.phenocv.utility;

/**
 * Represents an object wrapper to the concept of the "dirty" tag
 * for an individual object.
 *
 * The dirty tag is a commonly used marker to indicate when either
 *      some part of an object has changed and requires recalculation
 *      of another part of the object,
 *
 *      or when a part of an object has yet to be instantiated
 *
 * This class represents the second use-case. If the object isn't
 * instantiated, falling CalculatedObjectFactory<OutType, InType>#getFrom()
 * will calculate it and return it. If the object has been calculated,
 * it returns the object.
 *
 * OutType is the type of the calculated object that is output
 * from getFrom().
 *
 * InType is the type of the object that is required (if any) for
 * the calculation of the output object.
 *
 * @author cjmcentee
 */
public abstract class CalculatedObjectFactory<OutType, InType> {

    protected OutType calculatedObject;

    protected CalculatedObjectFactory() {
        calculatedObject = null;
    }

    protected abstract OutType calculate(InType seed);

    public OutType getFrom(InType seed) {
        if (calculatedObject == null)
            return calculate(seed);
        else
            return calculatedObject;
    }
}

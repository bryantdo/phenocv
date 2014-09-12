package src.ddpsc.phenocv.utility;

/**
 * @author cjmcentee
 */
public abstract class ReversableObjectFactory<OutType, InType> extends CalculatedObjectFactory<OutType, InType> {

    public void setFrom(OutType out) {
        calculatedObject = out;
    }
}

package src.ddpsc.phenocv.utility;

import org.apache.commons.cli.Option;
import java.util.Comparator;

/**
 * User: bryantd
 * Date: 9/17/14
 * Time: 12:07 PM
 * Description:
 */
public class OptionComparator<T extends Option> implements Comparator<T> {
  private static final String OPTS_ORDER = "tpo";

  public int compare(T o1, T o2) {
    return OPTS_ORDER.indexOf(o1.getOpt()) - OPTS_ORDER.indexOf(o2.getOpt());
  }
}

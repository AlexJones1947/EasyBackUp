package easybackup.place.ispl.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by infinium on 15/06/17.
 */
@Retention(CLASS) @Target(FIELD)
@interface Bind {
    int[] value();
}

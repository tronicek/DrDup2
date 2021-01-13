package p114;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.LOCAL_VARIABLE, ElementType.METHOD})
public @interface Source2 {

    String author();

    String version();
}

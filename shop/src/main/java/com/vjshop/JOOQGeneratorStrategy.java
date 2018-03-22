package com.vjshop;

import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;

/**
 * @author ck
 * @date 2017/5/8 0008
 */
public class JOOQGeneratorStrategy extends DefaultGeneratorStrategy {

    private String replacePrefix = "t_";

    @Override
    public String getJavaClassName(final Definition definition, final Mode mode) {
        /*String javaClassName = super.getJavaClassName(definition, mode);
        if (definition.getOutputName().startsWith(replacePrefix)) {
            return javaClassName.substring(1, javaClassName.length());
        } else {*/
            return super.getJavaClassName(definition, mode);
//        }
    }

}

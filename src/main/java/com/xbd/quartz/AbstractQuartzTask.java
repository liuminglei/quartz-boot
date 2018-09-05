package com.xbd.quartz;

public abstract class AbstractQuartzTask {

    public final static String DEFAULT_TARGETMETHOD = "execute";

    public abstract void execute() throws Exception;

}

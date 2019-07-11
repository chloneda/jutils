package com.chloneda.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chloneda
 * Description:
 */
public class SysLog {
    private static final Logger LOG= LoggerFactory.getLogger(SysLog.class);

    public static void main(String[] args) {
        String s="abc";
        System.out.println(s);
        LOG.debug(s);
        LOG.info(s);
        LOG.error(s);
        System.out.println("----------------");
        LOG.trace("Trace Message!");
        LOG.debug("Debug Message!");
        LOG.info("Info Message!");
        LOG.warn("Warn Message!");
        LOG.error("Error Message!");


    }

}

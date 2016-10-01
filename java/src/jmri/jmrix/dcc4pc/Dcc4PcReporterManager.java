// Dcc4PcReporterManager.java
package jmri.jmrix.dcc4pc;

import jmri.Reporter;

/**
 * Dcc4PcReporterManager implements the ReporterManager.
 * <P>
 * Description:	Implement Reporter manager for dcc4pc
 *
 * @author	Kevin Dickerson Copyright (C) 2012
 */
public class Dcc4PcReporterManager extends jmri.managers.AbstractReporterManager {

    // ctor has to register for LocoNet events
    public Dcc4PcReporterManager(Dcc4PcTrafficController tc, Dcc4PcSystemConnectionMemo memo) {
        this.memo = memo;
        this.tc = tc;
    }

    Dcc4PcTrafficController tc;
    Dcc4PcSystemConnectionMemo memo;

    public String getSystemPrefix() {
        return memo.getSystemPrefix();
    }

    public void dispose() {
        super.dispose();
    }

    public Reporter createNewReporter(String systemName, String userName) {
        Reporter r = new Dcc4PcReporter(systemName, userName);
        register(r);
        return r;
    }
}

/* @(#)Dcc4PcReporterManager.java */

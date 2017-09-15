package jmri.jmrit.symbolicprog;

import java.util.List;
import javax.swing.JLabel;
import jmri.jmrit.roster.RosterEntry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.jdom2.Element;

/**
 *
 * @author Paul Bender Copyright (C) 2017	
 */
public class FnMapPanelESUTest {

    @Test
    public void testCTor() {
        jmri.Programmer p = jmri.InstanceManager.getDefault(jmri.ProgrammerManager.class).getGlobalProgrammer();
        VariableTableModel tableModel = new VariableTableModel(
                new JLabel(""),
                new String[]{"Name", "Value"},
                new CvTableModel(new JLabel(""), p)
        );
        List<Integer> varsUsed = null;
        RosterEntry re = new RosterEntry();
        Element model = new Element("model");
        CvTableModel cvtm = new CvTableModel(new JLabel(), null);

        FnMapPanelESU t = new FnMapPanelESU(tableModel, varsUsed, model,re,cvtm);
        Assert.assertNotNull("exists",t);
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        jmri.util.JUnitUtil.setUp();
        jmri.util.JUnitUtil.initDebugProgrammerManager();
    }

    @After
    public void tearDown() {
        jmri.util.JUnitUtil.tearDown();
    }

}

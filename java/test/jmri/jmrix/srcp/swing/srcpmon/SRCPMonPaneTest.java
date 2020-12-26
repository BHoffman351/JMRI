package jmri.jmrix.srcp.swing.srcpmon;

import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 * @author Paul Bender Copyright(C) 2016
 */
public class SRCPMonPaneTest extends jmri.jmrix.AbstractMonPaneTestBase {
        
     private jmri.jmrix.srcp.SRCPSystemConnectionMemo memo = null;

    @Test
    public void testDefault() {
        jmri.util.swing.JmriNamedPaneAction f = new SRCPMonPane.Default();
        Assert.assertNotNull(f);
    }

    @BeforeEach
    @Override
    public void setUp() {
        JUnitUtil.setUp();

        memo = new jmri.jmrix.srcp.SRCPSystemConnectionMemo();
        jmri.InstanceManager.setDefault(jmri.jmrix.srcp.SRCPSystemConnectionMemo.class,memo);
                // pane for AbstractMonPaneTestBase; panel for JmriPanelTest 
        panel = pane = new SRCPMonPane();
        helpTarget = "package.jmri.jmrix.AbstractMonFrame";
        title = Bundle.getMessage("MenuItemSRCPCommandMonitorTitle");
    }

    @AfterEach
    @Override
    public void tearDown() {
        panel = pane = null;
        jmri.InstanceManager.deregister(memo, jmri.jmrix.srcp.SRCPSystemConnectionMemo.class);
        super.tearDown();
    }
}

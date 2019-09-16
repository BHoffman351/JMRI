package jmri.jmrit.display.layoutEditor;

import java.awt.GraphicsEnvironment;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JTextField;
import jmri.Block;
import jmri.BlockManager;
import jmri.InstanceManager;
import jmri.Turnout;
import jmri.jmrit.display.layoutEditor.LayoutTurntable;
import jmri.util.JUnitUtil;
import jmri.util.MathUtil;
import org.junit.*;
import org.netbeans.jemmy.EventTool;
import org.netbeans.jemmy.operators.*;

/**
 *
 * @author Paul Bender Copyright (C) 2017
 */
public class LayoutTrackEditorsTest {

    private static LayoutEditor layoutEditor = null;

    private static LayoutTurnout dxo = null;
    private static LayoutSlip slip = null;
    private static LevelXing xing = null;
    private static TrackSegment segment = null;
    private static LayoutTurntable turntable = null;

    @Test
    public void testCTor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LayoutEditor e = new LayoutEditor(); // create layout editor
        LayoutTrackEditors t = new LayoutTrackEditors(e);
        Assert.assertNotNull("exists",t);
        e.dispose();
    }

    @Test
    public void testHasNxSensorPairsNull(){
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LayoutEditor e = new LayoutEditor(); // create layout editor
        LayoutTrackEditors t = new LayoutTrackEditors(e);
        Assert.assertFalse("null block NxSensorPairs",t.hasNxSensorPairs(null));
        e.dispose();
    }

    @Test
    public void testHasNxSensorPairsDisconnectedBlock(){
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        LayoutEditor e = new LayoutEditor(); // create layout editor
        LayoutTrackEditors t = new LayoutTrackEditors(e);
        LayoutBlock b = new LayoutBlock("test", "test");
        Assert.assertFalse("disconnected block NxSensorPairs",t.hasNxSensorPairs(b));
        e.dispose();
    }

    @Test
    public void testEditTrackSegmentDone() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        createBlocks();

        // Edit the track segment
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(segment);
        JFrameOperator jfo = new JFrameOperator("Edit Track Segment");
        Assert.assertNotNull(jfo);

        // Select dashed
        JComboBoxOperator style_cbo = new JComboBoxOperator(jfo, 0);
        Assert.assertNotNull(style_cbo);
        style_cbo.selectItem(1);

        // Select mainline
        JComboBoxOperator track_cbo = new JComboBoxOperator(jfo, 1);
        Assert.assertNotNull(track_cbo);
        track_cbo.selectItem(0);

        // Enable Hide
        new JCheckBoxOperator(jfo, 0).doClick();

        // Select block
        JComboBoxOperator segment_blk_cbo = new JComboBoxOperator(jfo, 2);
        Assert.assertNotNull(segment_blk_cbo);
        segment_blk_cbo.selectItem(2);

        // Set arc angle
        JTextFieldOperator jtxt = new JTextFieldOperator(jfo, 1);
        jtxt.clickMouse();
        jtxt.setText("35");

        // Invoke layout block editor
        new JButtonOperator(jfo, "Create/Edit Block").doClick();
        JFrameOperator blkFO = new JFrameOperator("Edit Block");
        Assert.assertNotNull(blkFO);
        new JButtonOperator(blkFO, "OK").doClick();

        new JButtonOperator(jfo, "Done").doClick();
    }

    @Test
    public void testEditTrackSegmentCancel() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        // Edit the track segment
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(segment);
        JFrameOperator jfo = new JFrameOperator("Edit Track Segment");
        Assert.assertNotNull(jfo);

        new JButtonOperator(jfo, "Cancel").doClick();
    }

    @Test
    public void testEditTurnoutDone() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        createTurnouts();
        createBlocks();

        // Edit the double crossover
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(dxo);

        // Select main turnout
        JFrameOperator jfo = new JFrameOperator("Edit Turnout");
        Assert.assertNotNull(jfo);

        JComboBoxOperator tcbo = new JComboBoxOperator(jfo, 0);
        Assert.assertNotNull(tcbo);
        tcbo.selectItem(1);

        // Enable second turnout and select it
        new JCheckBoxOperator(jfo, 0).doClick();
        JComboBoxOperator tcbo2 = new JComboBoxOperator(jfo, 1);
        Assert.assertNotNull(tcbo2);
        tcbo2.selectItem(2);

        // Enable Invert and Hide
        new JCheckBoxOperator(jfo, 1).doClick();
        new JCheckBoxOperator(jfo, 2).doClick();

        // Select each block box, the first two get predefined blocks
        JComboBoxOperator blk_cbo_A = new JComboBoxOperator(jfo, 2);
        Assert.assertNotNull(blk_cbo_A);
        blk_cbo_A.selectItem(1);

        JComboBoxOperator blk_cbo_B = new JComboBoxOperator(jfo, 3);
        Assert.assertNotNull(blk_cbo_B);
        blk_cbo_B.selectItem(1);

        JComboBoxOperator blk_cbo_C = new JComboBoxOperator(jfo, 4);
        Assert.assertNotNull(blk_cbo_C);
        JTextField c = ((JTextField) blk_cbo_C.getEditor().getEditorComponent());
        c.setText("Blk 3");

        JComboBoxOperator blk_cbo_D = new JComboBoxOperator(jfo, 5);
        Assert.assertNotNull(blk_cbo_D);
        JTextField d = ((JTextField) blk_cbo_D.getEditor().getEditorComponent());
        d.setText("Blk 3");

        // Invoke layout block editor
        new JButtonOperator(jfo, "Create/Edit", 0).doClick();
        JFrameOperator blkFOa = new JFrameOperator("Edit Block");
        Assert.assertNotNull(blkFOa);
        new JButtonOperator(blkFOa, "OK").doClick();

        new JButtonOperator(jfo, "Create/Edit", 1).doClick();
        JFrameOperator blkFOb = new JFrameOperator("Edit Block");
        Assert.assertNotNull(blkFOb);
        new JButtonOperator(blkFOb, "OK").doClick();

        new JButtonOperator(jfo, "Create/Edit", 2).doClick();
        JFrameOperator blkFOc = new JFrameOperator("Edit Block");
        Assert.assertNotNull(blkFOc);
        new JButtonOperator(blkFOc, "OK").doClick();

        new JButtonOperator(jfo, "Create/Edit", 3).doClick();
        JFrameOperator blkFOd = new JFrameOperator("Edit Block");
        Assert.assertNotNull(blkFOd);
        new JButtonOperator(blkFOd, "OK").doClick();

        new JButtonOperator(jfo, "Done").doClick();
    }

    @Test
    public void testEditTurnoutCancel() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        // Edit the double crossover
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(dxo);
        JFrameOperator jfo = new JFrameOperator("Edit Turnout");
        Assert.assertNotNull(jfo);

        new JButtonOperator(jfo, "Cancel").doClick();
    }

    @Test
    public void testEditSlipDone() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        createTurnouts();
        createBlocks();

        // Edit the double slip
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(slip);

        // Select turnout A
        JFrameOperator jfo = new JFrameOperator("Edit Slip");
        Assert.assertNotNull(jfo);

        JComboBoxOperator tcbA = new JComboBoxOperator(jfo, 0);
        Assert.assertNotNull(tcbA);
        tcbA.selectItem(1);

        // Select turnout B
        JComboBoxOperator tcbB = new JComboBoxOperator(jfo, 1);
        Assert.assertNotNull(tcbB);
        tcbB.selectItem(2);

        // Select a block
        JComboBoxOperator blk_cbo_slip = new JComboBoxOperator(jfo, 10);
        Assert.assertNotNull(blk_cbo_slip);
        blk_cbo_slip.selectItem(1);

        // Enable Hide
        new JCheckBoxOperator(jfo, 0).doClick();

        // Trigger Test button
        new JButtonOperator(jfo, "Test").doClick();

        // Invoke layout block editor
        new JButtonOperator(jfo, "Create/Edit Block").doClick();
        JFrameOperator blkFO = new JFrameOperator("Edit Block");
        Assert.assertNotNull(blkFO);
        new JButtonOperator(blkFO, "OK").doClick();

        new JButtonOperator(jfo, "Done").doClick();
    }

    @Test
    public void testEditSlipCancel() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        // Edit the double slip
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(slip);
        JFrameOperator jfo = new JFrameOperator("Edit Slip");
        Assert.assertNotNull(jfo);

        new JButtonOperator(jfo, "Cancel").doClick();
    }

    @Test
    public void testEditXingDone() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        createBlocks();

        // Edit the level crossing
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(xing);
        JFrameOperator jfo = new JFrameOperator("Edit Level Crossing");
        Assert.assertNotNull(jfo);

        // Select AC block
        JComboBoxOperator blk_cbo_AC = new JComboBoxOperator(jfo, 0);
        Assert.assertNotNull(blk_cbo_AC);
        blk_cbo_AC.selectItem(1);

        // Select BD block
        JComboBoxOperator blk_cbo_BD = new JComboBoxOperator(jfo, 1);
        Assert.assertNotNull(blk_cbo_BD);
        blk_cbo_BD.selectItem(2);

        // Enable Hide
        new JCheckBoxOperator(jfo, 0).doClick();

        // Invoke layout block editor
        new JButtonOperator(jfo, "Create/Edit Block 1").doClick();
        JFrameOperator blkFOac = new JFrameOperator("Edit Block");
        Assert.assertNotNull(blkFOac);
        new JButtonOperator(blkFOac, "OK").doClick();

        // Invoke layout block editor
        new JButtonOperator(jfo, "Create/Edit Block 2").doClick();
        JFrameOperator blkFObd = new JFrameOperator("Edit Block");
        Assert.assertNotNull(blkFObd);
        new JButtonOperator(blkFObd, "OK").doClick();

        new JButtonOperator(jfo, "Done").doClick();
    }

    @Test
    public void testEditXingCancel() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        // Edit the level crossing
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(xing);
        JFrameOperator jfo = new JFrameOperator("Edit Level Crossing");
        Assert.assertNotNull(jfo);

        new JButtonOperator(jfo, "Cancel").doClick();
    }

    @Test
    public void testEditTurntableDone() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        createTurnouts();

        // Edit the level crossing
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(turntable);
        JFrameOperator jfo = new JFrameOperator("Edit Turntable");
        Assert.assertNotNull(jfo);

        // Set radius
        JTextFieldOperator jtxt = new JTextFieldOperator(jfo, 0);
        jtxt.clickMouse();
        jtxt.setText("30");

        // Add 5 rays
        new JButtonOperator(jfo, "New Ray Track").doClick();
        jtxt = new JTextFieldOperator(jfo, 1);
        jtxt.clickMouse();
        jtxt.setText("90");
        new JButtonOperator(jfo, "New Ray Track").doClick();
        jtxt.clickMouse();
        jtxt.setText("180");
        new JButtonOperator(jfo, "New Ray Track").doClick();
        jtxt.clickMouse();
        jtxt.setText("270");
        new JButtonOperator(jfo, "New Ray Track").doClick();
        jtxt.clickMouse();
        jtxt.setText("315");
        new JButtonOperator(jfo, "New Ray Track").doClick();

        // Delete the 5th ray
        Thread deleteRay = createModalDialogOperatorThread("Warning", "Yes", "deleteRay");  // NOI18N
        new JButtonOperator(jfo, "Delete", 4).doClick();
        JUnitUtil.waitFor(()->{return !(deleteRay.isAlive());}, "deleteRay finished");

        // Enable DCC control
        new JCheckBoxOperator(jfo, 0).doClick();

        // Change the first ray to 30 degrees
        jtxt = new JTextFieldOperator(jfo, 2);
        jtxt.clickMouse();
        jtxt.setText("30");

        // Set ray turnouts
        JComboBoxOperator turnout_cbo = new JComboBoxOperator(jfo, 0);
        JComboBoxOperator state_cbo = new JComboBoxOperator(jfo, 1);
        turnout_cbo.selectItem(1);
        state_cbo.selectItem(0);

        turnout_cbo = new JComboBoxOperator(jfo, 2);
        state_cbo = new JComboBoxOperator(jfo, 3);
        turnout_cbo.selectItem(1);
        state_cbo.selectItem(1);

        turnout_cbo = new JComboBoxOperator(jfo, 4);
        state_cbo = new JComboBoxOperator(jfo, 5);
        turnout_cbo.selectItem(2);
        state_cbo.selectItem(0);

        turnout_cbo = new JComboBoxOperator(jfo, 6);
        state_cbo = new JComboBoxOperator(jfo, 7);
        turnout_cbo.selectItem(2);
        state_cbo.selectItem(1);

        new JButtonOperator(jfo, "Done").doClick();
    }

    @Test
    public void testEditTurntableCancel() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());

        // Edit the Turntable
        LayoutTrackEditors trackEditor = new LayoutTrackEditors(layoutEditor);
        trackEditor.editLayoutTrack(turntable);
        JFrameOperator jfo = new JFrameOperator("Edit Turntable");
        Assert.assertNotNull(jfo);

        new JButtonOperator(jfo, "Cancel").doClick();
    }

    // from here down is testing infrastructure
    public void createTurnouts() {
        Turnout turnout1 = InstanceManager.getDefault(jmri.TurnoutManager.class).provideTurnout("IS101");
        turnout1.setUserName("Turnout 101");
        turnout1.setCommandedState(Turnout.CLOSED);

        Turnout turnout2 = InstanceManager.getDefault(jmri.TurnoutManager.class).provideTurnout("IS102");
        turnout2.setUserName("Turnout 102");
        turnout2.setCommandedState(Turnout.CLOSED);
    }

    public void createBlocks() {
        Block block1 = InstanceManager.getDefault(jmri.BlockManager.class).provideBlock("IB1");
        block1.setUserName("Blk 1");
        Block block2 = InstanceManager.getDefault(jmri.BlockManager.class).provideBlock("IB2");
        block2.setUserName("Blk 2");
    }

    Thread createModalDialogOperatorThread(String dialogTitle, String buttonText, String threadName) {
        Thread t = new Thread(() -> {
            // constructor for jdo will wait until the dialog is visible
            JDialogOperator jdo = new JDialogOperator(dialogTitle);
            JButtonOperator jbo = new JButtonOperator(jdo, buttonText);
            jbo.pushNoBlock();
        });
        t.setName(dialogTitle + " Close Dialog Thread: " + threadName);
        t.start();
        return t;
    }

    @BeforeClass
    public static void beforeClass() {
        JUnitUtil.setUp();
        if (!GraphicsEnvironment.isHeadless()) {
            JUnitUtil.resetProfileManager();
            jmri.util.JUnitUtil.resetInstanceManager();
            jmri.util.JUnitUtil.initInternalTurnoutManager();
            jmri.util.JUnitUtil.initInternalSensorManager();
            layoutEditor = new LayoutEditor();

            Point2D point = new Point2D.Double(150.0, 100.0);
            Point2D delta = new Point2D.Double(50.0, 75.0);

            // Double crossover
            dxo = new LayoutTurnout("Double Xover",
                    LayoutTurnout.DOUBLE_XOVER, point, 33.0, 1.1, 1.2, layoutEditor);

            // Double slip
            point = MathUtil.add(point, delta);
            slip = new LayoutSlip("Double Slip",
                    point, 0.0, layoutEditor, LayoutTurnout.DOUBLE_SLIP);

            // Level crossing
            point = MathUtil.add(point, delta);
            xing = new LevelXing("Level Xing",
                    point, layoutEditor);

            // Turntable
            point = MathUtil.add(point, delta);
            turntable = new LayoutTurntable("Turntable",
                    point, layoutEditor);

            // Track Segment
            PositionablePoint p1 = new PositionablePoint("a", PositionablePoint.ANCHOR, new Point2D.Double(0.0, 0.0), layoutEditor);
            PositionablePoint p2 = new PositionablePoint("b", PositionablePoint.ANCHOR, new Point2D.Double(1.0, 1.0), layoutEditor);
            segment = new TrackSegment("Segment", p1, LayoutTrack.POS_POINT, p2, LayoutTrack.POS_POINT, false, false, layoutEditor);
            segment.setArc(true);
            segment.setCircle(true);
        }
    }

    @AfterClass
    public static void afterClass() {
        if (layoutEditor != null) {
            JUnitUtil.dispose(layoutEditor);
        }
        if(dxo != null){
           dxo.remove();
           dxo.dispose();
           dxo = null;
        }

        if(slip != null){
           slip.remove();
           slip.dispose();
           slip = null;
        }

        if(xing != null){
           xing.remove();
           xing.dispose();
           xing = null;
        }

        if(segment != null){
           segment.remove();
           segment.dispose();
           segment = null;
        }

        if(turntable != null){
           turntable.remove();
           turntable.dispose();
           turntable = null;
        }

        layoutEditor = null;

        JUnitUtil.tearDown();
    }

    @Before
    public void setUp() {
        jmri.util.JUnitUtil.setUp();
        jmri.util.JUnitUtil.resetProfileManager();
    }

    @After
    public void tearDown() {
        jmri.util.JUnitUtil.tearDown();
    }

//     private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LayoutTrackEditorsTest.class);
}

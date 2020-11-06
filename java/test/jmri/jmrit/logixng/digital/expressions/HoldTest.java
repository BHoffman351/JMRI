package jmri.jmrit.logixng.digital.expressions;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import jmri.InstanceManager;
import jmri.NamedBean;
import jmri.jmrit.logixng.*;
import jmri.util.JUnitUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jmri.jmrit.logixng.actions.ActionAtomicBoolean;
import jmri.jmrit.logixng.actions.IfThenElse;
import jmri.jmrit.logixng.string.expressions.StringExpressionMemory;

/**
 * Test Hold
 * 
 * @author Daniel Bergqvist 2018
 */
public class HoldTest extends AbstractDigitalExpressionTestBase {

    private LogixNG logixNG;
    private ConditionalNG conditionalNG;
    private Hold expressionHold;
    private ActionAtomicBoolean actionAtomicBoolean;
    private AtomicBoolean atomicBoolean;
    
    
    @Override
    public ConditionalNG getConditionalNG() {
        return conditionalNG;
    }
    
    @Override
    public LogixNG getLogixNG() {
        return logixNG;
    }
    
    private static int beanID = 901;
    
    @Override
    public MaleSocket getConnectableChild() {
        DigitalExpressionBean childExpression = new True("IQDE"+Integer.toString(beanID++), null);
        MaleSocket maleSocketChild =
                InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(childExpression);
        return maleSocketChild;
    }
    
    @Override
    public String getExpectedPrintedTree() {
        return String.format(
                "Hold while E1. Trigger on E2%n" +
                "   ? E1%n" +
                "      Socket not connected%n" +
                "   ? E2%n" +
                "      Socket not connected%n");
    }
    
    @Override
    public String getExpectedPrintedTreeFromRoot() {
        return String.format(
                "LogixNG: A new logix for test%n" +
                "   ConditionalNG: A conditionalNG%n" +
                "      ! %n" +
                "         If E then A1 else A2%n" +
                "            ? E%n" +
                "               Hold while E1. Trigger on E2%n" +
                "                  ? E1%n" +
                "                     Socket not connected%n" +
                "                  ? E2%n" +
                "                     Socket not connected%n" +
                "            ! A1%n" +
                "               Set the atomic boolean to true%n" +
                "            ! A2%n" +
                "               Socket not connected%n");
    }
    
    @Override
    public NamedBean createNewBean(String systemName) {
        return new Hold(systemName, null);
    }
    
    @Override
    public boolean addNewSocket() {
        return false;
    }
    
    @Test
    public void testCtor() {
        Hold expression2;
        
        expression2 = new Hold("IQDE321", null);
        Assert.assertNotNull("object exists", expression2);
        Assert.assertNull("Username matches", expression2.getUserName());
        Assert.assertEquals("String matches", "Hold while E1. Trigger on E2", expression2.getLongDescription());
        
        expression2 = new Hold("IQDE321", "My expression");
        Assert.assertNotNull("object exists", expression2);
        Assert.assertEquals("Username matches", "My expression", expression2.getUserName());
        Assert.assertEquals("String matches", "Hold while E1. Trigger on E2", expression2.getLongDescription());
        
        boolean thrown = false;
        try {
            // Illegal system name
            new Hold("IQE55:12:XY11", null);
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
        
        thrown = false;
        try {
            // Illegal system name
            new Hold("IQE55:12:XY11", "A name");
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        Assert.assertTrue("Expected exception thrown", thrown);
    }
    
    @Test
    public void testCtorAndSetup1() {
        Hold expression = new Hold("IQDE321", null);
        Assert.assertNotNull("exists", expression);
        Assert.assertEquals("expression has 2 female sockets", 2, expression.getChildCount());
        expression.getChild(0).setName("XYZ123");
        expression.setHoldActionSocketSystemName("IQDE52");
        expression.getChild(1).setName("ZH12");
        expression.setTriggerExpressionSocketSystemName("IQDE554");
        
        Assert.assertEquals("expression female socket name is XYZ123",
                "XYZ123", expression.getChild(0).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(0).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(0).isConnected());
        
        Assert.assertEquals("expression female socket name is ZH12",
                "ZH12", expression.getChild(1).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(1).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(1).isConnected());
        
        // Setup action. This connects the child actions to this action
        expression.setup();
        
        jmri.util.JUnitAppender.assertMessage("cannot load digital expression IQDE52");
        jmri.util.JUnitAppender.assertMessage("cannot load digital expression IQDE554");
        
        Assert.assertEquals("expression female socket name is XYZ123",
                "XYZ123", expression.getChild(0).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(0).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(0).isConnected());
        
        Assert.assertEquals("expression female socket name is ZH12",
                "ZH12", expression.getChild(1).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(1).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(1).isConnected());
        
        Assert.assertEquals("expression has 2 female sockets", 2, expression.getChildCount());
    }
    
    @Test
    public void testCtorAndSetup2() {
        Hold expression = new Hold("IQDE321", null);
        Assert.assertNotNull("exists", expression);
        Assert.assertEquals("expression has 2 female sockets", 2, expression.getChildCount());
        expression.getChild(0).setName("XYZ123");
        expression.setHoldActionSocketSystemName(null);
        expression.getChild(1).setName("ZH12");
        expression.setTriggerExpressionSocketSystemName(null);
        
        Assert.assertEquals("expression female socket name is XYZ123",
                "XYZ123", expression.getChild(0).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(0).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(0).isConnected());
        
        Assert.assertEquals("expression female socket name is ZH12",
                "ZH12", expression.getChild(1).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(1).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(1).isConnected());
        
        // Setup action. This connects the child actions to this action
        expression.setup();
        
        Assert.assertEquals("expression female socket name is XYZ123",
                "XYZ123", expression.getChild(0).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(0).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(0).isConnected());
        
        Assert.assertEquals("expression female socket name is ZH12",
                "ZH12", expression.getChild(1).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(1).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(1).isConnected());
        
        Assert.assertEquals("expression has 2 female sockets", 2, expression.getChildCount());
    }
    
    @Test
    public void testCtorAndSetup3() {
        DigitalExpressionManager m = InstanceManager.getDefault(DigitalExpressionManager.class);
        
        MaleSocket childSocket0 = m.registerExpression(new ExpressionMemory("IQDE52", null));
        MaleSocket childSocket1 = m.registerExpression(new ExpressionMemory("IQDE554", null));
        
        Hold expression = new Hold("IQDE321", null);
        Assert.assertNotNull("exists", expression);
        Assert.assertEquals("expression has 2 female sockets", 2, expression.getChildCount());
        expression.getChild(0).setName("XYZ123");
        expression.setHoldActionSocketSystemName("IQDE52");
        expression.getChild(1).setName("ZH12");
        expression.setTriggerExpressionSocketSystemName("IQDE554");
        
        Assert.assertEquals("expression female socket name is XYZ123",
                "XYZ123", expression.getChild(0).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(0).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(0).isConnected());
        
        Assert.assertEquals("expression female socket name is ZH12",
                "ZH12", expression.getChild(1).getName());
        Assert.assertEquals("expression female socket is of correct class",
//                "jmri.jmrit.logixng.implementation.DefaultFemaleGenericExpressionSocket$DigitalSocket",
                "jmri.jmrit.logixng.digital.implementation.DefaultFemaleDigitalExpressionSocket",
                expression.getChild(1).getClass().getName());
        Assert.assertFalse("expression female socket is not connected",
                expression.getChild(1).isConnected());
        
        // Setup action. This connects the child actions to this action
        expression.setup();
        
        Assert.assertTrue("expression female socket is connected",
                expression.getChild(0).isConnected());
        Assert.assertEquals("child is correct bean",
                childSocket0,
                expression.getChild(0).getConnectedSocket());
        Assert.assertEquals("expression has 2 female sockets", 2, expression.getChildCount());
        
        Assert.assertTrue("expression female socket is connected",
                expression.getChild(1).isConnected());
        Assert.assertEquals("child is correct bean",
                childSocket1,
                expression.getChild(1).getConnectedSocket());
        Assert.assertEquals("expression has 2 female sockets", 2, expression.getChildCount());
        
        // Try run setup() again. That should not cause any problems.
        expression.setup();
        
        Assert.assertEquals("expression has 2 female sockets", 2, expression.getChildCount());
    }
    
    @Test
    public void testReset() throws SocketAlreadyConnectedException {
        _baseMaleSocket.setEnabled(false);
        
        Hold a = (Hold)_base;
        AtomicBoolean ab1 = new AtomicBoolean(false);
        AtomicBoolean ab2 = new AtomicBoolean(false);
        
        DigitalExpressionBean expr1 = new True("IQDE"+Integer.toString(beanID++), null) {
            @Override
            public void reset() {
                ab1.set(true);
            }
        };
        
        DigitalExpressionBean expr2 = new True("IQDE"+Integer.toString(beanID++), null) {
            @Override
            public void reset() {
                ab2.set(true);
            }
        };
        
        a.getChild(0).connect(
                InstanceManager.getDefault(DigitalExpressionManager.class)
                        .registerExpression(expr1));
        
        a.getChild(1).connect(
                InstanceManager.getDefault(DigitalExpressionManager.class)
                        .registerExpression(expr2));
        
        a.reset();
        Assert.assertTrue("Child 0 is reset", ab1.get());
        Assert.assertTrue("Child 1 is reset", ab2.get());
    }
    
    @Test
    public void testGetChild() {
        Assert.assertTrue("getChildCount() returns 2", 2 == expressionHold.getChildCount());
        
        Assert.assertNotNull("getChild(0) returns a non null value",
                expressionHold.getChild(0));
        Assert.assertNotNull("getChild(1) returns a non null value",
                expressionHold.getChild(1));
        
        boolean hasThrown = false;
        try {
            expressionHold.getChild(2);
        } catch (IllegalArgumentException ex) {
            hasThrown = true;
            Assert.assertEquals("Error message is correct", "index has invalid value: 2", ex.getMessage());
        }
        Assert.assertTrue("Exception is thrown", hasThrown);
    }
    
    @Test
    public void testCategory() {
        Assert.assertTrue("Category matches", Category.OTHER == _base.getCategory());
    }
    
    @Test
    public void testIsExternal() {
        Assert.assertFalse("is external", _base.isExternal());
    }
    
    @Test
    public void testDescription() {
        Hold e1 = new Hold("IQDE321", null);
        Assert.assertTrue("Hold".equals(e1.getShortDescription()));
        Assert.assertTrue("Hold while E1. Trigger on E2".equals(e1.getLongDescription()));
    }
    
    @Test
    @Override
    public void testEnableAndEvaluate() {
        // Not implemented.
        // This method is implemented for other digital expressions so no need
        // to add support here. It doesn't need to be tested for every digital
        // expression.
    }
    
    @Test
    @Override
    public void testDebugConfig() {
        // Not implemented.
        // This method is implemented for other digital expressions so no need
        // to add support here. It doesn't need to be tested for every digital
        // expression.
    }
    
    // The minimal setup for log4J
    @Before
    public void setUp() throws SocketAlreadyConnectedException {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
        
        _category = Category.OTHER;
        _isExternal = false;
        
        logixNG = InstanceManager.getDefault(LogixNG_Manager.class).createLogixNG("A new logix for test");  // NOI18N
        conditionalNG = InstanceManager.getDefault(ConditionalNG_Manager.class)
                .createConditionalNG("A conditionalNG");  // NOI18N
        conditionalNG.setRunOnGUIDelayed(false);
        conditionalNG.setEnabled(true);
        logixNG.addConditionalNG(conditionalNG);
        IfThenElse ifThenElse = new IfThenElse("IQDA321", null, IfThenElse.Type.TRIGGER_ACTION);
        MaleSocket maleSocket =
                InstanceManager.getDefault(DigitalActionManager.class).registerAction(ifThenElse);
        conditionalNG.getChild(0).connect(maleSocket);
        
        expressionHold = new Hold("IQDE321", null);
        MaleSocket maleSocket2 =
                InstanceManager.getDefault(DigitalExpressionManager.class).registerExpression(expressionHold);
        ifThenElse.getChild(0).connect(maleSocket2);
        
        _base = expressionHold;
        _baseMaleSocket = maleSocket2;
        
        atomicBoolean = new AtomicBoolean(false);
        actionAtomicBoolean = new ActionAtomicBoolean(atomicBoolean, true);
        MaleSocket socketAtomicBoolean = InstanceManager.getDefault(DigitalActionManager.class).registerAction(actionAtomicBoolean);
        ifThenElse.getChild(1).connect(socketAtomicBoolean);
        
        logixNG.setParentForAllChildren();
        logixNG.setEnabled(true);
        logixNG.activateLogixNG();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
}

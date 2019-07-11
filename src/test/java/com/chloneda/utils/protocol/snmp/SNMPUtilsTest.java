package com.chloneda.utils.protocol.snmp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;

import java.io.IOException;

/**
 * Created by chloneda
 * Description:
 */
public class SNMPUtilsTest {
    SNMPUtils snmpUtils;

    @Before
    public void init(){
        //snmp查询、设置操作
        snmpUtils = new SNMPUtils("192.167.2.120",161, SnmpConstants.version2c,"public");

        //snmp发送Trap操作
        //snmpUtils = new SNMPUtils("192.167.2.120",1623, SnmpConstants.version1,"public");
    }

    @Test
    public void testSnmpGet() throws Exception {
        //get-request
        SNMPUtils.snmpGet("1.3.6.1.2.1.1.4.0");//sysContact(系统联系人)
    }

    @Test
    public void testSnmpGetBulk() throws IOException {
        //getBulk-request
        SNMPUtils.snmpGetBulk("1.3.6.1.2.1.1");
    }

    @Test
    public void testSnmpSet(){
        //set-request
        //SNMPUtils.snmpSet(new OID("1.3.6.1.2.1.1.4.0"), new VariantVariable());
    }

    @Test
    public void testSnmpWalk() throws Exception {
        //getNext-request
        SNMPUtils.snmpWalk(new OID("1.3.6.1.2.1.2.2.1.2"), "WALK");//IfDescr(网络接口信息描述)
    }

    @Test
    public void testSnmpGetResponse() throws Exception {
        //get-response
        SNMPUtils.snmpGetResponse(false, true, "1.3.6.1.2.1.1.4.0");//sysContact(系统联系人)
    }

    @Test
    public void testSendSnmpTrap() throws IOException {
        ResponseEvent event=SNMPUtils.sendSnmpTrap("1.3.6.1.2.1.1.4.0");
        System.out.println(event.getResponse());
    }

    @Test
    public void testSnmpReport(){
        //report
    }

    @Test
    public void testSnmpInformRequest(){
        //inform-request
    }

    @Test
    public void testSnmpTrap(){
        //trap
    }

    @After
    public void destory(){
        snmpUtils.destory();
    }

}

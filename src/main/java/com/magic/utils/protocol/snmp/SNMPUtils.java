package com.magic.utils.protocol.snmp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;

/**
 * Created by chloneda
 *
 * Description:
 *      根据SNMP协议编写的SNMP常用操作
 */
public class SNMPUtils {
    private static final Logger log = LoggerFactory.getLogger(SNMPUtils.class);
    public static final String ROOT = "WALK";

    private static Snmp snmp = null;

    private static String host;
    private static int port;
    private static int version;
    private static String community;
    private static Target target;

    public SNMPUtils(String host) {
        this(host, 161);
    }

    public SNMPUtils(String host, int port) {
        this(host, port, SnmpConstants.version2c);
    }

    public SNMPUtils(String host, int port, int version) {
        this(host, port, version, "public");
    }

    public SNMPUtils(String host, int port, int version, String community) {
        this.host = host;
        this.port = port;
        this.version = version;
        this.community = community;
        initSNMP(version);
        createTarget(host, port, version, community);
    }

    public static void initSNMP(int version) {
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            if (version == SnmpConstants.version3) {
                // 设置安全模式
                USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
                SecurityModels.getInstance().addSecurityModel(usm);
            }
            // 开始监听消息
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTarget(String host, int port, int version, String community) {
        //Address targetAddress = GenericAddress.parse("udp:"+bindHost+"/"+bindPort);
        //设置Snmp Agent的访问地址和端口
        Address targetAddress = new UdpAddress(host + "/" + port);

        if (version == SnmpConstants.version3) {
            // 添加用户
            snmp.getUSM().addUser(new OctetString("MD5DES")
                    , new UsmUser(new OctetString("MD5DES")
                            , AuthMD5.ID, new OctetString("MD5DESUserAuthPassword")
                            , PrivDES.ID, new OctetString("MD5DESUserPrivPassword")));
            target = new UserTarget();
            // 设置安全级别
            ((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
            ((UserTarget) target).setSecurityName(new OctetString("MD5DES"));
            target.setVersion(SnmpConstants.version3);//设置snmp的版本号
        } else {
            target = new CommunityTarget();
            if (version == SnmpConstants.version1) {
                target.setVersion(SnmpConstants.version1);
                //设置共同体,默认是"public"
                ((CommunityTarget) target).setCommunity(new OctetString(community));
            } else {
                target.setVersion(SnmpConstants.version2c);
                ((CommunityTarget) target).setCommunity(new OctetString(community));
            }

        }
        target.setAddress(targetAddress);
        target.setRetries(3);//设置重试次数
        target.setTimeout(1000);
    }

    /**
     * 根据PDU报文类型构造
     *
     * @return
     */
    public static PDU createPDU(int pduType) {
        //return DefaultPDUFactory.createPDU(version);
        PDU pdu = null;

        //Snmp的三个版本号
        if (version == SnmpConstants.version1 || version == SnmpConstants.version2c) {
            pdu = new PDU();
            pdu.setType(pduType);
        } else if (version == SnmpConstants.version3) {
            pdu = new ScopedPDU();
            // 设置报文类型
            pdu.setType(pduType);
        } else {
            throw new IllegalArgumentException("version: " + version);
        }
        return pdu;
    }

    public static void snmpGet(String oid) throws Exception {
        snmpGet(Arrays.asList(oid));
    }

    /**
     * get-request 操作:从代理进程处提取一个或多个参数值
     *
     * @param oids
     * @throws Exception
     */
    public static void snmpGet(List<String> oids) throws Exception {

        Map<String, String> datas = new HashMap<String, String>();
        PDU pdu = createPDU(PDU.GET);
        for (String oid : oids) {
            if (oid.endsWith(".0")) {
                pdu.add(new VariableBinding(new OID(oid)));
            } else {
                throw new IllegalArgumentException(oid + " 为非基本节点");
            }
        }

        ResponseEvent respEvent = snmp.send(pdu, target);
        PDU response = respEvent.getResponse();

        if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
            Vector<? extends VariableBinding> vector = response.getVariableBindings();
            for (VariableBinding vb : vector) {
                String key = vb.getOid().toString();
                datas.put(key, vb.getVariable().toString());
            }
        } else {
            throw new Exception("Error:" + response.getErrorStatusText());
        }

        System.out.println("========>: " + datas);
    }

    public static void snmpGetBulk(Map<String, String> datas, String oid, String rootOID) throws IOException {
        PDU request = createPDU(PDU.GETBULK);
        request.setMaxRepetitions(10);//每次返回條數
//     request.setNonRepeaters(2);// 標量個數 //標量在前 矢量在後
        request.add(new VariableBinding(new OID()));
        ResponseEvent rspEvt = snmp.send(request, target);
        PDU response = rspEvt.getResponse();
        if (null != response && response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
            String curr_oid = null;
            //循環數據桶
            for (VariableBinding variable : response.getVariableBindings()) {
//                  String syn = variable.getVariable().getSyntaxString();//節點數據類型
                String key = variable.getOid().toString();
                if (key.contains(rootOID)) {//判断获得的值是否是指定根节点下面
                    String value = variable.getVariable().toString();
                    datas.put(key.replace(rootOID, ""), value);
                    //System.out.println(key.replace(rootOID, ""));
                    System.out.println("=======>" + datas);
                    curr_oid = variable.getOid().toString();

                } else {
                    return;
                }
            }
            if (null == curr_oid) {
                return;
            }
            snmpGetBulk(datas, curr_oid, rootOID);

        }
    }

    /**
     * get-next-request 操作:从代理进程处提取紧跟当前参数值的下一个参数值
     *
     * @param datas
     * @param oid
     * @param type
     * @throws Exception
     */
    public static void snmpWalk(Map<String, String> datas, OID oid, String type) throws Exception {
        if (type.equals(SNMPUtils.ROOT))
            type = oid.toString();
        PDU pdu = createPDU(PDU.GETNEXT);
        pdu.add(new VariableBinding(oid));
        ResponseEvent rspEvt = snmp.send(pdu, target);
        PDU response = rspEvt.getResponse();

        if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
            VariableBinding vb = (VariableBinding) response.getVariableBindings().firstElement();
            OID curr_oid = vb.getOid();
            String curr_str = curr_oid.toString();
            if (curr_str.contains(type)) {//判断获得的值是否是指定根节点下面
                String key = vb.getOid().toString();
                datas.put(key.replace(type, ""), vb.getVariable().toString());
                System.out.println("=======>: " + datas);
                snmpWalk(datas, curr_oid, type);
            }
        } else {
            throw new Exception("错误信息:" + response.getErrorStatusText());
        }
    }

    /**
     * set-request 操作:设置代理进程的一个或多个参数值
     *
     * @param oid
     * @param newVar
     * @throws Exception
     */
    public static void snmpSet(OID oid, Variable newVar) throws Exception {
        PDU request = createPDU(PDU.SET);
        request.add(new VariableBinding(oid, newVar));
        ResponseEvent resEvt = snmp.send(request, target);
        PDU response = resEvt.getResponse();
        if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {//判断返回报文是否正确
            VariableBinding vb = (VariableBinding) response.getVariableBindings().firstElement();
            Variable var = vb.getVariable();
            if (var.equals(newVar)) {//比较返回值和设置值
                System.out.println("SET操作成功 ！");
            } else {
                System.out.println("SET操作失败 ！");
            }
        } else {
            throw new Exception("错误信息:" + response.getErrorStatusText());
        }
    }

    public static void sendMessage(Boolean syn, final Boolean bro, String oid) throws IOException {
        sendMessage(syn, bro, Arrays.asList(oid));
    }

    public static void sendMessage(Boolean syn, final Boolean bro, List<String> oids) throws IOException {
        PDU pdu = createPDU(PDU.GET);
        for (String oid : oids) {
            pdu.addOID(new VariableBinding(new OID(oid)));
        }
        if (!syn) {
            // 发送报文 并且接受响应
            ResponseEvent response = snmp.send(pdu, target);
            // 处理响应
            System.out.println("Synchronize(同步) message(消息) from(来自) "
                    + response.getPeerAddress() + "\n" + "request(发送的请求):"
                    + response.getRequest() + "\n" + "response(返回的响应):"
                    + response.getResponse() + "=========:" + response.getUserObject());
        } else {
            // 设置监听对象
            ResponseListener listener = new ResponseListener() {

                public void onResponse(ResponseEvent event) {
                    if (bro.equals(false)) {
                        ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    }
                    // 处理响应
                    PDU request = event.getRequest();
                    PDU response = event.getResponse();
                    System.out.println("Asynchronise(异步) message(消息) from(来自) "
                            + event.getPeerAddress() + "\n" + "request(发送的请求):" + request
                            + "\n" + "response(返回的响应):" + response.toString());
                }

            };
            // 发送报文
            snmp.send(pdu, target, null, listener);
        }
    }


    public static void main(String[] args) throws Exception {

        SNMPUtils snmpUtils = new SNMPUtils("192.167.2.120");

        snmpGet("1.3.6.1.2.1.1.4.0");
        //snmpGetBulk(new HashMap<String, String>(),"1.3.6.1.2.1.1.4.0","1.3.6.1.2.1.1");

        //snmpSet(new OID("1.3.6.1.2.1.1.4.0"), new VariantVariable());

        //snmpWalk(new HashMap<String, String>(), new OID("1.3.6.1.2.1.2.2.1.6"), SNMPUtils.ROOT);
        //sendMessage(false, true, "1.3.6.1.2.1.1.1.0");


    }
}
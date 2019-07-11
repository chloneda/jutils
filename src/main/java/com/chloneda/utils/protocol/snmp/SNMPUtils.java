package com.chloneda.utils.protocol.snmp;

import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;

/**
 * Created by chloneda
 * Blog:
 *      https://www.cnblogs.com/chloneda
 *      https://chloneda.github.io/
 *
 * Description: 根据SNMP协议编写的SNMP常用操作:
 *
 *  1.get-request 操作: 从代理进程处提取一个或多个参数值,只查询MIB的叶子节点
 *  2.getNext-request 操作: 从代理进程处提取紧跟当前参数值的下一个参数值
 *  3.getBulk-request 操作: 会根据最大重试值执行一个连续的getNext操作,该操作常用于查询数据量较大的场景，可以提高效率
 *  4.set-request 操作: 设置代理进程的一个或多个参数值
 *  5.get-response 操作: 这个操作是由代理进程发出的,它是上述四种操作的响应
 *  6.inform-request 操作:
 *  7.report 操作:
 *  8.trap 操作: 代理进程主动发出的报文,通知管理进程有某些事情发生
 *
 */
public class SNMPUtils {
    private static final Logger logger = LoggerFactory.getLogger(SNMPUtils.class);
    private static final String ROOT = "WALK";

    private static Map<String,String> datas;
    private static Snmp snmp = null;

    private static String host;
    private static int port;
    private static int version;
    private static String community;
    private static Target target;

    private static String securityName="snmpuser";
    private static String authPassword="auth123456";
    private static String privPassword="priv123456";
    private static String privProtocol="DES";
    private static String authProtocol="MD5";
    private static int securityLevel=SecurityLevel.AUTH_PRIV;
    private static OID priProtocolBean;
    private static OID authProtocolBean;

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
        //设置Snmp Agent的访问地址和端口
        //Address targetAddress = GenericAddress.parse("udp:"+bindHost+"/"+bindPort);
        //Address targetAddress = new UdpAddress(String.format("%s/%s",host,port));
        Address targetAddress = new UdpAddress(host + "/" + port);

        if (version == SnmpConstants.version3) {
            setPriProtocolBean(privProtocol);
            setAuthProtocolBean(authProtocol);
            // 添加用户
            snmp.getUSM().addUser(new OctetString(securityName)
                    , new UsmUser(
                            new OctetString(securityName)
                            , authProtocolBean
                            , new OctetString(authPassword)
                            , priProtocolBean
                            , new OctetString(privPassword)));
            target = new UserTarget();
            // 设置安全级别 NOAUTH_NOPRIV AUTH_NOPRIV AUTH_PRIV
            target.setSecurityLevel(securityLevel);
            target.setSecurityName(new OctetString(securityName));
            //设置snmp的版本号
            target.setVersion(SnmpConstants.version3);
        } else {
            target = new CommunityTarget();
            if (version == SnmpConstants.version1) {
                //设置共同体,默认是"public"
                ((CommunityTarget) target).setCommunity(new OctetString(community));
                target.setVersion(SnmpConstants.version1);
            } else {
                ((CommunityTarget)target).setCommunity(new OctetString(community));
                target.setVersion(SnmpConstants.version2c);
            }

        }
        target.setAddress(targetAddress);
        target.setRetries(3);//设置重试次数
        target.setTimeout(1000);
    }

    private static void setPriProtocolBean(String privProtocol) {
        if (privProtocol.equalsIgnoreCase("des"))
            priProtocolBean = PrivDES.ID;
        else if (privProtocol.equalsIgnoreCase("aes128")
                || privProtocol.equalsIgnoreCase("aes"))
            priProtocolBean = PrivAES128.ID;
        else if (privProtocol.equalsIgnoreCase("aes192"))
            priProtocolBean = PrivAES192.ID;
        else if (privProtocol.equalsIgnoreCase("aes256"))
            priProtocolBean = PrivAES256.ID;
        else
            priProtocolBean = null;
    }

    private static void setAuthProtocolBean(String authProtocol) {
        if (authProtocol.equalsIgnoreCase("md5"))
            authProtocolBean = AuthMD5.ID;
        else if (authProtocol.equalsIgnoreCase("sha"))
            authProtocolBean = AuthSHA.ID;
        else
            authProtocolBean = null;
    }

    /**
     * 根据PDU报文类型构造(create the PDU)
     * pduType and version
     * @return
     */
    public static PDU createPDU(int pduType) {
        //return DefaultPDUFactory.createPDU(version);
        PDU pdu;
        switch (version) {
            case SnmpConstants.version3: {
                pdu = new ScopedPDU();
                break;
            }
            case SnmpConstants.version1: {
                pdu = new PDUv1();
                break;
            }
            default:
                pdu = new PDU();
        }
        pdu.setType(pduType);
        return pdu;
    }

    private static class MyDefaultPDUFactory extends DefaultPDUFactory {
        private OctetString contextEngineId = null;

        public MyDefaultPDUFactory(int pduType, OctetString contextEngineId) {
            super(pduType);
            this.contextEngineId = contextEngineId;
        }

        @Override
        public PDU createPDU(Target target) {
            PDU pdu = super.createPDU(target);
            if (target.getVersion() == SnmpConstants.version3) {
                ((ScopedPDU)pdu).setContextEngineID(contextEngineId);
            }
            return pdu;
        }
    }

    public static void snmpGet(String oid) throws Exception {
        snmpGet(Arrays.asList(oid));
    }

    /**
     * get-request 操作:从代理进程处提取一个或多个参数值,只查询MIB的叶子节点(叶子节点均以.0结尾)
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
                throw new IllegalArgumentException(oid + ": 为MIB中非叶子节点，请检查！");
            }
        }

        //ResponseEvent respEvent = snmp.send(pdu, target);
        ResponseEvent respEvent = snmp.get(pdu, target);
        PDU response = respEvent.getResponse();

        if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
            Vector<? extends VariableBinding> vector = response.getVariableBindings();
            for (VariableBinding vb : vector) {
                String key = vb.getOid().toString();
                datas.put(key, vb.getVariable().toString());
            }
        } else {
            throw new Exception("Error:{} " + response.getErrorStatusText());
        }

        System.out.println("Snmp get-request operation and the data is :{} " + datas);
    }

    // getBulk操作会根据最大重试值执行一个连续的GETNEXT操作。该操作常用于查询数据量较大的场景，可以提高效率
    // 非中继值决定要进行GETNEXT操作的变量列表中的变量数，对于剩下的变量，将根据最大重试值进行连续GETNEXT操作。
    public static void snmpGetBulk(String rootOID) throws IOException {
        datas=new HashMap<String, String>();
        PDU request = createPDU(PDU.GETBULK);
        //getBulk操作,一定要指定MaxRepetitions，默认值是0，那样不会返回任何结(must set it,default is 0)
        request.setMaxRepetitions(10);//每次返回條數
        request.setNonRepeaters(2);// 标量个数 //标量在前 矢量在后
        request.add(new VariableBinding(new OID(rootOID)));
        //ResponseEvent rspEvt = snmp.send(request, target);
        ResponseEvent rspEvt = snmp.getBulk(request, target);
        PDU response = rspEvt.getResponse();
        if (null != response && response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
            String currOid = null;
            //循环数据桶
            for (VariableBinding variable : response.getVariableBindings()) {
                // String syn = variable.getVariable().getSyntaxString();//节点数据类型
                String key = variable.getOid().toString();
                if (key.contains(rootOID)) {//判断获得的值是否是指定根节点下面
                    String value = variable.getVariable().toString();
                    datas.put(key.replace(rootOID, ""), value);
                    System.out.println("Snmp getBulk-request operation and the data is :{} " + datas);
                    currOid = variable.getOid().toString();
                } else {
                    return;
                }
            }
            if (null == currOid) {
                return;
            }
            snmpGetBulk(currOid);
        }
    }

    /**
     * get-next-request 操作:从代理进程处提取紧跟当前参数值的下一个参数值
     *
     * @param oid
     * @param type
     * @throws Exception
     */
    public static void snmpWalk(OID oid, String type) throws Exception {
        if (type.equals(SNMPUtils.ROOT))
            type = oid.toString();
        datas=new HashMap<String, String>();
        PDU pdu = createPDU(PDU.GETNEXT);
        pdu.add(new VariableBinding(oid));
        ResponseEvent rspEvt = snmp.send(pdu, target);
        //ResponseEvent rspEvt = snmp.getNext(pdu, target);
        PDU response = rspEvt.getResponse();

        if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
            VariableBinding vb = (VariableBinding) response.getVariableBindings().firstElement();
            OID curr_oid = vb.getOid();
            String curr_str = curr_oid.toString();
            if (curr_str.contains(type)) {//判断获得的值是否是指定根节点下面
                String key = vb.getOid().toString();
                datas.put(key.replace(type, ""), vb.getVariable().toString());
                System.out.println("Snmp getNext-request operation and the data is :{} " + datas);
                snmpWalk(curr_oid, type);
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
        //ResponseEvent resEvt = snmp.send(request, target);
        ResponseEvent resEvt = snmp.set(request, target);
        PDU response = resEvt.getResponse();
        if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {//判断返回报文是否正确
            VariableBinding vb = (VariableBinding) response.getVariableBindings().firstElement();
            Variable var = vb.getVariable();
            if (var.equals(newVar)) {//比较返回值和设置值
                System.out.println("Set operation is successful !");
            } else {
                System.out.println("Snmp set operation is fail !");
            }
        } else {
            throw new Exception("Error info :{} " + response.getErrorStatusText());
        }
    }

    public static ResponseEvent sendSnmpTrap(String oid) throws IOException {
        return sendSnmpTrap(Arrays.asList(oid));
    }

    //support snmp v1|v2c|v3
    public static ResponseEvent sendSnmpTrap(List<String> oids) throws IOException {
        PDU pdu ;
        if(version==SnmpConstants.version1)
            pdu=createPDU(PDU.V1TRAP);
        else
            pdu=createPDU(PDU.TRAP);

        for (String oid : oids) {
            if (oid.endsWith(".0")) {
                pdu.add(new VariableBinding(new OID(oid),new OctetString("SNMP Trap Test.")));
            } else {
                throw new IllegalArgumentException(oid + ": 为MIB中非叶子节点，请检查！");
            }
        }
        return snmp.set(pdu,target);
    }

    public static void snmpGetResponse(Boolean syn, final Boolean bro, String oid) throws IOException {
        snmpGetResponse(syn, bro, Arrays.asList(oid));
    }

    public static void snmpGetResponse(Boolean syn, final Boolean bro, List<String> oids) throws IOException {
        PDU pdu = createPDU(PDU.GET);
        for (String oid : oids) {
            pdu.addOID(new VariableBinding(new OID(oid)));
        }
        if (!syn) {
            /**
             * 同步发送模式也称阻塞模式。当管理端发送出一条消息之后，线程会被阻塞，
             * 直到收到对方的回应或者时间超时。同步发送模式编程较为简单，但是不适用于发送广播消息。
             */
            // 发送报文 并且接受响应
            ResponseEvent response = snmp.send(pdu, target);

            System.out.println(
                    "===> Synchronize message from " + response.getPeerAddress() + System.getProperty("line.separator")
                        + "===> request:" + response.getRequest() + System.getProperty("line.separator")
                        + "===> response:" + response.getResponse() + System.getProperty("line.separator")
                        + "===> get object: " + response.getUserObject() + System.getProperty("line.separator"));
        } else {
            /**
             * 异步发送模式也称非阻塞模式。当程序发送一条消息之后，线程将会继续执行，当收到消息的回应的时候，
             * 程序会对消息作出相应的处理。要实现异步发送模式，需要实例化一个实现了ResponseListener接口的类的对象。
             * ResponseListener接口中有一个名为onResponse的函数。这是一个回调函数，当程序收到响应的时候，
             * 会自动调用该函数。由该函数完成对响应的处理。
             */
            ResponseListener listener = new ResponseListener() {

                public void onResponse(ResponseEvent event) {
                    if (bro.equals(false)) {
                        ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    }
                    // 处理响应
                    PDU request = event.getRequest();
                    PDU response = event.getResponse();
                    System.out.println(
                            "===> Asynchronise message from " + event.getPeerAddress() + System.getProperty("line.separator")
                            + "===> request: " + request + System.getProperty("line.separator")
                            + "===> response: " + response.toString());
                }
            };
            // 发送报文
            snmp.send(pdu, target, null, listener);
        }
    }

    public void destory(){
        if(snmp!=null){
            try {
                snmp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
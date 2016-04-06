package pers.nbu.netcourse.config;

/**
 * Created by gracechan on 2015/11/23.
 */
public class SystemConfig {

    //ENCODING------GBK
    public static final String SERVER_CHAR_SET="UTF-8";

    //=======================URL config
    public static final String SERVER_IP = "10.45.60.54";//10.0.2.2本地 10.22.152.120
    public static final String SERVER_PORT = "8080";

    public static final String URL_BASE = "http://" + SERVER_IP + ":" + SERVER_PORT;

    public static final String URL_ALLANN = URL_BASE + "/netcourse/getAllAnnounInfo.action";
    public static final String URL_UPDATEANN = URL_BASE + "/netcourse/getAnnounInfoByNum.action";



    //=================params
    public static final String ANNTITLE="annTitle";
    public static final String ANNCON="annCon";
    public static final String ANNTIME="annTime";
    public static final String ANNURL="annUrl";
    public static final String TEACHNAME="teachName";
    public static final String COURNAME="courName";
    public static final String ANNNUM="annNum";


}

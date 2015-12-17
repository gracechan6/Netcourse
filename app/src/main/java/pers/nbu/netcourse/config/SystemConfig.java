package pers.nbu.netcourse.config;

/**
 * Created by gracechan on 2015/11/23.
 */
public class SystemConfig {
    //=======================URL config
    public static final String SERVER_IP = "10.22.152.120";//10.0.2.2本地
    public static final String SERVER_PORT = "8080";

    public static final String URL_BASE = "http://" + SERVER_IP + ":" + SERVER_PORT;

    public static final String URL_ALLANN = URL_BASE + "/netcourse/getAllAnnounInfo.action";


}

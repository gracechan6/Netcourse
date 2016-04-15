package pers.nbu.netcourse.config;

/**
 * Created by gracechan on 2015/11/23.
 */
public class SystemConfig {
    //ENCODING------GBK
    public static final String SERVER_CHAR_SET="UTF-8";

    //=======================URL config
    public static final String SERVER_IP = "10.22.152.114";//10.0.2.2本地 192.168.1.119/10.45.60.54、192.168.1.182
    public static final String SERVER_PORT = "8080";
    public static final String URL_BASE = "http://" + SERVER_IP + ":" + SERVER_PORT;
    public static final String URL_ALLANN = URL_BASE + "/netcourse/getAllAnnounInfo.action";
    public static final String URL_UPDATEANN = URL_BASE + "/netcourse/getAnnounInfoByNum.action";
    public static final String URL_GETTASK = URL_BASE + "/netcourse/getAllTask.action";
    public static final String URL_GETTASKM = URL_BASE + "/netcourse/getAllTaskManage.action";
    public static final String URL_UPDATETASKM = URL_BASE + "/netcourse/updateTaskManage.action";
    public static final String URL_LOGINVAILD = URL_BASE + "/netcourse/loginVaild.action";
    public static final String URL_GETATTEND = URL_BASE + "/netcourse/getAttend.action";
    public static final String URL_UPDATEATTEND = URL_BASE + "/netcourse/updateAttend.action";
    public static final String URL_UPDATESERVERATTEND = URL_BASE + "/netcourse/updateServerAttend.action";


    //=================params
        //LOGIN
    public static final String LOGINNAME="name";
    public static final String LOGINID="name";
    public static final String LOGINPWD="pwd";
    public static final String USERNAME="stuName";
    public static final String USERCLASS="stuCla";
    public static final String USERREGDATE="regDate";
        //-----ANN
    public static final String ANNTITLE="AnnTitle";
    public static final String ANNCON="AnnCon";
    public static final String ANNTIME="AnnTime";
    public static final String ANNURL="AnnUrl";
    public static final String TEACHNAME="TeachName";
    public static final String COURNAME="CourName";
    public static final String ANNNUM="AnnNum";
    public static final String ANNID="AnnId";

        //------TASK
    public static final String ENDTIME="EndTime";
    public static final String TASKTIME="TaskTime";
    public static final String TASKTITLE="TaskTitle";
    public static final String TASKNUM="TaskNum";
    public static final String TASKID="TaskId";
    public static final String TASKREQUIRE="TaskRequire";

        //------TASKMANAGE
    public static final String TREEID="Treeid";
    public static final String OPUSNUM="OpusNum";





}

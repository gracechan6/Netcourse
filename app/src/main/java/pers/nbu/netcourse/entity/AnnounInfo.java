package pers.nbu.netcourse.entity;

public class AnnounInfo {
	private Integer AnnNum;
    private Integer Treeid;
    private String TeachNum;
    //主要用到的部分
	private String AnnTitle;//1
	private String AnnCon;//2
	private String AnnUrl;//3
	private String AnnTime;//4
    private Integer read;//0已读 1未读

    public Integer getRead() {
        return read;
    }
    public void setRead(Integer read) {
        this.read = read;
    }
    public Integer getAnnNum() {
		return AnnNum;
	}
	public void setAnnNum(Integer annNum) {
		AnnNum = annNum;
	}
	public String getAnnTitle() {
		return AnnTitle;
	}
	public void setAnnTitle(String annTitle) {
		AnnTitle = annTitle;
	}
	public String getAnnCon() {
		return AnnCon;
	}
	public void setAnnCon(String annCon) {
		AnnCon = annCon;
	}
	public String getAnnUrl() {
		return AnnUrl;
	}
	public void setAnnUrl(String annUrl) {
		AnnUrl = annUrl;
	}
	public String getAnnTime() {
		return AnnTime;
	}
	public void setAnnTime(String annTime) {
		AnnTime = annTime;
	}
	public Integer getTreeid() {
		return Treeid;
	}
	public void setTreeid(Integer treeid) {
		Treeid = treeid;
	}
	public String getTeachNum() {
		return TeachNum;
	}
	public void setTeachNum(String teachNum) {
		TeachNum = teachNum;
	}
	
	
}

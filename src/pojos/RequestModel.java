package pojos;

import java.text.SimpleDateFormat;

public class RequestModel {
    private Account account;
    private Hoinghi conf;
    private ThamgiahoinghiId tghoinghiId;

    private int id;
    private String fullname;
    private String email;
    private String confName;
    private String time;
    
    public RequestModel(Account account, Hoinghi conf) {
        this.account = account;
        this.conf = conf;
        
        this.id = account.getIdAccount();
        this.fullname = account.getHoTen();
        this.email = account.getEmail();
        this.confName = conf.getTenHoiNghi();
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyy");
        this.time = sdf.format(conf.getThoiGian());
        this.tghoinghiId = new ThamgiahoinghiId(account.getIdAccount(), conf.getIdHoiNghi());
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Hoinghi getConf() {
        return conf;
    }

    public void setConf(Hoinghi conf) {
        this.conf = conf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public ThamgiahoinghiId getTghoinghiId() {
        return tghoinghiId;
    }

    public void setTghoinghiId(ThamgiahoinghiId tghoinghiId) {
        this.tghoinghiId = tghoinghiId;
    }
}

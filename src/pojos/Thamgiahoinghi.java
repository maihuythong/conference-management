package pojos;

public class Thamgiahoinghi  implements java.io.Serializable {


     private ThamgiahoinghiId id;
     private Account account;
     private Hoinghi hoinghi;
     private boolean active;

    public Thamgiahoinghi() {
    }

    public Thamgiahoinghi(ThamgiahoinghiId id, Account account, Hoinghi hoinghi, boolean active) {
       this.id = id;
       this.account = account;
       this.hoinghi = hoinghi;
       this.active = active;
    }
   
    public ThamgiahoinghiId getId() {
        return this.id;
    }
    
    public void setId(ThamgiahoinghiId id) {
        this.id = id;
    }
    public Account getAccount() {
        return this.account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    public Hoinghi getHoinghi() {
        return this.hoinghi;
    }
    
    public void setHoinghi(Hoinghi hoinghi) {
        this.hoinghi = hoinghi;
    }
    public boolean isActive() {
        return this.active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }




}



package pojos;

public class ThamgiahoinghiId  implements java.io.Serializable {


     private int idAccount;
     private int idHoiNghi;

    public ThamgiahoinghiId() {
    }

    public ThamgiahoinghiId(int idAccount, int idHoiNghi) {
       this.idAccount = idAccount;
       this.idHoiNghi = idHoiNghi;
    }
   
    public int getIdAccount() {
        return this.idAccount;
    }
    
    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }
    public int getIdHoiNghi() {
        return this.idHoiNghi;
    }
    
    public void setIdHoiNghi(int idHoiNghi) {
        this.idHoiNghi = idHoiNghi;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ThamgiahoinghiId) ) return false;
		 ThamgiahoinghiId castOther = ( ThamgiahoinghiId ) other; 
         
		 return (this.getIdAccount()==castOther.getIdAccount())
 && (this.getIdHoiNghi()==castOther.getIdHoiNghi());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdAccount();
         result = 37 * result + this.getIdHoiNghi();
         return result;
   }   


}



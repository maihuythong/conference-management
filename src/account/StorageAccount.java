package account;

import pojos.Account;

/**
 *
 * @author Huy Thông
 */
public class StorageAccount {
    
    private static volatile StorageAccount instance;
    
    private int accountId;
    
    private boolean isAdmin;
    
    private volatile Account account;
    
    private StorageAccount(){
        this.accountId = 0;
        this.isAdmin = false;
    }
    
    public static StorageAccount getInstance(){
        if (instance == null) {
            synchronized (StorageAccount.class) {
                if (instance == null) {
                    instance = new StorageAccount();
                }
            }
        }
        return instance;
    }
    
    public void setAccountId(int id){
        this.accountId = id;
    }
    
    public int getAccountId(){
        return this.accountId;
    }
    
    public void setIsAdmin(boolean value){
        this.isAdmin = value;
    }
    
    public boolean getIsAdmin(){
        return this.isAdmin;
    }
    
    public void setAccount(Account acc){
        this.account = acc;
    }
    
    public Account getAccount(){
        return this.account;
    }
    
    
}

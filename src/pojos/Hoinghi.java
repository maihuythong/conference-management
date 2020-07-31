package pojos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Hoinghi  implements java.io.Serializable {

     private Integer idHoiNghi;
     private Diadiem diadiem;
     private String tenHoiNghi;
     private String moTaNgan;
     private String moTaChiTiet;
     private byte[] hinhAnh;
     private Date thoiGian;
     private Integer khoangThoiGian;
     private int nguoiThamDu;
     private boolean active;
     private Set thamgiahoinghis = new HashSet(0);
     private String fullAddress;
     private Integer currentSize;
     private String status;
     private String dateString;
     private Integer maxSize;
     private int joined;
     
     public Hoinghi(){
         
     }

    public Hoinghi(Integer id, Diadiem diadiem, String tenHoiNghi, String moTaNgan, String moTaChiTiet, byte[] hinhAnh, Date thoiGian, Integer khoangThoiGian, int nguoiThamDu, boolean active, Set thamgiahoinghis) {
            this.idHoiNghi = id;
            this.diadiem = diadiem;
            this.tenHoiNghi = tenHoiNghi;
            this.moTaNgan = moTaNgan;
            this.moTaChiTiet = moTaChiTiet;
            this.hinhAnh = hinhAnh;
            this.thoiGian = thoiGian;
            this.khoangThoiGian = khoangThoiGian;
            this.nguoiThamDu = nguoiThamDu;
            this.active = active;
            this.thamgiahoinghis = thamgiahoinghis;
            this.currentSize = thamgiahoinghis.size();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            this.dateString = format.format(thoiGian);
            this.maxSize = diadiem.getSucChua();
            if(diadiem != null){
                this.fullAddress = diadiem.getTenDiaDiem() + ", " + diadiem.getDiaChi();
            }
            Date current = new Date();
            if(this.thoiGian.compareTo(current) == 1){
                status = "Chưa diễn ra";
            }else {
                status = "Đã diễn ra";
            }
            
            int count = 0;
            Iterator<Thamgiahoinghi> iterator = this.thamgiahoinghis.iterator();
            while (iterator.hasNext()) {
                if(iterator.next().isActive()){
                    count++;
                }
            }
            
            this.joined = count;
    }

    public Hoinghi(Diadiem diadiem, String tenHoiNghi, String moTaNgan, String moTaChiTiet, byte[] hinhAnh, Date thoiGian, Integer khoangThoiGian, boolean active) {
        this.diadiem = diadiem;
        this.tenHoiNghi = tenHoiNghi;
        this.moTaNgan = moTaNgan;
        this.moTaChiTiet = moTaChiTiet;
        this.hinhAnh = hinhAnh;
        this.thoiGian = thoiGian;
        this.khoangThoiGian = khoangThoiGian;
        this.active = active;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        this.dateString = format.format(thoiGian);
        this.maxSize = diadiem.getSucChua();
        if(diadiem != null){
            this.fullAddress = diadiem.getTenDiaDiem() + ", " + diadiem.getDiaChi();
        }
        Date current = new Date();
        if(this.thoiGian.compareTo(current) == 1){
            status = "Chưa diễn ra";
        }else {
            status = "Đã diễn ra";
        }
    }


    public Hoinghi(Diadiem diadiem, String tenHoiNghi, String moTaNgan, String moTaChiTiet, byte[] hinhAnh, Date thoiGian, Integer khoangThoiGian, int nguoiThamDu, boolean active, Set thamgiahoinghis) {
       this.diadiem = diadiem;
       this.tenHoiNghi = tenHoiNghi;
       this.moTaNgan = moTaNgan;
       this.moTaChiTiet = moTaChiTiet;
       this.hinhAnh = hinhAnh;
       this.thoiGian = thoiGian;
       this.khoangThoiGian = khoangThoiGian;
       this.nguoiThamDu = nguoiThamDu;
       this.active = active;
       this.thamgiahoinghis = thamgiahoinghis;
       
       this.currentSize = this.thamgiahoinghis.size();
   
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        this.dateString = format.format(thoiGian);
        this.maxSize = diadiem.getSucChua();
        if(diadiem != null){
            this.fullAddress = diadiem.getTenDiaDiem() + ", " + diadiem.getDiaChi();
        }
        Date current = new Date();
        if(this.thoiGian.compareTo(current) == 1){
            status = "Chưa diễn ra";
        }else {
            status = "Đã diễn ra";
        }
    }

    public Hoinghi(Diadiem diadiem, String tenHoiNghi, String moTaNgan, String moTaChiTiet, byte[] hinhAnh, Date thoiGian, Integer khoangThoiGian, int nguoiThamDu, boolean active) {
       this.diadiem = diadiem;
       this.tenHoiNghi = tenHoiNghi;
       this.moTaNgan = moTaNgan;
       this.moTaChiTiet = moTaChiTiet;
       this.hinhAnh = hinhAnh;
       this.thoiGian = thoiGian;
       this.khoangThoiGian = khoangThoiGian;
       this.nguoiThamDu = nguoiThamDu;
       this.active = active;
       this.thamgiahoinghis = thamgiahoinghis;    
       
       this.currentSize = this.thamgiahoinghis.size();
       
       
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        this.dateString = format.format(thoiGian);
        this.maxSize = diadiem.getSucChua();
        if(diadiem != null){
            this.fullAddress = diadiem.getTenDiaDiem() + ", " + diadiem.getDiaChi();
        }
        Date current = new Date();
        if(this.thoiGian.compareTo(current) == 1){
            status = "Chưa diễn ra";
        }else {
            status = "Đã diễn ra";
        }
    }
    
    
	
    public Hoinghi(Diadiem diadiem, String tenHoiNghi, Date thoiGian, int nguoiThamDu, boolean active) {
        this.diadiem = diadiem;
        this.tenHoiNghi = tenHoiNghi;
        this.thoiGian = thoiGian;
        this.nguoiThamDu = nguoiThamDu;
        this.active = active;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        this.dateString = format.format(thoiGian);
        this.maxSize = diadiem.getSucChua();
        if(diadiem != null){
            this.fullAddress = diadiem.getTenDiaDiem() + ", " + diadiem.getDiaChi();
        }
        Date current = new Date();
        if(this.thoiGian.compareTo(current) == 1){
            status = "Chưa diễn ra";
        }else {
            status = "Đã diễn ra";
        }
    }
    public Hoinghi(Diadiem diadiem, String tenHoiNghi, String moTaNgan, String moTaChiTiet, byte[] hinhAnh, Date thoiGian, int nguoiThamDu, boolean active, Set thamgiahoinghis) {
       this.diadiem = diadiem;
       this.tenHoiNghi = tenHoiNghi;
       this.moTaNgan = moTaNgan;
       this.moTaChiTiet = moTaChiTiet;
       this.hinhAnh = hinhAnh;
       this.thoiGian = thoiGian;
       this.nguoiThamDu = nguoiThamDu;
       this.active = active;
       this.thamgiahoinghis = thamgiahoinghis;
       
       this.currentSize = this.thamgiahoinghis.size();
       
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        this.dateString = format.format(thoiGian);
        this.maxSize = diadiem.getSucChua();
        if(diadiem != null){
            this.fullAddress = diadiem.getTenDiaDiem() + ", " + diadiem.getDiaChi();
        }
        Date current = new Date();
        if(this.thoiGian.compareTo(current) == 1){
            status = "Chưa diễn ra";
        }else {
            status = "Đã diễn ra";
        }
    }

    public Hoinghi(Integer i, Diadiem diadiem, String tenHoiNghi, String moTaNgan, String moTaChiTiet, byte[] hinhAnh, Date thoiGian, Integer khoangThoiGian, int nguoiThamDu, boolean active ) {
        this.idHoiNghi = i;
        this.diadiem = diadiem;
        this.tenHoiNghi = tenHoiNghi;
        this.moTaNgan = moTaNgan;
        this.moTaChiTiet = moTaChiTiet;
        this.hinhAnh = hinhAnh;
        this.thoiGian = thoiGian;
        this.khoangThoiGian = khoangThoiGian;
        this.nguoiThamDu = nguoiThamDu;
        this.active = active;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        this.dateString = format.format(thoiGian);

        this.currentSize = this.thamgiahoinghis.size();
        
        this.maxSize = diadiem.getSucChua();

        if(diadiem != null){
            this.fullAddress = diadiem.getTenDiaDiem() + ", " + diadiem.getDiaChi();
        }
        
        Date current = new Date();
        if(this.thoiGian.compareTo(current) == 1){
            status = "Chưa diễn ra";
        }else {
            status = "Đã diễn ra";
        }
     }

   
   
    public Integer getIdHoiNghi() {
        return this.idHoiNghi;
    }
    
    public void setIdHoiNghi(Integer idHoiNghi) {
        this.idHoiNghi = idHoiNghi;
    }
    public Diadiem getDiadiem() {
        return this.diadiem;
    }
    
    public void setDiadiem(Diadiem diadiem) {
        this.diadiem = diadiem;
    }
    public String getTenHoiNghi() {
        return this.tenHoiNghi;
    }
    
    public void setTenHoiNghi(String tenHoiNghi) {
        this.tenHoiNghi = tenHoiNghi;
    }
    public String getMoTaNgan() {
        return this.moTaNgan;
    }
    
    public void setMoTaNgan(String moTaNgan) {
        this.moTaNgan = moTaNgan;
    }
    public String getMoTaChiTiet() {
        return this.moTaChiTiet;
    }
    
    public void setMoTaChiTiet(String moTaChiTiet) {
        this.moTaChiTiet = moTaChiTiet;
    }
    public byte[] getHinhAnh() {
        return this.hinhAnh;
    }
    
    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    public Date getThoiGian() {
        return this.thoiGian;
    }
    
    public void setThoiGian(Date thoiGian) {
        this.thoiGian = thoiGian;
    }

    public Integer getKhoangThoiGian() {
        return khoangThoiGian;
    }

    public void setKhoangThoiGian(Integer khoangThoiGian) {
        this.khoangThoiGian = khoangThoiGian;
    }
    
    
    public int getNguoiThamDu() {
        return this.nguoiThamDu;
    }
    
    public void setNguoiThamDu(int nguoiThamDu) {
        this.nguoiThamDu = nguoiThamDu;
    }
    public boolean isActive() {
        return this.active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    public Set getThamgiahoinghis() {
        return this.thamgiahoinghis;
    }
    
    public void setThamgiahoinghis(Set thamgiahoinghis) {
        this.thamgiahoinghis = thamgiahoinghis;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public Integer getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(Integer currentSize) {
        this.currentSize = currentSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public int getJoined() {
        return joined;
    }

    public void setJoined(int joined) {
        this.joined = joined;
    }
}



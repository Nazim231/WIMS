package models;

public class ShopDetails {
    
    private int shopId, empId;
    private String shopName, shopAddress, empName;

    public ShopDetails(int shopId, String shopName, String shopAddress) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }

    public ShopDetails(int shopId, String shopName, String shopAddress, int empId, String empName) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.empId = empId;
        this.empName = empName;
    }

    public int getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public int getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }
}

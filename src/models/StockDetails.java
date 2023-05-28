package models;

public class StockDetails {
    
    String prodName, brandName;
    int categoryId, mrp, sellingPrice, costPrice, qty;

    public StockDetails(String prodName, String brandName, int categoryId, int mrp, int sellingPrice, int costPrice, int qty) {
        this.prodName = prodName;
        this.brandName = brandName;
        this.categoryId = categoryId;
        this.mrp = mrp;
        this.sellingPrice = sellingPrice;
        this.costPrice = costPrice;
        this.qty = qty;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setCostPrice(int costPrice) {
        this.costPrice = costPrice;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getProdName() {
        return prodName;
    }

    public String getBrandName() {
        return brandName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getMrp() {
        return mrp;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public int getCostPrice() {
        return costPrice;
    }

    public int getQty() {
        return qty;
    }

}

package models;

public class ShopStockDetails {

    private int id, stockId, quantity;
    private String productName, histroyDate, requestStatus;

    public ShopStockDetails(int id, int stockId, String productName, int quantity) {
        this.id = id;
        this.stockId = stockId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getStockId() {
        return stockId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setHistroyDate(String histroyDate) {
        this.histroyDate = histroyDate;
    }

    public String getHistoryDate() {
        return histroyDate;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestStatus() {
        return requestStatus;
    }
}

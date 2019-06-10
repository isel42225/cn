public class MonthlySale {
    private int totalQuantity;
    private int totalValue;
    private int totalDiscount;
    private int totalTotal;

    public MonthlySale() {
        this.totalQuantity = 0;
        this.totalValue = 0;
        this.totalDiscount = 0;
        this.totalTotal = 0;
    }

    public void addNewSale(int quantity, int value , int discount, int total) {
        this.totalQuantity += quantity;
        this.totalValue += value;
        this.totalDiscount += discount;
        this.totalTotal = total;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public int getTotalDiscount() {
        return totalDiscount;
    }

    public int getTotalTotal() {
        return totalTotal;
    }

    @Override
    public String toString() {
        return "MonthlySale{" +
                "totalQuantity=" + totalQuantity +
                ", totalValue=" + totalValue +
                ", totalDiscount=" + totalDiscount +
                ", totalTotal=" + totalTotal +
                '}';
    }
}

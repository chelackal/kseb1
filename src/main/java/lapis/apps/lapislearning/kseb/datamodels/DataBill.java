package lapis.apps.lapislearning.kseb.datamodels;

public class DataBill {
//{"bill_id":"1","consumer_no":"1212","createdDate":"26-02-2019","amount":"18954","paidDate":"26-02-2019",
// "billStatus":"New","fineDate":"28-03-2019","lastDate":"2019-2-11","netamt":"18954","account":"1"}
    String  billId,consumerNumberBill,createdDateBill,amountBill,paidDateBill,billStatus,fineDateBill,
        lastDateBIll,netAmountBill,accountBill;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getConsumerNumberBill() {
        return consumerNumberBill;
    }

    public void setConsumerNumberBill(String consumerNumberBill) {
        this.consumerNumberBill = consumerNumberBill;
    }

    public String getCreatedDateBill() {
        return createdDateBill;
    }

    public void setCreatedDateBill(String createdDateBill) {
        this.createdDateBill = createdDateBill;
    }

    public String getAmountBill() {
        return amountBill;
    }

    public void setAmountBill(String amountBill) {
        this.amountBill = amountBill;
    }

    public String getPaidDateBill() {
        return paidDateBill;
    }

    public void setPaidDateBill(String paidDateBill) {
        this.paidDateBill = paidDateBill;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getFineDateBill() {
        return fineDateBill;
    }

    public void setFineDateBill(String fineDateBill) {
        this.fineDateBill = fineDateBill;
    }

    public String getLastDateBIll() {
        return lastDateBIll;
    }

    public void setLastDateBIll(String lastDateBIll) {
        this.lastDateBIll = lastDateBIll;
    }

    public String getNetAmountBill() {
        return netAmountBill;
    }

    public void setNetAmountBill(String netAmountBill) {
        this.netAmountBill = netAmountBill;
    }

    public String getAccountBill() {
        return accountBill;
    }

    public void setAccountBill(String accountBill) {
        this.accountBill = accountBill;
    }
}

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long id;
    private Customer customer;
    private Date orderDate;
    private String status;
    private List<OrderItem> items = new ArrayList<>();
    private Payment payment;

    public Order(long id, Customer customer, Date orderDate, String status) {
        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = status;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}

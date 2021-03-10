package schema.edu_orders.orderconfirmed;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import schema.edu_orders.orderconfirmed.Customer;
import schema.edu_orders.orderconfirmed.Item;
import java.io.Serializable;

public class OrderConfirmed implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private BigDecimal id = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("currency")
  private String currency = null;

  @JsonProperty("customer")
  private Customer customer = null;

  @JsonProperty("items")
  private List<Item> items = null;

  public OrderConfirmed id(BigDecimal id) {
    this.id = id;
    return this;
  }
  

  public BigDecimal getId() {
    return id;
  }

  public void setId(BigDecimal id) {
    this.id = id;
  }

  public OrderConfirmed status(String status) {
    this.status = status;
    return this;
  }
  

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OrderConfirmed currency(String currency) {
    this.currency = currency;
    return this;
  }
  

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public OrderConfirmed customer(Customer customer) {
    this.customer = customer;
    return this;
  }
  

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public OrderConfirmed items(List<Item> items) {
    this.items = items;
    return this;
  }
  public OrderConfirmed addItemsItem(Item itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<Item>();
    }
    this.items.add(itemsItem);
    return this;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderConfirmed orderConfirmed = (OrderConfirmed) o;
    return Objects.equals(this.id, orderConfirmed.id) &&
        Objects.equals(this.status, orderConfirmed.status) &&
        Objects.equals(this.currency, orderConfirmed.currency) &&
        Objects.equals(this.customer, orderConfirmed.customer) &&
        Objects.equals(this.items, orderConfirmed.items);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(id, status, currency, customer, items);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderConfirmed {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

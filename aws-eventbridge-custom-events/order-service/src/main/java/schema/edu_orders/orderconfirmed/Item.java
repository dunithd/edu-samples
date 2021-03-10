package schema.edu_orders.orderconfirmed;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.io.Serializable;

public class Item implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("sku")
  private BigDecimal sku = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("price")
  private Double price = null;

  @JsonProperty("quantity")
  private BigDecimal quantity = null;

  public Item sku(BigDecimal sku) {
    this.sku = sku;
    return this;
  }
  

  public BigDecimal getSku() {
    return sku;
  }

  public void setSku(BigDecimal sku) {
    this.sku = sku;
  }

  public Item name(String name) {
    this.name = name;
    return this;
  }
  

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Item price(Double price) {
    this.price = price;
    return this;
  }
  

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Item quantity(BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }
  

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return Objects.equals(this.sku, item.sku) &&
        Objects.equals(this.name, item.name) &&
        Objects.equals(this.price, item.price) &&
        Objects.equals(this.quantity, item.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sku, name, price, quantity);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Item {\n");
    
    sb.append("    sku: ").append(toIndentedString(sku)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

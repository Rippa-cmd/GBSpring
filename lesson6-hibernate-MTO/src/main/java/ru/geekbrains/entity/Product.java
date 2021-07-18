package ru.geekbrains.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@NamedQueries({@NamedQuery(name = "allProducts", query = "select p from Product p")})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512, nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<UserOrder> usersOrders = new ArrayList<>();

    public Product() {
    }

    public Product(String title, Integer price) {
        this.title = title;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<UserOrder> getUsersOrders() {
        return usersOrders;
    }

    public void setUsersOrders(List<UserOrder> usersOrders) {
        this.usersOrders = usersOrders;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}


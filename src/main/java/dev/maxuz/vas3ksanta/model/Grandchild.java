package dev.maxuz.vas3ksanta.model;


import jakarta.persistence.*;

@Entity(name = "grandchild")
public final class Grandchild {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "grandchild_id_seq")
    private Long id;

    @Column(name = "tid", unique = true)
    private String telegramId;

    @Column(name = "vid", unique = true)
    private String vas3kId;

    @Column(name = "vslug")
    private String vslug;

    @Column(name = "admin")
    private boolean admin = false;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    public Grandchild() {
    }

    public Grandchild(String telegramId) {
        this.telegramId = telegramId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public String getVas3kId() {
        return vas3kId;
    }

    public void setVas3kId(String vas3kId) {
        this.vas3kId = vas3kId;
    }

    public String getVslug() {
        return vslug;
    }

    public void setVslug(String vslug) {
        this.vslug = vslug;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}

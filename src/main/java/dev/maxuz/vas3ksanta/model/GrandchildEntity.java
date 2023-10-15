package dev.maxuz.vas3ksanta.model;


import jakarta.persistence.*;

import java.util.List;

@Entity(name = "grandchild")
public final class GrandchildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "grandchild_id_seq")
    private Long id;

    @Column(name = "tid", unique = true)
    private String telegramId;

    @Column(name = "vas3k_uuid", unique = true)
    private String vas3kId;

    @Column(name = "vas3k_slug")
    private String vas3kSlug;

    @Column(name = "admin")
    private boolean admin = false;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @ManyToMany
    @JoinTable(name = "grandchild_recipient_country",
        joinColumns = @JoinColumn(name = "grandchild_id"),
        inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<CountryEntity> recipientCountries;

    public GrandchildEntity() {
    }

    public GrandchildEntity(String telegramId) {
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

    public String getVas3kSlug() {
        return vas3kSlug;
    }

    public void setVas3kSlug(String vas3kSlug) {
        this.vas3kSlug = vas3kSlug;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<CountryEntity> getRecipientCountries() {
        return recipientCountries;
    }

    public void setRecipientCountries(List<CountryEntity> recipientCountries) {
        this.recipientCountries = recipientCountries;
    }
}

package dev.maxuz.vas3ksanta.vas3k.dto;

public class Vas3kUser {
    private String id;
    private String slug;
    private String createdAt;
    private String city;
    private String country;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Vas3kUser{" +
            "id='" + id + '\'' +
            ", slug='" + slug + '\'' +
            ", createdAt=" + createdAt +
            ", city='" + city + '\'' +
            ", country='" + country + '\'' +
            '}';
    }
}

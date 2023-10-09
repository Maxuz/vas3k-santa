package dev.maxuz.vas3ksanta.vas3k.dto;

public class Vas3kUserResponse {
    private Vas3kUser user;

    public Vas3kUser getUser() {
        return user;
    }

    public void setUser(Vas3kUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vas3kUserResponse{" +
            "user=" + user +
            '}';
    }
}

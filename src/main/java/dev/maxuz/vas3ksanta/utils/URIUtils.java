package dev.maxuz.vas3ksanta.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URIUtils {
    private URIUtils() {
    }

    public static String getPublicIp() {
        try {
            String urlString = "http://checkip.amazonaws.com/";
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                return br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't get my ip address", e);
        }
    }
}

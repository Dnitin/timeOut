package com.fastenal.gen.config;

import com.fastenal.gen.model.RequestLeave;
import com.fastenal.gen.model.RequestSwipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.awt.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

@Configuration
public class TimeOutConfig {
    private static final Logger LOG = Logger.getLogger(TimeOutConfig.class.getName());

    @Bean
    public PopupMenu popupMenu() {
        LOG.info("TimeOutConfig :: popupMenu() : Initialised");
        return new PopupMenu();
    }

    @Bean
    public TrayIcon trayIcon() {
        LOG.info("TimeOutConfig :: trayIcon() : Initialised");
        URL url = Thread.currentThread().getContextClassLoader().getResource("images/icon.png");
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        int trayIconWidth = new TrayIcon(image).getSize().width;
        return new TrayIcon(image.getScaledInstance(trayIconWidth,
                -1,Image.SCALE_SMOOTH), "TimeOut", popupMenu());
    }

    @Bean
    public ObjectMapper objectMapper(){
        LOG.info("TimeOutConfig :: objectMapper() : Initialised");
        return new ObjectMapper();
    }

    @Bean
    public RequestLeave requestLeaveObject() {
        LOG.info("TimeOutConfig :: requestLeaveObject() : Initialised");
        return new RequestLeave();
    }

    @Bean
    public RequestSwipe requestSwipeObject() {
        LOG.info("TimeOutConfig :: requestSwipeObject() : Initialised");
        return new RequestSwipe();
    }

    @Bean
    public RestTemplate restTemplate()
    {
        LOG.info("TimeOutConfig :: restTemplate() : Initialised");
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            LOG.severe(e.getMessage());
        } catch (KeyManagementException e) {
            LOG.severe(e.getMessage());
        } catch (KeyStoreException e) {
            LOG.severe(e.getMessage());
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }
}

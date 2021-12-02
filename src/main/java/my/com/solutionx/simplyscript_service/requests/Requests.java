/*
 * Copyright 2021 SolutionX Software Sdn Bhd &lt;info@solutionx.com.my&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package my.com.solutionx.simplyscript_service.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContextBuilder;

/**
 *
 * @author SolutionX Software Sdn Bhd &lt;info@solutionx.com.my&gt;
 */
public class Requests {
    public static Object post(Map<String, Object> args) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, ParseException {
        CloseableHttpClient httpclient = null;
        
        boolean bVerify = true;
        Object objVerify = args.getOrDefault("verify", Boolean.TRUE);
        if (objVerify == Boolean.FALSE || objVerify.toString().equalsIgnoreCase("false")) {
            bVerify = false;
        }

        BasicCredentialsProvider provider = null;
        if (args.getOrDefault("user", null) != null) {
            provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                args.get("user").toString(),
                args.get("password").toString().toCharArray());
            provider.setCredentials(new AuthScope(null, -1), credentials);
        }

        HttpClientBuilder builder = HttpClients.custom();
        if (provider != null)
            builder.setDefaultCredentialsProvider(provider);
        if (bVerify) {
            httpclient = builder.build();
        } else {
            httpclient = builder.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(SSLContextBuilder.create()
                                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                                .build())
                        .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build())
                .build())
            .build();
        }
        
        try {
            HttpPost httpPost = new HttpPost((String) args.get("url"));
            Map<String, ?> mapHeaders = (Map) args.getOrDefault("headers", null);
            if (mapHeaders != null) {
                mapHeaders.entrySet().forEach(entry -> {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                });
            }
            Object objData = args.getOrDefault("data", null);
            if (objData != null) {
                if (objData instanceof Map) {
                    Map<String,?> mapData = (Map) args.getOrDefault("data", null);
                    List<NameValuePair> nvps = new ArrayList<>();
                    mapData.entrySet().forEach(entry -> {
                        String value = entry.getValue() != null ? entry.getValue().toString() : "";
                        nvps.add(new BasicNameValuePair(entry.getKey(), value));
                    });                   
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                    httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Content-type", "application/json");
                } else {
                    StringEntity entity = new StringEntity(objData.toString());
                    httpPost.setEntity(entity);
                }
            }

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                final int status = response.getCode();
                HttpEntity entity = response.getEntity();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    if (entity == null)
                        return null;
                    String content = EntityUtils.toString(entity);
                    EntityUtils.consume(entity);
                    if (entity.getContentType().equalsIgnoreCase("application/json")) {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(content, Map.class);
                    }
                    return content;
                }
                EntityUtils.consume(entity);
                return response.getReasonPhrase();
            }
        } finally {
            if (httpclient != null)
                httpclient.close();
        }
    }

    public static Object get(Map<String, Object> args) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, ParseException {
        CloseableHttpClient httpclient = null;
        
        boolean bVerify = true;
        Object objVerify = args.getOrDefault("verify", Boolean.TRUE);
        if (objVerify == Boolean.FALSE || objVerify.toString().equalsIgnoreCase("false")) {
            bVerify = false;
        }

        BasicCredentialsProvider provider = null;
        if (args.getOrDefault("user", null) != null) {
            provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                args.get("user").toString(),
                args.get("password").toString().toCharArray());
            provider.setCredentials(new AuthScope(null, -1), credentials);
        }

        HttpClientBuilder builder = HttpClients.custom();
        if (provider != null)
            builder.setDefaultCredentialsProvider(provider);
        if (bVerify) {
            httpclient = builder.build();
        } else {
            httpclient = builder.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(SSLContextBuilder.create()
                                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                                .build())
                        .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build())
                .build())
            .build();
        }
        
        try {
            HttpGet httpGet = new HttpGet((String) args.get("url"));
            Map<String, Object> mapHeaders = (Map) args.getOrDefault("headers", null);
            if (mapHeaders != null) {
                mapHeaders.entrySet().forEach(entry -> {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                });
            }

            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                final int status = response.getCode();
                HttpEntity entity = response.getEntity();
                if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                    if (entity == null)
                        return null;
                    String content = EntityUtils.toString(entity);
                    EntityUtils.consume(entity);
                    if (entity.getContentType().equalsIgnoreCase("application/json")) {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(content, Map.class);
                    }
                    return content;
                }
                EntityUtils.consume(entity);
                return response.getReasonPhrase();
            }
        } finally {
            if (httpclient != null)
                httpclient.close();
        }
    }
}

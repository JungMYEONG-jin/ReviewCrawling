package com.shinhan.review.crawler.google;

import com.shinhan.review.crawler.Crawler;
import com.shinhan.review.exception.GooleAPIException;
import com.shinhan.review.exception.KeyReadException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GoogleApi implements Crawler {

    private static final String keyPath = "static/google/client_secret.json";
    private static final String refresh_token = "1//0eHh6SNkcC1SPCgYIARAAGA4SNwF-L9IrO20uMiHCN2C-TL1xpKOl-J2-4RHi9VUaJ98jdnzfthNDFo5sbvzLA2cblzJNxQl1Le4";
    private static final int CONN_TIME_OUT = 1000 * 30;
    // 권한 획득 범위 https://www.googleapis.com/auth/androidpublisher

    public Map<String, String> getClientInfo(){
        JSONObject attr = (JSONObject) readJson(keyPath).get("web");
        JSONArray redirect_uris = (JSONArray) attr.get("redirect_uris");
        attr.remove("redirect_uris");
        attr.put("redirect_uri", redirect_uris.get(0).toString());
        return new HashMap<String, String>(attr);
    }

    public String getAccessToken(){
        Map<String, String> clientInfo = getClientInfo(); // client json parsing
        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/oauth2/v4/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String result = null;
        try {
            result = getAccessTokenX509Post(url, refresh_token, clientInfo.get("client_id"), clientInfo.get("client_secret"), clientInfo.get("redirect_uri"));
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        String resToken = "";
        try {
            JSONObject parse = (JSONObject)parser.parse(result);
            resToken = parse.get("access_token").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resToken;
    }

    /**
     * 해당 메소드로 정해진 형식으로 리뷰 정제해서 가져
     * @param packageName
     * @return
     * @throws MalformedURLException
     */
    public List<JSONObject> getReviewList(String packageName) throws MalformedURLException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, String> map = new HashMap<>();
        map.put("createdDate", "");map.put("nickName", "");map.put("rating", "");map.put("body", "");
        map.put("responseBody", "");map.put("answeredDate", "");map.put("appVersion", "");map.put("device", "");

        // token 생성
        String accessToken = getAccessToken();
        // 리뷰 가져오기
        List<JSONObject> reviewDetails = getReviewDetails(packageName, accessToken);
        if (reviewDetails.isEmpty())
            return null;

        List<JSONObject> res = new ArrayList<>();

        for (JSONObject reviewDetail : reviewDetails) {
            JSONArray reviews = (JSONArray) reviewDetail.get("reviews");
            if (reviews != null) {
                for (Object review : reviews) {
                    JSONObject attr = new JSONObject(map);
                    JSONObject val = (JSONObject) review;
                    if (val.containsKey("authorName")) {
                        String authorName = val.get("authorName").toString();
                        attr.put("nickName", authorName);
                    }
                    JSONArray comments = (JSONArray) val.get("comments");
                    JSONObject comment = (JSONObject) comments.get(0);
                    JSONObject userComment = (JSONObject) comment.get("userComment");
                    String userText = userComment.get("text").toString();
                    attr.put("body", userText);
                    JSONObject userLastModified = (JSONObject) userComment.get("lastModified");
                    Long userSec = Long.parseLong(userLastModified.get("seconds").toString());
                    attr.put("createdDate", dateFormat.format(new Date(userSec * 1000)).toString());
                    String starRating = userComment.get("starRating").toString();
                    attr.put("rating", starRating);
                    if (userComment.containsKey("device")) {
                        String device = userComment.get("device").toString();
                        attr.put("device", device);
                    }
                    if (userComment.containsKey("appVersionName")) {
                        String appVersionName = userComment.get("appVersionName").toString();
                        attr.put("appVersion", appVersionName);
                    }
                    if (userComment.containsKey("androidOsVersion")) {
                        String androidOsVersion = userComment.get("androidOsVersion").toString();
                        attr.put("osVersion", androidOsVersion);
                    }
                    if (comment.containsKey("developerComment")) {
                        JSONObject developerComment = (JSONObject) comment.get("developerComment");
                        String text = developerComment.get("text").toString();
                        attr.put("responseBody", text);
                        attr.put("answeredDate", dateFormat.format(new Date(userSec * 1000)).toString());

                    }
                    res.add(attr);
                }
            }
        }
        return res;
    }

    public List<JSONObject> getReviewDetails(String packageName, String token) throws MalformedURLException {
        String link = "https://www.googleapis.com/androidpublisher/v3/applications/"+packageName+"/reviews?access_token="+token;
        List<JSONObject> res = commonJsonTask(link);
        return res;
    }

    private List<JSONObject> commonJsonTask(String link) throws MalformedURLException {
        URL url = null;
        String reviewDetails = "";
        String nextToken = "";
        List<JSONObject> res = new ArrayList<>();
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            reviewDetails = getConnectResultByX509(url);
            JSONParser parser = new JSONParser();
            JSONObject parseResult = (JSONObject) parser.parse(reviewDetails);
            res.add(parseResult);
            JSONObject next = null;
            nextToken = getNextToken(parseResult, next, nextToken);
            while(nextToken!=null) {

                if (nextToken==null)
                    break;
                reviewDetails = getConnectResultByX509(new URL(link +"&token="+nextToken));
                parseResult = (JSONObject) parser.parse(reviewDetails);
                res.add(parseResult);

                next = null;
                nextToken = null;
                nextToken = getNextToken(parseResult, next, nextToken);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String getNextToken(JSONObject parseResult, JSONObject next, String nextToken) {
        if(parseResult.containsKey("tokenPagination"))
            next = (JSONObject) parseResult.get("tokenPagination");
        if (next !=null && next.containsKey("nextPageToken"))
            nextToken = next.get("nextPageToken").toString();
        return nextToken;
    }

    private String getAccessTokenX509Post(URL url, String token, String clientId, String clientSecret, String redirectURI) throws NoSuchAlgorithmException {

        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLContext sslContext = SSLContext.getInstance("SSL");
        try {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { trustManager },
                    new SecureRandom());
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext,
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme sch = new Scheme("https", 443, socketFactory);
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);

            HttpParams httpParam = httpClient.getParams();
            org.apache.http.params.HttpConnectionParams.setConnectionTimeout(httpParam, CONN_TIME_OUT);
            org.apache.http.params.HttpConnectionParams.setSoTimeout(httpParam, CONN_TIME_OUT);
            HttpRequestBase http = null;
            try {
                HttpPost httpPost = new HttpPost(url.toURI());
                MultipartEntity multipartEntity = new MultipartEntity();
                StringBody grantBody = new StringBody("refresh_token");
                StringBody tokenBdoy = new StringBody(refresh_token);
                StringBody idBody = new StringBody(clientId);
                StringBody secretBody = new StringBody(clientSecret);
                StringBody redirectBody = new StringBody(redirectURI);
                multipartEntity.addPart("refresh_token", tokenBdoy);
                multipartEntity.addPart("client_id", idBody);
                multipartEntity.addPart("client_secret", secretBody);
                multipartEntity.addPart("redirect_uri", redirectBody);
                multipartEntity.addPart("grant_type", grantBody);
                httpPost.setEntity(multipartEntity);
                http = httpPost;
            } catch (Exception e) {
                System.out.println(" error " );
                http = new HttpPost(url.toURI());
            }

            HttpResponse response = null;
            HttpEntity entity = null;
            HttpRequest request = null;
            String responseBody = null;
            /**
             * ??? ?? OUTPUT
             */
            // Time Out
            response = httpClient.execute(http);
            entity = response.getEntity();
            responseBody = EntityUtils.toString(entity, "UTF-8");
            result = responseBody; // json 형식

        } catch (Exception e) {
            throw new GooleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    private String getConnectResultByX509(URL url) throws NoSuchAlgorithmException {
        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLContext sslContext = SSLContext.getInstance("SSL");
        try {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {

                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { trustManager },
                    new SecureRandom());
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext,
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme sch = new Scheme("https", 443, socketFactory);
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);

            HttpParams httpParam = httpClient.getParams();
            org.apache.http.params.HttpConnectionParams.setConnectionTimeout(httpParam, CONN_TIME_OUT);
            org.apache.http.params.HttpConnectionParams.setSoTimeout(httpParam, CONN_TIME_OUT);

            HttpRequestBase http = null;
            try {
                http = new HttpGet(url.toURI());
            } catch (Exception e) {
                http = new HttpPost(url.toURI());
            }

            HttpResponse response = null;
            HttpEntity entity = null;
            HttpRequest request = null;
            String responseBody = null;
            /**
             * ??? ?? OUTPUT
             */
            // Time Out
            response = httpClient.execute(http);
            entity = response.getEntity();
            responseBody = EntityUtils.toString(entity, "UTF-8");
            result = responseBody; // json 형식

        } catch (Exception e) {
            throw new GooleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }


    private JSONObject readJson(String keyPath)
    {
        InputStream inputStream = null;
        inputStream = this.getClass().getClassLoader().getResourceAsStream(keyPath); // in native read without spring core
        JSONObject content = null;
        try
        {
            JSONParser parser = new JSONParser();
            content = (JSONObject)parser.parse(new InputStreamReader(inputStream)); // jar 배포시 getFile은 에러 발생 가능성 높음. inputstream으로 읽어오기
        }catch(IOException e)
        {
            throw new KeyReadException("Private Key read Failed... " + e);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return content;
    }

    @Override
    public List<JSONObject> getReview(String packageName) {
        try {
            return getReviewList(packageName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

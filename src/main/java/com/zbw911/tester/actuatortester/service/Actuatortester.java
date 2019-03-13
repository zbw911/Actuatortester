package com.zbw911.tester.actuatortester.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.actuate.management.ThreadDumpEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangbaowei
 * Create  on 2019/3/12 10:48.
 */
@Service
public class Actuatortester {


    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private EnvironmentEndpoint environmentEndpoint;

    @Autowired
    private ThreadDumpEndpoint threadDumpEndpoint;
    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private MetricsEndpoint metricsEndpoint;


    public void testMeter() {

        List<MetricsEndpoint.MetricResponse> metricResponseList = new ArrayList<>();

        for (String name : metricsEndpoint.listNames().getNames()) {

            MetricsEndpoint.MetricResponse metric = metricsEndpoint.metric(name, null);

            metricResponseList.add(metric);
        }

        ActouorModel actouorModel = new ActouorModel();

        actouorModel.setAppname(appName);
        actouorModel.setType("meter");
        actouorModel.setData(metricResponseList);
        actouorModel.setHost(getHostName());
        actouorModel.setIp(getIp());

        post(toJson(actouorModel));

    }


    public void testenvironmentEndpoint() {
        EnvironmentEndpoint.EnvironmentDescriptor environment = environmentEndpoint.environment("");

        ActouorModel actouorModel = new ActouorModel();

        actouorModel.setAppname(appName);
        actouorModel.setType("env");
        actouorModel.setData(environment);
        actouorModel.setHost(getHostName());
        actouorModel.setIp(getIp());
        post(toJson(actouorModel));
    }


    public void testthreadDumpEndpoint() {


        ActouorModel actouorModel = new ActouorModel();

        actouorModel.setAppname(appName);
        actouorModel.setType("threaddump");
        actouorModel.setData(threadDumpEndpoint.threadDump());
        actouorModel.setHost(getHostName());
        actouorModel.setIp(getIp());
        post(toJson(actouorModel));
    }

    private String toJson(Object obj) {
        try {
            return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }

        return "Exception";
    }


    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();

            return "exeption host";
        }
    }


    private String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();

            return "0.0.0.1";
        }
    }

    private String post(String requestJson) {

        String url = "http://10.168.99.124:11090";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);


        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.toString();
    }

}


class ActouorModel {
    private String type;
    private String appname;
    private Object Data;
    private String host;
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHost() {

        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}

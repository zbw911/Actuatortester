package com.zbw911.tester.actuatortester;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.actuate.management.ThreadDumpEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActuatortesterApplicationTests {


    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    EnvironmentEndpoint environmentEndpoint;

    @Autowired
    ThreadDumpEndpoint threadDumpEndpoint;
    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private MetricsEndpoint metricsEndpoint;


    @Test
    public void contextLoads() {
    }


    @Test
    public void TestMeter1() throws JsonProcessingException {


        List<MetricsEndpoint.MetricResponse> metricResponseList = new ArrayList<>();

        for (String name : metricsEndpoint.listNames().getNames()) {
//            System.out.println(name);
            MetricsEndpoint.MetricResponse metric = metricsEndpoint.metric(name, null);

            metricResponseList.add(metric);
        }


//        System.out.println(toJson(metricResponseList));
//        String s = new ObjectMapper().writeValueAsString(list);
//
//        System.out.println(s);

        ActouorModel actouorModel = new ActouorModel();

        actouorModel.setAppname(appName);
        actouorModel.setType("meter");
        actouorModel.setData(metricResponseList);
        actouorModel.setHost("this is a host");

        System.out.println(toJson(actouorModel));

    }


    @Test
    public void TestenvironmentEndpoint() {
        EnvironmentEndpoint.EnvironmentDescriptor environment = environmentEndpoint.environment("");

        ActouorModel actouorModel = new ActouorModel();

        actouorModel.setAppname(appName);
        actouorModel.setType("env");
        actouorModel.setData(environment);
        actouorModel.setHost("this is a host");

        System.out.println(toJson(actouorModel));
    }

    @Test
    public void TestjmxEndpoint() {


    }

    @Test
    public void TestthreadDumpEndpoint() {


        ActouorModel actouorModel = new ActouorModel();

        actouorModel.setAppname(appName);
        actouorModel.setType("threaddump");
        actouorModel.setData(threadDumpEndpoint.threadDump());
        actouorModel.setHost("this is a host");

        System.out.println(toJson(actouorModel));

        System.out.println(post(toJson(actouorModel)));
    }

    private String toJson(Object obj) {
        try {
            return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }

        return "Exception";
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

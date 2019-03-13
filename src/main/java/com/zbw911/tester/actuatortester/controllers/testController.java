package com.zbw911.tester.actuatortester.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * @author zhangbaowei
 * Create  on 2019/3/12 18:53.
 */

@RestController
public class testController {


    @GetMapping("/aa/b")
    public String delay() throws InterruptedException {
        Thread.sleep(1 * 1000);

        return new Date().toString();
    }

    @GetMapping("/aa/ip")
    public String ip() throws InterruptedException, UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        String hostName = localHost.getHostName();

        String hostAddress = localHost.getHostAddress();
        return hostName + "|" + hostAddress;
    }
}

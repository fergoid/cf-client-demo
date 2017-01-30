package com.ferg.cf.service;

import com.ferg.cf.config.DefaultConfig;
import lombok.extern.java.Log;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Created by a261004 on 22/12/2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DefaultConfig.class})
@Log
public class TestCFService {

    @Autowired
    CFClientService cfClientService;

    @Autowired
    PasswordGrantTokenProvider tokenProvider;

    @Autowired
    DefaultConnectionContext connectionContext;

    @Autowired
    ReactorCloudFoundryClient cloudFoundryClient;


    @Test
    public void testGetApps() {
        log.info("testGetApps");
        List<String> apps = cfClientService.getApplications();
        apps.forEach(System.out::println);
    }

    @Test
    public void testGetOrgs() {
        log.info("testGetOrgs");
        List<String> apps = cfClientService.getOrgs();
        apps.forEach(System.out::println);
    }

    @Test
    public void testMF() {
        log.info("testMF");
        cloudFoundryClient.organizations()
                .list(ListOrganizationsRequest.builder()
                        .page(1)
                        .build())
                .flatMap(response -> Flux.fromIterable(response.getResources()))
                .subscribe(System.out::println);
    }

}

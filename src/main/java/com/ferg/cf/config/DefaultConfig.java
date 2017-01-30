package com.ferg.cf.config;

import com.ferg.cf.service.CFClientService;
import lombok.extern.java.Log;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.UaaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.Duration;

/**
 * Created by a261004 on 22/12/2016.
 */
@Configuration
@Log
public class DefaultConfig {
    @Bean
    DefaultConnectionContext connectionContext(@Value("${cf.apiHost}") String apiHost) {
        log.info(">>> connectionContext");
        return DefaultConnectionContext.builder()
                .apiHost(apiHost)
                .port(443)
                .skipSslValidation(true)
                .sslHandshakeTimeout(Duration.ofSeconds(30))
                .build();
    }

    @Bean
    PasswordGrantTokenProvider tokenProvider(@Value("${cf.username}") String username,
                                             @Value("${cf.password}") String password) {
        log.info(">>> tokenProvider");
        return PasswordGrantTokenProvider.builder()
                .password(password)
                .username(username)
                .clientId("cf")
                .clientSecret("admin-client-secret")
                .build();
    }

    @Bean
    @DependsOn({"connectionContext","tokenProvider"})

    ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        log.info(">>> cloudFoundryClient");

        return ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Bean
    @DependsOn({"connectionContext","tokenProvider"})

    ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        log.info(">>> dopplerClient");

        return ReactorDopplerClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Bean
    @DependsOn({"connectionContext","tokenProvider"})

    ReactorUaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        log.info(">>> uaaClient");
        return ReactorUaaClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Bean
    @DependsOn({"uaaClient","dopplerClient","cloudFoundryClient"})

    DefaultCloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
                                                         DopplerClient dopplerClient,
                                                         UaaClient uaaClient,
                                                         @Value("${cf.organization}") String organization,
                                                         @Value("${cf.space}") String space) {

        log.info(">>> cloudFoundryOperations");

        return DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .dopplerClient(dopplerClient)
                .uaaClient(uaaClient)
                .organization(organization)
                .space(space)
                .build();
    }

    @Bean
    @DependsOn("cloudFoundryOperations")
    CFClientService getCFClientService() {
        log.info(">>> getCFClientService");

        return new CFClientService();
    }
}

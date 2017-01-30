package com.ferg.cf.service;

import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a261004 on 22/12/2016.
 */
public class CFClientService {

    @Autowired
    private CloudFoundryOperations cloudFoundryOperations;


    private List<String> orgs = new ArrayList<>();
    private List<String> apps = new ArrayList<>();


    public List<String> getOrgs() {

        cloudFoundryOperations.organizations()
                .list()
                .map(OrganizationSummary::getName)
                .subscribe(System.out::println);

        cloudFoundryOperations.
                organizations().
                list().
                map(OrganizationSummary::getName).subscribe(s -> orgs.add(s));
        return orgs;
    }

    public List<String> getApplications() {

        cloudFoundryOperations.applications()
                .list()
                .map(ApplicationSummary::getName)
                .subscribe(System.out::println);

        cloudFoundryOperations.
                applications().
                list().
                map(ApplicationSummary::getName).subscribe(s -> apps.add(s));
        return apps;
    }

}

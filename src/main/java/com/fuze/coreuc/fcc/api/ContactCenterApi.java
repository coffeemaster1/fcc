package com.fuze.coreuc.fcc.api;

import com.fuze.coreuc.fcc.api.rest.CallResource;
import com.fuze.coreuc.fcc.api.rest.ExtensionResource;
import com.fuze.coreuc.fcc.api.rest.PeerResource;
import com.fuze.coreuc.fcc.api.rest.QueueResource;
import com.fuze.coreuc.fcc.api.serialization.CustomGsonProvider;
import com.google.inject.Inject;
import com.thinkingphones.absolute.api.ApiServer;
import com.thinkingphones.absolute.api.ApiServerBuilder;
import com.thinkingphones.absolute.api.interceptors.CrossDomainHeaderInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ContactCenterApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactCenterApi.class);

    private final ApiServer apiServer;

    @Inject
    public ContactCenterApi(QueueResource queues, ExtensionResource extensions, CallResource calls, PeerResource peers) {

        // Build API server
		apiServer = new ApiServerBuilder()
				.port(8081)
				.defaultHttpStatusCodeForException(400)
				.registerProviderInstances(new CrossDomainHeaderInterceptor("Origin", "X-Requested-With", "Content-Type", "Accept"))
				.registerProviderClasses(CustomGsonProvider.class)
				.registerResourceInstances(queues)
				.registerResourceInstances(extensions)
				.registerResourceInstances(calls)
				.registerResourceInstances(peers)
				.build();
    }

    public void start() {
        apiServer.start();
        LOGGER.info("API server started...");
    }

    public void stop() {
        apiServer.stop();
        LOGGER.info("API server shutdown");
    }
}

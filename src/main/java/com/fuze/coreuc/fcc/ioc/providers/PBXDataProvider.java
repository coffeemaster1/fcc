package com.fuze.coreuc.fcc.ioc.providers;

import com.fuze.coreuc.fcc.builders.PBXBuilder;
import com.fuze.coreuc.fcc.config.MyAppConfig;
import com.fuze.coreuc.fcc.data.CallDataSource;
import com.fuze.coreuc.fcc.data.PBXDataSource;
import com.fuze.coreuc.fcc.data.PeerDataSource;
import com.fuze.coreuc.fcc.models.PBX;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.asteriskjava.manager.TimeoutException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PBXDataProvider implements Provider<PBXDataSource> {

	private PBXDataSource pbxMap;

	@Inject
	public PBXDataProvider(MyAppConfig cfg, PBXBuilder builder) throws IOException, TimeoutException {
		Map<String, PBX> pbxs = new HashMap<>();

		for (String s : cfg.pbxs()) {
			pbxs.put(s,builder.buildPBX(s));
		}
		pbxMap = new PBXDataSource( pbxs );

	}

	@Override
	public PBXDataSource get() {
		return pbxMap;
	}
}

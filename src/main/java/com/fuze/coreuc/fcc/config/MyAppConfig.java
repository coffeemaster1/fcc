package com.fuze.coreuc.fcc.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

import java.util.List;

@Sources({"file:/Users/rforest/IdeaProjects/fcc/src/main/resources/config.properties", "classpath:config.properties"})
//@Config.Sources({"file:/opt/fcc/config/config.properties", "classpath:config.properties"})
public interface MyAppConfig extends Config {

	@Key("ami.port")
	Integer amiPort();

	@Key("ami.host")
	String amiHost();

	@Key("ami.username")
	String amiUser();

	@Key("ami.password")
	String amiPassword();

	@Key("pbx.list")
	List<String> pbxs();

	@Key("db.username")
	String getDBUser();

	@Key("db.password")
	String getDBPass();

	@Key("db.host")
	String getDBHost();

	@Key("db.db")
	String getDBDB();

}


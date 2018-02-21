package com.fuze.coreuc.fcc.datasource;

import com.fuze.coreuc.fcc.models.Extension;
import com.fuze.coreuc.fcc.models.Peer;
import com.fuze.coreuc.fcc.models.Queue;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface CfMapper {

	@Select("SELECT peer_name as peerName, exten_did as extension, user_id as userName, label, exten_pbx as pbx, sip_buddies.context as transferContext from peer_info join sip_buddies on peer_info.peer_name = sip_buddies.name where exten_pbx = #{pbx}")
	@MapKey("peerName")
	Map<String, Peer> getPeersFromPBX(@Param("pbx") String pbx);

	@Select("SELECT extension, did, mailbox, mailbox_context as mailboxContext, user_id as userName from extension_info where pbx = #{pbx}")
	@MapKey("did")
	Map<String, Extension> getExtensionsFromPBX(@Param("pbx") String pbx);

    @Select("SELECT name as queueName, SUBSTRING( name , 1 ,POSITION(\"-\" in name) - 1) as pbx from queue_table where name like #{pbx}")
	@MapKey("queueName")
	Map<String, Queue> getQueuesFromPBX(@Param("pbx") String pbx);

    @Select("SELECT distinct accountcode from sip_buddies join peer_info on sip_buddies.name=peer_info.peer_name where peer_info.exten_pbx = #{pbx}")
	List<String> getOrgCodes(@Param("pbx") String pbx);

}
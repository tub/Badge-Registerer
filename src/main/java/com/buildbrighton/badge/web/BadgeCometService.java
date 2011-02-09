package com.buildbrighton.badge.web;

import java.util.Map;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

import com.buildbrighton.badge.Badge;
import com.buildbrighton.badge.BadgeEventListener;

public class BadgeCometService extends AbstractService implements BadgeEventListener{

	public BadgeCometService(BayeuxServer bayeux) {
	    super(bayeux, "badge");
	    addService("/badgeId", "processBadgeRequest");
    }

	public void processBadgeRequest(ServerSession remote, Map<String, Object> data){
		remote.deliver(getServerSession(), "/badgeId", data, null);
	}
	
	public void badgeIdChanged(Badge b) {
        getBayeux().getChannel("/badgeId").publish(getServerSession(), b.getId(), null);
    }

}

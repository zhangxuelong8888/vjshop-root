
package com.vjshop.service.impl;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * Service - Daemon
 * 
 * @author VJSHOP Team
 * @version 4.0
 */
@Service("daemonServiceImpl")
public class DaemonServiceImpl implements ApplicationListener<ContextRefreshedEvent> {

	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		try {
//			ClassUtils.getClass(StringUtils.remove("ne?t.?sh?op?xx?.e?xc?ep?ti?on?.I?nc?or?re?ct?Li?ce?ns?eE?xc?ep?ti?on", "?"));
			ClassUtils.getClass(StringUtils.remove("co?m.?vj?sh?op?.e?xc?ep?ti?on?.I?nc?or?re?ct?Li?ce?ns?eE?xc?ep?ti?on", "?"));
			ClassUtils.getClass(StringUtils.remove("co?m.?vj?sh?op?.A?ut?he?nt?ic?at?io?nR?ea?lm", "?"));
//			ClassUtils.getClass(StringUtils.remove("or?g.?ap?ac?he?.s?hi?ro?.r?ea?lm?.A?ut?hR?ea?lm", "?"));
		} catch (ClassNotFoundException e) {
			System.exit(0);
		}
	}

}
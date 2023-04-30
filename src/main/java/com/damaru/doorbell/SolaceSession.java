
package com.damaru.doorbell;

import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.SpringJCSMPFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Scope("prototype")
public class SolaceSession {

	@Autowired
	private SpringJCSMPFactory springJCSMPFactory;
	private JCSMPSession session;

	@PostConstruct
	public void init() throws Exception {
		session = springJCSMPFactory.createSession();
	}

	public JCSMPSession getSession() {
		return session;
	}

	public void close() {
		if (!session.isClosed()) {
			session.closeSession();
		}
	}
}

package com.itsaky.androidide.tasks.callables;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.File;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.util.FS;
import com.itsaky.androidide.utils.PrivateKeyUtils;
import org.eclipse.jgit.transport.Transport;

public class SshTransportConfigCallback implements TransportConfigCallback {

	private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
		@Override
		protected void configure(OpenSshConfig.Host hc, Session session) {
			session.setConfig("StrictHostKeyChecking", "no");
		}

		@Override
		protected JSch createDefaultJSch(FS fs) throws JSchException {
			//Todo convert it to ILogger
			PrivateKeyUtils.copySshKey();
			JSch.setLogger(new MyLogger());
			JSch jsch = new JSch();
			//jsch.setKnownHosts(PrivateKeyUtils.getSshKeyFolder().getAbsolutePath() + "/known_hosts");
			JSch jSch = super.createDefaultJSch(fs);
			jSch.removeAllIdentity();
			//curently jsch works with only is_rsa 4096-bit key
			String string = PrivateKeyUtils.getSshKeyFolder().getAbsoluteFile() + "/id_rsa";
			//not impleted for custom secret-passphrase
			jSch.addIdentity(string, "super-secret-passphrase".getBytes());

			return jSch;
		}
	};

	@Override
	public void configure(Transport transport) {
		SshTransport sshTransport = (SshTransport) transport;
		sshTransport.setSshSessionFactory(sshSessionFactory);
	}

	public static class MyLogger implements com.jcraft.jsch.Logger {
		static java.util.Hashtable name = new java.util.Hashtable();

		static {
			name.put(new Integer(DEBUG), "DEBUG: ");
			name.put(new Integer(INFO), "INFO: ");
			name.put(new Integer(WARN), "WARN: ");
			name.put(new Integer(ERROR), "ERROR: ");
			name.put(new Integer(FATAL), "FATAL: ");
		}

		public boolean isEnabled(int level) {
			return true;
		}

		public void log(int level, String message) {
			System.err.print(name.get(new Integer(level)));
			System.err.println(message);
		}
	}

}

package app.msupply.com.ideaurben.Commonclass;

import java.security.AccessController;
import java.security.Provider;

/**
 * Created by uOhmac Technologies on 09-Aug-17.
 */

class JSSEProvider extends Provider {


    protected JSSEProvider(String name, double version, String info) {
        super(name, version, info);
    }

    public JSSEProvider() {
        super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
        AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
            public Void run() {
                put("SSLContext.TLS",
                        "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
                put("Alg.Alias.SSLContext.TLSv1", "TLS");
                put("KeyManagerFactory.X509",
                        "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
                put("TrustManagerFactory.X509",
                        "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
                return null;
            }
        });
    }
}

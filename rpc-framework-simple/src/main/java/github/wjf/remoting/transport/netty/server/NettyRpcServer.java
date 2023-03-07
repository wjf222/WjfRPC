package github.wjf.remoting.transport.netty.server;

import github.wjf.factory.SingletonFactory;
import github.wjf.provider.ServiceProvider;
import github.wjf.provider.impl.ZkServiceProviderImpl;
import org.springframework.stereotype.Component;

@Component
public class NettyRpcServer {
    public static final int PORT = 9998;

    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
}

package github.wjf.registry.zookeeper;

import github.wjf.enums.RpcErrorMessageEnum;
import github.wjf.exception.RpcException;
import github.wjf.extension.ExtensionLoader;
import github.wjf.loadbalance.LoadBalance;
import github.wjf.registry.ServiceDiscovery;
import github.wjf.registry.zookeeper.util.CuratorUtils;
import github.wjf.remoting.dto.RpcRequest;
import github.wjf.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class ZookeeperServiceDiscoverImpl implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ZookeeperServiceDiscoverImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}

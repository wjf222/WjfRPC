package github.wjf.remoting.transport;

import github.wjf.extension.SPI;
import github.wjf.remoting.dto.RpcRequest;

@SPI
public interface RpcRequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}

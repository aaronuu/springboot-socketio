package cn.sonicshield.socketio.handler;

import cn.sonicshield.socketio.model.Message;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author ：zhangyu
 * @Date ：Created in 16:20 2019-05-22
 * @Description ：
 * @ModifiedBy ：
 */
@Slf4j
public class MessageHandler {

  private final SocketIOServer server;

  public MessageHandler(SocketIOServer server) {
    this.server = server;
  }

  //添加 connect 事件，当客户端发起连接时调用
  @OnConnect
  public void onConnect(SocketIOClient client) {
    log.info("onConnect > namespace={}, sessionId={}", client.getNamespace().getName(), client.getSessionId().toString());
  }

  //添加 disconnect 事件，客户端断开连接时调用
  @OnDisconnect
  public void onDisconnect(SocketIOClient client) {
    client.disconnect();
    log.info("onDisconnect > namespace={}, sessionId={}", client.getNamespace().getName(), client.getSessionId().toString());
  }

  // 自定义 message 事件
  @OnEvent(value = "message")
  public void onEvent(SocketIOClient client, AckRequest ackRequest, Message message) {
    log.info("接收到客户端 namespace message = {}", client.getNamespace().getName(), message);
    if (ackRequest.isAckRequested()) {
      ackRequest.sendAckData(Message.builder().message("Hello World").ts(System.currentTimeMillis()).build());
    }
    client.sendEvent("message", message);
  }

  /**
   * How to use the protobuf protocol:
   * https://github.com/mrniko/netty-socketio/issues/497
   */

}
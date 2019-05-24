package cn.sonicshield.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class NettySocketConfig {

  private static final String TOKEN = "TOKEN";

  // 监听端口
  private static final Integer SOCKET_PORT = 9099;

  // Ping消息间隔（毫秒），默认25000。客户端向服务器发送一条心跳消息间隔
  private static final Integer PING_INTERVAL = 60000;

  // Ping消息超时时间（毫秒），默认60000，这个时间间隔内没有接收到心跳消息就会发送超时事件
  private static final Integer PING_TIMEOUT = 180000;

  // 协议升级超时时间（毫秒），默认10000。HTTP握手升级为ws协议超时时间
  private static final Integer UPGRADE_TIMEOUT = 10000;

  @Bean
  public SocketIOServer socketIOServer() {
    /*
     * 创建Socket，并设置监听端口
     */
    com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

    // 设置主机名，默认是0.0.0.0
    // config.setHostname("localhost");

    config.setPort(SOCKET_PORT);
    config.setUpgradeTimeout(UPGRADE_TIMEOUT);
    config.setPingInterval(PING_INTERVAL);
    config.setPingTimeout(PING_TIMEOUT);

    // 握手协议参数使用JWT的Token认证方案
    config.setAuthorizationListener(data -> {
      String token = data.getSingleUrlParam(TOKEN);
      return !Strings.isNullOrEmpty(token);
    });
    return new SocketIOServer(config);
  }

  @Bean
  public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
    return new SpringAnnotationScanner(socketServer);
  }
}

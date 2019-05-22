package cn.sonicshield.socketio.common;

import cn.sonicshield.socketio.handler.MessageHandler;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class ServerRunner implements CommandLineRunner {

  private List<String> namespaceList = Lists.newArrayList("/message", "/test");

  private final SocketIOServer server;
  private final SocketIONamespace messageSocketNameSpace;
  private final SocketIONamespace testSocketNameSpace;

  @Autowired
  private ServerRunner(SocketIOServer server) {
    this.server = server;
    messageSocketNameSpace = server.addNamespace(namespaceList.get(0));
    testSocketNameSpace = server.addNamespace(namespaceList.get(1));
  }

  @Bean(name = "messageNamespace")
  public SocketIONamespace messageNameSpace() {
    messageSocketNameSpace.addListeners(new MessageHandler(server));
    return messageSocketNameSpace;
  }

  @Bean(name = "testNamespace")
  public SocketIONamespace testSpace() {
    testSocketNameSpace.addListeners(new MessageHandler(server));
    return testSocketNameSpace;
  }

  @Override
  public void run(String... args) {
    log.info("#############################");
    log.info("#                           #");
    log.info("#  ServerRunner 开始启动...  #");
    log.info("#                           #");
    log.info("#############################");
    server.start();
  }
}
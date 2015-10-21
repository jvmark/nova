package com.onmysky.nova.server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.onmysky.nova.common.constant.ZkConstant;

/**
 * Created by mark on 15/10/21.
 */
public class ServiceRegistry {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

  private CountDownLatch latch = new CountDownLatch(1);

  private String registryAddress;

  public ServiceRegistry(String registryAddress) {
    this.registryAddress = registryAddress;
  }

  public void register(String data) {
    if (data != null) {
      ZooKeeper zk = connectServer();
      if (zk != null) {
        createNode(zk, data);
      }
    }
  }

  private ZooKeeper connectServer() {
    ZooKeeper zk = null;
    try {
      zk = new ZooKeeper(registryAddress, ZkConstant.ZK_SESSION_TIMEOUT, new Watcher() {

        public void process(WatchedEvent event) {
          if (event.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
          }
        }
      });
      latch.await();
    } catch (IOException | InterruptedException e) {
      LOGGER.error("", e);
    }
    return zk;
  }

  private void createNode(ZooKeeper zk, String data) {
    try {
      byte[] bytes = data.getBytes();
      String path = zk.create(ZkConstant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
      LOGGER.debug("create zookeeper node ({} => {})", path, data);
    } catch (KeeperException | InterruptedException e) {
      LOGGER.error("", e);
    }
  }
}

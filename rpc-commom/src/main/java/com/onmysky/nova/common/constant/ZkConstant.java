package com.onmysky.nova.common.constant;

/**
 * Created by mark on 15/10/21.
 */
public interface ZkConstant {
  int ZK_SESSION_TIMEOUT = 5000;

  String ZK_REGISTRY_PATH = "/registry";
  String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}

package com.wuyou.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.wuyou.common.config.Global;
import com.wuyou.common.core.text.CharsetKit;
import com.wuyou.common.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取地址类
 *
 * @author wuyou
 */
@Slf4j
public class AddressUtils {

  // IP 地址查询
  public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
  // 未知地址
  public static final String UNKNOWN = "XX XX";

  public static String getRealAddressByIp(String ip) {
    String address = UNKNOWN;

    // 内网不查询
    if (IpUtils.internalIp(ip)) {
      return "内网 IP";
    }
    if (Global.isAddressEnabled()) {
      try {
        String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", CharsetKit.GBK.name());
        if (StringUtils.isEmpty(rspStr))
        {
          log.error("获取地理位置异常 {}", ip);
          return UNKNOWN;
        }
        JSONObject obj = JSONObject.parseObject(rspStr);
        String region = obj.getString("pro");
        String city = obj.getString("city");
        return String.format("%s %s", region, city);
      } catch (Exception e) {
        log.error("获取地理位置异常 {}", e);
      }
    }
    return address;
  }
}

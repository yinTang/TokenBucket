package cn.com.bucket.controller;


import cn.com.bucket.Shaping;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author yintang
 */
@RequestMapping("api/")
@Controller
public class TestController {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    /**
     * 为每一个接口初始化一个令牌桶
     */
    @PostConstruct
    private void init() {
        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            String mapping = entry.getKey().getPatternsCondition().toString();
            String[] methodPattern = mapping.replaceAll("\\[|\\]", "").split("\\|\\|");
            if (ArrayUtils.isNotEmpty(methodPattern)) {
                for (String m : methodPattern) {
                    Shaping.updateResourceQps(m, 10d);
                }
            }
        }
    }

    @RequestMapping("test1")
    @ResponseBody
    public Object test1() {
        return "test1........";
    }

    @RequestMapping("test2")
    @ResponseBody
    public Object test2() {
        return "test2........";
    }
}

package cn.mintimate.bugmaker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class VulnerableController {

    /**
     * CVE-2021-44228 漏洞演示端点
     * 通过 User-Agent 头触发 JNDI 注入
     */
    @GetMapping("/api/log")
    public String logUserAgent(@RequestHeader(value = "User-Agent", required = false) String userAgent) {
        log.info("Received request from User-Agent: {}", userAgent);
        return "Request logged successfully!";
    }

    /**
     * 通过查询参数触发漏洞
     */
    @GetMapping("/api/search")
    public String search(@RequestParam(value = "query", required = false) String query) {
        log.info("Search query: {}", query);
        return "Search completed for: " + query;
    }

    /**
     * 健康检查端点
     */
    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
}

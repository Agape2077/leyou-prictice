package com.Leyou.gateway.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("leyou.filter")
public class FilterProperties {

    public List<String> getAllowPaths() {
        return AllowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        AllowPaths = allowPaths;
    }

    private List<String>AllowPaths ;



}

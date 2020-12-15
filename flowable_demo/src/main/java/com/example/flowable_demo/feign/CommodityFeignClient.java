package com.example.flowable_demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "commodity.application.name")
public interface CommodityFeignClient {

    @PostMapping(value = "/product/update")
    void updateProductStatus();

    @PostMapping(value = "/commodity/update")
    void updateCommodityStatus();

}

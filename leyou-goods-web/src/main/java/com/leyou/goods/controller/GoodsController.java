package com.leyou.goods.controller;


import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileNotFoundException;
import java.util.Map;

@Controller
public class GoodsController {

    @Autowired private GoodsService goodsService;

    @Autowired private GoodsHtmlService goodsHtmlService;


    @GetMapping("item/{spuId}.html") public String toItemPage(Model model, @PathVariable Long spuId)   {
        Map<String, Object> modelMap = this.goodsService.loadData(spuId);
        model.addAllAttributes(modelMap);

        this.goodsHtmlService.createHtml(spuId);

    return "item";
    }
}

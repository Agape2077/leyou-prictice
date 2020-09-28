package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService {
    @Autowired private TemplateEngine templateEngine;
    @Autowired private GoodsService goodsService;

    public void createHtml(Long spuId)    {

        Context context = new Context();
        context.setVariables(this.goodsService.loadData(spuId));

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new File("C:\\Users\\Agape\\IdeaProjects\\leyou\\nginx-1.8.0\\html\\item\\" + spuId + ".html"));
            this.templateEngine.process("item", context, printWriter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (printWriter !=null) printWriter.close();
        }


    }

    public void deleteHtml(Long spuId) {
        File file = new File("C:\\Users\\Agape\\IdeaProjects\\leyou\\nginx-1.8.0\\html\\item\\" + spuId + ".html");
        file.deleteOnExit();
    }
}

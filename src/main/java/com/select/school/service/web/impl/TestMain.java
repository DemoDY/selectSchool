package com.select.school.service.web.impl;

import com.select.school.model.entity.teset;

import java.io.File;

public class TestMain {

    public static void main(String[] args) throws Exception {
        //账票对象
        teset ticket = new teset();
        ticket.setDatabirthday("20181221271");
        ticket.setDatahigh("180");
        ticket.setDataphone("1298471324132");
        ticket.setDataweight("78");
        ticket.setDatasix("女");
        //模板对象
        GeneratePdf Ticketpdf = new GeneratePdf();
        //模板源路径
//        Ticketpdf.setTargetPdfpath("E:/test.pdf");
        //设置tarGet的路径，这个是项目中的源路径
//        Ticketpdf.setTargetPdfpath("D:\\a.pdf");
        //set封装账票信息
        Ticketpdf.setTeset(ticket);
        //指定生成的新路径
        File file = new File("E:/op.pdf");
        //创建文件
        file.createNewFile();
        //使生成的文件file生效，这个必须有
        Ticketpdf.templetTicket(file);
    }
}

package com.select.school.service.web.impl;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.*;
import com.select.school.model.entity.teset;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
public class GeneratePdf {

//    private static void fillTemplate(String pdfName, Map<String, Object> data) {
//        //模板路径
//        String file = "E:/test.pdf";
//
//        //生成的新文件路径
//        String newPDFPath = "E:/newTest.pdf";
////        String realPath = ResourceBundle.getBundle("systemconfig").getString("upLoadFolder") + File.separator + "comfirmationDoc";
//        String dateFolder = DateFormatUtils.format(new Date(), "yyyyMMdd");
//        String folderPath = File.separator + dateFolder;
//        //设置文件名
//        String fileName = pdfName + "_" + DateFormatUtils.format(new Date(), "yyyyMMddhhmmss") + ".pdf";
//        String savePath = "E:"+ File.separator + fileName;
//
//        AcroFields s = null;
//        PdfReader reader = null;
//        FileOutputStream out;
//        ByteArrayOutputStream bos = null;
//        PdfStamper stamper = null;
//        try {
////            String file = this.getClass().getClassLoader().getResource("E:/test.pdf").getPath();//模板
//            //设置字体
////            String font = this.getClass().getClassLoader().getResource("YaHei.ttf").getPath();
////            BaseFont bf=BaseFont.createFont("font/simsun.ttc,1",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
//            reader = new PdfReader(file);
//            bos = new ByteArrayOutputStream();
//            stamper = new PdfStamper(reader, bos);
//            s = stamper.getAcroFields();
//            //使用中文字体 使用 AcroFields填充值的不需要在程序中设置字体，在模板文件中设置字体为中文字体 Adobe 宋体 std L
////            BaseFont bfChinese = BaseFont.createFont(font, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            //设置编码格式
////            s.addSubstitutionFont(bfChinese);
//            // 遍历data 给pdf表单表格赋值
//            for (String key : data.keySet()) {
//                if (data.get(key) != null) {
//                    s.setField(key, data.get(key).toString());
//                }
//            }
//            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
//            stamper.setFormFlattening(true);
//            stamper.close();
//            FileOutputStream fos = new FileOutputStream(savePath);
//            fos.write(bos.toByteArray());
//            fos.flush();
//            fos.close();
////            return savePath;

    private String templatePdfPath;//模板路径
    private String ttcPath;//生成的新路径
    private String targetPdfpath;//源路径
    private teset teset;//账票实体
    private static final String FONT = "中黑体.ttf";//引入的字体
    //get与set
    public String getTemplatePdfPath() {
        return templatePdfPath;
    }

    public void setTemplatePdfPath(String templatePdfPath) {
        this.templatePdfPath = templatePdfPath;
    }

    public String getTtcPath() {
        return ttcPath;
    }

    public void setTtcPath(String ttcPath) {
        this.ttcPath = ttcPath;
    }

    public String getTargetPdfpath() {
        return targetPdfpath;
    }

    public void setTargetPdfpath(String targetPdfpath) {
        this.targetPdfpath = targetPdfpath;
    }

    public com.select.school.model.entity.teset getTeset() {
        return teset;
    }

    public void setTeset(com.select.school.model.entity.teset teset) {
        this.teset = teset;
    }

    //无参构造
    public GeneratePdf() {
        super();
    }
    //有参构造
    public GeneratePdf(String templatePdfPath, String ttcPath, String targetPdfpath,teset teset) {
        this.templatePdfPath = templatePdfPath;
        this.ttcPath = ttcPath;
        this.targetPdfpath = targetPdfpath;
        this.teset = teset;
    }
    //获取
    public void templetTicket(File file) throws Exception {
        //创建一个pdf读取对象
        PdfReader reader = new PdfReader("E:/test.pdf");
        //创建一个输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //创建pdf模板，参数reader  bos
        PdfStamper ps = new PdfStamper(reader, bos);
        //封装数据
        AcroFields s = ps.getAcroFields();
        s.setField("dataweight", teset.getDataweight());
        s.setField("databirthday", teset.getDatabirthday());
        s.setField("datahigh", teset.getDatahigh());
        s.setField("dataphone", teset.getDataphone());
        s.setField("datasix", teset.getDatasix());
        ps.setFormFlattening(true);//这里true表示pdf可编辑
        ps.close();//关闭PdfStamper
        FileOutputStream fos = new FileOutputStream(file);//创建文件输出流
        fos.write(bos.toByteArray());//写入数据
        fos.close();//关闭输出流
    }

}


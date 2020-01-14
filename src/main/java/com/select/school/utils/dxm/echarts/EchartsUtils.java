/**
 * projectName: selectSchool
 * fileName: EchartsUtils.java
 * packageName: com.select.school.utils.dxm.echarts
 * date: 2020-01-13 2:49 下午
 * copyright(c) 德慧
 */
package com.select.school.utils.dxm.echarts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * @version: V1.0
 * @author: DXM
 * @className: EchartsUtils
 * @packageName: com.select.school.utils.dxm.echarts
 * @description: Echarts
 * @data: 2020-01-13
 **/
public class EchartsUtils {
//    private static final String JSpath = "E:\\testProgram\\EchartsDemo\\tawa\\src\\main\\web\\js\\echarts-convert.js";
    private static final String JSpath = "/Users/dxm/workSpaces/ideaProjects/selectSchool/src/main/resources/static/js/echarts-convert.js";

    public static void main(String[] args) {

        String options = "{\n" +
                "    \"backgroundColor\": \"#ffffff\",\n" +
                "    \"color\": [\n" +
                "        \"#37A2DA\",\n" +
                "        \"#FF9F7F\"\n" +
                "    ],\n" +
                "    \"tooltip\": {\n" +
                "        \"show\": false\n" +
                "    },\n" +
                "    \"legend\": {\n" +
                "        \"data\": [\n" +
                "            \"个人\",\n" +
                "            \"学校\"\n" +
                "        ],\n" +
                "        \"orient\": \"vertical\",\n" +
                "    },\n" +
                "    \"xAxis\": {\n" +
                "        \"show\": false\n" +
                "    },\n" +
                "    \"yAxis\": {\n" +
                "        \"show\": false\n" +
                "    },\n" +
                "    \"radar\": {\n" +
                "        \"indicator\": [\n" +
                "            {\n" +
                "                \"name\": \"GPA\",\n" +
                "                \"max\": 100\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"IB\",\n" +
                "                \"max\": 100\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"排名\",\n" +
                "                \"max\": 100\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"TOLFL\",\n" +
                "                \"max\": 100\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"SAT\",\n" +
                "                \"max\": 100\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"series\": [\n" +
                "        {\n" +
                "            \"name\": \"\",\n" +
                "            \"type\": \"radar\",\n" +
                "            \"data\": [\n" +
                "                {\n" +
                "                    \"value\": [\n" +
                "                        \"100\",\n" +
                "                        \"76\",\n" +
                "                        \"80\",\n" +
                "                        \"79\",\n" +
                "                        \"45\"\n" +
                "                    ],\n" +
                "                    \"name\": \"个人\",\n" +
                "                    \"itemStyle\": {\n" +
                "                        \"normal\": {\n" +
                "                            \"color\": \"rgba(255,225,0,.9)\",\n" +
                "                            \"lineStyle\": {\n" +
                "                                \"color\": \"rgba(255,225,0,.9)\"\n" +
                "                            }\n" +
                "                        }\n" +
                "                    },\n" +
                "                    \"label\": {\n" +
                "                        \"normal\": {\n" +
                "                            \"show\": false\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"value\": [\n" +
                "                        \"80\",\n" +
                "                        \"41\",\n" +
                "                        \"60\",\n" +
                "                        \"100\",\n" +
                "                        \"45\"\n" +
                "                    ],\n" +
                "                    \"name\": \"学校\",\n" +
                "                    \"itemStyle\": {\n" +
                "                        \"normal\": {\n" +
                "                            \"color\": \"rgba(60,135,213,.9)\",\n" +
                "                            \"lineStyle\": {\n" +
                "                                \"color\": \"rgba(60,135,213,.9)\"\n" +
                "                            }\n" +
                "                        }\n" +
                "                    },\n" +
                "                    \"label\": {\n" +
                "                        \"normal\": {\n" +
                "                            \"show\": false\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"animation\": true,\n" +
                "    \"animationDuration\": 2000\n" +
                "}";
//        String options = "{\"backgroundColor\":\"#ffffff\",\"color\":[\"#37A2DA\",\"#FF9F7F\"],\"tooltip\":{\"show\":false},\"legend\":{\"data\":[\"个人\",\"学校\"],\"orient\":\"vertical\",\"left\":\"center\",\"bottom\":\"bottom\"},\"xAxis\":{\"show\":false},\"yAxis\":{\"show\":false},\"radar\":{\"indicator\":[{\"name\":\"GPA\",\"max\":100},{\"name\":\"IB\",\"max\":100},{\"name\":\"排名\",\"max\":100},{\"name\":\"TOLFL\",\"max\":100},{\"name\":\"SAT\",\"max\":100}]},\"series\":[{\"name\":\"\",\"type\":\"radar\",\"data\":[{\"value\":[\"100\",\"76\",\"80\",\"79\",\"45\"],\"name\":\"个人\",\"itemStyle\":{\"normal\":{\"color\":\"rgba(255,225,0,.9)\",\"lineStyle\":{\"color\":\"rgba(255,225,0,.9)\"}}},\"label\":{\"normal\":{\"show\":false}}},{\"value\":[\"80\",\"41\",\"60\",\"100\",\"45\"],\"name\":\"学校\",\"itemStyle\":{\"normal\":{\"color\":\"rgba(60,135,213,.9)\",\"lineStyle\":{\"color\":\"rgba(60,135,213,.9)\"}}},\"label\":{\"normal\":{\"show\":false}}}]}],\"animation\":true,\"animationDuration\":2000}";
        String picPath = generateEChart(options);
        System.out.println(picPath);
    }

    /*
     * 主程序
     */
    public static  String generateEChart(String options) {
        String dataPath = writeFile(options);
        String fileName= UUID.randomUUID().toString() + ".png";
        String path = "/Users/dxm/Desktop/echart/" +fileName;
        try {
            File file = new File(path);     //文件路径
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            String cmd = "phantomjs " + JSpath + " -infile " + dataPath + " -outfile " + path;//生成命令行
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
            }
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally{

        }
        return path;
    }

    /*
     *
     * options生成文件存储
     */
    public static String writeFile(String options) {
        String dataPath="/Users/dxm/Desktop/data/"+ UUID.randomUUID().toString().substring(0, 8) +".json";
        try {
            /* option写入文本文件 用于执行命令*/
            File writename = new File(dataPath);
            if (!writename.exists()) {
                File dir = new File(writename.getParent());
                dir.mkdirs();
                writename.createNewFile(); //
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(options);
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataPath;
    }
}

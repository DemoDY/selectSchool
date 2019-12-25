package com.select.school.utils;

import com.select.school.utils.file.FileNameLengthLimitExceededException;
import com.select.school.utils.result.V2Config;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

/**
 * 文件上传工具类
 *
 * @author fuce
 * @date: 2018年9月22日 下午10:33:23
 */
public class FileUploadUtils {

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 52428800;

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = V2Config.getProfile();

    /**
     * 默认的文件名最大长度
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 200;

    /**
     * 默认文件类型jpg
     */
    public static final String IMAGE_JPG_EXTENSION = ".jpg";

    private static int counter = 0;

    public static String getDefaultBaseDir( ) {
        return defaultBaseDir;
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException {
        try {
            return upload(getDefaultBaseDir(), file, FileUploadUtils.IMAGE_JPG_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload(baseDir, file, FileUploadUtils.IMAGE_JPG_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir   相对应用的基目录
     * @param file      上传的文件
     * @param
     * @param extension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException                          比如读写文件出错时
     */
    public static final String upload(String baseDir, MultipartFile file, String extension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException {
        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(file.getOriginalFilename(), fileNamelength,
                    FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        assertAllowed(file);
        String fileName = encodingFilename(file.getOriginalFilename(), extension);
        File desc = getAbsoluteFile(baseDir, baseDir + fileName);
        file.transferTo(desc);
        return fileName;
    }

    private static final File getAbsoluteFile(String uploadDir, String filename) throws IOException {
        File desc = new File(File.separator + filename);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    /**
     * 编码文件名
     */
    private static final String encodingFilename(String filename, String extension) {
        filename = filename.replace("_", " ");
        filename = new Md5Hash(filename + System.nanoTime() + counter++).toHex().toString() + extension;
        return filename;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     */
    public static final void assertAllowed(MultipartFile file) throws FileSizeLimitExceededException {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
            throw new FileSizeLimitExceededException("not allowed upload upload", size, DEFAULT_MAX_SIZE);
        }
    }

    /**
     * 列出文件夹及其子文件夹下所有符合条件的文件
     *
     * @param filePath
     * @param fileVec
     * @param fileFilter
     * @return
     */
    public static List<File> listAllFile(String filePath, List<File> fileVec, FileFilter fileFilter) {
        File tempdir = new File(filePath);
        File[] filelist = tempdir.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            File fileP = filelist[i];
            //System.out.println(file);
            if (fileP.isFile() && fileFilter.accept(fileP)) {
                fileVec.add(fileP);
            } else if (fileP.isDirectory()) {
                listAllFile(fileP.getPath(), fileVec, fileFilter);
            }
        }
        //System.out.println("fileDir" + fileVec.toString());
        return fileVec;
    }

    /**
     * @param file   读入文件
     * @param toFile 输出文件
     * @param width  输出控制宽
     * @param height 输出控制长
     * @param suffix 后缀名
     * @throws IOException
     */
    public static void scaleImage(File file, File toFile, int width, int height, String suffix) throws IOException {
        System.setProperty("java.awt.headless", "true");
        if (suffix == null) {
            suffix = "JPEG";
        }
        //读为bufferedImage 以便取得参数
        BufferedImage bi = ImageIO.read(file);
        //计算成为合适参数
        bi.getColorModel();
        double originalW = (double) bi.getWidth();
        double originalH = (double) bi.getHeight();
        double originalRatio = originalW / originalH;
        if (originalRatio > 1) {
            height = (int) (height * originalRatio);
        } else if (originalRatio < 1) {
            width = (int) (width * originalRatio);
        }
        //AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(wRatio, hRatio), null);
        //创建输出图片
        int type = bi.getType();
        BufferedImage out;
        if (type == BufferedImage.TYPE_CUSTOM) { //handmade
            ColorModel cm = bi.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            out = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            out = new BufferedImage(width, height, type);
        }
        //将输入图片按照输出图片格式输出
        out.getGraphics().drawImage(bi.getScaledInstance(width, height, out.getType()), 0, 0, null);
        //将输入图片按照输出图片格式输出
        ImageIO.write(out, suffix, toFile);

    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 根据文件路径复制到相同目录，返回新文件路径
     *
     * @return String, String oldDir, String newDir
     */
    public static String copyFileFromPath(String oldPath, String newPath) throws IOException {
        //	String newPath = "copy_" + oldPath;
        File newFile = new File(newPath);
        boolean c = newFile.createNewFile();
        System.out.println(c);
        File oldFile = new File(oldPath);
        copyFile(oldFile, newFile);
        return newPath;
    }

    /**
     * 复制文件
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File source, File dest) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest);
        FileInputStream fis = new FileInputStream(source);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
        fis.close();
    }

    /**
     * 根据原始文件名,返回一个服务器保持的文件路径
     * 命名规则为 yyyy/mm/dd
     *
     * @return
     */
    public static String dateStylePath() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);    //获取年
        int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        return String.valueOf(year) + File.separator + String.valueOf(month) + File.separator + String.valueOf(day);
    }

    /**
     * 创建任意深度的文件所在文件夹,可以用来替代直接new File(path)。
     *
     * @param path 文件(不是文件夹)
     * @return File对象 (文件夹,不是文件)
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public static File createFileDir(String path) {
        File file = new File(path);
        //寻找父目录是否存在
        File parent = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)));
        //如果父目录不存在，则递归寻找更上一层目录
        if (!parent.exists()) {
            createFileDir(parent.getPath());
            //创建父目录
            parent.mkdirs();
        }
        return file;
    }

    /**
     * 清空文件内容
     *
     * @param fileName
     */
    public static void clearFileContent(String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加文件写入，设置编码为UTF-8.
     */
    public static void writeByAppendOfUTF8(String path, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path, true), "UTF-8"));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 去除字符串中的所有数字类、字符类、特殊符号类
     *
     * @param content
     * @return
     */
    public static String removeSymbol(String content) {
        StringBuffer term = new StringBuffer();
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122) || (chars[i] >= 65 && chars[i] <= 90)) {
                term.append(chars[i]);
            }
        }
        return term.toString();
    }

    /**
     * B方法追加文件：使用FileWriter
     */
    public static void writeByAppend(String path, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(path, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAgentFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String new_filename = fileName;
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        String rtn = fileName;
        userAgent = userAgent.toLowerCase();
        // IE浏览器，只能采用URLEncoder编码
        if (userAgent.contains("trident")) {
            new_filename = URLEncoder.encode(fileName, "UTF8");
            new_filename = StringUtils.replace(new_filename, "+", "%20");//替换空格
            rtn = "filename=\"" + new_filename + "\"";
        }
        // Opera浏览器只能采用filename*
        else if (userAgent.contains("opera")) {
            new_filename = URLEncoder.encode(fileName, "UTF8");
            rtn = "filename*=UTF-8''" + new_filename;
        }
        // Safari浏览器，只能采用ISO编码的中文输出
        else if (userAgent.contains("safari")) {
            rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
        }
        // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
        else if (userAgent.contains("applewebkit")) {
            new_filename = new String(new_filename.getBytes("utf8"), "iso8859-1");
            rtn = "filename=\"" + new_filename + "\"";
        }
        // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
        else if (userAgent.contains("mozilla")) {
            new_filename = new String(new_filename.getBytes("utf8"), "iso8859-1");
            rtn = "filename*=UTF-8''" + new_filename;
        }
        return rtn;
    }
}

import com.hankcs.hanlp.HanLP;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by zyf on 2017/8/8.
 */
public class ChangeSimpleToTraditional {


    public static void changeFileFromSimpleChineseToTradionalWithRootPath(String path) {
        ArrayList<String> tempArray = new ArrayList<String>();
        ArrayList<String> fileList = traverseFolder2(path, tempArray);
        System.out.println("文件数组" + fileList);

        if (fileList.size() == 0) {
            return;
        }
        ;
        for (int i = 0; i < fileList.size(); i++) {
            readOldFileAndWriteNewFileWithFilePath(fileList.get(i));
        }
    }


    public static void readOldFileAndWriteNewFileWithFilePath(String filePath) {
        // 简体转繁体 
        try {
//            File file = new File(filePath);
//            String outputDirPath = file.getParentFile().getParentFile().getAbsolutePath() + File.separator + "values-zh-rTW";
//            File tw_dir = new File(outputDirPath);
//            tw_dir.mkdir();
//            String outputFilePath = tw_dir.getAbsolutePath() + File.separator+"strings.xml";
            BufferedReader bufRead = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
            StringBuffer strBuffer = new StringBuffer();

            for (String temp = null; (temp = bufRead.readLine()) != null; temp = null) {
//                Pattern pattern = Pattern.compile("[\u4e00-\u9fcc]+");
//                if (pattern.matcher(temp).find()) {
//                    temp = getChinese(temp);
//                }
                System.out.println("pre temp is " + temp);
                temp = HanLP.convertToTraditionalChinese(temp);
                System.out.println("after temp is " + temp);

                strBuffer.append(temp);
                strBuffer.append(System.getProperty("line.separator"));
            }
            System.out.println(strBuffer.toString());
            bufRead.close();
            PrintWriter printWriter = new PrintWriter(filePath);
            printWriter.write(strBuffer.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 迭代遍历传入的根文件夹，获取每一级文件夹的每个文件
     * 并把文件名称以字符串形式装在数组返回
     * path：根文件夹路径
     * listFileName：用于返回文件路径的数组，由于这个是迭代方法采用外部传入该数组
     */
    public static ArrayList<String> traverseFolder2(String path, ArrayList<String> listFileName) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return null;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder2(file2.getAbsolutePath(), listFileName);
                    } else {
                        String sbsolutePath = file2.getAbsolutePath();
                        if (sbsolutePath.endsWith("strings.xml")&&!file2.getParentFile().getAbsolutePath().endsWith("zh-rTW") || sbsolutePath.equals("common_controls.json") || sbsolutePath.equals("hot_commands.json")) {
                            listFileName.add(file2.getAbsolutePath());
                            System.out.println("文件:" + file2.getAbsolutePath());
                        }

                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }

        return listFileName;
    }
}

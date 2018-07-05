package pers.hhelibep.Bestful.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class FileHelper {

    public static File findFileInFolder(File folder, String fileName) {
        if (StringUtils.isEmpty(fileName.trim()) || null == folder || !folder.exists()) {
            return null;
        }
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileName)) {
                return file;
            } else if (file.isDirectory()) {
                File file2 = findFileInFolder(file, fileName);
                if (null != file2) {
                    return file2;
                }
            }
        }
        return null;
    }

    public static File findFileInCurrentProject(String fileName) {
        return findFileInFolder(new File(System.getProperty("user.dir")).getAbsoluteFile(), fileName);
    }
}

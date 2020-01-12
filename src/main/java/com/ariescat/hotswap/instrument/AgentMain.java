package com.ariescat.hotswap.instrument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

/**
 * @author Ariescat
 * @version 2020/1/10 9:57
 */
public class AgentMain {

    public static void agentmain(String args, Instrumentation inst) {
        premain(args, inst);
    }

    public static void premain(String args, Instrumentation inst) {
        System.out.println("Hotswap agent start...  [args is " + args);

        test(inst);

        System.out.println("Hotswap agent end.");
    }

    private static void test(Instrumentation inst) {
        String pathname = "D:\\GitHubProjects\\ariescat-hotswap-core\\target\\classes\\com\\ariescat\\hotswap\\example\\RedefineBean.class";
        File file = new File(pathname);
        if (!file.exists()) {
            System.err.println("file no exists. [" + file.getAbsolutePath());
            return;
        }
        try {
            byte[] bytes = fileToBytes(file);
            System.out.println("文件大小：" + bytes.length);
            Class<?> clazz = Class.forName("com.ariescat.hotswap.example.RedefineBean");
            ClassDefinition classDefinition = new ClassDefinition(clazz, bytes);
            System.out.println("转换代码 -> " + file.getName());
            inst.redefineClasses(classDefinition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] fileToBytes(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        in.close();
        return bytes;
    }
}

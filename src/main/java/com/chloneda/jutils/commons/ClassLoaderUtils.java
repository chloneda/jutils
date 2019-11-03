package com.chloneda.jutils.commons;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chloneda
 * @description:
 */
public class ClassLoaderUtils extends ClassLoader {

    public ClassLoaderUtils() {
        super();
    }

    public ClassLoaderUtils(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    /**
     * 创建新类加载器。使用方法 getSystemClassLoader() 返回的 ClassLoader 创建一个新的类加载器，将该加载器作为父类加载器。
     *
     * @return
     */
    public static ClassLoader createNewClassLoader() {
        return new ClassLoaderUtils(getSystemClassLoader());
    }

    /**
     * 创建新类加载器。使用指定的、用于委托操作的父类加载器创建新的类加载器。
     *
     * @param parentClassLoader 父类加载器
     * @return
     */
    public static ClassLoader createNewClassLoader(ClassLoader parentClassLoader) {
        if (parentClassLoader == null) {
            throw new IllegalArgumentException("parentClassLoader must not be null");
        }
        return new ClassLoaderUtils(parentClassLoader);
    }

    /**
     * 获取默认类加载器
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();

        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader -> falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassLoaderUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * 用自定义类加载器复写线程上下文类加载器
     *
     * @param classLoader 用户自定义类加载器
     * @return
     */
    public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader must not be null");
        }
        Thread currentThread = Thread.currentThread();
        ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
        if (!classLoader.equals(threadContextClassLoader)) {
            currentThread.setContextClassLoader(classLoader);
            return threadContextClassLoader;
        } else {
            return null;
        }
    }

    /**
     * 返回委托的系统类加载器。该加载器是新的 ClassLoader 实例的默认委托父类加载器，通常是用来启动应用程序的类加载器。
     * 该类是使用默认系统类加载器进行加载的
     *
     * @return 委托的系统 ClassLoader，如果没有这样的类加载器，则返回 null
     */
    public static ClassLoader getSystemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    /**
     * 返回委托的父类加载器。如果类加载器的父类加载器就是引导类加载器，则此方法将在这样的实现中返回 null。
     *
     * @param classLoader
     * @return
     */
    public static ClassLoader getParentClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader must not be null");
        }
        return classLoader.getParent();
    }

    /**
     * 获取类加载器的所有父加载器
     *
     * @param classLoader
     * @return
     */
    public static List<ClassLoader> getAllParentClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader must not be null");
        }
        List<ClassLoader> list = new ArrayList<ClassLoader>();
        ClassLoader parent = getParentClassLoader(classLoader);
        if (parent != null) {
            list.add(parent);
            parent = parent.getParent();
        }
        return list;
    }

    /**
     * 获取类加载器的所有父加载器的类名
     *
     * @param classLoader
     * @return
     */
    public static List<String> getAllParentClassLoaderName(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader must not be null");
        }
        List<String> list = new ArrayList<String>();
        ClassLoader parent = getParentClassLoader(classLoader);
        if (parent != null) {
            list.add(parent.getClass().getName());
            parent = parent.getParent();
        }
        return list;
    }

    /**
     * 是否该加载器存在指定的包
     *
     * @param packageName 全限定包名
     * @return
     */
    public boolean isPackageInClassLoader(String packageName) {
        return getPackageByPackageName(packageName) != null;
    }

    /**
     * 根据包名获取包, 返回由此类加载器或其任何祖先所定义的 Package
     *
     * @param packageName 全限定包名
     * @return
     */
    public Package getPackageByPackageName(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("package name must not be null");
        }
        return super.getPackage(packageName);
    }

    /**
     * 返回此类加载器及其祖先所定义的所有 Package
     *
     * @return
     */
    public Package[] getAllPackageInClasssLoader() {
        return super.getPackages();
    }

    /**
     * 返回此类加载器及其祖先所定义的所有全限定包名
     *
     * @return
     */
    public List<String> getAllPackageNameInClassLoader() {
        List<String> packageList = new ArrayList<String>();
        Package[] packages = this.getAllPackageInClasssLoader();
        if (packages != null && packages.length > 0) {
            for (Package p : packages) {
                packageList.add(p.getName());
            }
        }
        return packageList;
    }

    /**
     * 使用指定的二进制名称查找类
     * 此方法应该被类加载器的实现重写，该实现按照委托模型来加载类。在通过父类加载器检查所请求的类后，此方法将被 loadClass 方法调用。
     * 任何作为 String 类型参数传递给 ClassLoader 中方法的类名称都必须是一个二进制名称。
     *
     * @param binaryName 类的二进制名称
     *                   example："java.lang.String","javax.swing.JSpinner$DefaultEditor","java.security.KeyStore$Builder$FileBuilder$1"
     * @return
     */
    public Class<?> findClassByBinaryName(String binaryName) {
        if (binaryName == null) {
            throw new IllegalArgumentException("Binary Class name must not be null");
        }
        try {
            return super.findClass(binaryName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找名称为 name 的已经被加载过的类
     * 如果 Java 虚拟机已将此加载器记录为具有给定二进制名称的某个类的启动加载器，则返回该二进制名称的类。否则，返回 null。
     *
     * @param binaryName 类的二进制名称
     * @return
     */
    public Class<?> findLoadedClassByBinaryName(String binaryName) {
        if (binaryName == null) {
            throw new IllegalArgumentException("Binary Class name must not be null");
        }
        return super.findLoadedClass(binaryName);
    }

    /**
     * 是否是被加载过的类
     *
     * @param binaryName 类的二进制名称
     * @return
     */
    public boolean isLoadedClass(String binaryName) {
        return findLoadedClassByBinaryName(binaryName) != null;
    }

    /**
     * 把字节数组 b 中的内容转换成 Java 类, 这个方法被声明为 final 的
     *
     * @param binaryName 所需要的类的二进制名称，如果不知道此名称，则该参数为 null
     * @param b          组成类数据的字节
     * @param off        类数据的 b 中的起始偏移
     * @param len        类数据的长度
     * @return
     */
    public Class<?> defineClassFromBytes(String binaryName, byte[] b, int off, int len) {
        if (binaryName == null) {
            throw new IllegalArgumentException("Binary Class name must not be null");
        }
        return super.defineClass(binaryName, b, off, len);
    }

    /**
     * 使用指定的二进制名称来加载类
     * 此方法的默认实现将按以下顺序搜索类
     * 1.首先调用 findLoadedClass(String) 来检查是否已经加载类。
     * 2.接着在父类加载器上调用 loadClass 方法。如果父类加载器为 null，则使用虚拟机的内置类加载器。
     * 3.调用 findClass(String) 方法查找类。
     * 4.如果使用上述步骤找到类，并且 resolve 标志为真，则此方法将在得到的 Class 对象上调用 resolveClass(Class) 方法。
     *
     * @param binaryName
     * @param resolve
     * @return
     */
    public Class<?> loadClassByBinaryName(String binaryName, boolean resolve) {
        if (binaryName == null) {
            throw new IllegalArgumentException("Binary Class name must not be null");
        }
        try {
            if (resolve) {
                return super.loadClass(binaryName, true);
            } else {
                return super.loadClass(binaryName);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用指定的二进制名称来加载类, 未调用 resolveClass(Class) 方法进行链接
     *
     * @param binaryName
     * @return
     */
    public Class<?> loadClassByBinaryName(String binaryName) {
        return loadClassByBinaryName(binaryName, false);
    }

    /**
     * 链接指定的类。
     * 类加载器可以使用此方法（其名称容易使人误解）来链接类。
     *
     * @param clazz 要链接的类
     */
    public void resolveClazz(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        super.resolveClass(clazz);
    }

    /**
     * 为指定包设置默认断言状态。包的默认断言状态确定了以后初始化属于指定包或其子包的类的断言状态。
     * 包的优先级默认情况下高于类加载器的默认断言状态，并且可以通过调用 setClassAssertionStatus(String, boolean) 在每个类的基础上进行重写。
     *
     * @param classLoader
     * @param packageName 要设置其默认包断言状态的包名。null 值指示未命名的包为“当前”状态
     * @param enabled     如果由此类加载器加载并属于指定包或其子包的类在默认情况下启用断言，则该参数为 true；如果在默认情况下禁用断言，则该参数为 false。
     */
    public static void setPackageAssertionStatus(ClassLoader classLoader, String packageName, boolean enabled) {
        if (classLoader == null) {
            classLoader = getDefaultClassLoader();
        }
        classLoader.setPackageAssertionStatus(packageName, enabled);
    }

    /**
     * 设置此类加载器的默认断言状态。此设置确定由此类加载器加载并在将来初始化的类在默认情况下是启用还是禁用断言。
     * 通过调用 setPackageAssertionStatus(String, boolean) 或 setClassAssertionStatus(String, boolean)，在每个包或每个类上重写此设置。
     *
     * @param classLoader 此类加载器
     * @param enabled     如果由此类加载器加载的类将默认为启用断言，则该参数为 true；如果默认为禁用断言，则该参数为 false。
     */
    public static void setClassLoaderAssertionStatus(ClassLoader classLoader, boolean enabled) {
        if (classLoader == null) {
            classLoader = getDefaultClassLoader();
        }
        classLoader.setDefaultAssertionStatus(enabled);
    }

    /**
     * 设置在此类加载器及其包含的嵌套类中指定的最高层类所需的断言状态。
     * 该设置的优先级高于类加载器的默认断言状态以及可用的任何包的默认值。如果已经初始化指定类，则此方法无效。（初始化类后，其断言状态无法再改变。）
     * 如果指定类不是最高层的类，则此调用对任何类的实际断言都无效。
     *
     * @param classLoader 此类加载器
     * @param className   全限定类名
     * @param enabled     如果指定类在初始化时启用断言，则该参数为true；如果该类禁用断言，则该参数为false。
     */
    public static void setClassAssertionStatus(ClassLoader classLoader, String className, boolean enabled) {
        if (classLoader == null) {
            classLoader = getDefaultClassLoader();
        }
        classLoader.setClassAssertionStatus(className, enabled);
    }

    /**
     * 将此类加载器的默认断言状态设置为 false，并放弃与此类加载器关联的所有默认包或类断言状态设置。
     * 提供此方法可以让类加载器忽略任何命令行或持久断言状态设置，并且“无不良记录”。
     *
     * @param classLoader
     */
    public static void clearAssertionStatus(ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = getDefaultClassLoader();
        }
        classLoader.clearAssertionStatus();
    }

    /**
     * 获取文件系统类
     *
     * @param rootDir
     * @param className
     * @return
     */
    public Class<?> getFileSystemClass(String rootDir, String className) {
        if (rootDir == null || "".equals(rootDir.trim())) {
            throw new IllegalArgumentException("rootDir must not be null or empty");
        }
        if (className == null || "".equals(rootDir.trim())) {
            throw new IllegalArgumentException("className must not be null or empty");
        }
        Class<?> clazz = null;
        try {
            clazz = new FileSystemClassLoader(rootDir).findClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * 文件系统类加载器
     */
    public class FileSystemClassLoader extends ClassLoader {

        //完整.class文件的绝对路径
        private String rootDir;

        public FileSystemClassLoader(String rootDir) {
            this.rootDir = rootDir;
        }

        @Override
        protected Class<?> findClass(String className) throws ClassNotFoundException {
            byte[] classData = getClassData(className);
            if (classData == null) {
                throw new ClassNotFoundException();
            } else {
                return defineClass(className, classData, 0, classData.length);
            }
        }


        private byte[] getClassData(String className) {
            String path = classNameToPath(className);
            try {
                InputStream ins = new FileInputStream(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];
                int bytesNumRead = 0;
                while ((bytesNumRead = ins.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesNumRead);
                }
                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String classNameToPath(String className) {
            return rootDir + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
        }
    }

}

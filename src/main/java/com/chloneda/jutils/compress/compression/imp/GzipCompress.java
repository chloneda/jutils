package com.chloneda.jutils.compress.compression.imp;

import com.chloneda.jutils.compress.compression.Compression;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompress implements Compression {

    @Override
    public void uncompression(File file,boolean delete,String outName) {
        FileInputStream fileInputStream = null;
        GZIPInputStream in = null;
        FileOutputStream out = null;
        try {
            if (!file.getName().contains(".gz")) {
                System.err.println("File name must have extension of \".gz\"");
                return ;
            }
            fileInputStream = new FileInputStream(file);
            in = new GZIPInputStream(fileInputStream);
            out = new FileOutputStream(outName);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            if(delete)
                file.delete();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            close(fileInputStream);
            close(in);
            close(out);
        }
    }
    public static File pack(File[] sources, File target){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        TarArchiveOutputStream os = new TarArchiveOutputStream(out);
        for (File file : sources) {
            try {
                os.putArchiveEntry(new TarArchiveEntry(file));
                IOUtils.copy(new FileInputStream(file), os);
                os.closeArchiveEntry();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(os != null) {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return target;
    }
    @Override
    public void compression(File[] files,boolean delete,String inFileName) {
        pack(files,new File(inFileName+".tar"));
        try {
            System.out.println("Creating the GZIP output stream.");
            GZIPOutputStream out = null;
            try {
                out = new GZIPOutputStream(new FileOutputStream(inFileName));
            } catch(FileNotFoundException e) {
                System.err.println("Could not create file: " + inFileName);
                return;
            }
            System.out.println("Opening the input file.");
            FileInputStream in = null;
            try {
                in = new FileInputStream(inFileName);
            } catch (FileNotFoundException e) {
                System.err.println("File not found. " + inFileName);
                System.exit(1);
            }
            System.out.println("Transfering bytes from input file to GZIP Format.");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            close(in);
            System.out.println("Completing the GZIP file!");
            out.finish();
            close(out);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void close(AutoCloseable closeable){
        if(closeable!=null){
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

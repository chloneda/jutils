package com.chloneda.jutils.compress.compression;

import java.io.File;

public interface Compression {

     void uncompression(File file, boolean delete, String inFileName);

     void compression(File[] file, boolean delete, String inFileName);

     void close(AutoCloseable closeable);

}

package com.chloneda.jutils.compress.compression;


import com.chloneda.jutils.compress.compression.imp.GzipCompress;
import com.chloneda.jutils.compress.compression.imp.ZipCompress;

public abstract class CompressionFactory {
    public static Compression getCompression(CompressionType type){
        Compression compression = null;
        if (type==CompressionType.GZ)
            compression = new GzipCompress();
        else if (type==CompressionType.ZIP)
            compression = new ZipCompress();
        return compression;
    }
}

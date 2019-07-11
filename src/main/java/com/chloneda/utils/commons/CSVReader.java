package com.chloneda.utils.commons;

//import org.apache.commons.csv.CSVRecord;

import com.csvreader.CsvReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by chloneda
 * Description:
 */
public class CSVReader {
    private Reader reader;
    private String fileName;
    private Charset charset;
    private char separatorChar;

    public CSVReader(String path) throws FileNotFoundException {
        this(path, ',');
    }

    public CSVReader(String path,char separatorChar) throws FileNotFoundException {
        this(path, separatorChar, Charset.forName("ISO-8859-1"));
    }

    public CSVReader(String path,char separatorChar,Charset charset) throws FileNotFoundException {
        if (path == null) {
            throw new IllegalArgumentException("Parameter fileName can not be null.");
        } else if (charset == null) {
            throw new IllegalArgumentException("Parameter charset can not be null.");
        } else if (!(new File(path)).exists()) {
            throw new FileNotFoundException("File " + path + " does not exist.");
        } else {
            this.fileName = path;
            this.separatorChar = separatorChar;
            this.charset = charset;
        }
    }

    public CSVReader(Reader reader) {
        this(reader, ',');
    }

    public CSVReader(Reader reader, char separatorChar) {
        this.reader = null;
        this.fileName = null;
        this.charset = null;

        if (reader == null) {
            Objects.requireNonNull(reader,"Parameter inputStream can not be null.");
        } else {
            this.reader = reader;
        }
    }

    public CSVReader(InputStream inputStream, char separatorChar, Charset var3) {
        this((Reader)(new InputStreamReader(inputStream, var3)), separatorChar);
    }

    public CSVReader(InputStream inputStream, Charset separatorChar) {
        this((Reader)(new InputStreamReader(inputStream, separatorChar)));
    }


    public void readHeader(){

    }

    public String[] getHeader(){
        return new String[]{};
    }

    public void setHeader(String[] header){

    }

    public static void main(String[] args) throws IOException {
        //CSVRecord s=new CSVRecord();
        List<String[]> csvList=new ArrayList<String[]>();
        CsvReader reader = new CsvReader( "/Workspaces/IntelliJIDEA/IDEAMyProj/Github/utils/src/main/resources/csv/data.csv",',', Charset.forName("UTF-8"));
        //reader.readHeaders(); //跳过表头,不跳可以注释掉
        while(reader.readRecord()){
            csvList.add(reader.getValues()); //按行读取，并把每一行的数据添加到list集合
        }
        reader.close();
        System.out.println("读取的行数："+csvList.size());

    }
}

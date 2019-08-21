package com.chloneda.jutils.java.io;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by chloneda
 * Description:
 */
public class LoadData {

    public List<String[]> loadData(String path) throws Exception {
        Objects.requireNonNull(path);
        List<String[]> csvData=new ArrayList<String[]>();

        File file=new File(Paths.get(path).toString());
        if(file.exists()&&file.isFile()){
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader=null;
            try {
                inputStream = new FileInputStream(file);
                inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                reader = new BufferedReader(inputStreamReader);
                String str = null;
                while((str = reader.readLine())!=null ) {
                    String[] csvVal=str.split(",");
                    csvData.add(csvVal);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(null!=inputStream){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(null!=inputStreamReader){
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(null!=reader){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            throw new Exception("L");
        }


        return csvData;
    }

}

package org.superbiz.moviefun;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvUtils {

    public static String readFile(String path) throws IOException {

        InputStream inputStream = null;
        try {
            //System.out.print("--- PATH :"+path);
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            //Scanner scanner = new Scanner(new File(path)).useDelimiter("\\A");
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");

            if (scanner.hasNext()) {
                String scnrString = scanner.next();
                //System.out.print("---------------LINE  :"+scnrString);
                return scnrString;
            } else {
                return "";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            if(inputStream!= null) {
                inputStream.close();
            }
        }
    }

    public static <T> List<T> readFromCsv(ObjectReader objectReader, String path) {
        try {
            List<T> results = new ArrayList<>();

            MappingIterator<T> iterator = objectReader.readValues(readFile(path));

            while (iterator.hasNext()) {
                results.add(iterator.nextValue());
            }

            return results;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

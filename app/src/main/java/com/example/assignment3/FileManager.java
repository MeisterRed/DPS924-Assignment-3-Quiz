package com.example.assignment3;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileManager {
    String fileName = "results.txt";
    FileOutputStream fos;
    FileInputStream fis;

    public void saveResults(Context context, int correctAns, int totalOfQues) {
        String[] temp = openResults(context);
        if (!temp[0].equals("null")) {
            correctAns += Integer.parseInt(temp[0]);
            totalOfQues += Integer.parseInt(temp[1]);
        }
        String results = correctAns + " " + totalOfQues;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(results.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] openResults(Context context) {
        String[] results = new String[2];
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                stringBuilder.append(line);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                results = stringBuilder.toString().split(" ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void deleteResults(Context context) {
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(("").getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

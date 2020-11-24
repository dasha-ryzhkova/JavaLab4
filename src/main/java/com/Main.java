package com;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)throws IOException
    {
        ArrayList<ArrayList<String>> filesLinesArray = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter catalog path:");

        String folderPath = "C:\\Users\\VivoBook\\Desktop\\4";
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        System.out.println("Enter delimiter:");
        //String del1 = scanner.next();
        String del1 = ",";

        System.out.println("Enter new delimiter:");
        //String newdel1 = scanner.next();
        String newdel1 = "+";

        System.out.println("Enter path for new file:");
        //String newpath = scanner.next();
        String newpath = "C:\\Users\\VivoBook\\Desktop\\result.txt";
        char del = del1.charAt(0);
        char newdel = newdel1.charAt(0);

        System.out.println("Enter count of thread:");
        //Integer numb = scanner.nextInt();;
        Integer numb = 7;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                CSVParser parser = new CSVParser(file.getPath(), del, '"', newdel, numb);
                filesLinesArray.addAll(parser.readCSVFile());
            }
        }
        CSVParser parser = new CSVParser();
        parser.writeToFile(newpath, filesLinesArray, newdel);

        scanner.close();
    }
}

// C:\Users\VivoBook\Desktop\lab1.csv
// C:\Users\VivoBook\Desktop\result.txt
package com;

import org.junit.Test;
import org.junit.BeforeClass;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;


public class CSVParserTest {

    @Test
    public void testCSVParserConstruct1() {
        CSVParser parser = new CSVParser(',', '"');
        assertEquals(',', parser.delimiter);
    }

    @Test
    public void testCSVParserConstruct2() {
        CSVParser parser = new CSVParser("path", ',', '"', '+', 4);
        assertEquals("path", parser.inputFile);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteToFile1() {
        Scanner scanner = new Scanner(System.in);

        String path = new String("C:\\Users\\VivoBook\\Desktop\\lab1.csv");
        File f = new File(path);
        if (f.exists()) {
            char del = ',';
            char newdel = '+';
            String newpath = new String("C:\\Users\\VivoBook\\Desktop\\result1.txt");
            CSVParser parser = new CSVParser(newpath, del, '"', newdel, 4);
            ArrayList<ArrayList<String>> result = null;
            parser.writeToFile(newpath, result, newdel);
        }
        scanner.close();
    }

    @Test(expected = NullPointerException.class)
    public void testWriteToFile2() {
        Scanner scanner = new Scanner(System.in);

        String path = new String("C:\\Users\\VivoBook\\Desktop\\lab1.csv");
        File f = new File(path);

        char del = ',';
        char newdel = '+';
        String newpath = null;
        CSVParser parser = new CSVParser(path, del, '"', newdel, 4);
        ArrayList<ArrayList<String>> result = null;
        try {
            result = parser.readCSVFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parser.writeToFile(newpath, result, newdel);

        scanner.close();

    }

    @Test
    public void testWriteToFile3() {
        Scanner scanner = new Scanner(System.in);

        String path = new String("C:\\Users\\VivoBook\\Desktop\\lab1.csv");
        File f = new File(path);

        char del = ',';
        char newdel = '+';
        String newpath = new String("C:\\Users\\VivoBook\\Desktop\\result1.txt");
        CSVParser parser = new CSVParser(path, del, '"', newdel, 4);
        ArrayList<ArrayList<String>> result = null;
        try {
            result = parser.readCSVFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parser.writeToFile(newpath, result, newdel);
        scanner.close();
        String temp = null;
        try(BufferedReader br = new BufferedReader(new FileReader(newpath))){
            temp = br.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        assertEquals("3+4+6", temp);
    }


    @Test
    public void testReadCSVFile1() {
        Scanner scanner = new Scanner(System.in);

        String path = new String("C:\\Users\\VivoBook\\Desktop\\lab1.csv");
        File f = new File(path);
        if (f.exists()) {
            char del = ',';
            char newdel = '+';
            String newpath = new String("C:\\Users\\VivoBook\\Desktop\\result.txt");
            CSVParser parser = new CSVParser(newpath, del, '"', newdel, 4);
            ArrayList<ArrayList<String>> result = null;
            try {
                result = parser.readCSVFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertEquals("3+4+6", result.get(0).get(0));
        }
        scanner.close();
    }

    @Test(expected = NullPointerException.class)
    public void testReadCSVFile2() {
        Scanner scanner = new Scanner(System.in);

        String path = null;
        File f = new File(path);

        char del = ',';
        char newdel = '+';
        String newpath = new String("C:\\Users\\VivoBook\\Desktop\\result.txt");
        CSVParser parser = new CSVParser(newpath, del, '"', newdel, 4);
        try {
            parser.readCSVFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testParseCSV() {
        ArrayList<ArrayList<String>> linesArr = new ArrayList<>();
        ArrayList<String> wordsOfLine = new ArrayList<>();
        CSVParser pars = new CSVParser(',', '"');
        pars.outputFile = "result1.txt";
        ArrayList<String> lines = new ArrayList<>();
        lines.add("abc,1234,q2!cvb");
        pars.parseCSV(lines);
        assertEquals("abc", pars.linesArr.get(0).get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testExtractWordFromLineNull() {
        CSVParser pars = new CSVParser(',', '"');
        pars.wordsOfLine = null;
        assertEquals("\"\" \"\",\"\"Australia\"\"\"", pars.extractWordFromLine("\"12,\"\"AU\"\",\"\" \"\",\"\"Australia\"\"\""));
    }

    @Test
    public void testExtractWordFromLine() {
        CSVParser pars = new CSVParser(',', '"');
        pars.wordsOfLine = new ArrayList<>();
        assertEquals("\"\" \"\",\"\"Australia\"\"\"", pars.extractWordFromLine("\"12,\"\"AU\"\",\"\" \"\",\"\"Australia\"\"\""));


        assertEquals("1234,q!bv5", pars.extractWordFromLine("abc,1234,q!bv5"));
        assertEquals("q!bv5", pars.extractWordFromLine("1234,q!bv5"));
    }

    @Test
    public void testExtractWordFromLineNoQuotes() {
        CSVParser pars = new CSVParser(',', '"');
        pars.wordsOfLine = new ArrayList<>();
        assertEquals("1234,q!bv5", pars.extractWordFromLine("abc,1234,q!bv5"));

    }

    @Test(expected = NullPointerException.class)
    public void testDeleteQuotesOfLineNull() {
        CSVParser pars = new CSVParser(',', '"');
        String str = null;
        assertEquals("\"Australia\"\"", pars.deleteQuotesOfLine(str));
    }

    @Test
    public void testDeleteQuotesOfLine() {
        CSVParser pars = new CSVParser(',', '"');
        assertEquals("\"Australia\"\"", pars.deleteQuotesOfLine("\"\"Australia\"\"\""));
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteDoubleQuotesInWordNull() {
        CSVParser pars = new CSVParser(',', '"');
        String str = null;
        assertEquals("Aus\"\"tralia", pars.deleteDoubleQuotesInWord(str));
    }

    @Test
    public void testDeleteDoubleQuotesInWord() {
        CSVParser pars = new CSVParser(',', '"');
        assertEquals("Aus\"\"tralia", pars.deleteDoubleQuotesInWord("Aus\"\"\"\"tralia"));


    }
}
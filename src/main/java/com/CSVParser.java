package com;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVParser{
    String inputFile;
    String outputFile;
    char delimiter;
    char combiner;
    char newdel;
    Integer numb;
    ArrayList<ArrayList<String>> linesArr = new ArrayList<>();
    ArrayList<String> wordsOfLine = new ArrayList<>();
    ArrayList<String>  lines;


    public CSVParser(String inputFile, char delimiter, char combiner, char newdel, Integer numb) {
        this.inputFile = inputFile;
        this.delimiter = delimiter;
        this.combiner = combiner;
        this.newdel = newdel;
        this.numb = numb;
    }

    public CSVParser(char delimiter, char combiner){
        this.delimiter = delimiter;
        this.combiner = combiner;
    }

    public CSVParser() {

    }


    public ArrayList<ArrayList<String>> readCSVFile() throws IOException{
        lines = new ArrayList<>();
        Path path = Paths.get(inputFile);
        long lineCount = Files.lines(path).count();
        int size = (int)(lineCount / numb + 1);
        int distance = 0;

        FileInputStream str = new FileInputStream(inputFile);
        FileChannel chnl = str.getChannel();

        for(int i = 0; i < numb; i++){
            if(lineCount < i*size + size){
                distance =(int)(lineCount - i*size);
            }else{
                distance = size;
            }
            OpenFile openFile = new OpenFile(i * size, distance, chnl);
            Thread thread = new Thread(openFile);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        str.close();
        parseCSV(lines);
        return linesArr;
    }



    public void parseCSV(ArrayList<String> lines) {
        for (String s : lines) {
            String cleanedline = deleteQuotesOfLine(s);
            wordsOfLine = new ArrayList<>();
            while (cleanedline.length() > 0) {
                cleanedline = extractWordFromLine(cleanedline);
            }
            linesArr.add(wordsOfLine);
        }
    }

    public void writeToFile(String outputFile, ArrayList<ArrayList<String>> filesLinesArray, char newdelimitr) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));) {
            for (ArrayList<String> l : filesLinesArray) {
                for (int i = 0; i < l.size(); i++) {
                    if (i < l.size() - 1) {
                        writer.write(l.get(i).length() + "" + newdelimitr);
                    } else {
                        writer.write(l.get(i).length() + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String extractWordFromLine(String line) {
        int i = 0;
        String word = "";
        if(line.charAt(i) == '\uFEFF'){
            i++;
        }
        if(line.charAt(0) == combiner) {
            do{
                word += line.charAt(i);
                i++;
            }while (i != line.length() && line.charAt(i) != delimiter || line.charAt(i - 1) != combiner);
            word = deleteQuotesOfLine(word);
            word = deleteQuotesOfLine(word);
            word = deleteDoubleQuotesInWord(word);
            wordsOfLine.add(word);
            while (i < line.length() - 1 && line.charAt(i) == delimiter) {
                i++;
            }
            if (i < line.length()) {
                line = line.substring(i);
            } else {
                line = "";
            }
        } else {
            do {
                word += line.charAt(i);
                i++;
            } while (i != line.length() && line.charAt(i) != delimiter);
            word = deleteDoubleQuotesInWord(word);
            wordsOfLine.add(word);
            while (i < line.length() - 1 && line.charAt(i) == delimiter) {
                i++;
            }
            if (i < line.length()) {
                line = line.substring(i);
            } else {
                line = "";
            }
        }
        return line;
    }


    public String deleteQuotesOfLine(String str) {
        if (str.length() > 1 && str.charAt(0) == combiner && str.charAt(str.length() - 1) == combiner) {
            str = str.substring(1, str.length() - 1);
        }
        return str;
    }

    public String deleteDoubleQuotesInWord(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++){
            if (i != str.length() - 1 && str.charAt(i) == combiner && str.charAt(i + 1) == combiner) {
                result += combiner;
                i++;
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }

    public class OpenFile implements Runnable {
        private FileChannel channel;
        private int startLocation;
        private int distance;
        private ArrayList<String> temp;

        public OpenFile(int startLocation, int distance, FileChannel chnl) {
            this.startLocation = startLocation;
            this.distance = distance;
            this.channel = chnl;
        }


        public void run() {
            temp = new ArrayList<>();
            try (Stream<String> lines = Files.lines(Paths.get(inputFile))) {
                for(int i = startLocation; i < startLocation + distance; i++){
                    String line = Files.readAllLines(Paths.get(inputFile)).get(i);
                    temp.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            lines.addAll(temp);
        }
    }
}


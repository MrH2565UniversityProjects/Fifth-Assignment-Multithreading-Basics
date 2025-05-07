import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TypingTest {

    private static String lastInput = "";
    private static Scanner scanner = new Scanner(System.in);
    public static class InputRunnable implements Runnable {


        @Override
        public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                lastInput = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void testWord(String wordToTest) {
        try {
            System.out.println(wordToTest);
            lastInput = "";

            Thread inputThread = new Thread(new InputRunnable());
            inputThread.start();

            int waitTime = 10; // seconds
            for (int i = 0; i < waitTime * 10; i++) {
                if (!lastInput.isEmpty()) {
                    break;
                }
                Thread.sleep(100);
            }
            System.out.println();
            if (lastInput.isEmpty()) {
                System.out.println("Time's up!");
                inputThread.interrupt();
            }else{
                System.out.println("You typed: " + lastInput);
                if (lastInput.equals(wordToTest)) {
                    System.out.println("Correct");
                } else {
                    System.out.println("Incorrect");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void typingTest(List<String> inputList) throws InterruptedException {

        for (int i = 0; i < inputList.size(); i++) {
            String wordToTest = inputList.get(i);
            testWord(wordToTest);
            Thread.sleep(2000); // Pause briefly before showing the next word
        }

        // TODO: Display a summary of test results
    }
    public static List<String> readWordsFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    words.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
        return words;
    }
    public static void main(String[] args) throws InterruptedException {
        List<String> words = readWordsFromFile("src/main/resources/Words.txt");
        System.out.print("Enter word's count: ");
        int wordCount = scanner.nextInt();
        if(wordCount > words.size()){
            System.out.println("max size is " + words.size());
        }
        wordCount = words.size();
        typingTest(words.subList(0,wordCount));
        System.out.println("Press enter to exit.");
    }
}
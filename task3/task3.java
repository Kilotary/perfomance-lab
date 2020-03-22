import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class task3 {
    private List<Float> cash1;
    private List<Float> cash2;
    private List<Float> cash3;
    private List<Float> cash4;
    private List<Float> cash5;

    public static void main(String[] args) {
        task3 task = new task3(args[0]);

        int interval = task.findTimeIntervalWithMaxCustomer();

        System.out.println(interval);
    }

    public task3(String pathToDirectory) {
        cash1 = loadNumbersFromFile(pathToDirectory + "\\Cash1.txt");
        cash2 = loadNumbersFromFile(pathToDirectory + "\\Cash2.txt");
        cash3 = loadNumbersFromFile(pathToDirectory + "\\Cash3.txt");
        cash4 = loadNumbersFromFile(pathToDirectory + "\\Cash4.txt");
        cash5 = loadNumbersFromFile(pathToDirectory + "\\Cash5.txt");
    }

    public List<Float> loadNumbersFromFile(String filePath) {
        List<Float> numbers = new ArrayList<>();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                numbers.add(Float.parseFloat(line));
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numbers;
    }

    public int findTimeIntervalWithMaxCustomer() {
        float max = 0;
        int maxTimeInterval = 0;
        float intermediateMax = 0;

        for (int i = 0; i < 16; i++) {
            intermediateMax = cash1.get(i) + cash2.get(i) + cash3.get(i) + cash4.get(i) + cash5.get(i);

            if (intermediateMax > max) {
                max = intermediateMax;
                maxTimeInterval = i + 1;
            }
        }

        return maxTimeInterval;
    }
}

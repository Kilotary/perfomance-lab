import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class task1 {
    private List<Integer> numbers;

    public static void main(String[] args) {
        task1 task = new task1(args[0]);

        System.out.printf(
                Locale.ENGLISH,
                "%.2f\n%.2f\n%.2f\n%.2f\n%.2f\n",
                task.percentile90(),
                task.median(),
                task.max(),
                task.min(),
                task.average()
        );
    }


    public task1(String path) {
        numbers = loadNumbersFromFile(path)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Integer> loadNumbersFromFile(String filePath) {
        List<Integer> numbers = new ArrayList<>();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                numbers.add(Integer.parseInt(line));
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numbers;
    }

    public float min() {
        float minNumber = numbers.stream()
                .min(Integer::compareTo)
                .get();

        return minNumber;
    }

    public float max() {
        float maxNumber = numbers.stream()
                .max(Integer::compareTo)
                .get();

        return maxNumber;
    }

    public float average() {
        float averageNum = (float) numbers.stream()
                .mapToDouble((s -> s))
                .average()
                .getAsDouble();

        return averageNum;
    }

    public float percentile(float perc) {
        float k = perc * numbers.size();
        float rank = k - perc + 1;

        float num1 = numbers.get((int) Math.floor(k - 1));
        float num2 = numbers.get((int) Math.floor(k));

        return num1 + (rank % 1) * (num2 - num1);
    }

    public float median() {
        return percentile(0.5f);
    }

    public float percentile90() {
        return percentile(0.9f);
    }
}

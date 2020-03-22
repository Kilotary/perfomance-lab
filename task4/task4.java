import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class task4 {
    private List<VisitTime> visitsTime;

    public static void main(String[] args) {
        task4 task = new task4(args[0]);
        List<VisitInterval> intervals = task.findIntervals();
        SimpleDateFormat sf = new SimpleDateFormat("H:mm");

        for (VisitInterval interval : intervals) {
            System.out.println(sf.format(interval.getStartInterval()) +
                    " " +
                    sf.format(interval.getEndInterval()) +
                    " " +
                    interval.getCustomerCount()
            );
        }

    }

    public task4(String pathToDirectory) {
        loadFromFile(pathToDirectory);
    }

    public void loadFromFile(String filePath) {
        List<VisitTime> times = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("H:mm");
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] timeInterval = line.split(" ");

                Date enterDate = format.parse(timeInterval[0]);
                Date exitDate = format.parse(timeInterval[1]);

                times.add(new VisitTime(enterDate, 1));
                times.add(new VisitTime(exitDate, -1));
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.visitsTime = times.stream()
                .sorted(Comparator.comparingLong(p -> p.getDate().getTime()))
                .collect(Collectors.toList());
    }

    public List<VisitInterval> getVisitIntervals() {
        List<VisitInterval> intersect = new ArrayList<>();
        Date intervalStart;
        Date intervalEnd;
        int peopleCount = 0;

        for (int i = 0; i < visitsTime.size() - 1; i++) {
            intervalStart = visitsTime.get(i).getDate();
            intervalEnd = intervalStart;
            peopleCount += visitsTime.get(i).getIncrement();

            while (i + 1 < visitsTime.size() && intervalEnd.getTime() == visitsTime.get(i + 1).getDate().getTime()) {
                i++;
                intervalEnd = visitsTime.get(i).getDate();
                peopleCount += visitsTime.get(i).getIncrement();
            }

            if (i + 1 < visitsTime.size()) {
                intervalEnd = visitsTime.get(i + 1).getDate();
            }

            intersect.add(new VisitInterval(intervalStart, intervalEnd, peopleCount));
        }

        return intersect;
    }

    private List<VisitInterval> mergeSiblingVisitIntervalsWithSamePeopleAmount(List<VisitInterval> intervals) {
        List<VisitInterval> merged = new ArrayList<>();
        Date startDate;
        Date endDate;
        int peopleCount = 0;


        for (int i = 0; i < intervals.size(); i++) {
            startDate = intervals.get(i).getStartInterval();
            endDate = intervals.get(i).getEndInterval();
            peopleCount = intervals.get(i).getCustomerCount();

            while ((i + 1 < intervals.size()) &&
                    (peopleCount == intervals.get(i+1).getCustomerCount()) &&
                    (endDate.getTime() == intervals.get(i+1).getStartInterval().getTime())) {
                i++;
                endDate = intervals.get(i).getEndInterval();
            }

            merged.add(new VisitInterval(startDate, endDate, peopleCount));
        }

        return merged;
    }

    public List<VisitInterval> findIntervals() {
        return getIntervalsWithMaxCustomer(mergeSiblingVisitIntervalsWithSamePeopleAmount(getVisitIntervals()));
    }

    private List<VisitInterval> getIntervalsWithMaxCustomer(List<VisitInterval> intervals) {
        List<VisitInterval> maxVisitIntervals = new ArrayList<>();

        int max = intervals.stream()
                .max((p1,p2)->(p1.getCustomerCount() > p2.customerCount) ? 1 :
                (p1.getCustomerCount() < p2.getCustomerCount()) ? -1 : 0)
                .get()
                .getCustomerCount();

        for (VisitInterval interval : intervals) {
            if (interval.getCustomerCount() == max) {
                maxVisitIntervals.add(interval);
            }
        }
        return maxVisitIntervals;
    }

    class VisitInterval {
        private Date startInterval;
        private Date endInterval;
        private int customerCount;

        public VisitInterval(Date start, Date end, int count) {
            startInterval = start;
            endInterval = end;
            customerCount = count;
        }

        public Date getStartInterval() {
            return startInterval;
        }

        public Date getEndInterval() {
            return endInterval;
        }

        public int getCustomerCount() {
            return customerCount;
        }
    }

    class VisitTime {
        private Date date;
        private int increment;

        public VisitTime(Date date, int increment) {
            this.date = date;
            this.increment = increment;
        }

        public Date getDate() {
            return date;
        }

        public int getIncrement() {
            return increment;
        }
    }
}

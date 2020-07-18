package com.exam.eventmetrics.engine;

import com.exam.eventmetrics.pojoentites.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@Service
public class DateTimeProcessor {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    private static final Map<Integer, Integer> hoursToCommitsCountMap = new HashMap<>();
    List<Integer> hourList = new ArrayList<>();

    public void processDateTimeOfEvents(Event event) {
            if(event != null){
                if(isNotEmpty(event.getCreatedAt())){
                    try {
                        Date parseDate = simpleDateFormat.parse(event.getCreatedAt());
                        int hours = parseDate.getHours();
                        performSlidingWindowOperations();
                        hourList.add(hours);
                        Integer count = hoursToCommitsCountMap.getOrDefault(hours, null);
                        count = count == null ? 1: count+1;
                        hoursToCommitsCountMap.put(hours, count);
                        printHappeningHour();
                    } catch (ParseException e) {
                        log.error("Error while parsing the datetime",e);
                        e.printStackTrace();
                    }
                }
            }
    }

    private void performSlidingWindowOperations() {
        if(hourList.size() >= 10) {
            Integer toBeRemoved = hourList.get(0);
            Integer count = hoursToCommitsCountMap.getOrDefault(toBeRemoved, null);
            count = count == null ? 0: count-1;
            hoursToCommitsCountMap.put(toBeRemoved, count);
            hourList.remove(0);
        }
    }

    private void printHappeningHour() {
        HappeningHour happeningHour = mostCommonHourOFDay();
        System.out.println("Most Happening hour of the current frame is: "+happeningHour.getHour()+" with latest "+happeningHour.getCount() +" events.");
    }

    public HappeningHour mostCommonHourOFDay(){
        int maxCount = 0;
        HappeningHour happeningHour = new HappeningHour();
        for (Map.Entry<Integer, Integer> entry : hoursToCommitsCountMap.entrySet()) {
            if(maxCount < entry.getValue()){
                maxCount = entry.getValue();
                happeningHour = new HappeningHour(entry.getKey(),maxCount);
            }
        }
        return happeningHour;
    }
}

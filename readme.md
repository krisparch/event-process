# App Event Process
##Problem
##### consume these events from the message bus they were published to

- Print the following information for a 'sliding window' of the 10 most recently consumed messages:
the 5 most commonly used words in the commit messages (in case of a tie, sort alphabetically)

- The most common 'hour of day' the commits took place, normalized to one time zone
    
    To clarify the 'sliding window': For each of the first 10 messages, consider all events seen so far. For example, after the fifth message, the data printed should represent messages 1 through 5. For message 11, the statistics should represent data from messages 2 through 11. By message 25, data should represent messages 16 through 25. By message 30, data should represent messages 21-30.

This project will read Github event from kafka broker and process them and generate some metrics
#### Metric 1
- Most repeated words in the last 10 events. If there is a tie, then sorted based on alphabetically order.
#### Metric 2
- Most Common hour (Converted to running machine timezone) of the events i.e. at which hour in day the frequency of events is more. 

#### Output in the command prompt
 - we display the output in the command prompt for each event.
 
### Run the Application
 `mvn spring-boot:run`
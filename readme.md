# App Event Process

This project will read Github event from kafka broker and process them and generate some metrics
#### Metric 1
- Most repeated words in the last 10 events. If there is a tie, then sorted based on alphabetically order.
#### Metric 2
- Most Common hour (Converted to running machine timezone) of the events i.e. at which hour in day the frequency of events is more. 

#### Output in the command prompt
 - we display the output in the command prompt for each event.
 
 ## Run the Application
 `mvn spring-boot:run`
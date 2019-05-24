cd /d %KAFKA_HOME%\bin\windows\
kafka-console-consumer.bat --zookeeper localhost:2181 --topic test -from-beginning

pause
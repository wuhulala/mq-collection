cd /d D:\tools\kafka\bin\windows\


kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 4 --topic test


pause
# SecureLog

This project provides creation and verification of log-data. Through keyed-hash message authentication codes it is possible to check whether the log-data was tampered or not.

For every file you need to specify a secret. This secret is used to generate and verify the log data later on.

## Getting Started

These instructions will show you how to integrate SecureLog in your project and how to use it.

### Prerequisites

To use SecureLog you need to add these dependencies in your Project.

```
	<dependencies>
		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-api</artifactId>
			<version>2.8.2</version>
		</dependency>
		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-core</artifactId>
			<version>2.8.2</version>
		</dependency>
		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-1.2-api</artifactId>
			<version>2.8.2</version>
		</dependency>
		<dependency>
			<groupId>commons-codec</groupId>
			<artifactId>commons-codec</artifactId>
			<version>1.10</version>
		</dependency>
	</dependencies>	
```

### Integration


Step 1: Adding libray

```
Add the SecureLog-1.0.jar file from the target folder in your build-path.
```

Step 2: Add a log4j2 configuration file "log4j2.xml" in your classpath. More infos about log4j2 configuration [here](https://logging.apache.org/log4j/2.0/manual/configuration.html)

Here is an example config file. 

* Note: The PatternLayout has to be exact the same as the one in the config file. No linebreaks and modicifations!!

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="FATAL"> 
	<Properties> <Property name="log-path">Your logging path !!</Property> 
	</Properties> 
	<Appenders> 
	<File name="MyFile" fileName="${log-path}/Your File Name!!"> 
	<PatternLayout> 
	<Pattern>%d %p %c{1.} [%t];userId=%X{userId};activity=%X{activity};activityDetail=%X{activityDetail};HMAC=</Pattern> 
	</PatternLayout> 
	</File> 
	</Appenders> 
	<Loggers> 
	<Root level="TRACE">
	 <AppenderRef ref="MyFile" /> 
	 </Root> 
	 </Loggers> 
</Configuration>
```

### Usage Examples

* SecureLogger(String secret, String loggingPath) Constructor: The Constructor has the secret of the log-file and the path of the log-file, which you want to generate or verify, as input parameters.

```
	SecureLogger secureLogger = new SecureLogger("testSecret", "/Users/testUser/Desktop/test.log");
```

* void secureLog(LogLevel logLevel, String userId, String activity, String activityDetail) Method: This method is for logging. It logs the input parameters into the specified log-file.

```
	secureLogger.secureLog(LogLevel.TRACE, "testUser", "DELETE", "deleted XY");
	secureLogger.secureLog(LogLevel.FATAL, "testUser1", "INSERT", "inserted XY");
```

* boolean verifyLog() Method: This method checks wheter the logs in a file were altered or not. If the file is not altered it returns "true", otherwise it returns "false".

```
	boolean isValid = secureLogger.verifyLog();	
```

## Built With

* [Log4j 2](https://logging.apache.org/log4j/2.x/) - Generating log-data
* [Maven](https://maven.apache.org/) - Dependency Management

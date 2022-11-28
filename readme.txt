Requirements with links to download:
1. Java 17 
	- (Azul Zulu JDK) https://cdn.azul.com/zulu/bin/zulu17.38.21-ca-jdk17.0.5-win_x64.msi?_gl=1*m9tr7d*_ga*MTYxNTUwNDU0MC4xNjYxMzI1MTM3*_ga_42DEGWGYD5*MTY2OTM4MDQwMi4yLjEuMTY2OTM4MDQwNC41OC4wLjA
3. Apache ActiveMq 5.17.2
	- https://www.apache.org/dyn/closer.cgi?filename=/activemq/5.17.2/apache-activemq-5.17.2-bin.zip&action=download
4. Docker Container/ Docker Desktop
	- https://desktop.docker.com/win/main/amd64/Docker%20Desktop%20Installer.exe?utm_source=docker&utm_medium=webreferral&utm_campaign=dd-smartbutton&utm_location=module
5. Spring Tool Suite
6. Postman

Steps to run:

1.Run ActiveMQ server
  - Configure according to given configurations.
  - Open extracted file go to bin folder and run cmd
  - Run command: activemq start
  - Check if it is running 

2. Get following docker image running in docker container
    https://hub.docker.com/r/xyzassessment/backend-services


3. IDE setup 
   - Clone project in STS fromgithub repository : ""
   - Import as Maven project in STS 
   - Ensure Maven update and build
  
3. Update application.properties file which is located on path src/main/resources
	a. Update properties for ActiveMq (Ensure enter tcp url see ref ss: https://github.com/rahulkasaudhan99/configurations/blob/main/Screenshot%201.png )
  		spring.activemq.broker-url=<amq-brocker-url>
		spring.activemq.user=<activemq-username>
		spring.activemq.password=<activemq-password>

	b. Update properties for External Api's as per Docker container (update uri : hostname and port here) 
           pricing.api.base.url=http://localhost:9080/pricing?q=
           tracking.api.base.url=http://localhost:9080/track?q=
           shipment.api.base.url=http://localhost:9080/shipments?q=

4. Run as Spring Boot Application

5. Open Postman 
    - Do a GET request on below uri
	  http://localhost:8080/aggregation?pricing=CR,NR,PN,RN,BN&track=123456881,109347263,109347272,109347282,109347283&shipments=123456884,109347252,109347292,109347282,109347242
	
	
	
Steps to run automated tests in "src/test/java": 
	- Do run automated test cases as Junit Tests. 
	- see all four automated test cases covering all possible scenarios. 
	
	
	
	
	
	
For Reference

Configurations :

Active mq :
	- Download ActiveMQ zip file
    - Extract zip file 
    - Copy Extracted file to location(eg : "C:\Program Files")
    - Goto location apache-activemq-5.17.2 -> webapps -> api -> WEB-INF -> web.xml 
    - Update file location for "jolokia-access.xml" in web.xml file (see ref ss: 												https://github.com/rahulkasaudhan99/configurations/blob/main/File%20location%20change%20in%20web.xml.png
    - location for jolokia-access.xml : apache-activemq-5.17.2 -> conf

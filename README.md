# Task Tracker web app using JSP, Servlet

### why?
Simply because I want to learn and try something using Tomcat, web developing, session maintaining, JWT, API, authentications

### Tech stack used:
**Programming language:** [Java openJDK 21](https://jdk.java.net/archive/)  
**Build tool:** Maven(provided by IntelliJ not separately installed)  
**Scripting language:** JavaScript  
**Web stying:** plain-CSS  
**Web Page:** JSP  
**Font:** [Jetbrains Mono](https://www.jetbrains.com/lp/mono/)  
**DataBase:** [MySql 8.4](https://dev.mysql.com/doc/relnotes/mysql/8.4/en/)  
**Cloud:** [Cloudinary](https://cloudinary.com/)  
**IDE:** Of course! [Jetbrains IntelliJ Idea](https://www.jetbrains.com/idea/) <sub>Only Ultimate works for JSP</sub>  
**Web Server/Servlet container:** [Tomcat 10.1.46](https://tomcat.apache.org/download-10.cgi)  

> SVG's are from figma iconify plugin imported from figma as svg [iconify](https://iconify.design)

### More about This project:
So, This is just another ordinary to do kinda app.  
This is fully using HTTPS not HTTP configured in server.xml in conf dir of tomcat itself and generated tls certificate locally for encryption
for data in transit, Server Side Rendering Web Application.
Used cloudinary for managing image asset.

### Future of this project:
-> Filter option  
-> progress bar  
-> make UI like calendar to track as per day  
-> add logic and page modification for editing task => task edit can edit task title, task due date  
-> paging to reduce lag (Most probably covered with calendar style dashboard)  
-> add API so that can access via mobile app build later that communicate to this server for JSON response  

### Want to Run this Locally? here step to follow:
#### Requirements:
Java 21  
MySql 8.4  
Tomcat 10.1.46  
IDE of your choice (IntelliJ Idea highly recommended) IDE is for development purpose only  
Git - optional, can download whole repo [here](https://github.com/MrKumaran/TaskTracker/archive/refs/heads/main.zip) as zip if not git  

> [!NOTE]
> Developed using Java openJDK 21, MySql 8.4, Tomcat 10.1.46 but upon my knowledge I didn't use any version specific methods. So, previous(1 or 2) version may work dependency error may occur,
> only checked for Java openJDK 21, MySql 8.4, Tomcat 10.1.46 for plug and play experience use recommended versions for all

If you want to run on server not development, generate War and copy files into tomcat directory and start Tomcat server, [Refer this](https://www.baeldung.com/tomcat-deploy-war)   
Config [this](src/main/webapp/META-INF/context.xml) xml with DB details and if you want your application to use HTTPS generate from authorized CA and put into Server.xml
and good to GO...
> [!CAUTION]
> if running on home lab want access outside home, port forward it - Do this at your own risk! if not done correct, hacker can hack your network.


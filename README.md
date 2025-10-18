# Task Tracker web app using JSP, Servlet

### why?
Simply because I want to learn and try something using Tomcat, web developing, session maintaining, JWT, API, authentications.
<sub>This contains many different ways to do same things, those are purely for me to have some learning</sub>

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

<!-- ### Future of this project:
-> Filter option  
-> progress bar  
-> make UI like calendar to track as per day  
-> add logic and page modification for editing task => task edit can edit task title, task due date  
-> paging to reduce lag (Most probably covered with calendar style dashboard)  
-> add API so that can access via mobile app build later that communicate to this server for JSON response  -->

### Want to Run this Locally? here step to follow:
#### Requirements:
Java 21  
MySql 8.4  
Tomcat 10.1.46
Cloudinary API KEY from [Cloudinary](https://cloudinary.com/)  
IDE of your choice (IntelliJ Idea highly recommended) IDE is for development purpose only  
Git - optional, can download whole repo [here](https://github.com/MrKumaran/TaskTracker/archive/refs/heads/main.zip) as zip if not git  

> [!NOTE]
> Developed using Java openJDK 21, MySql 8.4, Tomcat 10.1.46 but upon my knowledge I didn't use any version specific methods. So, previous(1 or 2) version may work dependency error may occur,
> only checked for Java openJDK 21, MySql 8.4, Tomcat 10.1.46 for plug and play experience use recommended versions for all

If you want to run on server not development, generate War and copy files into tomcat directory and start Tomcat server, [Refer this](https://www.baeldung.com/tomcat-deploy-war)   
Config context.xml in tomcat server with DB details and if you want your application to use HTTPS generate from authorized CA and put it into tomcat's Server.xml, in tomcat's context.xml add cloudinary details and good to GO...

Modify below snippet with your details and Add it into tomcat's context.xml
```XML
    <Environment name="cloudinary.api.cloudName" value="<YOUR-CLOUD-NAME>" type="java.lang.String"/>
    <Environment name="cloudinary.api.key" value="<YOUR-API-KEY>" type="java.lang.String"/>
    <Environment name="cloudinary.api.secret" value="<YOUR-API-SECRET>" type="java.lang.String"/>
    <Resource name="db/tasktracker" auth="Container" type="javax.sql.DataSource"
      maxTotal="20" maxIdle="1" maxWaitMillis="5000"
      username="<DB user>" password="<DB password>"
      driverClassName="com.mysql.cj.jdbc.Driver"
      url="jdbc:mysql://<DB HOST>:<DB port>/tasktracker"/>
```

> [!CAUTION]
> if running on home lab want access outside home, port forward it - Do this at your own risk! if not done correct, hacker can hack your network.


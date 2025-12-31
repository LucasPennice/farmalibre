# Problemas con la base de datos. Driver no encontrado

**Error**

``` bash
java.sql.SQLException: No suitable driver found for "jdbc:mysql://localhost:3306/farmacia_db?useSSL=false&serverTimezone=UTC" java.sql/java.sql.DriverManager.getConnection(DriverManager.java:708) java.sql/java.sql.DriverManager.getConnection(DriverManager.java:230) Utils.DbUtil.getConnection(DbUtil.java:21) db.DatabaseInitializer.init(DatabaseInitializer.java:11) web.FrontController.init(FrontController.java:54) jakarta.servlet.GenericServlet.init(GenericServlet.java:145) jakarta.servlet.http.HttpServlet.init(HttpServlet.java:124) org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482) org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:83) org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:654) org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:341) org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:397) org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63) org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:903) org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1778) org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:946) org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:480) org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:57) java.base/java.lang.Thread.run(Thread.java:1583)
```

**Solución**
Copiar el mysql-connector-j-8.4.0.jar que está dentro del .war a la carpeta lib de donde se tenga instaldo el tomcat que se está usando (para brew -> brew --prefix tomcat)

# Ver logs

Ejecutar

`tail -f /opt/homebrew/opt/tomcat/libexec/logs/catalina.out`


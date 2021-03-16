FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} asm_poc_medation.jar
COPY asmRestObserver.cer /tmp/certificate.cer
RUN  keytool -noprompt -import -alias mycert -storepass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts -file /tmp/certificate.cer
#CMD ["keytool", "-list", "-keystore",  "/opt/java/openjdk/lib/security/cacerts", "-alias", "mycert", "-storepass", "changeit" ]
ENTRYPOINT ["java","-Dcom.sun.jndi.ldap.object.disableEndpointIdentification=true","-jar","/asm_poc_medation.jar"]

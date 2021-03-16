FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} asm_poc_medation.jar && \
     asmFileObserver.crt /tmp/cert/certificate.crt
RUN  keytool -noprompt -import -alias mycert -storepass changeit -keystore /opt/java/openjdk/lib/security/cacerts -file /tmp/cert/certificate.crt
#CMD ["keytool", "-list", "-keystore",  "/opt/java/openjdk/lib/security/cacerts", "-alias", "mycert", "-storepass", "changeit" ]
ENTRYPOINT ["java","-jar","/asm_poc_medation.jar"]

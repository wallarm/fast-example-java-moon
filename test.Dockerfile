FROM maven:3.8.1-ibmjava-8-alpine

WORKDIR /usr/src/mymaven

# Add all tests code and configs
ADD . /usr/src/mymaven

# Create a pre-packaged repository
# See https://hub.docker.com/_/maven in the section 'Packaging a local repository with the image'
COPY pom.xml /tmp/pom.xml
RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve

CMD [ "mvn", "test" ]

# fast-example-java-moon

### Prerequisites

1. Java version: 8+ (tested on "OpenJDK 64-Bit Server VM (build 14.0.2+12-Ubuntu-120.04, mixed mode, sharing)")
2. Apache Maven (tested on "Apache Maven 3.6.3")
3. Docker engine for deploying DVWA (tested on "Client/Server: Docker Engine - Community 20.10.6")

You also need those 3 things (their underlying machines) to have network access to each other:
1. the tests from this repo should have access to the Moon cluster,
2. the Moon cluster should have access to the FAST docker container,
3. the FAST docker container should have access to the test app "DVWA" (see below).

### Quick start
#### Deploy DVWA
Here we use this docker image with the app: https://hub.docker.com/r/vulnerables/web-dvwa/

**Warning**: do not deploy it to any public servers.
1. Run docker container with the test vulnerable app 'DVWA': `docker run --name web-dvwa --rm -it -p 80:80 vulnerables/web-dvwa`
2. Open the deployed DVWA "Database Setup" page in a browser
3. Setup DVWA app by clicking on the "Create / Reset database" button.

#### Deploy FAST
1. Create env file 'fast.env' with the content bellow:
```
REVERSE_PROXY=true
#WALLARM_API_HOST=us1.api.wallarm.com ## default: us1.api.wallarm.com
BACKEND=http://$DVWA_HOST
WALLARM_API_TOKEN=$WALLARM_API_TOKEN
```
2. Replace $DVWA_HOST with the actual domain name or IP address (and add a port if you changed it from 80 when running the DVWA docker container)
3. Replace $WALLARM_API_TOKEN with the actual FAST node token (see how to get it: https://docs.fast.wallarm.com/en/operations/create-node.html)
4. Run FAST docker container: `docker run --rm --name fast-test-dvwa --env-file=$(pwd)/fast.env -p 8080:8080 wallarm/fast:latest`

#### Deploy Moon on Minikube
You can omit this if you already have a Moon cluster.

1. Install minikube for deploying the Moon (if you don't already have minikube) as described here: https://minikube.sigs.k8s.io/docs/start/
2. Deploy the Moon to minikube as describe here: https://aerokube.com/moon/latest/#install-minikube
3. Run `minikube ip` to know its IP address.
4. Optionally for convenience you can add minikube IP address to the file `/etc/hosts` with an arbitrary domain name like this: `192.168.49.2    minikube`, then you can open Moon Web UI in a browser on http://minikube:8080

#### Run tests
1. Clone this repo
2. Edit the config file `config.properties` at the repo root: uncomment lines and modify values with actual addresses of deployed fast_proxy and minikube.
3. Run the tests: `mvn test` (also, you can open the project in any IDE and run the tests in it)

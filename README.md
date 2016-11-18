# Metrics Basics Workshop

## Preparing the environment

* install Docker with docker-machine and docker-compose
* create new docker-machine (if not already running - some installation processes might create default one)

```
docker-machine create default --driver virtualbox
# (HyperV driver on Windows)
docker-machine env default
# follow instructions on the screen to enable this machine as default in this env
# also take note of docker machine IP address
```

* run docker-compose in project `docker` directory

```
cd docker
docker-compose up -d
```

check if everything is ok by entering docker-machine IP address (from `docker-machine env default`) into the
browser - it should display Graphite main view

* run `./gradlew build` to load dependencies
* open the project in your IDE of choice, so that it loads any dependencies and data it needs

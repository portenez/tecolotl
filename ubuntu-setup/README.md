#ubuntu-setup
Contains several automation scripts to configure a newly created ubuntu VM/installation.

You'll need to `sudo` run the scripts in order for them to work. 

#Dockerfile & testing
The scripts were tested by being executed within the docker container provided here.

##Building the test docker container

```Shell
sudo docker build -t ${my-tag} .
```


##Running the docker container with mounted folder for tests

```Shell
sudo docker run -i -v `pwd`:${target-dir-to-mount}` --rm -t ${my-tag} /bin/bash
```

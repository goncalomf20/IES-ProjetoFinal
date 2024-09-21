# IES_project1
Projeto IES SundayCrypto
# Team Elements
### Team Manager:  
108902  Diogo Almeida almeidadiogo03@ua.pt
### Architect: 
108624 Tomás Matos tomas.matos@ua.pt
### Product Owner: 
107853 Gonçalo Ferreira goncalomf@ua.pt
### DevOps: 
Diogo Almeida, Tomás Matos, Gonçalo Ferreira


### Iteração 1.2 está nos reports

## api keys 30/min

### key Diogo: 
CG-gmcXY27MsaHfVMGsJghswQpe

### key Tomás:
CG-Aw2syneGkuqosMWmdwLZgwj6

## RABBITMQ 
http://localhost:15672/ depois de instalar


# SEM DOCKER COMPOSE UP 
## Correr em 3 terminais
#### 1) python3 Back_end/MessageBroker/manage_broker.py 
#### 2) python3 DataGen/info_receiver.py 

#### 3) docker run --name mysql5 -e MYSQL_ROOT_PASSWORD=secret1 -e MYSQL_DATABASE=demo -e MYSQL_USER=demo -e MYSQL_PASSWORD=secret2 -p 33061:3306 -d mysql/mysql-server:5.7 
#### cd /Back_end/sunday
#### mvn package
#### ./mvnw spring-boot:run 


# COM DOCKER COMPOSE UP 

### docker-compose up --build


### Portas que têm que estar livres:
#### rabbitmq - 5672 ou 15672
#### react_app - 5173
#### spring_app - 8080 
#### sql - 3306 

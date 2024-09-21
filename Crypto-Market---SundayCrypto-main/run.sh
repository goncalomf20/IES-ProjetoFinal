#!/bin/bash

gnome-terminal -- bash -c "python3 DataGen/info_receiver.py; exec bash"

gnome-terminal -- bash -c "python3 Back_end/MessageBroker/manage_broker.py;exec bash"

gnome-terminal -- bash -c "cd Back_end/sunday;mvn package;./mvnw spring-boot:run; exec bash"

sleep 5

gnome-terminal -- bash -c "cd Front_End/front-end-app/;npm run dev;exec bash;"

xdg-open "http://localhost:5173/"


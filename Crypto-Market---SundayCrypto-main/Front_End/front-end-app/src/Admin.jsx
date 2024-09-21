import React, { useEffect, useState } from "react";
import Footer from "./components/Footer";
import Card_Ticket from "./components/CardTicket";
import NavbarAdmin from "./components/NavbarAdmin";

function Admin() {
 
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [tickets, setTickets] = useState([]);
    const [user2 ,setUser2] = useState({});
    const [newKey, setNewKey] = useState(""); // State to store the new key


    const handleTickets = async (id) => {
        try {
          const responsetickets = await fetch("http://localhost:8080/ticket/getTickets/" + id, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
          });
       
          const ticketsdata = await responsetickets.json();
          if (Array.isArray(ticketsdata)) {
            setTickets(ticketsdata);
            console.log(ticketsdata);
          } else {
            console.error("Expected an array of tickets, but got:", ticketsdata);
          }
        } catch (error) {
          console.error("Error during ticket assign:", error);
        }
       };
       

  const handleLogin = async () => {
    try {
      const response = await fetch("http://localhost:8080/admin/token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ token: localStorage.getItem("admin") }),
      });

      const admindata = await response.json();
      if (response.status == response.notFound) {
        setIsLoggedIn(false);
        console.error("Login failed");
      } else {
        console.log("Login successful");
        setUser2(admindata);
        setIsLoggedIn(true);
        handleTickets(admindata.id);
      }
    } catch (error) {
      console.error("Error during login:", error);
    }
  };

  const handleCreateKey = async () => {
    try {
      const responsez = await fetch("http://localhost:8080/admintkn/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({tkn:""}),
      });

      const result = await responsez.json();
      setNewKey(result)
      alert("New key created successfully");
    } catch (error) {
      console.error("Error during key creation:", error);
    }
  };


  useEffect(() => {
    handleLogin();
  }, []);

  
  {
    return (
      <>
      <style>
      {`
        body {
          background-color: #969595;
          margin: 0; /* Remove default margin to cover the entire screen */
        }
      `}
    </style>
        <div className="container-fluid" style={{backgroundColor:'#969595'}}>
            <div className="row">
            <NavbarAdmin></NavbarAdmin>
            </div>
            {isLoggedIn ? (
              <div>
                <button type="button" onClick={handleCreateKey}>
                  Create Key
                </button>
                <p>
                  <h1>Key : </h1> {newKey.tkn}
                </p>
              </div>
            ) : (
              <p></p>
              )};
            <div className="row">
            {tickets.map((ticket, index) => (
                <div
                key={index}
                className="col-md-3 mb-4 d-flex justify-content-center align-items-center"
                >
                <Card_Ticket ticket={ticket}/>
                </div>
            ))}
            </div>
          </div>
      </>
    );

  }
}

export default Admin;

import { faBell } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import { Dropdown } from "react-bootstrap";
import { Link } from "react-router-dom";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "stompjs";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function Navbar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [selectedDropop, setselectedDropop] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [newnotificonisShown, setnewnotificonisShown] = useState(false);

  const notificationCount = notifications.length;


  const options = ["Withdraw", "Add Money"];

  const handleDeleteNotification = (notificationId) => {
    const updatedNotifications = notifications.filter(
      (notification) => notification.id !== notificationId
    );
    handleDeleteAlert(notificationId);
    setNotifications(updatedNotifications);
  };

  const handleDeleteAlert = async (id) => {
    try {
      const responsed = await fetch(
        `http://localhost:8080/alerts/getAlert/${id}`,
        {
          method: "DELETE",
        }
      );

      if (responsed.ok) {
        // Handle success, e.g., update state or perform other actions
        
        getAlerts();
      } else {
        // Handle error, e.g., show an error message
        console.error(`Failed to delete alert with id ${id}`);
      }
    } catch (error) {
      console.error("Error deleting alert:", error);
    }
  };


  const getAlerts = async () => {
    try {
      const responsex = await fetch(`http://localhost:8080/alerts/getAlert`);
      if (responsex.ok) {
        const data = await responsex.json();
        console.log("getalert Data: " + data);
        const filteredData = data.filter(item => item["isDone"] === true);
        
        let all_noti = [];
        for (let index = 0; index < filteredData.length; index++) {
          const element = filteredData[index];
          let msg = "";

          if (element["sell_buy"] === "buy") {
            msg = "You bought " + element["amount_to_exange"] + " units of " + element["coinid"] + " for " + element["limit_value"] + "€.";
          } else if (element["sell_buy"] === "sell") {
            msg = "You sold "+ element["amount_to_exange"] + " units of " + element["coinid"] + " for " + element["limit_value"] + "€.";
          }

          if (element["isPossible"] === false) {
            if (element["sell_buy"] === "buy") {
              msg = "You don't have enough money to " + element["sell_buy"] + " " + element["amount_to_exange"] + " units of " + element["coinid"];
            } else if (element["sell_buy"] === "sell") {
              msg = "You don't have enough units of " + element["coinid"] + " to " + element["sell_buy"] + " " + element["amount_to_exange"] + " units.";
            }
          }

          let dic = {
            id: element["alertID"],
            message: msg
          }
          all_noti.push(dic);
        }

        setNotifications(all_noti);

      } else {
        console.error("Failed to fetch coin data");
      }
    } catch (error) {
      console.error("Error fetching coin data:", error);
    }
  };

  const handleLogin = async () => {
    try {
      const responsez = await fetch("http://localhost:8080/investor/token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ token: localStorage.getItem("token") }),
      });
      console.log(JSON.stringify({ token: localStorage.getItem("token") }));
      const userdata = await responsez.json();
      if (responsez.status == responsez.notFound) {
        setIsLoggedIn(false);
        console.error("Login failed");
      } else {
        console.log("Login successful");
        setUser(userdata);
        setIsLoggedIn(true);
        console.log(user);

        
      }
    } catch (error) {
      console.error("Error during login:", error);
    }
  };

  const setupWebSocket = () => {
    try {
      const socket = new SockJS("http://localhost:8080/ws");
      const stompClient = Stomp.over(socket);
      if (user == null) {
        console.log("User is null");
        return;
      }

      stompClient.connect(
        {},
        () => {
          console.log("Connected to WebSocket");
          console.log(user);
          console.log(user.id);

          stompClient.subscribe("/topic/notification/" + user.id, (message) => {
            try {
              let msg_ = JSON.parse(message.body);
              alert = msg_[0];

              let current_balance = msg_[1];
              let msg = "";
              if (alert["sell_buy"] === "buy") {
                msg = "You bought " + alert["amount_to_exange"] + " units of " + alert["coinid"] + " for " + current_balance + "€.";
            } else if (alert["sell_buy"] === "sell") {
                msg = "You sold "+ alert["amount_to_exange"] + " units of " + alert["coinid"] + " for " + current_balance + "€.";
            } else {
                msg = "You sold";
            }
            console.log(msg_.length, msg_[2]);
            if (msg_.length == 3) {
              if (msg_[2] === "sell") {
                msg = "You don't have enough units of " + alert["coinid"] + " to " + alert["sell_buy"] + " " + alert["amount_to_exange"] + " units.";
              } else if (msg_[2] === "buy") {
                console.log("entrou");
                msg = "You don't have enough money to " + alert["sell_buy"] + " " + alert["amount_to_exange"] + " units of " + alert["coinid"];
              }

              
            }
            console.log(msg);
              let dic = {
                id: alert["alertID"],
                message: msg,

              };
              console.log(dic);
              let noti = []
              // if (dic in notifications) {
              //   console.log("Already in notifications");
              // } else {
                // notifications.concat(dic);
              noti = notifications;
              let all_diferent = true;
              for (let index = 0; index < noti.length; index++) {
                const element = noti[index];
                if (element["id"] === dic["id"]) {
                  all_diferent = false;
                }
              }
              if (all_diferent && msg_[0]["isDone"] === true) {
                noti.push(dic);
              }
              
              // }

              for (let index = 0; index < noti.length; index++) {
                toast.success("New Notification", {
                  position: "top-right",
                  autoClose: 5000, // Set the duration for how long the notification should be visible
                  hideProgressBar: false,
                  closeOnClick: true,
                  pauseOnHover: true,
                  draggable: true,
                });
              }
              
              setNotifications(noti);
              console.log(notifications );
              
            } catch (error) {
              console.error("Error parsing WebSocket message:", error);
            }
          });

        },
        (error) => {
          console.error("Error connecting to WebSocket:", error);
        }
      );

      return () => {
        if (stompClient && stompClient.connected) {
          stompClient.disconnect();
          console.log("Disconnected from WebSocket");
        }
      };
    } catch (error) {
      console.error("Error setting up WebSocket:", error);
    }
  };
  useEffect(() => {
    handleLogin();
    getAlerts();

  }, []);

  const handleOpchange = (selectedKey) => {
    setselectedDropop(selectedKey); // Update the selectedPortfolio state
  };

  setupWebSocket();
  
  return (
    <>
      <ToastContainer />
      <nav
        className="navbar navbar-expand-lg navbar-light"
        style={{
          backgroundColor: "#242424",
          borderBottom: "0.7px solid #fff",
          maxHeight: "65px",
        }}
      >
        <div className="container-fluid">
          <Link to="/">
            <img
              src="/src/assets/bitcoinWhite_png.png"
              alt="Logo"
              style={{ height: "50px", borderRadius: "50px" }}
            />
          </Link>
          <Link to="/" style={{ textDecoration: "none" }}>
            <div
              className="navbar-brand"
              style={{
                paddingLeft: "5px",
                color: "#fff",
              }}
            >
              Sunday Crypto
            </div>
          </Link>
          <h2
            style={{
              paddingLeft: "300px",
              color: "#fff",
            }}
          >
            List of Cryptocurrencies
          </h2>
          <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
              <li
                className="nav-item dropdown"
                style={{
                  paddingRight: "20px",
                  borderRight: "1px solid #fff",
                }}
              >
                {isLoggedIn ? (
                  <Link
                    className="nav-link"
                    to="/usrdetails"
                    style={{
                      color: "#fff",
                    }}
                  >
                    {user.username}
                  </Link>
                ) : (
                  <Link
                    className="nav-link"
                    to="/login"
                    style={{
                      color: "#fff",
                    }}
                  >
                    Login
                  </Link>
                )}
              </li>
                
              
              {isLoggedIn ? (
                <li
                  className="nav-item dropdown"
                  style={{
                    paddingRight: "20px",
                    borderRight: "1px solid #fff",
                  }}
                >
                  <Dropdown onSelect={handleOpchange}>
                    <Dropdown.Toggle variant="dark" id="dropdown-basic">
                      {user.balance} €
                    </Dropdown.Toggle>
                    <Dropdown.Menu>
                      <Dropdown.Item eventKey="Withdraw">
                        <Link to="/withdraw" style={{ textDecoration: "none" }}>
                          Withdraw
                        </Link>
                      </Dropdown.Item>

                      <Dropdown.Item eventKey="Add Money">
                        <Link to="/addmoney" style={{ textDecoration: "none" }}>
                          Add Money
                        </Link>
                      </Dropdown.Item>
                    </Dropdown.Menu>
                  </Dropdown>
                </li>
              ) : (
                <p></p>
              )}
              {isLoggedIn ? (
                <li
                  className="nav-item dropdown"
                  style={{
                    paddingRight: "20px",
                    borderRight: "1px solid #fff",
                  }}
                >
                  <Dropdown onSelect={handleOpchange}>
                    <Dropdown.Toggle variant="dark" id="dropdown-basic">
                      <FontAwesomeIcon icon={faBell} />
                      {notificationCount > 0 && (
                        <span className="badge badge-danger">
                          {notificationCount}
                        </span>
                      )}
                    </Dropdown.Toggle>
                    <Dropdown.Menu>
                      {notifications.map((notification) => (
                        <Dropdown.Item
                        key={notification.id}
                        style={{
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "space-between",
                          marginBottom: "10px"
                        }}
                      >
                        <p style={{ margin: "0" , marginRight: "15px"}}>{notification.message}</p>
                        <button
                          className="btn btn-dark btn-sm"
                          onClick={() => handleDeleteNotification(notification.id)}
                        >
                          X
                        </button>
                      </Dropdown.Item>
                      ))}
                    </Dropdown.Menu>
                  </Dropdown>
                </li>
              ) : (
                <p></p>
              )}

              <li
                className="nav-item dropdown"
                style={{ paddingRight: "20px", paddingLeft: "20px" }}
              >
                {isLoggedIn ? (
                  <Link
                    id="1312312"
                    className="nav-link"
                    onClick={logout}
                    style={{
                      color: "#fff",
                    }}
                  >
                    Logout
                  </Link>
                ) : (
                  <Link
                    className="nav-link"
                    to="/register"
                    style={{
                      color: "#fff",
                    }}
                  >
                    Register
                  </Link>
                )}
              </li>
            </ul>
            {/* <form className="d-flex">
              <input
                className="form-control me-2"
                type="search"
                placeholder="Search"
                aria-label="Search"
              />
              <button className="btn btn-outline-dark" type="submit">
                <FontAwesomeIcon icon={faSearch} />
              </button>
            </form> */}
          </div>
        </div>
      </nav>
    </>
  );
}

export default Navbar;

const logout = async () => {
  localStorage.removeItem("token");
  console.log("Apagado");
  window.location.replace("http://localhost:5173/");
};

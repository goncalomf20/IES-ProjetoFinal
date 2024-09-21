import {
  faFileImport,
  faPlus,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import "./UserDetails.css"; // Import the CSS file
import Footer from "./components/Footer";
import Navbar from "./components/Navbar";
import Portfolio from "./components/Portfolio";

const UserDetails = () => {
  const [user, setUser] = useState(null);
  const [portfolio, setPortfolio] = useState([]);

  const [showModal, setShowModal] = useState(false);
  const [alerts, setAlerts] = useState([]);
  const [showModal2, setShowModal2] = useState(false);
  const [kkey, setKkey] = useState("");

  const handleModalShow = () => {
    getAlerts(); // Fetch alerts when the modal is shown
    setShowModal(true);
  };
  const handleModalClose = () => setShowModal(false);

  const getAlerts = async () => {
    try {
      const response = await fetch(`http://localhost:8080/alerts/getAlert/` + user.id);
      if (response.ok) {
        const data = await response.json();

        const filteredData = data.filter((item) => item["isDone"] === false);

        setAlerts(filteredData);
      } else {
        console.error("Failed to fetch coin data");
      }
    } catch (error) {
      console.error("Error fetching coin data:", error);
    }
  };

  const handleDeleteAlert = async (id) => {
    try {
      const response = await fetch(
        `http://localhost:8080/alerts/getAlert/${id}`,
        {
          method: "DELETE",
        }
      );

      if (response.ok) {
        // Handle success, e.g., update state or perform other actions
        console.log(`Alert with id ${id} deleted successfully`);
        // alert(`Alert with id ${id} deleted successfully`);
        getAlerts();
      } else {
        // Handle error, e.g., show an error message
        console.error(`Failed to delete alert with id ${id}`);
      }
    } catch (error) {
      console.error("Error deleting alert:", error);
    }
  };

  useEffect(() => {
    const handleLogin = async () => {
      try {
        const response = await fetch("http://localhost:8080/investor/token", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ token: localStorage.getItem("token") }),
        });

        const userdata = await response.json();
        if (response.status == response.notFound) {
          console.error("Login failed");
        } else {
          console.log("Login successful");
          console.log(userdata);
          setUser(userdata);

          const getPortfolio = async () => {
            const responsep = await fetch(
              "http://localhost:8080/portfolio/investor/" + userdata.id,
              {
                method: "GET",
                headers: {
                  "Content-Type": "application/json",
                },
              }
            );
            const portdata = await responsep.json();
            console.log(portdata);
            setPortfolio(portdata);
          };
          getPortfolio();
        }
      } catch (error) {
        console.error("Error during login:", error);
      }
    };

    handleLogin();
  }, []);

  const openModal2 = () => {
    setShowModal2(true);
  };

  const closeModal2 = () => {
    setShowModal2(false);
  };

  const importKey = async (kkey, id) => {
    const responsep = await fetch(
      "http://localhost:8080/portfolio/importPort/" + id,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ key: kkey }),
      }
    );
    const portdata = await responsep.json();
    console.log(portdata);
    window.location.replace("http://localhost:5173/usrdetails");
  };

  const addPortfolio = async (uid) => {
    const responsep = await fetch(
      "http://localhost:8080/portfolio/addPortfolio/" + uid,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    const portdata = await responsep.json();
    console.log(portdata);
    window.location.replace("http://localhost:5173/usrdetails");
  };

  return (
    <>
      <Navbar></Navbar>
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-4 mb-4 d-flex flex-column justify-content-start align-items-start mt-4 ml-2">
            <button
              className="btn btn-dark"
              style={{ fontSize: "20px" }}
              onClick={handleModalShow}
            >
              Active Automatic Tasks
            </button>
            {/* Modal */}
            <Modal show={showModal} onHide={handleModalClose}>
              <Modal.Header closeButton>
                <Modal.Title>Pending Automatic Tasks</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                {/* Add your modal content here */}
                <div>
                  {alerts.map((alert) => (
                    <div key={alert.alertID}>
                      <p>
                        Alert to{" "}
                        <span
                          style={{
                            color: alert.sell_buy === "sell" ? "red" : "green",
                          }}
                        >
                          {alert.sell_buy}
                        </span>{" "}
                        <strong>{alert.amount_to_exange} units</strong> of{" "}
                        {alert.coinid} when price hits{" "}
                        <strong>{alert.limit_value}€</strong>
                        <button
                          className="btn btn-link"
                          onClick={() => handleDeleteAlert(alert.alertID)}
                          style={{ color: "black" }}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </button>
                      </p>
                    </div>
                  ))}
                </div>
              </Modal.Body>
              <Modal.Footer>
                <Button variant="secondary" onClick={handleModalClose}>
                  Close
                </Button>
                {/* Add additional buttons or controls if needed */}
              </Modal.Footer>
            </Modal>
          </div>
          <div className="col-md-4 mb-4 d-flex justify-content-center align-items-center">
            <div className="user-details">
              <div className="user-details-content">
                <div className="user-details-card">
                  <h2 className="user-details-title">User Details</h2>
                  <div className="user-details-item">
                    <strong>First Name:</strong>{" "}
                    {user ? user.fname : "Loading..."}
                  </div>
                  <div className="user-details-item">
                    <strong>Last Name:</strong>{" "}
                    {user ? user.lname : "Loading..."}
                  </div>
                  <div className="user-details-item">
                    <strong>Username:</strong>{" "}
                    {user ? user.username : "Loading..."}
                  </div>
                  <div className="user-details-item">
                    <strong>Age:</strong> {user ? user.age : "Loading..."}
                  </div>
                  <div className="user-details-item">
                    <strong>Email:</strong> {user ? user.email : "Loading..."}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-md-4 mb-4 d-flex justify-content-center align-items-center"></div>
          <div className="container mt-3">
            <div className="row">
              <div className="col-md-6 offset-md-3 d-flex justify-content-center align-items-center">
                <h1 className="text-center mb-0 mr-2">Portfolios</h1>
                <button
                  className="btn"
                  style={{
                    backgroundColor: "black",
                    color: "white",
                    marginLeft: "8px",
                  }}
                  onClick={() => addPortfolio(user.id)}
                >
                  {" "}
                  <FontAwesomeIcon icon={faPlus} />
                </button>

                <button
                  className="btn"
                  style={{
                    backgroundColor: "black",
                    color: "white",
                    marginLeft: "8px",
                  }}
                  onClick={() => openModal2()}
                >
                  <FontAwesomeIcon icon={faFileImport} />
                </button>
              </div>

              {showModal2 && (
                <div
                  className="modal"
                  tabIndex="-1"
                  role="dialog"
                  style={{ display: "block" }}
                >
                  <div className="modal-dialog" role="document">
                    <div className="modal-content">
                      <div className="modal-header">
                        <div className="d-flex align-items-center">
                          <div>
                            <h5 className="modal-title">Import Portfolio</h5>
                          </div>
                        </div>

                        <button
                          type="button"
                          className="btn btn-dark close"
                          data-dismiss="modal"
                          aria-label="Close"
                          onClick={closeModal2} // Close modal on button click
                        >
                          <span aria-hidden="true">&times;</span>
                        </button>
                      </div>
                      <div className="modal-body">
                        <div>
                          <div className="d-flex align-items-center"></div>
                          <div className="d-flex flex-column align-items-center mt-3">
                            <h5>
                              Enter the key of the portfolio you want to import
                            </h5>
                          </div>
                          <div className="d-flex flex-column align-items-center mt-3">
                            <input
                              type="String"
                              placeholder="Enter key"
                              className="mb-2"
                              onChange={(e) => setKkey(e.target.value)}
                            />

                            <div className="d-flex flex-column align-items-center mt-3">
                              <div className="d-flex mx-4">
                                <button
                                  type="submit"
                                  className="btn btn-dark mrb-3"
                                  style={{ marginRight: "10px" }}
                                  onClick={() => importKey(kkey, user.id)}
                                >
                                  Import
                                </button>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              )}
              <div className="row">
                {portfolio.map((portfolio) => (
                  <div
                    key={portfolio.id}
                    className="col-md-4 mb-4 d-flex justify-content-center align-items-center"
                  >
                    <Portfolio key={portfolio.id} portfolio={portfolio} />
                  </div>
                ))}
              </div>
            </div>
            <div className="row">
              <Footer></Footer>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};  

export default UserDetails;

import { useEffect, useState } from "react";
import { Dropdown } from "react-bootstrap";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBell } from "@fortawesome/free-solid-svg-icons";

function NavbarAdmin() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);

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
        setUser(admindata);
        setIsLoggedIn(true);
        console.log(user);
      }
    } catch (error) {
      console.error("Error during login:", error);
    }
  };

  useEffect(() => {
    handleLogin();
  }, []);


  return (
    <>
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
            List of Tickets
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
                    to="/admin"
                    style={{
                      color: "#fff",
                    }}
                  >
                    {user.username}
                  </Link>
                ) : (
                  <Link
                    className="nav-link"
                    to="/logina"
                    style={{
                      color: "#fff",
                    }}
                  >
                    Login
                  </Link>
                )}
              </li>


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
                    to="/registera"
                    style={{
                      color: "#fff",
                    }}
                  >
                    Register
                  </Link>
                )}
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </>
  );
}

export default NavbarAdmin;

const logout = async () => {
  localStorage.removeItem("admin");
  console.log("Apagado");
  window.location.replace("http://localhost:5173/");
};

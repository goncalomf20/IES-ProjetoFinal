import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";

function Navbar({ title }) {
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
            {title}
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
                <Link
                  className="nav-link"
                  to="/login"
                  style={{
                    color: "#fff",
                  }}
                >
                  Login
                </Link>
              </li>

              <p></p>
              <p></p>

              <li
                className="nav-item dropdown"
                style={{ paddingRight: "20px", paddingLeft: "20px" }}
              >
                <Link
                  className="nav-link"
                  to="/register"
                  style={{
                    color: "#fff",
                  }}
                >
                  Register
                </Link>
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

import React, { useState } from "react";
import Footer from "./components/Footer";
import Navbar from "./components/Navbar_Register_Login";

const Register = () => {
  const [formData, setFormData] = useState({
    fname: "",
    lname: "",
    age: "",
    username: "",
    email: "",
    password: "",
    balance: 0,
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(JSON.stringify(formData)); // Log the data to the console;

    try {
      const response = await fetch("http://localhost:8080/investor/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const jsonString = await response.text();
        localStorage.setItem("token", jsonString);
        console.log("Registration successful");
        window.location.replace("http://localhost:5173/");
      } else {
        console.error("Registration failed");
      }
    } catch (error) {
      console.error("Error during registration:", error);
    }
  };

  return (
    <>
      <Navbar title={"Register"} />
      <style>
        {`
        body {
          background-color: #969595;
        }
      `}
      </style>
      <div className="container mt-4">
        <h1 className="mb-4">Register</h1>
        <form onSubmit={handleSubmit}>
          <div className="row mb-3">
            <div className="col-md-6 mb-3">
              <label htmlFor="fname" className="form-label">
                First Name
              </label>
              <input
                type="text"
                className="form-control"
                id="fname"
                name="fname"
                value={formData.fname}
                onChange={handleChange}
              />
            </div>
            <div className="col-md-6 mb-3">
              <label htmlFor="lname" className="form-label">
                Last Name
              </label>
              <input
                type="text"
                className="form-control"
                id="lname"
                name="lname"
                value={formData.lname}
                onChange={handleChange}
              />
            </div>
            <div className="col-md-6 mb-3">
              <label htmlFor="age" className="form-label">
                Age
              </label>
              <input
                type="number"
                className="form-control"
                id="age"
                name="age"
                value={formData.age}
                onChange={handleChange}
              />
            </div>
            <div className="col-md-6 mb-3">
              <label htmlFor="username" className="form-label">
                Username
              </label>
              <input
                type="text"
                className="form-control"
                id="username"
                name="username"
                value={formData.username}
                onChange={handleChange}
              />
            </div>
            <div className="col-12 mb-3">
              <label htmlFor="email" className="form-label">
                Email
              </label>
              <input
                type="email"
                className="form-control"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
              />
            </div>
            <div className="col-12 mb-3">
              <label htmlFor="password" className="form-label">
                Password
              </label>
              <input
                type="password"
                className="form-control"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
              />
            </div>
          </div>
          <div className="d-grid gap-2 col-6 mx-auto mb-4">
            <button type="submit" className="btn btn-primary">
              Register
            </button>
          </div>
        </form>
      </div>
    </>
  );
};

export default Register;

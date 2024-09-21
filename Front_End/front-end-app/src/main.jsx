import "@fortawesome/fontawesome-free/css/all.css";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap/dist/js/bootstrap.bundle.js";
import React from "react";
import { createRoot } from "react-dom/client";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import Addmoney from "./Addmoney.jsx";
import App from "./App.jsx";
import Crypto_page from "./Crypto_page.jsx";
import Login from "./Login.jsx";
import PortfolioPage from "./PortfolioPage.jsx";
import Register from "./Register.jsx";
import UsrDetails from "./UsrDetails.jsx";
import Withdraw from "./Withdraw.jsx";
import Admin from "./Admin.jsx";
import RegisterAdmin from "./RegisterAdmin.jsx";
import LoginAdmin from "./LoginAdmin.jsx";


const root = createRoot(document.getElementById("root"));

root.render(
  <React.StrictMode>  
    <Router>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/cryptodetails/:coinId" element={<Crypto_page/>} />
        <Route path="/portfolio/:id" element={<PortfolioPage/>} />
        <Route path="/usrdetails" element={<UsrDetails />} />
        <Route path="/withdraw" element={<Withdraw />} />
        <Route path="/addmoney" element={<Addmoney />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/registera" element={<RegisterAdmin />} />
        <Route path="/logina" element={<LoginAdmin />} />
      </Routes>
    </Router>
  </React.StrictMode>
);
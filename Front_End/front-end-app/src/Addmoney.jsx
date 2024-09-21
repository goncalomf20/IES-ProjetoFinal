
import React, { useEffect, useState } from "react";
import Footer from './components/Footer';
import Navbar from './components/Navbar';

function Addmoney() {
  const [amount, setAmount] = useState('');
  const [cardNumber, setCardNumber] = useState('');
  const [cvv, setCVV] = useState('');
  const [expiryDate, setExpiryDate] = useState('');
  const [userdata, setUserdata] = useState({});  

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
          if (response.status === 404) {
            console.error("Login failed");
          } else {
            console.log("Login successful");
            setUserdata(userdata);
            console.log(userdata.id);
    
          }
        } catch (error) {
          console.error("Error during login:", error);
        }
      };

    handleLogin();
  }, []);

  

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Basic validation
    if (!amount || !cardNumber || !cvv || !expiryDate) {
      alert('Please fill in all fields.');
      return;
    }

    console.log(userdata.id)
    try {
      console.log('http://localhost:8080/investor/addmoney/'+ userdata.id);  
      const response = await fetch('http://localhost:8080/investor/addmoney/' + userdata.id, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: amount
      });
  

      // TODO: Handle the response as needed
      console.log('Response:', response);
      window.location.replace('http://localhost:5173/');

    } catch (error) {
      console.error('Error:', error);
      // TODO: Handle errors
    }
  };

  return (
    <>
      <Navbar />
      <div className="container">
        <div className="row">
          <div className="text-center">
            <h1>Add Money</h1>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="amount">Amount</label>
                <input
                  type="number"
                  className="form-control"
                  id="amount"
                  aria-describedby="emailHelp"
                  placeholder="Enter Amount"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label htmlFor="cardNumber">Card Number</label>
                <input
                  type="number"
                  className="form-control"
                  id="cardNumber"
                  placeholder="Enter Card Number"
                  value={cardNumber}
                  onChange={(e) => setCardNumber(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label htmlFor="cvv">CVV</label>
                <input
                  type="number"
                  className="form-control"
                  id="cvv"
                  placeholder="Enter CVV"
                  value={cvv}
                  onChange={(e) => setCVV(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label htmlFor="expiryDate">Expiry Date</label>
                <input
                  type="date"
                  className="form-control"
                  id="expiryDate"
                  placeholder="Enter Expiry Date"
                  value={expiryDate}
                  onChange={(e) => setExpiryDate(e.target.value)}
                />
              </div>

              <br />
              <br />
              <button type="submit" className="btn btn-primary text-center">
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
      <div style={{ width: '100%', bottom: '0', position: 'fixed' }}>
        <Footer />
      </div>
    </>
  );
}

export default Addmoney;

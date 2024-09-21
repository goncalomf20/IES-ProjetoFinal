import React, { useEffect, useState } from "react";
import Footer from "./components/Footer";
import Navbar from "./components/Navbar";

function Withdraw() {
    const [userdata, setUserdata] = useState({}); 
    const [amount, setAmount] = useState('');
 

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
        if (!amount) {
          alert('Please fill in all fields.');
          return;
        }
        console.log(amount, userdata.balance, typeof(amount), typeof(userdata.balancee))
        if (amount > userdata.balance) {
            alert('Insufficient Balance');
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
            body: -amount
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
        <div className="text-center">
            <h1>Withdraw</h1>
        </div>
        
        <div className="container text-center">
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

                <br />
                <br />
                <button type="submit" className="btn btn-primary text-center">Submit</button>
            </form>
            
        </div>

        <div style={{ width: "100%", bottom: "0", position: "fixed" }}><Footer /></div>
        
        </>
    )   
}

export default Withdraw;
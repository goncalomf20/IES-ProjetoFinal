import React, { useState } from "react";
import { Button, Modal } from 'react-bootstrap';
function Portfolio({ portfolio }) {
  const [showKey, setShowKey] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [selectedCrypto, setSelectedCrypto] = useState([]);
  const [coinData, setCoinData] = useState(null);
  const [portfolioName, setPortfolioName] = useState('');

  const [quantity, setQuantity] = useState(0);
  const [amountToExchange, setAmountToExchange] = useState(0);


  const openModal = (cryptoName, cryptoId,amount) => {
    setSelectedCrypto([cryptoName, cryptoId,amount]);
    setShowModal(true);
    fetchCryptoData(cryptoId);
  };

  const closeModal = () => {
    setSelectedCrypto(null); // Reset the selectedCrypto when the modal is closed
    setShowModal(false);
  };

  const fetchCryptoData = async (cryptoId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/v1/coins/${cryptoId}`
      );
      if (response.ok) {
        const data = await response.json();
        setCoinData(data);
      } else {
        console.error("Failed to fetch coin data");
      }
    } catch (error) {
      console.error("Error fetching coin data:", error);
    }
  };

  
  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await fetch(`http://localhost:8080/portfolio/updateName/${portfolio.portfolioKey}`, {
    method: 'POST',
    headers: {
     'Content-Type': 'application/json',
    },
    body: JSON.stringify({ name: portfolioName }),
    });
   
    if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
    }
   
    const data = await response.json();
    console.log(data);
    window.location.replace("http://localhost:5173/usrdetails")
  };

  const handleBuy = () => {
    // Update alertData based on Buy action, if needed
    
    console.log(portfolio.portfolioKey)
    console.log(selectedCrypto[1])
    console.log(quantity)
    console.log(amountToExchange)
    console.log(coinData.current_price)
    const alertData = { 
      portfolioid: portfolio.portfolioKey,
      coinid: selectedCrypto[1],
      limit_value: amountToExchange,
      amount_to_exange: quantity,
      sell_buy : "buy",
      coinValueWhenAlert : coinData.current_price,
      isDone : false,
      isPossible: false
    }
    PostAlert(alertData);
  };

  const handleSell = () => {
    // Update alertData based on Sell action, if needed
    console.log(portfolio.portfolioKey)
    console.log(selectedCrypto[1])
    console.log(quantity)
    console.log(amountToExchange)
    console.log(coinData.current_price)

    const alertData = { 
      portfolioid: portfolio.portfolioKey,
      coinid: selectedCrypto[1],
      limit_value: amountToExchange,
      amount_to_exange: quantity,
      sell_buy : "sell",
      coinValueWhenAlert : coinData.current_price,
      isDone : false,
      isPossible: false
    }
    PostAlert(alertData);
  };

  const PostAlert = async (alertData) => {

    try {
        const response = await fetch('http://localhost:8080/alerts/getAlert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body:JSON.stringify(alertData) 
            
        });

        if (response.ok) {
            closeModal();
            alert("Alert created successfully");
        } else {
            console.error('Registration failed');
        }
    } catch (error) {
        console.error('Error during registration:', error);
    }
  };
   

  return (
    <div className="card" style={{ minWidth: "400px", minHeight: "400px" }}>
      <div className="card-body">
        <h4 className="card-title">Portfolio</h4>
        <button onClick={() => setShowKey(!showKey)}>
          Toggle Portfolio Key
        </button>
        {showKey && <p>{portfolio.portfolioKey}</p>}
        <p>Investor ID: {portfolio.investorId}</p>
        <h5>Portfolio Name: {portfolio.name}</h5>
        <form onSubmit={handleSubmit}>
        <input
           type="text"
           value={portfolioName}
           onChange={(e) => setPortfolioName(e.target.value)}/>
          <button type="submit">Submit</button>
          </form>
          <h5>Assets:</h5>
          {Object.entries(portfolio.assets).map(([crypto, amount]) => {
          const match = crypto.match(/name=(.*?),/);
          const matchid = crypto.match(/id=(.*?),/);
          const name = match ? match[1] : crypto;
          const id = matchid ? matchid[1] : crypto;
          return (
            <div
              key={crypto}
              className="d-flex justify-content-between align-items-center mb-2 mt-3"
            >
              <p>
                {name}: {amount}
              </p>
              <button
                className="btn btn-dark"
                style={{ fontSize: "13px" }}
                onClick={() => openModal(name, id , amount)}
              >
                Automatic Rebalancing
              </button>
            </div>
          );
        })}
      </div>

      
      
      {showModal && (
        <Modal show={showModal} onHide={closeModal}>
        <Modal.Header closeButton>
          {coinData && (
            <div className="d-flex align-items-center">
              <div>
                <h5 className="modal-title">
                  Automatic Rebalancing for {coinData.name}
                </h5>
              </div>
              <div className="ml-2 mx-3">
                <img
                  src={coinData.image}
                  style={{ width: "50px" }}
                  alt={coinData.name}
                />
              </div>
            </div>
          )}
        </Modal.Header>
        <Modal.Body>
          {coinData && (
            <div>
              <div className="d-flex align-items-center">
                <h5>Atual Price: {coinData.current_price}€</h5>
              </div>
              <div className="d-flex align-items-center">
                <h5>Quantity: {selectedCrypto[2]} units ==== {selectedCrypto[2]*coinData.current_price}€</h5>
              </div>
              <div className="d-flex align-items-center">
                <h6
                  style={{
                    color:
                      coinData.price_change_percentage_24h < 0 ? "red" : "green",
                  }}
                >
                  {coinData.price_change_percentage_24h}%
                </h6>
              </div>
              <div className="d-flex flex-column align-items-center mt-3">
                <h5>Enter the quantity to automatic buy/sell</h5>
              </div>
              <div className="d-flex flex-column align-items-center mt-3">
                <input
                  type="number"
                  placeholder="Enter quantity"
                  className="mb-2"
                  min={0}
                  onChange={(e) => setQuantity(e.target.value)}
                />
                <div className="d-flex flex-column align-items-center mt-3">
                  <h5>Enter the value of crypto to do the action</h5>
                </div>
                <div className="d-flex flex-column align-items-center mt-3">
                  <input
                    type="number"
                    placeholder="Enter value"
                    className="mb-2"
                    min={0}
                    onChange={(e) => setAmountToExchange(e.target.value)}
                  />
                  <div className="d-flex mx-4">
                    <Button
                      className="btn-success mr-3"
                      style={{ marginRight: '10px' }}
                      onClick={handleBuy}
                    >
                      Buy
                    </Button>
                    <Button
                      className="btn-danger mr-3"
                      style={{ marginRight: '10px' }}
                      onClick={handleSell}
                    >
                      Sell
                    </Button>
                  </div>
                </div>
              </div>
            </div>
          )}
        </Modal.Body>
      </Modal>
      )}
    </div>
  );
}

export default Portfolio;


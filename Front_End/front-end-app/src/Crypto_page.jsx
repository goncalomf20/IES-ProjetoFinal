import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "stompjs";
import Footer from "./components/Footer";
import Graph_Complete from "./components/Graph_Complete";
import Navbar from "./components/Navbar";
import { Dropdown } from "react-bootstrap";
import "./Button_active.css";

function Crypto_page() {
  const cryptoId = useParams();
  console.log(cryptoId.coinId);
  const [coinData, setCoinData] = useState(null);
  const [coinData7, setCoinData7] = useState(null);
  const [coin, setCoin] = useState(null);
  const [activeButton, setActiveButton] = useState("1D");
  const [coinsToBuy, setNrcoinsToBuy] = useState("");
  const [userdata, setUserdata] = useState(null); // Added userdata state
  const [allPort, setAllPort] = useState([]); // Added allPort state

  const [selectedPortfolio, setSelectedPortfolio] = useState(null);
  const [activeButtonBuySell, setActiveButtonBuySell] = useState("Buy");
  const [selectedPortfolioName, setSelectedPortfolioName] = useState("");

  const [btcConversion, setBtcConversion] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/v1/coinsArrayTime/${cryptoId.coinId}`
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

    const fetchData7d = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/v1/array7d/${cryptoId.coinId}`
        );
        if (response.ok) {
          const data = await response.json();

          setCoinData7(data["statusJson"]);
        } else {
          console.error("Failed to fetch coin data");
        }
      } catch (error) {
        console.error("Error fetching coin data:", error);
      }
    };

    const fetchCryptoData = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/v1/coins/${cryptoId.coinId}`
        );
        if (response.ok) {
          const data = await response.json();
          //console.log(data);
          setCoin(data);
        } else {
          console.error("Failed to fetch coin data");
        }
      } catch (error) {
        console.error("Error fetching coin data:", error);
      }
    };

    const handleButtonClick = async (type) => {
      console.log(type);
      if (type === "1D") {
        fetchData();
      } else if (type === "7D") {
        fetchData7d();
      }

      setActiveButton(type);
    };

    const handleButtonClickBuySell = async (type) => {
      setActiveButtonBuySell(type);
    };

    const setupWebSocket = () => {
      try {
        if (activeButton === "7D") {
          console.log("WebSocket setup skipped for 7D button");
          return;
        }
        const socket = new SockJS("http://localhost:8080/ws");
        const stompClient = Stomp.over(socket);
        stompClient.connect({}, () => {
          console.log("connected");
          stompClient.subscribe("/topic/array", (message1) => {
            try {
              let array_time_price = JSON.parse(message1.body);

              if (array_time_price[0][0] === cryptoId.coinId) {
                array_time_price = array_time_price.slice(1);
                array_time_price = array_time_price[0];
                console.log("array_time_price !!! ", array_time_price);
                setCoinData(array_time_price);
              }
            } catch (error) {
              console.error("Error parsing WebSocket message:", error);
            }
          });

          console.log("funfou");
        });
      } catch (error) {
        console.error("Error parsing WebSocket message:", error);
      }
    };

    console.log("allPort", allPort);

    //console.log(coinData);

    const handleComprarClick = (inputValue) => {
      const intValue = parseInt(inputValue);
      console.log("--- input2 --- ", inputValue);
      if (intValue <= 0) {
        return;
      }
    };

    const handleLoginAndFetchPortfolios = async () => {
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

          // Fetch portfolios only if userdata.id is available
          if (userdata.id) {
            fetchAllPortfolios(userdata.id);
          }
        }
      } catch (error) {
        console.error("Error during login:", error);
      }
    };

    const fetchAllPortfolios = async (id) => {
      if (id === 0) {
        return;
      }

      try {
        const response = await fetch(
          `http://localhost:8080/portfolio/investor/${id}`
        );
        if (response.ok) {
          const data = await response.json();
          setAllPort(data);
        } else {
          console.error("Failed to fetch portfolio data");
        }
      } catch (error) {
        console.error("Error fetching portfolio data:", error);
      }
    };

    // info do array de 24h
    fetchData();
    //detalhes de uma bitcoin
    fetchCryptoData();

    const cleanupWebSocket = setupWebSocket();

    handleLoginAndFetchPortfolios();

    window.handleButtonClick = handleButtonClick;
    window.handleComprarClick = handleComprarClick;
    window.handleButtonClickBuySell = handleButtonClickBuySell;

    return () => {};
  }, [cryptoId]);

  const handleInsertCoin = async (key, coin, money) => {
    try {
      const response = await fetch(
        `http://localhost:8080/portfolio/insertcoin/${key}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },

          body: JSON.stringify({ coin: coin, money: money }),
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      console.log(data);
      alert(data);
      if (response.ok){
        window.location.replace('http://localhost:5173/');
        }
    } catch (error) {
      console.error(`Error: ${error}`);
    }
  };

  const handleSellCoin = async (key, coin, money) => {
    try {
      const responses = await fetch(
        `http://localhost:8080/portfolio/sellcoin/${key}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },

          body: JSON.stringify({ coin: coin, money: money }),
        }
      );


      const data = await responses.json();
      console.log(data);
      alert(data);
      
      if (responses.ok){
      window.location.replace('http://localhost:5173/');
      }
    } catch (error) {
      console.error(`Error: ${error}`);
    }
  };

  const handlePortfolioSelect = (selectedKey) => {
    const selectedPortfolioObj = allPort.find(
      (portfolio) => portfolio.portfolioKey === selectedKey
    );
    const selectedPortfolio = selectedKey;
    const selectedPortfolioName = selectedPortfolioObj
      ? selectedPortfolioObj.name
      : "";
    setSelectedPortfolio(selectedPortfolio);
    setSelectedPortfolioName(selectedPortfolioName);
  };



  const handleInputChange = (e) => {
    const inputValue = e.target.value;
    setNrcoinsToBuy(inputValue);
    if (coinData && coinData.length > 0 && coin) {
      const exchange_value = coin["current_price"];
  
      // Perform the conversion to BTC
      const btcValue = inputValue / exchange_value;
      setBtcConversion(btcValue.toFixed(8)); // Adjust the number of decimal places as needed
    }
  };

  const ValuesComponent = () => {
    // Check if coinData is not null and has at least one element
    if (coinData && coinData.length > 0 && coin) {
      const lastValue = coinData[coinData.length - 1][1];
      console.log("COIN", coin);
      handleInsertCoin(1, coin, 1);
      return (
        <>
          <p>Current value: {lastValue}</p>
          <p>
            Symbol: {coin["symbol"]} Name: {coin["name"]}{" "}
          </p>
          <p>Image: {coin["image"]}</p>
          <p>
            {coin["current_price"]} {coin["market_cap"]}{" "}
            {coin["market_cap_rank"]} {coin["total_volume"]} {coin["high_24h"]}{" "}
            {coin["low_24h"]} {coin["price_change_24h"]}{" "}
            {coin["price_change_percentage_24h"]}{" "}
            {coin["market_cap_change_24h"]}{" "}
            {coin["market_cap_change_percentage_24h"]} {coin["total_supply"]}{" "}
            {coin["max_supply"]}
          </p>
        </>
      );
    } else {
      // Handle the case where coinData is null or empty
      return <p>No data available</p>;
    }
  };
  const handlePortfolioChange = (selectedKey) => {
    setSelectedPortfolio(selectedKey); // Update the selectedPortfolio state
  };


  return (
    <>
      <Navbar></Navbar>
      <div style={{ padding: "10px", backgroundColor: "#969595" }}>
        {/* <ValuesComponent /> */}
        {coinData && coinData.length > 0 && coin && (
          <>
            <div
              className="row align-items-center"
              style={{ color: "#fff", marginTop: "30px" }}
            >
              <div className="col-md-2 text-center"></div>
              <div className="col-md-8 d-flex align-items-center">
                <img
                  src={coin["image"]}
                  alt={coin["name"]}
                  style={{ marginRight: "10px", width: "50px" }}
                />
                <h1>{coin["name"]} Price</h1>
                <h5 style={{ color: "#e3e1e1" }}>
                  ({coin["symbol"].toUpperCase()})
                </h5>
              </div>
              <div className="col-md-2 text-center"></div>
            </div>
            <div
              className="row align-items-end"
              style={{ color: "#fff", marginTop: "30px" }}
            >
              <div className="col-md-2 text-center"></div>
              <div className="col-md-8 d-flex align-items-end">
                <h3>€ {coin["current_price"]}</h3>
                <h4
                  style={{
                    marginLeft: "20px",
                    color:
                      coin["price_change_percentage_24h"] < 0 ? "red" : "green",
                  }}
                >
                  {coin["price_change_percentage_24h"]}%
                </h4>
              </div>
              <div className="col-md-2 text-center"></div>
            </div>
          </>
        )}

        <div
          className="row align-items-center"
          style={{ color: "#fff", marginTop: "30px" }}
        >
          <div className="col-md-2 text-center"></div>
          <div className="col-md-8 d-flex align-items-center">
            <button
              className={`btn btn-transparent text-dark border-0 ${
                activeButton === "1D" ? "active" : ""
              } mx-2`}
              onClick={() => window.handleButtonClick("1D")}
            >
              1D
            </button>

            <button
              className={`btn btn-transparent text-dark border-0 ${
                activeButton === "7D" ? "active" : ""
              } mx-2`}
              onClick={() => window.handleButtonClick("7D")}
            >
              7D
            </button>
          </div>
          <div className="col-md-2 text-center"></div>
        </div>

        {/* Graph for 1D */}
        {activeButton === "1D" && (
          <div className="row">
            <div className="col-md-2 text-center"></div>
            <div className="col-md-8 text-center">
              <Graph_Complete array={coinData} />
            </div>
            <div className="col-md-2 text-center"></div>
          </div>
        )}

        {/* Graph for 7D */}
        {activeButton === "7D" && (
          <div className="row">
            <div className="col-md-2 text-center"></div>
            <div className="col-md-8 text-center">
              <Graph_Complete array={coinData7} />
            </div>
            <div className="col-md-2 text-center"></div>
          </div>
        )}
        <div className="row" style={{ marginBottom: "50px" }}>
          <div className="col-md-2 text-center"></div>

          <div
            className="col-md-8 text-center"
            style={{
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <h7 style={{ marginRight: "30px" }}>Portfolio </h7>

            <Dropdown
              onSelect={(selectedKey) => handlePortfolioSelect(selectedKey)}
            >
              <Dropdown.Toggle
                style={{ backgroundColor: "rgb(240, 186, 52)" }}
                id="dropdown-basic"
              >
                {selectedPortfolioName
                  ? selectedPortfolioName
                  : "Select Portfolio"}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                {allPort.map((portfolio) => (
                  <Dropdown.Item
                    key={portfolio.portfolioKey}
                    eventKey={portfolio.portfolioKey}
                  >
                    {portfolio.name}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            </Dropdown>
          </div>
          <div className="col-md-2 text-center"></div>
        </div>
        {coinData && coinData.length > 0 && coin && (
          <>
            <div className="row">
              <div className="col-md-2 text-center"></div>
              <div className="col-md-8 text-center">
                <button
                  className={`btn btn-transparent border-0 mx-2 ${
                    activeButtonBuySell === "Buy" ? "active" : ""
                  }`}
                  style={{ color: "#fff", fontSize: "30px" }}
                  onClick={() => window.handleButtonClickBuySell("Buy")}
                >
                  Buy {coin["symbol"].toUpperCase()}
                </button>

                <button
                  className={`btn btn-transparent border-0 mx-2 ${
                    activeButtonBuySell === "Sell" ? "active" : ""
                  }`}
                  style={{ color: "#fff", fontSize: "30px" }}
                  onClick={() => window.handleButtonClickBuySell("Sell")}
                >
                  Sell {coin["symbol"].toUpperCase()}
                </button>
              </div>

              <div className="col-md-2 text-center"></div>
            </div>
            <div className="row">
              <div className="col-md-2 text-center"></div>
              <div className="col-md-8 text-center">
                <div
                  style={{
                    border: "1px solid #ccc",
                    borderRadius: "5px",
                    padding: "10px",
                    width: "400px",
                    margin: "0 auto",
                    backgroundColor: "#696969",
                  }}
                >
                  <div style={{ marginBottom: "10px", color: "#fff" }}>
                    {activeButtonBuySell === "Buy"
                      ? `Buy ${coin["symbol"].toUpperCase()}`
                      : `Sell ${coin["symbol"].toUpperCase()}`}
                    <img
                      src={coin["image"]}
                      style={{ marginLeft: "10px", width: "30px" }}
                    />
                  </div>
                  <div style={{ marginBottom: "10px", color: "#fff" }}>
                    Eur €
                  </div>
                  <input
                    type="number"
                    id="coinsToBuy"
                    style={{
                      backgroundColor: "#696969",
                      color: "#fff",
                      width: "100%",
                      boxSizing: "border-box",
                      border: "none",
                      borderBottom: "1px solid #ccc",
                      outline: "none",
                    }}
                    value={coinsToBuy}
                    //onChange={(e) => setNrcoinsToBuy(e.target.value)}
                    onChange={handleInputChange}
                  />
                  <div style={{ marginTop: "10px", color: "#fff" }}>
                    {`Equivalent in ${coin.symbol.toUpperCase()}: ${btcConversion} ${coin.symbol.toUpperCase()}`}
                  </div>
                </div>
              </div>
                <div className="col-md-2 text-center">
                </div>
            </div>
            <div className="row" style={{ marginBottom: "60px" }}>
              <div className="col-md-2 text-center"></div>
              <div className="col-md-8 text-center">
                <button
                  className="btn1"
                  style={{
                    width: "300px",
                    height: "50px",
                    marginTop: "30px",
                    borderRadius: "5px",
                    border: "1px solid #696969",
                    backgroundColor: "rgb(240,186,52)",
                    color: "#000",
                  }}
                  onClick={
                    () =>
                      activeButtonBuySell === "Buy"
                        ? handleInsertCoin(selectedPortfolio, coin, coinsToBuy)
                        : handleSellCoin(selectedPortfolio, coin, coinsToBuy) //Sell Function
                  }
                >
                  {activeButtonBuySell === "Buy"
                    ? `Buy ${coin["symbol"].toUpperCase()}`
                    : `Sell ${coin["symbol"].toUpperCase()}`}
                </button>
              </div>
              <div className="col-md-2 text-center"></div>
            </div>
          </>
        )}
      </div>
      <Footer></Footer>
    </>
  );
}

export default Crypto_page;
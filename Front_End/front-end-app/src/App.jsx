import { initParticlesEngine } from "@tsparticles/react";
import { loadSlim } from "@tsparticles/slim";
import React, { useEffect, useState } from "react";
import PropagateLoader from "react-spinners/PropagateLoader";
import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "stompjs";
import Card_Crypto from "./components/Card_Crypto";
import Footer from "./components/Footer";
import Navbar from "./components/Navbar";

function App() {
  console.log("App.jsx");
  const [cryptoData, setCryptoData] = useState([]);
  const [cryptoPriceTime, setCryptoPriceTime] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [particlesInitialized, setParticlesInitialized] = useState(false);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const [response, allArray] = await Promise.all([
          fetch("http://localhost:8080/api/v1/coins"),
          fetch("http://localhost:8080/api/v1/allCoinsArrayTime"),
        ]);
        const initialData = await response.json();
        const allArrayData = await allArray.json();

        const sortedCryptoData = initialData.sort((a, b) => {
          return a.market_cap_rank - b.market_cap_rank;
        });
        setCryptoData(sortedCryptoData);

        for (let index = 0; index < allArrayData.length; index++) {
          const dict = allArrayData[index];
          const coinId = dict["coin"];
          // console.log("coinId", coinId);
          const array_time_price = dict["statusJson"];
          //console.log("array_time_price", array_time_price);
          cryptoPriceTime[coinId] = array_time_price;
        }
        //console.log("cryptoPriceTime", cryptoPriceTime);
        setCryptoPriceTime(cryptoPriceTime);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching initial data:", error);
        setIsLoading(false);
      }
    };

    let updatedCryptoData = [];
    let coin_arr = {};

    const setupWebSocket = () => {
      try {
        const socket = new SockJS("http://localhost:8080/ws");
        const stompClient = Stomp.over(socket);

        stompClient.connect(
          {},
          () => {
            //console.log("Connected to WebSocket");
            stompClient.subscribe("/topic/cryptoUpdates", (message) => {
              try {
                updatedCryptoData = JSON.parse(message.body);
                //console.log("Received updated crypto data:", updatedCryptoData);
                const sortedCryptoData = updatedCryptoData.sort((a, b) => {
                  return a.market_cap_rank - b.market_cap_rank;
                });
                setCryptoData(sortedCryptoData);
                // cryptoDataTimeout = setTimeout(() => {
                // }, 600);
              } catch (error) {
                console.error("Error parsing WebSocket message:", error);
              }
            });

            stompClient.subscribe("/topic/array", (message1) => {
              try {
                const array_time_price = JSON.parse(message1.body);

                //console.log("Received array crypto data:", array_time_price);
                const coin = array_time_price[0][0];
                //console.log("coin", coin);
                // take the first element of the array which is the coin name
                // and use it as a key in the coin_arr object

                coin_arr[coin] = array_time_price[1];
                // const sortedCryptoData = updatedCryptoData.sort((a, b) => {
                //   return a.market_cap_rank - b.market_cap_rank;
                // });
                // setCryptoData(sortedCryptoData);
                //console.log("coin_arr", coin_arr);
                setCryptoPriceTime(coin_arr);
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

    fetchInitialData();

    // Setup WebSocket connection
    const cleanupWebSocket = setupWebSocket();

    return () => {
      cleanupWebSocket();
    };
  }, []);

  useEffect(() => {
    initParticlesEngine(async (engine) => {
      // you can initiate the tsParticles instance (engine) here, adding custom shapes or presets
      // this loads the tsparticles package bundle, it's the easiest method for getting everything ready
      // starting from v2 you can add only the features you need reducing the bundle size
      //await loadAll(engine);
      //await loadFull(engine);
      await loadSlim(engine);
      //await loadBasic(engine);
    }).then(() => {
      // setInit(true);
    });
  }, []);

  const particlesLoaded = (container) => {
    console.log(container);
  };

  if (isLoading) {
    return (
      <>
        <div className="container-fluid" style={{ background: "#969595", minHeight: "100vh" }}>
          <div className="row">
            <Navbar />
          </div>
          <div className="row justify-content-center"  style={{ minHeight: "100%" }}>
            <div
              className="col-md-4 text-center"
              style={{ paddingTop: "100px" }}
            >
              <h2>LOADING</h2>
              <PropagateLoader color={"#000"} size={20} />
            </div>
          </div>
          <div
            className="row"
            style={{ width: "100%", bottom: "0", position: "fixed" }}
          >
            <Footer />
          </div>
        </div>
      </>
    );
  } else {
    return (
      <>
        {/* <Particles
            id="tsparticles"
            particlesLoaded={particlesLoaded}
            options={{
                background: {
                    color: {
                        value: "#ffffff",
                    },
                },
                fpsLimit: 120,
                interactivity: {
                    events: {
                        onClick: {
                            enable: true,
                            mode: "push",
                        },
                        onHover: {
                            enable: true,
                            mode: "repulse",
                        },
                        resize: true,
                    },
                    modes: {
                        push: {
                            quantity: 3,
                        },
                        repulse: {
                            distance: 200,
                            duration: 0.4,
                        },
                    },
                },
                particles: {
                    color: {
                        value: "#000000",
                    },
                    move: {
                        direction: "none",
                        enable: true,
                        outModes: {
                            default: "bounce",
                        },
                        random: false,
                        speed: 4,
                        straight: false,
                    },
                    number: {
                        density: {
                            enable: true,
                            area: 800,
                        },
                        value: 50,
                    },
                    opacity: {
                        value: 0.6,
                    },
                    shape: {
                        type: "circle",
                    },
                    size: {
                        value: { min: 1, max: 5 },
                    },
                },
                detectRetina: false,
            }}
        /> */}
        <div className="container-fluid" style={{backgroundColor:'#969595'}}>
          <div className="row">
            <Navbar></Navbar>
          </div>
          <div className="row">
            {cryptoData.map((crypto, index) => (
              <div
                key={index}
                className="col-md-4 mb-4 d-flex justify-content-center align-items-center"
              >
                <Card_Crypto
                  crypto={crypto}
                  array_time_price={cryptoPriceTime[crypto.id]}
                />
              </div>
            ))}
          </div>
          <div className="row">
            <Footer></Footer>
          </div>
        </div>
      </>
    );
  }
}

export default App;

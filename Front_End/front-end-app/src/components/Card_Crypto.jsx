import React, { useState } from "react";
import Graph from "./Graph";
import { Link } from "react-router-dom";


function Card_Crypto({ crypto, array_time_price }) {
  const [isHovered, setIsHovered] = useState(false);
  const color_line = crypto.price_change_percentage_24h < 0 ? "red" : "green";

  return (
   
    <div
      className="card p-3"
      style={{
        margin: "20px",
        height: "330px",
        width: "300px",
        textAlign: "center",
        background: '#242424',
        borderRadius: "20px",
        border: "0px",
        boxShadow: "0px 0px 20px 0px rgba(0,0,0,1)",
      }}
      // onMouseEnter={() => setIsHovered(true)}
    >
      <div className="card-body text-center" style={{width:"100%",color:'#fff'}}>
        <Link to={`/cryptodetails/${crypto.id}`}style={{ textDecoration: 'none' }}>
        <div className="row text-center">
          <div className="col-2">
            <img
              src={crypto.image}
              style={{ width: "50px" }}
              alt="Crypto Image"
            />
          </div>
          <div className="col-10">
            <h4 className="card-title" style={{color:'#fff'}}>{crypto.name}</h4>
          </div>
        </div>

        <h3
          className="card-subtitle mb-2 text-center" style={{ paddingTop: "20px" , color:'#fff'}}
        >
          {crypto.current_price}€
        </h3>
        <h3
          className="card-text text-center"
          style={{
            color: crypto.price_change_percentage_24h < 0 ? "red" : "green",
          }}
        >
          {crypto.price_change_percentage_24h}%
        </h3>
        </Link>
        
      </div>
      
      <div style={{width:"100%",marginTop:"-10px"}}>
          <Graph array={array_time_price} color_line={color_line}/>
      </div>
    </div>
    );
}

export default Card_Crypto;

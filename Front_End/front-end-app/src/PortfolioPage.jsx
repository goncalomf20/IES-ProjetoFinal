import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";

function PortfolioPage() {
 const [portfolio, setPortfolio] = useState(null);
 const portfolioId = useParams();
 console.log(portfolioId.id)
 useEffect(() => {
   fetch(`http://localhost:8080/portfolio/insertcoin/${portfolioId.id}`)
     .then(response => response.json())
     .then(data => setPortfolio(data))
     .catch(error => console.error(error));
 }, [id]);

console.log(response.json())

 return (
   <div>
     {portfolio && (
       <div>
         <h2>{portfolio.portfolioKey}</h2>
         <p>{portfolio.investorId}</p>
         {Object.entries(portfolio.assets).map(([crypto, amount]) => (
           <p key={crypto}>{crypto}: {amount}</p>
         ))}
       </div>
     )}
   </div>
 );
}

export default PortfolioPage;
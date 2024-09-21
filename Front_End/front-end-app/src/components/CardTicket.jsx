import React from "react";
import { useState } from "react";

function CardTicket(props) {
  const { ticket } = props;
  const [reply, setReply] = useState(""); // State to hold the reply

  const getInvestorEmail = async (investorId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/investor/investors/${investorId}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          // If needed, add a request body with any necessary data
        }
      );
      const investor = await response.json();

      // Assuming the response contains the email field
      const email = investor.email;
      return email;
    } catch (error) {
      console.error("Error getting investor:", error);
    }
  };
  const handleSendResponse = async (ticket, reply) => {
    try {
      console.log(ticket);
      const formData = new FormData();
      formData.append("to", await getInvestorEmail(ticket["investorId"]));
      formData.append("subject", "Your Email Subject");
      formData.append("body", reply);
  
      const response = await fetch("http://localhost:8080/mail/send", {
        method: "POST",
        body: formData,
      });
  
      if (response.ok) {
        console.log("Email sent successfully");
  
        // Delete the ticket after successfully sending the email
        await fetch("http://localhost:8080/ticket/del/"+ticket["id"], {
          method: "DELETE",
        });
        alert("Email sent successfully");
        window.location.reload();
  
        // Optionally, update the UI or perform other actions after email is sent
      } else {
        console.error("Failed to send email:", response.statusText);
        // Optionally, handle errors or show a message to the user
      }
    } catch (error) {
      console.error("Error sending email:", error.message);
      // Optionally, handle errors or show a message to the user
    }
  };

  return (
    <div className="card mb-3">
      <div className="card-body">
        <h4 className="card-title border-bottom pb-2">{ticket.title}</h4>
        <p className="card-text">{ticket.description}</p>
        <p className="card-text">
          <strong>Status:</strong> {ticket.status}
        </p>
        <p className="card-text">
          <strong>Date:</strong>{" "}
          {new Date(ticket.date).toLocaleString(undefined, {
            day: "numeric",
            month: "long",
            year: "numeric",
            hour: "numeric",
            minute: "numeric",
          })}
        </p>

        {/* Box for reply */}
        <div className="mb-3">
          <label htmlFor={`replyBox-${ticket.id}`} className="form-label">
            Your Reply:
          </label>
          <textarea
            className="form-control"
            id={`replyBox-${ticket.id}`}
            rows="3"
            onChange={(e) => setReply(e.target.value)}
          ></textarea>
        </div>

        {/* Button to send response */}
        <button
          className="btn btn-primary"
          onClick={() => handleSendResponse(ticket, reply)}
        >
          Send Response
        </button>
      </div>
    </div>
  );
}

export default CardTicket;

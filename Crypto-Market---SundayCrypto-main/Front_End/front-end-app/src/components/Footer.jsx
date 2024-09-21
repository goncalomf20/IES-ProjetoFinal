import { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';

function Footer() {
  const [showModal, setShowModal] = useState(false);
  const [questionTitle, setQuestionTitle] = useState('');
  const [questionText, setQuestionText] = useState('');
  const [user, setUser] = useState(null);

  const handleOpenModal = () => setShowModal(true);
  const handleCloseModal = () => {
    setShowModal(false);
    // Reset the inputs when the modal is closed
    setQuestionTitle('');
    setQuestionText('');
  };

  const handleQuestionTitleChange = (e) => setQuestionTitle(e.target.value);
  const handleQuestionTextChange = (e) => setQuestionText(e.target.value);

  const handleAskQuestion = async () => {
    try {
      // Prepare the data to send to the backend
      const ticketData = {
        investorId: user.id,
        adminId: 1,
        title: questionTitle,
        description: questionText,
        status: 'OPEN',
        date: new Date().toISOString(),
        reply: '',
      };
  
      // Send the ticketData to the backend API
      const response = await fetch('http://localhost:8080/ticket/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(ticketData),
      });
  
      // Check the response status and handle accordingly
      if (response.ok) {
        console.log('Asked question successfully! Wait for the admin to reply.');
      } else {
        console.error('Failed to create ticket:', response.statusText);
      }
    } catch (error) {
      console.error('Error creating ticket:', error.message);
    }
  
    // Reset the inputs and close the modal
    setQuestionTitle('');
    setQuestionText('');
    handleCloseModal();
  };

  const handleLogin = async () => {
    try {
      const responsez = await fetch("http://localhost:8080/investor/token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ token: localStorage.getItem("token") }),
      });
      console.log(JSON.stringify({ token: localStorage.getItem("token") }));
      const userdata = await responsez.json();
      setUser(userdata);
    } catch (error) {
      console.error("Error during login:", error);
    }
  };


  useEffect(() => {
    handleLogin();
  }, []);


  return (
    <>
      <footer
        className="text-center text-lg-start bg-light text-muted"
        style={{
          background: "rgba(0, 0, 0, 0)",
          borderTop: "1px solid #242424",
          maxHeight: "65px",
        }}
      >
        <div className="d-flex justify-content-between align-items-center p-4">
          <div>
            <a className="text-reset fw-bold">
              IES 2023 Projeto
            </a>
          </div>
          <div className="text-right">
            <a
              className="text-reset fw-bold"
              onClick={handleOpenModal}
              style={{ cursor: 'pointer' }}
            >
              Make Question to Admin
            </a>
          </div>
        </div>

        <Modal show={showModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>Make a Question</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group controlId="questionTitleInput">
                <Form.Label>Title:</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter the title of your question"
                  value={questionTitle}
                  onChange={handleQuestionTitleChange}
                />
              </Form.Group>
              <Form.Group controlId="questionTextInput">
                <Form.Label>Your Question:</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={3}
                  placeholder="Enter your question here"
                  value={questionText}
                  onChange={handleQuestionTextChange}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
            <Button variant="primary" onClick={handleAskQuestion}>
              Ask Question
            </Button>
          </Modal.Footer>
        </Modal>
      </footer>
    </>
  );
}

export default Footer;

import React, { useState } from 'react';
import Footer from './components/Footer';
import Navbar from './components/Navbar_Register_Login';

const LoginAdmin = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null); // Reset error state

        console.log(JSON.stringify(formData));
        try {
            const response = await fetch('http://localhost:8080/admin/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                const jsonString = await response.text();
                console.log(jsonString)
                localStorage.setItem('admin', jsonString);
                window.location.replace('http://localhost:5173/admin'); // Redirect to the dashboard or another page after successful login
            } else {
                setError('Invalid credentials. Please try again.');
            }
        } catch (error) {
            console.error('Error during login:', error);
        }
    };

    return (
        <>
            <Navbar title={"Login"} />
            <div className="container mt-4">
                <h1 className="mb-4">Login</h1>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="username" className="form-label">Username</label>
                        <input
                            type="text"
                            className="form-control"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Password</label>
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>
                    {error && (
                        <div className="alert alert-danger" role="alert">
                            {error}
                        </div>
                    )}
                    <div className="d-grid gap-2 col-6 mx-auto mb-4">
                        <button type="submit" className="btn btn-primary">
                            Login
                        </button>
                    </div>
                </form>
            </div>
            <div style={{ width: '100%', position: 'fixed', bottom: '0' }}><Footer /></div>
        </>
    );
};

export default LoginAdmin;

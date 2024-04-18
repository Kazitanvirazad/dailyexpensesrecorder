import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../css/login.css';
import '../css/googlefonts.css';
import bg1 from '../assets/img/bg1.jpg.webp';
import constants from '../utils/constants.json';
import Cookies from 'js-cookie';

let loginData = {};

const LoginPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [rememberMe, setRememberMe] = useState(false);
    const [emailValidationError, setEmailValidationError] = useState(null);
    const [passwordValidationError, setPasswordValidationError] = useState(null);

    const handleChange = (event) => {
        event.preventDefault();
        let name = event.currentTarget.dataset.attrname;
        let value = event.target.value;
        loginData[name] = value;
        setEmailValidationError(null);
        setPasswordValidationError(null);
    };

    const handleRememberMe = (event) => {
        event.preventDefault();
        if (rememberMe === false) {
            setRememberMe(() => true);
        } else {
            setRememberMe(() => false);
        }
    };

    const isValidFormData = (email, password) => {
        if (!email || !email.match(constants.EMAIL_REGEX)) {
            setEmailValidationError("Invalid Email!");
            return false;
        }
        if (!password || !password.match(constants.PASSWORD_REGEX)) {
            setPasswordValidationError("Password must contain at least 8 characters.");
            return false;
        }
        return true;
    };

    const handleOnSubmit = (event) => {
        event.preventDefault();
        if (!isValidFormData(loginData.email, loginData.password)) {
            return;
        }
        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let loginapi = import.meta.env.VITE_API_LOGIN;
        let method = import.meta.env.VITE_API_METHOD_LOGIN;
        fetch(hostname + loginapi, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(loginData)
        }).then(response => {
            return response.json();
        }).then(data => {
            if (data) {
                let token = data.access_token;
                if (rememberMe) {
                    let date = new Date();
                    date.setSeconds(date.getSeconds() + (data.expires_in - 2));
                    Cookies.set(constants.BEARER_TOKEN, token, { expires: date });
                } else {
                    Cookies.set(constants.BEARER_TOKEN, token);
                }
                let lastVisitedPage = location.state ? location.state.page : null;
                lastVisitedPage ? navigate(lastVisitedPage) : navigate("/home");
            }
        }).catch(err => {
            console.log(err);
            alert("Server Error!");
        });
    };

    const handleViewPassword = (event) => {
        event.preventDefault();
        let className = event.target.className;
        if (className && className === "fa fa-fw field-icon toggle-password fa-eye-slash") {
            event.target.className = "fa fa-fw field-icon toggle-password fa-eye";
            const passkey = document.getElementById("passkey");
            passkey.type = "text";
        } else {
            event.target.className += "-slash";
            passkey.type = "password";
        }
    };

    return (
        <>
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"></link>
            <section className="ftco-section">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-md-6 text-center mb-5">
                            <h2 className="heading-section">Daily Expenses Recorder</h2>
                        </div>
                    </div>
                    <div className="row justify-content-center">
                        <div className="col-md-6 text-center mb-2">
                            <h2 className="sub-heading-section">Login To View and Add Entries</h2>
                        </div>
                    </div>
                    <div className="row justify-content-center">
                        <div className="col-md-7 col-lg-5">
                            <div className="wrap">
                                <div className="img" style={{ backgroundImage: `url(${bg1})` }}></div>
                                <div className="login-wrap p-4 p-md-5">
                                    <div className="d-flex">
                                        <div className="w-100">
                                            <h3 className="mb-4">Sign In</h3>
                                        </div>
                                    </div>
                                    <form className="signin-form">
                                        <div className="form-group mt-3">
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="email" data-attrname="email" defaultValue=""
                                                id="email" required={true} />
                                            <label className="form-control-placeholder" htmlFor="email">Email</label>
                                            <div className="error_desc">{emailValidationError ? emailValidationError : null}</div>
                                        </div>
                                        <div className="form-group mt-3" >
                                            <input className="form-control" onChange={handleChange}
                                                type="password" name="passkey" data-attrname="password" defaultValue=""
                                                id="passkey" required={true} />
                                            <label className="form-control-placeholder" htmlFor="passkey">Password</label>
                                            <span onClick={handleViewPassword} className="fa fa-fw field-icon toggle-password fa-eye-slash"></span>
                                            <div className="error_desc">{passwordValidationError ? passwordValidationError : null}</div>
                                        </div>
                                        <div className="form-group">
                                            <button type="submit" className="form-control btn btn-primary rounded submit px-3"
                                                onClick={handleOnSubmit}>Sign In</button>
                                        </div>
                                        <div className="form-group d-md-flex">
                                            <div className="w-50 text-left">
                                                <label className="checkbox-wrap checkbox-primary mb-0">Remember Me
                                                    <input type="checkbox" onChange={handleRememberMe} />
                                                    <span className="checkmark"></span>
                                                </label>
                                            </div>
                                            <div className="w-50 text-md-right">
                                                <a href="#"><span>Forgot Password</span></a>
                                            </div>
                                        </div>
                                    </form>
                                    <p className="text-center">New here? <a data-toggle="tab" href="#signup">Sign Up</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    )
};

export default LoginPage;
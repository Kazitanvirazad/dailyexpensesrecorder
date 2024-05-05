import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../css/signuplogin.css';
import '../css/googlefonts.css';

import bg1 from '../assets/img/bg1.jpg.webp';
import constants from '../utils/constants.json';
import Cookies from 'js-cookie';
import { trimFormData } from '../utils/utilityHelper';

let signupData = {};

const SignupPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [firstnameValidationError, setFirstnameValidationError] = useState(null);
    const [lastNameValidationError, setLastNameValidationError] = useState(null);
    const [phoneValidationError, setPhoneValidationError] = useState(null);
    const [emailValidationError, setEmailValidationError] = useState(null);
    const [passwordValidationError, setPasswordValidationError] = useState(null);
    const [confPasswordValidationError, setConfPasswordValidationError] = useState(null);
    const [confpassword, setConfpassword] = useState(null);

    const handleChange = (event) => {
        event.preventDefault();
        let name = event.currentTarget.dataset.attrname;
        let value = event.target.value;
        signupData[name] = value;
        setEmailValidationError(null);
        setPasswordValidationError(null);
        setFirstnameValidationError(null);
        setLastNameValidationError(null);
        setPhoneValidationError(null);
        setConfPasswordValidationError(null);
    };

    const handleConfPassChange = (event) => {
        event.preventDefault();
        let value = event.target.value;
        setConfpassword(value);
        setEmailValidationError(null);
        setPasswordValidationError(null);
        setFirstnameValidationError(null);
        setLastNameValidationError(null);
        setPhoneValidationError(null);
        setConfPasswordValidationError(null);
    };

    const isValidFormData = (email, password, firstname, lastname, phone) => {
        if (!firstname || !firstname.match(/\S/)) {
            setFirstnameValidationError("Firstname should not be empty.")
            return false;
        }
        if (!lastname || !lastname.match(/\S/)) {
            setLastNameValidationError("Lastname should not be empty.")
            return false;
        }
        if (!phone || !phone.match(constants.PHONE_REGEX)) {
            setPhoneValidationError("Invalid Phone number!");
            return false;
        }
        if (!email || !email.match(constants.EMAIL_REGEX)) {
            setEmailValidationError("Invalid Email!");
            return false;
        }
        if (!password || !password.match(constants.PASSWORD_REGEX)) {
            setPasswordValidationError("Password must contain at least 8 characters.");
            return false;
        }
        if (!confpassword || confpassword !== password) {
            setConfPasswordValidationError("Password confirmation mismatch!");
            return false;
        }
        return true;
    };

    const handleOnSubmit = (event) => {
        event.preventDefault();
        trimFormData(signupData);
        if (!isValidFormData(signupData.email, signupData.password, signupData.firstName, signupData.lastName, signupData.phone)) {
            return;
        }
        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let loginapi = import.meta.env.VITE_API_CREATEUSER;
        let method = import.meta.env.VITE_API_METHOD_CREATEUSER;
        fetch(hostname + loginapi, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(signupData)
        }).then(response => {
            if (response.status == 201) {
                alert("Profile created successfully.");
            } else if (response.status >= 400 && response.status < 500) {
                alert("Insufficient Sign up form data.");
            } else if (response.status > 500) {
                alert("Something went wrong. Try again later.");
            }
            return response.json();
        }).then(data => {
            if (data) {
                let token = data.access_token;
                let date = new Date();
                date.setSeconds(date.getSeconds() + (data.expires_in - 2));
                Cookies.set(constants.BEARER_TOKEN, token, { expires: date });

                let lastVisitedPage = location.state ? location.state.page : null;
                lastVisitedPage ? navigate(lastVisitedPage) : navigate("/home");
            }
        }).catch(err => {
            console.log(err);
            alert("Something went wrong. Try again later.");
        });
    };

    const handleViewPassword = (event) => {
        event.preventDefault();
        let passkey = document.getElementById("passkey");
        let className = event.target.className;
        if (className && className === "fa fa-fw field-icon toggle-password fa-eye-slash") {
            event.target.className = "fa fa-fw field-icon toggle-password fa-eye";
            passkey.type = "text";
        } else {
            event.target.className += "-slash";
            passkey.type = "password";
        }
    };

    const handleViewConfPassword = (event) => {
        event.preventDefault();
        let confPasskey = document.getElementById("confpasskey");
        let className = event.target.className;
        if (className && className === "fa fa-fw field-icon toggle-password fa-eye-slash") {
            event.target.className = "fa fa-fw field-icon toggle-password fa-eye";
            confPasskey.type = "text";
        } else {
            event.target.className += "-slash";
            confPasskey.type = "password";
        }
    };

    const handleSigninNavigatorHandler = (event) => {
        event.preventDefault();
        navigate("/login", { state: { page: "/home" } });
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
                        <div className="col-md-7 col-lg-5">
                            <div className="wrap">
                                <div className="img" style={{ backgroundImage: `url(${bg1})` }}></div>
                                <div className="login-wrap p-4 p-md-5">
                                    <div className="d-flex">
                                        <div className="w-100">
                                            <h3 className="mb-4">Sign Up</h3>
                                        </div>
                                    </div>
                                    <form className="signin-form">
                                        <div className="form-group mt-3">
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="firstname" data-attrname="firstName" defaultValue=""
                                                id="firstname" required={true} />
                                            <label className="form-control-placeholder" htmlFor="firstname">First name</label>
                                            <div className="error_desc">{firstnameValidationError && firstnameValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3">
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="lastname" data-attrname="lastName" defaultValue=""
                                                id="lastname" required={true} />
                                            <label className="form-control-placeholder" htmlFor="lastname">Last name</label>
                                            <div className="error_desc">{lastNameValidationError && lastNameValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3">
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="phone" data-attrname="phone" defaultValue=""
                                                id="phone" required={true} />
                                            <label className="form-control-placeholder" htmlFor="phone">Phone</label>
                                            <div className="error_desc">{phoneValidationError && phoneValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3">
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="bio" data-attrname="bio" defaultValue=""
                                                id="bio" required={true} />
                                            <label className="form-control-placeholder" htmlFor="bio">Bio</label>
                                        </div>
                                        <div className="form-group mt-3">
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="email" data-attrname="email" defaultValue=""
                                                id="email" required={true} />
                                            <label className="form-control-placeholder" htmlFor="email">Email</label>
                                            <div className="error_desc">{emailValidationError && emailValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3" >
                                            <input className="form-control" onChange={handleChange}
                                                type="password" name="passkey" data-attrname="password" defaultValue=""
                                                id="passkey" required={true} />
                                            <label className="form-control-placeholder" htmlFor="passkey">Password</label>
                                            <span onClick={handleViewPassword} className="fa fa-fw field-icon toggle-password fa-eye-slash"></span>
                                            <div className="error_desc">{passwordValidationError && passwordValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3" >
                                            <input className="form-control" onChange={handleConfPassChange}
                                                type="password" name="confpasskey" data-attrname="confpassword" defaultValue=""
                                                id="confpasskey" required={true} />
                                            <label className="form-control-placeholder" htmlFor="confpasskey">Confirm password</label>
                                            <span onClick={handleViewConfPassword} className="fa fa-fw field-icon toggle-password fa-eye-slash"></span>
                                            <div className="error_desc">{confPasswordValidationError && confPasswordValidationError}</div>
                                        </div>
                                        <div className="form-group">
                                            <button type="submit" className="form-control btn btn-primary rounded submit px-3"
                                                onClick={handleOnSubmit}>Register</button>
                                        </div>
                                    </form>
                                    <p className="text-center">Already have an account? <a data-toggle="tab" href="#" onClick={handleSigninNavigatorHandler}>Sign In</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
};

export default SignupPage;
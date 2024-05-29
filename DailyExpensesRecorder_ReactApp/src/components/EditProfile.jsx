import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

import constants from '../utils/constants.json';
import Cookies from 'js-cookie';
import { trimFormData } from '../utils/helpers/validationHelper';

let editProfileData = {};

const EditProfile = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [firstnameValidationError, setFirstnameValidationError] = useState(null);
    const [lastNameValidationError, setLastNameValidationError] = useState(null);
    const [phoneValidationError, setPhoneValidationError] = useState(null);
    const [bioValidationError, setBioValidationError] = useState(null);
    const [avatarIdValidationError, setAvatarIdValidationError] = useState(null);

    const [avatarList, setAvatarList] = useState([]);

    const [selectedAvatar, setSelectedAvatar] = useState(null);

    const [user, setUser] = useState({});

    const [showModal, setShowModal] = useState(false);

    const handleModalClose = () => setShowModal(false);
    const handleModalShow = () => {
        if (avatarList.length == 0) {
            const token = Cookies.get(constants.BEARER_TOKEN);

            if (!token) {
                navigate("/login", { state: { page: "/editprofile" } });
                return;
            }

            let hostname = import.meta.env.VITE_API_HOSTNAME;
            let method = import.meta.env.VITE_API_METHOD_GETAVATAR;
            let allavatarapi = import.meta.env.VITE_API_GETALLAVATAR;

            fetch(hostname + allavatarapi, {
                method: method,
                headers: {
                    "Authorization": "Bearer " + token
                }
            }).then(response => {
                if (response.status == 401) {
                    alert("Login to continue");
                    navigate("/login", { state: { page: "/editprofile" } });
                } else {
                    return response.json();
                }
            }).then(data => {
                if (data && data.data) {
                    setAvatarList(data.data);
                }
            }).catch(err => {
                console.log(err);
                alert("Server Error!");
            });
        }
        setShowModal(true);
    };

    const handleChange = (event) => {
        event.preventDefault();
        let name = event.currentTarget.dataset.attrname;
        let value = event.target.value;
        editProfileData[name] = value ? value : null;
        setFirstnameValidationError(null);
        setLastNameValidationError(null);
        setPhoneValidationError(null);
        setBioValidationError(null);
        setAvatarIdValidationError(null);
    };

    const handleAvatarSelection = (event) => {
        event.preventDefault();
        let avatarid = event.currentTarget.dataset.attrname;
        editProfileData["avatarId"] = avatarid;
        for (let avatar of avatarList) {
            if (avatar.avatarId == avatarid) {
                setSelectedAvatar(avatar.avatarEncodedImage);
                break;
            }
        }
        setShowModal(false);
    };

    const nullifyEmptyAttr = (profileData) => {
        Object.keys(profileData).forEach(key => {
            if (key == "firstName" && profileData[key] != null && profileData[key].length == 0) {
                profileData[key] = null;
            } else if (key == "lastName" && profileData[key] != null && profileData[key].length == 0) {
                profileData[key] = null;
            } else if (key == "phone" && profileData[key] != null && profileData[key].length == 0) {
                profileData[key] = null;
            } else if (key == "bio" && profileData[key] != null && profileData[key].length == 0) {
                profileData[key] = null;
            } else if (key == "avatarId" && profileData[key] != null && profileData[key].length == 0) {
                profileData[key] = null;
            }
        });
    };

    const isValidFormData = (phone, firstname, lastname, bio, avatarId) => {
        if (phone != null && !phone.match(constants.PHONE_REGEX)) {
            setPhoneValidationError("Invalid Phone number!");
            return false;
        }
        if (firstname != null && !firstname.match(/\S/)) {
            setFirstnameValidationError("Name must have atleast one non whitespace character.");
            return false;
        }
        if (lastname != null && !lastname.match(/\S/)) {
            setLastNameValidationError("Name must have atleast one non whitespace character.");
            return false;
        }
        if (bio != null && !bio.match(/\S/)) {
            setBioValidationError("Bio must have atleast one non whitespace character.");
            return false;
        }
        if (avatarId != null && isNaN(avatarId)) {
            setAvatarIdValidationError("Invalid Avatar selection.");
        }
        return true;
    };

    const handleOnSubmit = (event) => {
        event.preventDefault();
        nullifyEmptyAttr(editProfileData);
        trimFormData(editProfileData);
        if (!isValidFormData(editProfileData.phone, editProfileData.firstName, editProfileData.lastName, editProfileData.bio, editProfileData.avatarId)) {
            return;
        }

        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let updateapi = import.meta.env.VITE_API_UPDATEUSER;
        let method = import.meta.env.VITE_API_METHOD_UPDATEUSER;

        const token = Cookies.get(constants.BEARER_TOKEN);

        if (!token) {
            navigate("/login", { state: { page: "/editprofile" } });
            return;
        }

        fetch(hostname + updateapi, {
            method: method,
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(editProfileData)
        }).then(response => {
            if (response.status == 401) {
                alert("Login to continue");
                navigate("/login", { state: { page: "/editprofile" } });
            } else {
                return response.json();
            }
        }).then(data => {
            if (data) {
                alert(data.message);
                let lastVisitedPage = location.state ? location.state.page : null;
                lastVisitedPage ? navigate(lastVisitedPage) : navigate("/home");
            }
        }).catch(err => {
            console.log(err);
            alert("Server Error!");
        });
    };

    useEffect(() => {
        const token = Cookies.get(constants.BEARER_TOKEN);

        if (!token) {
            navigate("/login", { state: { page: "/editprofile" } });
        }

        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let userDetailapi = import.meta.env.VITE_API_USERDETAIL;
        let method = import.meta.env.VITE_API_METHOD_USERDETAIL;

        fetch(hostname + userDetailapi, {
            method: method,
            headers: {
                "Authorization": "Bearer " + token
            }
        }).then(response => {
            if (response.status == 401) {
                alert("Login to continue");
                navigate("/login", { state: { page: "/editprofile" } });
            } else {
                return response.json();
            }
        }).then(data => {
            if (data && data.data) {
                const userData = data.data;
                setUser(userData);
                setSelectedAvatar(data.data.avatarEncodedImage);
                editProfileData["firstName"] = userData.firstName;
                editProfileData["lastName"] = userData.lastName;
                editProfileData["bio"] = userData.bio;
                editProfileData["phone"] = userData.phone;
            }
        }).catch(err => {
            console.log(err);
            alert("Server Error!");
        });
    }, []);

    const handleHomeNavigatorHandler = (event) => {
        event.preventDefault();
        navigate("/home");
    };

    return (
        <>
            <Modal show={showModal} onHide={handleModalClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Select your avatar</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="grid-container">
                        {avatarList.length > 0 && avatarList.map((avatar, index) =>
                            <div onClick={handleAvatarSelection} className="avatar-modal-img" data-attrname={avatar.avatarId}
                                key={index} style={{ backgroundImage: `url(data:image/png;base64,${avatar.avatarEncodedImage})` }}></div>
                        )}
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button className="form-control" size="sm" variant="outline-success" onClick={handleModalClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
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
                                <div className="login-wrap p-4 p-md-5">
                                    <div className="d-flex">
                                        <div className="w-100">
                                            <h3 className="mb-4">Edit profile</h3>
                                        </div>
                                    </div>
                                    <form className="signin-form">
                                        <div className="form-group mt-3">
                                            <input className="form-control" onInput={handleChange}
                                                type="text" name="firstname" data-attrname="firstName"
                                                defaultValue={user.firstName && user.firstName}
                                                id="firstname" required={true} />
                                            <label className="form-control-placeholder" htmlFor="firstname">First name</label>
                                            <div className="error_desc">{firstnameValidationError && firstnameValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3">
                                            <input className="form-control" onInput={handleChange}
                                                type="text" name="lastname" data-attrname="lastName"
                                                defaultValue={user.lastName && user.lastName}
                                                id="lastname" required={true} />
                                            <label className="form-control-placeholder" htmlFor="lastname">Last name</label>
                                            <div className="error_desc">{lastNameValidationError && lastNameValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3">
                                            <input className="form-control" onInput={handleChange}
                                                type="text" name="phone" data-attrname="phone"
                                                defaultValue={user.phone && user.phone}
                                                id="phone" required={true} />
                                            <label className="form-control-placeholder" htmlFor="phone">Phone</label>
                                            <div className="error_desc">{phoneValidationError && phoneValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3">
                                            <input className="form-control" onInput={handleChange}
                                                type="text" name="bio" data-attrname="bio"
                                                defaultValue={user.bio && user.bio}
                                                id="bio" required={true} />
                                            <label className="form-control-placeholder" htmlFor="bio">Bio</label>
                                            <div className="error_desc">{bioValidationError && bioValidationError}</div>
                                        </div>
                                        <div className="form-group mt-3">
                                            <div onClick={handleModalShow} name="avatarId" data-attrname="avatarId" id="avatarId">
                                                <a href="#" className="d-flex flex-column justify-content-center align-items-center">
                                                    <div className="avatar-img" style={{ backgroundImage: `url(data:image/png;base64,${selectedAvatar})` }}></div>
                                                    <span>Choose Avatar</span>
                                                </a>
                                            </div>
                                            <div className="error_desc text-center">{avatarIdValidationError && avatarIdValidationError}</div>

                                        </div>
                                        <div className="form-group">
                                            <button type="submit" className="form-control btn btn-primary rounded submit px-3"
                                                onClick={handleOnSubmit}>Update profile</button>
                                        </div>
                                    </form>
                                    <p className="text-center">Back to <a data-toggle="tab" href="#" onClick={handleHomeNavigatorHandler}>Home</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div >
            </section >
        </>
    );
};

export default EditProfile;
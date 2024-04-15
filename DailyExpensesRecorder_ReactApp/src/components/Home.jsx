import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/homepage.css';
import Cookies from 'js-cookie';
import constants from '../utils/constants.json';

const Home = () => {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [encodedAvatar, setEncodedAvatar] = useState(null);

    useEffect(() => {
        const token = Cookies.get(constants.BEARER_TOKEN);

        if (!token) {
            navigate("/login", { page: "/home" });
        }
        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let loginapi = import.meta.env.VITE_API_USERDETAIL;
        let method = import.meta.env.VITE_API_METHOD_USERDETAIL;

        fetch(hostname + loginapi, {
            method: method,
            headers: {
                "Authorization": "Bearer " + token
            }
        }).then(response => {
            if (response.status == 401) {
                alert("Login to continue");
                navigate("/login", { page: "/home" });
            } else {
                return response.json();
            }
        }).then(data => {
            if (data) {
                setUser(data.data);
                setEncodedAvatar(data.data.avatarEncodedImage);
            } else {
                navigate("/login", { page: "/home" });
            }
        }).catch(err => {
            console.log(err);
            alert("Server Error!");
        })
    }, []);

    return (
        <>
            <section className="ftco-section">
                <div className="container mt-4 mb-4 p-3 d-flex justify-content-center">
                    <div className="card p-4">
                        <div className=" image d-flex flex-column justify-content-center align-items-center"> <button
                            className="btn btn-secondary">  {encodedAvatar && <img src={`data:image/jpeg;base64,${encodedAvatar}`} height="100"
                                width="100" />}</button> <span className="name mt-3">{user && user.firstName + " " + user.lastName}</span> <span
                                    className="idd">@eleanorpena</span>
                            <div className="d-flex flex-row justify-content-center align-items-center gap-2"> <span
                                className="idd1">Oxc4c16a645_b21a</span> <span><i className="fa fa-copy"></i></span> </div>
                            <div className="d-flex flex-row justify-content-center align-items-center mt-3"> <span
                                className="number">1069 <span className="follow">Followers</span></span> </div>
                            <div className=" d-flex mt-2"> <button className="btn1 btn-dark">Edit Profile</button> </div>
                            <div className="text mt-3"> <span>Eleanor Pena is a creator of minimalistic x bold graphics and digital
                                artwork.<br /><br /> Artist/ Creative Director by Day #NFT minting@ with FND night. </span> </div>
                            <div className="gap-3 mt-3 icons d-flex flex-row justify-content-center align-items-center"> <span><i
                                className="fa fa-twitter"></i></span> <span><i className="fa fa-facebook-f"></i></span> <span><i
                                    className="fa fa-instagram"></i></span> <span><i className="fa fa-linkedin"></i></span> </div>
                            <div className=" px-2 rounded mt-4 date "> <span className="join">Joined May,2021</span> </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
};

export default Home;
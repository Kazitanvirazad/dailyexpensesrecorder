import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import Select from "react-select";
import '../css/signuplogin.css';
import '../css/googlefonts.css';
import bg1 from '../assets/img/bg1.jpg.webp';
import rawData from "../utils/rawData.json";
import Cookies from 'js-cookie';
import constants from '../utils/constants.json';
import { trimFormData } from '../utils/utilityHelper';

let addEntryData = {};

const AddEntry = () => {
    const navigate = useNavigate();
    const [yearValidationError, setYearValidationError] = useState(null);
    const [monthValidationError, setMonthValidationError] = useState(null);

    const handleChange = (event) => {
        event.preventDefault();
        let name = event.currentTarget.dataset.attrname;
        let value = event.target.value;
        addEntryData[name] = value ? value : null;
        setYearValidationError(null);
        setMonthValidationError(null);
    };

    const handleMonthSelectChange = (event) => {
        const value = event?.value;
        addEntryData["month"] = value ? value : null;
        setYearValidationError(null);
        setMonthValidationError(null);
    };

    const handleYearSelectChange = (event) => {
        const value = event?.value;
        addEntryData["year"] = value ? value : null;
        setYearValidationError(null);
        setMonthValidationError(null);
    };

    const isValidFormData = (year, month) => {
        if (year == null) {
            setYearValidationError("Select Year to proceed.");
            return false;
        }
        if (year != null && isNaN(year) || !(year >= 1950 && year <= 2099)) {
            setYearValidationError("Invalid Year selection.");
            return false;
        }
        if (month == null) {
            setMonthValidationError("Select Month to proceed.");
            return false;
        }
        if (month != null && !month.match(/\S/)) {
            setMonthValidationError("Invalid Month selection.");
            return false;
        }
        return true;
    };

    const handleOnSubmit = (event) => {
        event.preventDefault();
        trimFormData(addEntryData);
        console.log(addEntryData);
        if (!isValidFormData(addEntryData.year, addEntryData.month)) {
            return;
        }

        const token = Cookies.get(constants.BEARER_TOKEN);

        if (!token) {
            navigate("/login", { state: { page: "/addentry" } });
        }

        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let updateapi = import.meta.env.VITE_API_CREATEENTRY;
        let method = import.meta.env.VITE_API_METHOD_CREATEENTRY;

        fetch(hostname + updateapi, {
            method: method,
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(addEntryData)
        }).then(response => {
            if (response.status == 401) {
                alert("Login to continue");
                navigate("/login", { state: { page: "/addentry" } });
            } else {
                return response.json();
            }
        }).then(data => {
            if (data) {
                alert(data.message);
                navigate("/home");
            }
        }).catch(err => {
            console.log(err);
            alert("Server Error!");
        });
    };

    const handleHomeNavigatorHandler = (event) => {
        event.preventDefault();
        navigate("/home");
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
                                            <h3 className="mb-4">Add Entry</h3>
                                        </div>
                                    </div>
                                    <form className="signin-form" style={{ marginBottom: "80px" }}>
                                        <div className="mb-5">
                                            <Select
                                                className="form-control"
                                                id="month"
                                                name="month"
                                                isSearchable={true}
                                                isClearable={true}
                                                options={rawData.monthOptions}
                                                onChange={handleMonthSelectChange}
                                                isMulti={false}
                                                placeholder={"Select Entry Month"}
                                                required={true}
                                            />
                                            <div className="error_desc">{monthValidationError && monthValidationError}</div>
                                        </div>
                                        <div className="mb-5">
                                            <Select
                                                className="form-control"
                                                id="year"
                                                name="year"
                                                isSearchable={true}
                                                isClearable={true}
                                                options={rawData.yearOptions}
                                                onChange={handleYearSelectChange}
                                                isMulti={false}
                                                placeholder={"Select Year"}
                                                required={true}
                                            />
                                            <div className="error_desc">{yearValidationError && yearValidationError}</div>
                                        </div>
                                        <div className="form-group mb-5">
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="description" data-attrname="description" defaultValue=""
                                                id="description" required={true} />
                                            <label className="form-control-placeholder" htmlFor="description">Add Description</label>
                                        </div>
                                        <div className="form-group">
                                            <button type="submit" className="form-control btn btn-primary rounded submit px-3"
                                                onClick={handleOnSubmit}>Save Entry</button>
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

export default AddEntry;
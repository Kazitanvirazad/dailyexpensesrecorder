import { useNavigate, useLocation } from 'react-router-dom';
import { useState } from 'react';
import Select from "react-select";
import rawData from "../utils/rawData.json";
import Cookies from 'js-cookie';
import constants from '../utils/constants.json';
import { trimFormData, getMonthLastDate, getMonthIndex, isValidMonthName, isValidDateSelection, isValidYear } from '../utils/helpers/validationHelper';
import { getEntryYearSelectionData } from "../utils/helpers/entryHelper";
import backicon2 from "../assets/backicon2.svg";

let addEntryData = {};

const AddEntry = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [yearValidationError, setYearValidationError] = useState(null);
    const [monthValidationError, setMonthValidationError] = useState(null);
    const [dayValidationError, setDayValidationError] = useState(null);
    const [nameValidationError, setNameValidationError] = useState(null);

    const handleChange = (event) => {
        event.preventDefault();
        let name = event.currentTarget.dataset.attrname;
        let value = event.target.value;
        addEntryData[name] = value ? value : null;
        setYearValidationError(null);
        setMonthValidationError(null);
        setDayValidationError(null);
        setNameValidationError(null);
    };

    const handleMonthSelectChange = (event) => {
        const value = event?.value;
        addEntryData["month"] = value ? value : null;
        setYearValidationError(null);
        setMonthValidationError(null);
        setDayValidationError(null);
        setNameValidationError(null);
    };

    const handleYearSelectChange = (event) => {
        const value = event?.value;
        addEntryData["year"] = value ? value : null;
        setYearValidationError(null);
        setMonthValidationError(null);
        setDayValidationError(null);
        setNameValidationError(null);
    };

    const isValidFormData = (year, month, day, entryName) => {
        if (!entryName) {
            setNameValidationError("Enter Name to proceed.");
            return false;
        }
        if (!entryName.match(/\S/)) {
            setNameValidationError("Invalid Name selection.");
            return false;
        }
        if (year == null) {
            setYearValidationError("Select Year to proceed.");
            return false;
        }
        if (!isValidYear(year)) {
            setYearValidationError("Invalid Year selection.");
            return false;
        }
        if (month == null) {
            setMonthValidationError("Select Month to proceed.");
            return false;
        }
        if (!isValidMonthName(month)) {
            setMonthValidationError("Invalid Month selection.");
            return false;
        }
        if (day == null) {
            setDayValidationError("Enter Date to proceed.");
            return false;
        }
        if (isNaN(day) || !(day > 0 && day <= getMonthLastDate(year, getMonthIndex(month)))) {
            setDayValidationError("Invalid Date selection. Date should be between 1st to last date" +
                " of the selected month. Ex: For January -> 1 to 31");
            return false;
        }
        if (!isValidDateSelection(year, month, day)) {
            setYearValidationError("Entry Year must be equal or before the present date.");
            setMonthValidationError("Entry Month must be equal or before the present date.");
            setDayValidationError("Entry Date must be equal or before the present date.");
            return false;
        }
        return true;
    };

    const handleOnSubmit = (event) => {
        event.preventDefault();
        trimFormData(addEntryData);
        if (!isValidFormData(addEntryData.year, addEntryData.month, addEntryData.day, addEntryData.entryName)) {
            return;
        }

        const token = Cookies.get(constants.BEARER_TOKEN);

        if (!token) {
            navigate("/login", { state: { page: "/addentry" } });
            return;
        }

        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let createapi = import.meta.env.VITE_API_CREATEENTRY;
        let method = import.meta.env.VITE_API_METHOD_CREATEENTRY;

        fetch(hostname + createapi, {
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
                let year = location.state ? location.state.year : null;
                let month = location.state ? location.state.month : null;
                if (lastVisitedPage && year && month)
                    navigate("/login", { state: { page: "/addentry", year: year, month: month } });
                else if (lastVisitedPage && year)
                    navigate("/login", { state: { page: "/addentry", year: year } });
                else
                    navigate("/login", { state: { page: "/addentry" } });
            } else {
                return response.json();
            }
        }).then(data => {
            if (data) {
                alert(data.message);
                let year = data.data && data.data.year ? data.data.year : null;
                let month = data.data && data.data.month ? data.data.month : null;
                if (year && month)
                    navigate("/allentries", { state: { year: year, month: month } });
                else
                    navigate("/entrylistbyyear");
            }
        }).catch(err => {
            console.log(err);
            alert("Server Error!");
        });
    };

    const handleBackButtonNavigatorHandler = (event) => {
        event.preventDefault();
        let lastVisitedPage = location.state ? location.state.page : null;
        let year = location.state ? location.state.year : null;
        let month = location.state ? location.state.month : null;

        if (lastVisitedPage && year && month)
            navigate(lastVisitedPage, { state: { year: year, month: month } });
        else if (lastVisitedPage && year)
            navigate(lastVisitedPage, { state: { year: year } });
        else
            navigate("/entrylistbyyear");
    };

    const handleHomeNavigatorHandler = (event) => {
        event.preventDefault();
        navigate("/home");
    };

    return (
        <>
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
                                        <div className="w-100" style={{ textAlign: "center" }}>
                                            <h3 className="mb-4">Add Entry</h3>
                                            <a href="#" onClick={handleBackButtonNavigatorHandler}
                                                className="text-right position-absolute top-0 start-0" style={{
                                                    marginTop: "35px",
                                                    marginLeft: "25px"
                                                }}><img style={{ height: "30px" }}
                                                    src={backicon2} alt="Back Button" /></a>
                                        </div>
                                    </div>
                                    <form className="signin-form" style={{ marginBottom: "80px" }}>
                                        <div className="form-group mb-5" style={{ marginTop: "50px" }}>
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="entryName" data-attrname="entryName" defaultValue=""
                                                id="entryName" required={true} />
                                            <label className="form-control-placeholder" htmlFor="entryName">Enter Name</label>
                                            <div className="error_desc">{nameValidationError && nameValidationError}</div>
                                        </div>
                                        <div className="mb-5" style={{ marginTop: "50px" }}>
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
                                        <div className="mb-5" style={{ marginTop: "50px" }}>
                                            <Select
                                                className="form-control"
                                                id="year"
                                                name="year"
                                                isSearchable={true}
                                                isClearable={true}
                                                options={getEntryYearSelectionData()}
                                                onChange={handleYearSelectChange}
                                                isMulti={false}
                                                placeholder={"Select Year"}
                                                required={true}
                                            />
                                            <div className="error_desc">{yearValidationError && yearValidationError}</div>
                                        </div>
                                        <div className="form-group mb-5" style={{ marginTop: "50px" }}>
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="description" data-attrname="day" defaultValue=""
                                                id="day" required={true} />
                                            <label className="form-control-placeholder" htmlFor="day">Enter Date</label>
                                            <div className="error_desc">{dayValidationError && dayValidationError}</div>
                                        </div>
                                        <div className="form-group mb-5" style={{ marginTop: "50px" }}>
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="description" data-attrname="description" defaultValue=""
                                                id="description" required={true} />
                                            <label className="form-control-placeholder" htmlFor="description">Add Description</label>
                                        </div>
                                        <div className="form-group" style={{ marginTop: "80px" }}>
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
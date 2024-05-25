import { useNavigate, useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Cookies from 'js-cookie';
import constants from '../utils/constants.json';
import { trimFormData, getMonthLastDate, getMonthIndex, isValidMonthName, isValidDateSelection, isValidYear } from '../utils/helpers/validationHelper';
import { getEntryMaxEligibleYearSelection } from "../utils/helpers/entryHelper";
import backicon2 from "../assets/backicon2.svg";

let modifyEntryData = {};

const ModifyEntry = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [yearValidationError, setYearValidationError] = useState(null);
    const [monthValidationError, setMonthValidationError] = useState(null);
    const [dayValidationError, setDayValidationError] = useState(null);
    const [nameValidationError, setNameValidationError] = useState(null);

    const [entry, setEntry] = useState({});

    const entryId = location.state ? location.state.entry_id : null;
    const year = location.state ? location.state.year : null;
    const month = location.state ? location.state.month : null;

    useEffect(() => {

        if (!entryId) {
            navigate("/allentries", { state: { year: year, month: month } });
            return;
        }

        if (!year || !month) {
            navigate("/entrylistbyyear");
            return;
        }

        const token = Cookies.get(constants.BEARER_TOKEN);

        if (!token) {
            navigate("/login", { state: { page: "/modifyentry", year: year, month: month, entry_id: entryId } });
            return;
        }

        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let fetchEntryApiUrl = import.meta.env.VITE_API_GETENTRYBYID;
        let method = import.meta.env.VITE_API_METHOD_GETENTRYBYID;

        fetchEntryApiUrl += `?entry_id=${entryId}`;

        fetch(hostname + fetchEntryApiUrl, {
            method: method,
            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }).then(response => {
            if (response.status == 401) {
                alert("Login to continue");
                navigate("/login", { state: { page: "/modifyentry", year: year, month: month, entry_id: entryId } });
            } else {
                return response.json();
            }
        }).then(data => {
            if (data && data.data) {
                const fetchedEntry = data.data;
                setEntry(fetchedEntry);
                modifyEntryData["entry_id"] = fetchedEntry.entryId;
                modifyEntryData["month"] = fetchedEntry.month;
                modifyEntryData["year"] = fetchedEntry.year;
                modifyEntryData["day"] = fetchedEntry.day;
                modifyEntryData["entryName"] = fetchedEntry.entryName;
                if (fetchedEntry.desc)
                    modifyEntryData["description"] = fetchedEntry.desc;
            }
        }).catch(err => {
            console.log(err);
            alert("Server Error!");
        });
    }, []);

    const handleChange = (event) => {
        event.preventDefault();
        let name = event.currentTarget.dataset.attrname;
        let value = event.target.value;
        modifyEntryData[name] = value ? value : null;
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
        if (month == null) {
            setMonthValidationError("Select Month to proceed.");
            return false;
        }
        if (!isValidMonthName(month)) {
            setMonthValidationError("Invalid Month selection.");
            return false;
        }
        if (year == null) {
            setYearValidationError("Select Year to proceed.");
            return false;
        }
        if (!isValidYear(year)) {
            setYearValidationError("Year selection should be from " + constants.ENTRY_MIN_YEAR +
                " to present year " + getEntryMaxEligibleYearSelection());
            return false;
        }
        if (day == null) {
            setDayValidationError("Select Date to proceed.");
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

        trimFormData(modifyEntryData);

        if (!isValidFormData(modifyEntryData.year, modifyEntryData.month, modifyEntryData.day, modifyEntryData.entryName)) {
            return;
        }

        const token = Cookies.get(constants.BEARER_TOKEN);

        if (!token) {
            navigate("/login", { state: { page: "/modifyentry", year: year, month: month, entry_id: entryId } });
            return;
        }

        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let updateapi = import.meta.env.VITE_API_UPDATEENTRY;
        let method = import.meta.env.VITE_API_METHOD_UPDATEENTRY;

        fetch(hostname + updateapi, {
            method: method,
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(modifyEntryData)
        }).then(response => {
            if (response.status == 401) {
                alert("Login to continue");
                navigate("/login", { state: { page: "/modifyentry", year: year, month: month, entry_id: entryId } });
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
        navigate("/allentries", { state: { year: year, month: month } });
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
                                            <h3 className="mb-4">Update Entry</h3>
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
                                                type="text" name="entryName" data-attrname="entryName" defaultValue={entry && entry.entryName}
                                                id="entryName" required={true} />
                                            <label className="form-control-placeholder" htmlFor="entryName">Enter Name</label>
                                            <div className="error_desc">{nameValidationError && nameValidationError}</div>
                                        </div>
                                        <div className="form-group mb-5" style={{ marginTop: "50px" }}>
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="month" data-attrname="month" defaultValue={entry && entry.month} id="month" required={true} />
                                            <label className="form-control-placeholder" htmlFor="month">Add Month</label>
                                            <div className="error_desc">{monthValidationError && monthValidationError}</div>
                                        </div>
                                        <div className="form-group mb-5" style={{ marginTop: "50px" }}>
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="year" data-attrname="year" defaultValue={entry && entry.year}
                                                id="year" required={true} />
                                            <label className="form-control-placeholder" htmlFor="year">Add Year</label>
                                            <div className="error_desc">{yearValidationError && yearValidationError}</div>
                                        </div>
                                        <div className="form-group mb-5" style={{ marginTop: "50px" }}>
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="description" data-attrname="day" defaultValue={entry && entry.day}
                                                id="day" required={true} />
                                            <label className="form-control-placeholder" htmlFor="day">Enter Date</label>
                                            <div className="error_desc">{dayValidationError && dayValidationError}</div>
                                        </div>
                                        <div className="form-group mb-5" style={{ marginTop: "50px" }}>
                                            <input className="form-control" onChange={handleChange}
                                                type="text" name="description" data-attrname="description" defaultValue={entry && entry.desc} id="description" required={true} />
                                            <label className="form-control-placeholder" htmlFor="description">Add Description</label>
                                        </div>
                                        <div className="form-group" style={{ marginTop: "80px" }}>
                                            <button type="submit" className="form-control btn btn-primary rounded submit px-3"
                                                data-entryid={entry && entry.entryId} onClick={handleOnSubmit}>Update</button>
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

export default ModifyEntry;
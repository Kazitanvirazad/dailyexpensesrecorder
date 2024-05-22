import { useNavigate, useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Cookies from 'js-cookie';
import backicon2 from "../assets/backicon2.svg";
import addicon from "../assets/addicon.svg";
import constants from '../utils/constants.json';

const EntryListByMonth = () => {

    const navigate = useNavigate();
    const location = useLocation();

    const [entries, setEntries] = useState([]);

    const handleAddEntryNavigatorHandler = (event) => {
        event.preventDefault();
        let year = location.state ? location.state.year : null;
        if (year) {
            navigate("/addentry", { state: { page: "/entrylistbymonth", year: year } });
            return;
        }
        navigate("/addentry");
    };

    const handleHomeNavigatorHandler = (event) => {
        event.preventDefault();
        navigate("/home");
    };

    const handleEntryYearNavigatorHandler = (event) => {
        event.preventDefault();
        navigate("/entrylistbyyear");
    };

    const handleViewEntriesHandler = (event) => {
        event.preventDefault();
        let year = event.currentTarget.dataset.year;
        let month = event.currentTarget.dataset.month;
        navigate("/allentries", { state: { year: year, month: month } });
    };

    useEffect(() => {
        let year = location.state ? location.state.year : null;
        if (!year) {
            navigate("/entrylistbyyear");
            return;
        }

        const token = Cookies.get(constants.BEARER_TOKEN);

        if (!token) {
            navigate("/login", { state: { page: "/entrylistbymonth", year: year } });
            return;
        }

        let hostname = import.meta.env.VITE_API_HOSTNAME;
        let getallentrybymonthapi = import.meta.env.VITE_API_GETALLENTRYBYMONTH;
        let method = import.meta.env.VITE_API_METHOD_GETALLENTRYBYMONTH;

        let queryParam = "?year=" + year;

        fetch(hostname + getallentrybymonthapi + queryParam, {
            method: method,
            headers: {
                "Authorization": "Bearer " + token
            }
        }).then(response => {
            if (response.status == 401) {
                alert("Login to continue");
                navigate("/login", { state: { page: "/entrylistbymonth", year: year } });
            } else {
                return response.json();
            }
        }).then(data => {
            if (data && data.data) {
                setEntries(data.data);
            }
        }).catch(err => {
            console.log(err);
            alert("Server Error!");
        });
    }, []);

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
                                            <h3 className="mb-4">{entries && entries.length > 0 ? "Monthly Entries" : "Monthly Entry"}</h3>
                                            <a href="#" onClick={handleEntryYearNavigatorHandler}
                                                className="text-right position-absolute top-0 start-0" style={{
                                                    marginTop: "35px",
                                                    marginLeft: "25px"
                                                }}><img style={{ height: "30px" }}
                                                    src={backicon2} alt="Back Button" /></a>
                                            <a href="#" onClick={handleAddEntryNavigatorHandler}
                                                className="text-right position-absolute top-0 end-0" style={{
                                                    marginTop: "35px",
                                                    marginRight: "25px"
                                                }}><img style={{ height: "30px" }}
                                                    src={addicon} alt="Add New Entry" /></a>
                                        </div>
                                    </div>
                                    {entries && entries.length > 0 ?
                                        entries.map((entry, index) => {
                                            return <div key={index} className="card" style={{ width: 'auto', marginBottom: '10px' }}>
                                                <div className="card-body">
                                                    <h5 className="card-title" style={{ textAlign: "center" }}>
                                                        {entry.year && entry.year && entry.month && `${entry.month} ${entry.year}`}</h5>
                                                    {entry.entryCount != null &&
                                                        <p className="card-text"
                                                            style={{ fontSize: ".9rem", marginBottom: "5px", marginTop: "5px" }}>
                                                            Total Entries: {entry.entryCount}</p>}
                                                    {entry.itemCount != null &&
                                                        <p className="card-text"
                                                            style={{ fontSize: ".9rem", marginBottom: "5px", marginTop: "5px" }}>
                                                            Total items: {entry.itemCount}</p>}
                                                    <div data-year={entry.year && entry.year}
                                                        data-month={entry.month && entry.month}
                                                        onClick={handleViewEntriesHandler} className="d-flex"
                                                        style={{ textAlign: 'center' }} >
                                                        <a className="form-control btn btn-primary" style={{ margin: "5px", fontSize: "15px", textAlign: 'center' }}>View Entries</a>
                                                    </div>
                                                </div>
                                            </div>
                                        }) : <div>No Entries. Click on the + icon to add an Entry</div>}
                                </div>
                                <p className="text-center">Back to <a data-toggle="tab" href="#" onClick={handleHomeNavigatorHandler}>Home</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
};

export default EntryListByMonth;
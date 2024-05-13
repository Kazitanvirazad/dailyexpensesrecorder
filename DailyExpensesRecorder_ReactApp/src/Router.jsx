import Home from "./components/Home";
import LoginPage from "./components/LoginPage";
import SignupPage from "./components/SignupPage";
import EditProfile from "./components/EditProfile";
import AddEntry from "./components/AddEntry";
import EntryList from "./components/EntryList";
import EntryListByYear from "./components/EntryListByYear";

import {
    createBrowserRouter,
    createRoutesFromElements,
    Route,
    RouterProvider
} from "react-router-dom";

const Routers = () => {
    const router = createBrowserRouter(
        createRoutesFromElements(
            <>
                <Route path="/" element={<Home />} />
                <Route path="/*" element={<Home />} />
                <Route path="/home" element={<Home />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/editprofile" element={<EditProfile />} />
                <Route path="/addentry" element={<AddEntry />} />
                <Route path="/entrylistbyyear" element={<EntryListByYear />} />
                <Route path="/allentries" element={<EntryList />} />
            </>
        )
    )

    return (
        <>
            <RouterProvider router={router} />
        </>
    );
};

export default Routers;
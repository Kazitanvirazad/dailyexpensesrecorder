import Home from "./components/Home";
import LoginPage from "./components/LoginPage";

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
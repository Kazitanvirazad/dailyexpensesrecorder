"use strict";

import constants from "../constants.json";

export const getEntryYearSelectionData = () => {
    let currentDate = new Date();
    let currentYear = currentDate.getFullYear();
    let yearSelectionData = [];
    for (let i = currentYear; i >= constants.ENTRY_MIN_YEAR; i--) {
        yearSelectionData.push({ value: i, label: i });
    }
    return yearSelectionData;
};
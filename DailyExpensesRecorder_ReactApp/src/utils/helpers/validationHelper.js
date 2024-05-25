"use strict";

import constants from "../constants.json";
import { getEntryMaxEligibleYearSelection } from "./entryHelper.js";

export const trimFormData = (formData) => {
    Object.keys(formData).forEach(key => {
        if (formData[key] != null && typeof formData[key] == 'string') {
            formData[key] = formData[key].trim();
        }
    });
};

export const getMonthLastDate = (year, month) => {
    if (month > 12 || month < 1) {
        return -1;
    }
    if (month == 2) {
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        } else if (year % 4 == 0) {
            return 29;
        } else {
            return 28;
        }
    }
    if (month < 8 && (month % 2 != 0)) {
        return 31;
    } else if (month > 7 && (month % 2 == 0)) {
        return 31;
    } else {
        return 30;
    }
};

export const getMonthIndex = (monthName) => {
    if (!monthName || monthName.length < 3) {
        return 0;
    }
    switch (monthName.toLowerCase().substring(0, 3)) {
        case "jan": return 1;
        case "feb": return 2;
        case "mar": return 3;
        case "apr": return 4;
        case "may": return 5;
        case "jun": return 6;
        case "jul": return 7;
        case "aug": return 8;
        case "sep": return 9;
        case "oct": return 10;
        case "nov": return 11;
        case "dec": return 12;
        default: return 0;
    };
};

export const isValidDateSelection = (year, month, day) => {
    if (!isValidMonthName(month)) {
        return false;
    }
    let inputDateString = year + "-" + getMonthIndex(month) + "-" + day;
    let inputDateMillis = Date.parse(inputDateString);
    let inputDate = new Date(inputDateMillis);
    return inputDate == new Date() || inputDate < new Date();
};

export const isValidMonthName = (monthName) => {
    const monthSet = new Set(["january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", "jan", "feb", "mar", "apr", "jun", "jul", "aug", "sep", "oct", "nov", "dec"]);
    return monthName != null && monthName.length >= 3 && monthSet.has(monthName.toLowerCase()) && getMonthIndex(monthName) > 0;
};

export const isValidYear = (year) => {
    return !isNaN(year) && (year >= constants.ENTRY_MIN_YEAR && year <= getEntryMaxEligibleYearSelection());
};
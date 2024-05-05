"use strict";

export const trimFormData = (formData) => {
    Object.keys(formData).forEach(key => {
        if (formData[key] != null && typeof formData[key] == 'string') {
            formData[key] = formData[key].trim();
        }
    });
};
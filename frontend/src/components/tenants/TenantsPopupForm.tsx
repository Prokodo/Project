"use client"

import React, { useState } from "react";
import PropertiesForm from "@/components/properties/PropertiesForm";
import TenantsRegistrationForm from "@/components/tenants/TenantsRegistrationForm";

const TenantsPopupForm = () => {
    const [isOpen, setIsOpen] = useState(false);

    const togglePopup = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div>
            <button onClick={togglePopup}
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring focus:ring-blue-300">
                Add new tenant
            </button>

            {isOpen && (
                <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-lg w-full max-w-2xl p-6 relative">
                        <button onClick={togglePopup} className="absolute top-3 right-3 text-gray-500 hover:text-gray-700 focus:outline-none">
                            ✖
                        </button>
                        <h2 className="text-2xl font-bold text-center mb-6">Add new tenant</h2>
                        <TenantsRegistrationForm setIsOpen={setIsOpen} />
                    </div>
                </div>
            )}
        </div>
    );
};

export default TenantsPopupForm;
"use client"

import {getCookie} from "@/utils/cookies";
import React, {JSX, useState} from "react";
import {useProperties} from "@/components/properties/PropertiesContext";
import {Property, Request} from "@/types/types";
import RequestsForm from "@/components/requests/RequestsForm";

const PropertiesList = (): JSX.Element => {
    const { properties } = useProperties();

    const [isRequestFormOpen, setIsRequestFormOpen] = useState(false);
    const [requests, setRequests] = useState<Record<number, Request[]>>({});
    const [expandedPropertyId, setExpandedPropertyId] = useState<number | null>(null);
    const [propertyForRequest, setPropertyForRequest] = useState<Property | null>(null);

    const toggleProperty = async (propertyId: number) => {
        if (expandedPropertyId === propertyId) {
            setExpandedPropertyId(null);
            return;
        }

        if (!requests[propertyId]) {
            try {
                const url: string = `http://localhost:8080/api/requests/properties/${propertyId}`;
                const authToken: string = getCookie("authToken") || "";
                const response = await fetch(url, {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                    cache: "no-store",
                });

                const fetchedRequests: Request[] = await response.json();
                setRequests((prev) => ({ ...prev, [propertyId]: fetchedRequests }));
            } catch (error) {
                console.error("Failed to fetch requests:", error);
                setRequests((prev) => ({ ...prev, [propertyId]: [] })); // Handle error gracefully
            }
        }

        setExpandedPropertyId(propertyId);
    };

    const openRequestForm = (property: Property) => {
        setPropertyForRequest(property);
        setIsRequestFormOpen(true);
    };

    return (
        <div>
        <div className="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Properties List</h1>

            <div className="space-y-4">
                {properties.map((property) => (
                    <div key={property.id} className="border border-gray-300 rounded-md shadow-sm">
                        <div className="p-4 flex justify-between items-center cursor-pointer"
                             onClick={() => toggleProperty(property.id)}>
                            <div>
                                <h3 className="text-lg font-semibold">{property.name}</h3>
                                <p>{property.address}</p>
                            </div>

                            <button className="text-gray-500">
                                {expandedPropertyId === property.id ? "Hide" : "Show"} Requests
                            </button>

                            <button className="bg-blue-500 text-white px-3 py-2 rounded" onClick={() => openRequestForm(property)}>
                                Add Request
                            </button>
                        </div>

                        {expandedPropertyId === property.id && (
                            <div className="p-4 bg-gray-50">
                                {requests[property.id] && requests[property.id].length > 0 ? (
                                    <ul className="list-disc list-inside">
                                        {requests[property.id].map((request) => (
                                            <li key={request.id} className="flex items-center space-x-2">
                                                <span title={request.status} className={`w-3 h-3 rounded-full ${
                                                      request.status === "REQUESTED" ? "bg-yellow-500" :
                                                      request.status === "IN_PROGRESS" ? "bg-blue-500" : 
                                                      request.status === "COMPLETED" ? "bg-green-500" : "bg-red-500"
                                                }`} />

                                                <span>
                                                    <strong>
                                                        {request.requestDate}
                                                    </strong>
                                                    :
                                                    {request.description}
                                                </span>
                                            </li>
                                        ))}
                                    </ul>
                                ) : (
                                    <p className="text-gray-600">
                                        No requests found for this property.
                                    </p>
                                )}
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>

            {isRequestFormOpen && propertyForRequest && (
                <div className="fixed inset-0  z-10 bg-gray-800 bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded shadow-md w-96">
                    <RequestsForm property={propertyForRequest}
                                  setIsOpen={setIsRequestFormOpen} />
                </div>
            </div>
        )}
        </div>
    );
};

export default PropertiesList;

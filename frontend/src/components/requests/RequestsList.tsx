"use client"

import {getCookie} from "@/utils/cookies";
import {validRoles} from "@/services/global";
import {Property, Request} from "@/types/types";
import React, {ChangeEvent, JSX, useState} from "react";
import RequestsForm from "@/components/requests/RequestsForm";
import {useProperties} from "@/components/properties/PropertiesContext";

const RequestsList = ({ roles=[] }: { roles: validRoles[] }): JSX.Element => {
    const { properties } = useProperties();

    const isAdmin: boolean = roles.includes("ROLE_ADMIN");
    const [isRequestFormOpen, setIsRequestFormOpen] = useState(false);
    const [filterStatus, setFilterStatus] = useState<string | null>(null);
    const [requests, setRequests] = useState<Record<number, Request[]>>({});
    const [expandedPropertyId, setExpandedPropertyId] = useState<number | null>(null);
    const [propertyForRequest, setPropertyForRequest] = useState<Property | null>(null);

    const openRequestForm = (property: Property) => {
        setPropertyForRequest(property);
        setIsRequestFormOpen(true);
    };

    const filteredRequests = (propertyId: number) => {
        if (!filterStatus) return requests[propertyId] || [];
        return (requests[propertyId] || []).filter((request) => request.status === filterStatus);
    };

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

    const updateRequestStatus = async (propertyId: number, requestId: number, newStatus: string) => {
        try {
            const url: string = `http://localhost:8080/api/requests/status/${requestId}`;
            const authToken: string = getCookie("authToken") || "";
            await fetch(url, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                },
                body: JSON.stringify({
                    status: newStatus
                }),
            });

            setRequests((prev: any): any => {
                const updatedRequests = prev[propertyId].map((request: any): any =>
                    request.id === requestId ? { ...request, status: newStatus } : request
                );
                return { ...prev, [propertyId]: updatedRequests };
            });
        } catch (error) {
            console.error("Failed to update request status:", error);
        }
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
                                <div className="flex space-x-2 mb-4">
                                    <button onClick={(): void => setFilterStatus(null)}
                                            className={`px-3 py-2 rounded ${filterStatus === null ? "bg-gray-300" : "bg-gray-100"}`}>
                                        All
                                    </button>

                                    <button onClick={(): void => setFilterStatus("REQUESTED")}
                                            className={`px-3 py-2 rounded ${filterStatus === "REQUESTED" ? "bg-yellow-500 text-white" : "bg-gray-100"}`}>
                                        Requested
                                    </button>

                                    <button onClick={(): void => setFilterStatus("IN_PROGRESS")}
                                            className={`px-3 py-2 rounded ${filterStatus === "IN_PROGRESS" ? "bg-blue-500 text-white" : "bg-gray-100"}`}>
                                        In Progress
                                    </button>

                                    <button onClick={() => setFilterStatus("COMPLETED")}
                                            className={`px-3 py-2 rounded ${filterStatus === "COMPLETED" ? "bg-green-500 text-white" : "bg-gray-100"}`}>
                                        Completed
                                    </button>

                                    <button onClick={() => setFilterStatus("REJECTED")}
                                            className={`px-3 py-2 rounded ${filterStatus === "REJECTED" ? "bg-red-500 text-white" : "bg-gray-100"}`}>
                                        Rejected
                                    </button>
                                </div>

                                {filteredRequests(property.id).length > 0 ? (
                                    <ul className="list-disc list-inside">
                                        {filteredRequests(property.id).map((request, index) => (
                                            <li key={request.id} className={`flex justify-between items-center space-x-2 ${
                                                    index !== 0 ? "border-t border-dashed border-gray-300 py-2" : "pb-2"
                                                }`}>
                                                <div className="flex items-center space-x-4">
                                                    <span title={request.status}
                                                          className={`w-3 h-3 block rounded-full ${
                                                              request.status === "REQUESTED" ? "bg-yellow-500" :
                                                              request.status === "IN_PROGRESS" ? "bg-blue-500" :
                                                              request.status === "COMPLETED" ? "bg-green-500" : "bg-red-500"
                                                          }`}/>

                                                    <div className="flex flex-col">
                                                        <div>
                                                            <strong>Request Date:</strong> {request.requestDate}
                                                        </div>

                                                        <div>
                                                            <strong>Description:</strong> {request.description}
                                                        </div>

                                                        {request.completionDate && (
                                                            <div>
                                                                <strong>Completion Date:</strong> {request.completionDate}
                                                            </div>
                                                        )}
                                                    </div>
                                                </div>

                                                {isAdmin ? (
                                                    <select value={request.status} className="ml-4 bg-transparent rerounded"
                                                            onChange={(e: ChangeEvent<HTMLSelectElement>): Promise<void> =>
                                                                updateRequestStatus(property.id, request.id, e.target.value)
                                                            }>
                                                        <option value="REQUESTED">Requested</option>
                                                        <option value="IN_PROGRESS">In Progress</option>
                                                        <option value="COMPLETED">Completed</option>
                                                        <option value="REJECTED">Rejected</option>
                                                    </select>
                                                ) : (
                                                    <span>{request.status}</span>
                                                )}
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
                        <RequestsForm property={propertyForRequest} setIsOpen={setIsRequestFormOpen}/>
                    </div>
                </div>
            )}
        </div>
    );
};

export default RequestsList;
